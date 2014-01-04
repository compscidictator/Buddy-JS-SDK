using System;
using System.Net;
using System.Threading;
using System.Xml.Linq;
using System.Reflection;
using System.IO.IsolatedStorage;
using System.IO;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using System.Text;
using System.Linq;


#if __ANDROID__
using Android.App;
using Android.Content;
using Android.Content.PM;
using Android.Net;
using Android.OS;
using Android.Provider;
#elif __IOS__
using MonoTouch.CoreLocation;
using MonoTouch.CoreFoundation;
using MonoTouch.UIKit;
using MonoTouch.Foundation;
using MonoTouch.SystemConfiguration;
#endif

namespace BuddySDK
{
    public abstract class PlatformAccess
    {

        public enum NetworkConnectionType {
            None,
            Carrier,
            WiFi
        }

        // device info
        //
        public abstract string Platform {get;}
        public abstract string Model {get;}
        public abstract string DeviceUniqueId { get;}
        public abstract string OSVersion { get;}
        public abstract bool   IsEmulator { get; }
        public abstract string ApplicationID {get;}
        public abstract string AppVersion {get;}

        public abstract NetworkConnectionType ConnectionType {get;}
        // TODO: Connection speed?

        private int _activity = 0;
        public bool ShowActivity {
            get {
                return _activity > 0;
            }
            set {
                SetActivityInternal (value);
            }
        }

        protected virtual void OnShowActivity(bool show) {

        }

        private void SetActivityInternal(bool isActive) {
            bool wasActive = ShowActivity;

            if (isActive) {
                _activity++;
            } else if (_activity > 0) {
                _activity--;
            }

            if (ShowActivity != wasActive) {
                OnShowActivity (ShowActivity);
            }
        }

		private const string UserSettingExpireEncodeDelimiter = "\t";

		protected string EncodeUserSetting(string value, DateTime? expires = default(DateTime?))
		{
			var dt = expires.GetValueOrDefault (new DateTime (0)); // TODO: why both default(DateTime?) & new DateTime (0)?

			return String.Format ("{0}{1}{2}", dt.Ticks, UserSettingExpireEncodeDelimiter, value);
		}

		protected string DecodeUserSetting(string value)
		{
			if (string.IsNullOrEmpty (value)) {
				return null;
			}

			var tabIndex = value.IndexOf (UserSettingExpireEncodeDelimiter);

			if (tabIndex == -1)
				throw new ArgumentOutOfRangeException ("Unexpected Buddy user setting value.");

			var ticks = Int64.Parse (value.Substring (0, tabIndex));

			if (ticks > 0 && new DateTime(ticks) < DateTime.UtcNow) {
				return null;
			}

			return value.Substring (tabIndex + 1);
		}

        // settings
        public abstract string GetConfigSetting(string key);

        public abstract void SetUserSetting(string key, string value, DateTime? expires = null);
        public abstract string GetUserSetting(string key);

        public abstract void ClearUserSetting (string str);

        // platform
        //
        public abstract void InvokeOnUiThread(Action a);



        static PlatformAccess   _current;
        public static PlatformAccess Current {
            get {
                if (_current == null) {
					#if __ANDROID__
					_current = new AndroidPlatformAccess();
					#elif __IOS__
                    _current = new IosPlatformAccess();
                    #else
                    _current = new DotNetPlatformAccess();
                    #endif

                    if (_current == null) {
                        throw new NotSupportedException ("Unknown platform");
                    }
                }
                return _current;
            }
        }

    }

	#if __ANDROID__
	internal class AndroidPlatformAccess : PlatformAccess
    {
        public override string Platform {
			get { return "Android"; }
		}

        public override string Model {
			// TODO: verify this delimiter is a good one for analytics, and that it doesn't stomp on known Manufacturers and\or Models
			get { return Build.Manufacturer + " : " + Build.Model; }
		}

        public override string DeviceUniqueId {
			get {
				// TODO: verify this is sufficient.  See http://developer.samsung.com/android/technical-docs/How-to-retrieve-the-Device-Unique-ID-from-android-device
				// and http://android-developers.blogspot.com/2011/03/identifying-app-installations.html
				return Settings.Secure.GetString (Application.Context.ContentResolver,
					Settings.Secure.AndroidId);
			}
		}

        public override string OSVersion {
			get { return ((int) Build.VERSION.SdkInt).ToString(); }
		}

        public override bool IsEmulator {
			get {
				// The other recommended method is "goldfish".Equals (Android.OS.Build.Hardware.ToLowerInvariant());
				return Android.OS.Build.Fingerprint.StartsWith("generic");
			}
		}

        public override string ApplicationID {
			get { 
				return Application.Context.PackageName;
			}
		}

        public override string AppVersion {
			get {
				var context = Application.Context;
				 
				var packageInfo = context.PackageManager.GetPackageInfo (context.PackageName, 0);
					
				return packageInfo.VersionName;
			}
		}

        public override PlatformAccess.NetworkConnectionType ConnectionType {
			get {
				var cs = (ConnectivityManager) Android.App.Application.Context.GetSystemService (Context.ConnectivityService);

				if (!cs.ActiveNetworkInfo.IsConnected)
					return PlatformAccess.NetworkConnectionType.None;

				if (cs.ActiveNetworkInfo.Subtype == ConnectivityType.Wifi)
					return PlatformAccess.NetworkConnectionType.WiFi;
				else
					return PlatformAccess.NetworkConnectionType.Carrier;
			}
		}

        public override string GetConfigSetting(string key)
        {
			var context = Application.Context;

			var appInfo = context.PackageManager.GetApplicationInfo (context.PackageName, 
				Android.Content.PM.PackageInfoFlags.MetaData);

			var metaData = appInfo.MetaData;

			var value = metaData != null && metaData.ContainsKey(key) ? metaData.GetString (key) : null;

			return value;
        }

		private Android.Content.ISharedPreferences GetPreferences()
		{
			var preferences = Application.Context.GetSharedPreferences ("com.buddy-" + ApplicationID, FileCreationMode.Private);

			return preferences;
		}

		public override void SetUserSetting (string key, string value, DateTime? expires = default(DateTime?))
		{
			if (key == null)
			throw new ArgumentNullException ("key");

			var preferences = GetPreferences ();

			var editor = preferences.Edit ();

			string encodedValue = this.EncodeUserSetting (value, expires);

			editor.PutString (key, encodedValue);

			editor.Commit ();
        }

        public override string GetUserSetting(string key)
        {
			var preferences = GetPreferences ();

			object val = null;
			var keyExists = preferences.All.TryGetValue (key, out val);

			if (!keyExists) {
				return null;
			}

			var value = base.DecodeUserSetting ((string) val);

			if (value == null) {
				ClearUserSetting (key);
			}

			return value;
		}

		public override void ClearUserSetting(string key)
        {
			var preferences = GetPreferences ();

			var editor = preferences.Edit ();

			editor.Remove (key);

			editor.Commit ();
        }

        public override void InvokeOnUiThread(Action a)
		{
			// SynchronizationContext can't be cached
			if (SynchronizationContext.Current != null)
			{
				SynchronizationContext.Current.Post((s) => { a(); }, null);
			}
			else
			{
				a ();
			}
        }
    }

	#elif __IOS__

    internal class IosPlatformAccess : PlatformAccess {
        #region implemented abstract members of PlatformAccess

        private NSObject invoker = new NSObject();

        public override string GetConfigSetting (string key)
        {
            var val = NSBundle.MainBundle.ObjectForInfoDictionary(key);

            if (val != null) {
                return val.ToString();
            }

            return null;
        }

        public override void SetUserSetting (string key, string value, DateTime? expires = default(DateTime?))
        {
			string encodedValue = base.EncodeUserSetting (value, expires);

			NSUserDefaults.StandardUserDefaults.SetValueForKey (new NSString(encodedValue), new NSString(key));
        }

        public override void ClearUserSetting (string key)
        {
            try {
                NSUserDefaults.StandardUserDefaults.SetNilValueForKey (new NSString(key));
            }
            catch {
            }
        }

        public override string GetUserSetting (string key)
        {
            var value = NSUserDefaults.StandardUserDefaults.ValueForKey (new NSString(key));
            if (value == null) {
                return null;
            }

			var decodedValue = base.DecodeUserSetting (value.ToString());

			if (decodedValue == null) {
				ClearUserSetting (key);
			}
		
			return decodedValue;
        }

        public override string Platform {
            get {
                return "iOS";
            }
        }

        public override string Model {
            get {
                // TODO: see code at http://pastebin.com/FJfpGRbQ
                return "iPhone";
            }
        }

        public override string DeviceUniqueId {
            get {
                return UIDevice.CurrentDevice.IdentifierForVendor.AsString ();
            }
        }

        public override string OSVersion {
            get {
                return "1.0";
            }
        }

      
        public override bool IsEmulator {
            get {
                return MonoTouch.ObjCRuntime.Runtime.Arch == MonoTouch.ObjCRuntime.Arch.SIMULATOR;
            }
        }

        public override string ApplicationID {
            get {
                return NSBundle.MainBundle.BundleIdentifier;
            }
           
        }

        public override string AppVersion {
            get {
                NSDictionary infoDictionary =  NSBundle.MainBundle.InfoDictionary;

                var val = infoDictionary [new NSString("CFBundleShortVersionString")];
                if (val != null) {
                    return val.ToString ();
                }
                return null;

            }
        }

        protected override void OnShowActivity (bool show)
        {
            UIApplication.SharedApplication.NetworkActivityIndicatorVisible = show;
        }


        public override void InvokeOnUiThread (Action a)
        {
           
            NSAction nsa = () => {
                a ();
            };

            // do this async so we don't deadlock
            ThreadPool.QueueUserWorkItem ((state) => {
                invoker.InvokeOnMainThread (nsa);
            }, null);
           
        }

       
        // FROM: https://github.com/xamarin/monotouch-samples/tree/master/ReachabilitySample
        // Licence: Apache 2.0
        // Author: Miguel de Icaza
        //
        public static class Reachability {
            public enum NetworkStatus {
                NotReachable,
                ReachableViaCarrierDataNetwork,
                ReachableViaWiFiNetwork
            }

            public static string HostName = "www.buddy.com";

            public static bool IsReachableWithoutRequiringConnection (NetworkReachabilityFlags flags)
            {
                // Is it reachable with the current network configuration?
                bool isReachable = (flags & NetworkReachabilityFlags.Reachable) != 0;

                // Do we need a connection to reach it?
                bool noConnectionRequired = (flags & NetworkReachabilityFlags.ConnectionRequired) == 0;

                // Since the network stack will automatically try to get the WAN up,
                // probe that
                if ((flags & NetworkReachabilityFlags.IsWWAN) != 0)
                    noConnectionRequired = true;

                return isReachable && noConnectionRequired;
            }

            // Is the host reachable with the current network configuration
            public static bool IsHostReachable (string host)
            {
                if (host == null || host.Length == 0)
                    return false;

                using (var r = new NetworkReachability (host)){
                    NetworkReachabilityFlags flags;

                    if (r.TryGetFlags (out flags)){
                        return IsReachableWithoutRequiringConnection (flags);
                    }
                }
                return false;
            }

            // 
            // Raised every time there is an interesting reachable event, 
            // we do not even pass the info as to what changed, and 
            // we lump all three status we probe into one
            //
            public static event EventHandler ReachabilityChanged;

            static void OnChange (NetworkReachabilityFlags flags)
            {
                var h = ReachabilityChanged;
                if (h != null)
                    h (null, EventArgs.Empty);
            }

            //
            // Returns true if it is possible to reach the AdHoc WiFi network
            // and optionally provides extra network reachability flags as the
            // out parameter
            //
            static NetworkReachability adHocWiFiNetworkReachability;
            public static bool IsAdHocWiFiNetworkAvailable (out NetworkReachabilityFlags flags)
            {
                if (adHocWiFiNetworkReachability == null){
                    adHocWiFiNetworkReachability = new NetworkReachability (new IPAddress (new byte [] {169,254,0,0}));
                    adHocWiFiNetworkReachability.SetCallback (OnChange);
                    adHocWiFiNetworkReachability.Schedule (CFRunLoop.Current, CFRunLoop.ModeDefault);
                }

                if (!adHocWiFiNetworkReachability.TryGetFlags (out flags))
                    return false;

                return IsReachableWithoutRequiringConnection (flags);
            }

            static NetworkReachability defaultRouteReachability;
            static bool IsNetworkAvailable (out NetworkReachabilityFlags flags)
            {
                if (defaultRouteReachability == null){
                    defaultRouteReachability = new NetworkReachability (new IPAddress (0));
                    defaultRouteReachability.SetCallback (OnChange);
                    defaultRouteReachability.Schedule (CFRunLoop.Current, CFRunLoop.ModeDefault);
                }
                if (!defaultRouteReachability.TryGetFlags (out flags))
                    return false;
                return IsReachableWithoutRequiringConnection (flags);
            }        

            static NetworkReachability remoteHostReachability;
            public static NetworkStatus RemoteHostStatus ()
            {
                NetworkReachabilityFlags flags;
                bool reachable;

                if (remoteHostReachability == null){
                    remoteHostReachability = new NetworkReachability (HostName);

                    // Need to probe before we queue, or we wont get any meaningful values
                    // this only happens when you create NetworkReachability from a hostname
                    reachable = remoteHostReachability.TryGetFlags (out flags);

                    remoteHostReachability.SetCallback (OnChange);
                    remoteHostReachability.Schedule (CFRunLoop.Current, CFRunLoop.ModeDefault);
                } else
                    reachable = remoteHostReachability.TryGetFlags (out flags);                        

                if (!reachable)
                    return NetworkStatus.NotReachable;

                if (!IsReachableWithoutRequiringConnection (flags))
                    return NetworkStatus.NotReachable;

                if ((flags & NetworkReachabilityFlags.IsWWAN) != 0)
                    return NetworkStatus.ReachableViaCarrierDataNetwork;

                return NetworkStatus.ReachableViaWiFiNetwork;
            }

            public static NetworkStatus InternetConnectionStatus ()
            {
                NetworkReachabilityFlags flags;
                bool defaultNetworkAvailable = IsNetworkAvailable (out flags);
                if (defaultNetworkAvailable){
                    if ((flags & NetworkReachabilityFlags.IsDirect) != 0)
                        return NetworkStatus.NotReachable;
                } else if ((flags & NetworkReachabilityFlags.IsWWAN) != 0)
                    return NetworkStatus.ReachableViaCarrierDataNetwork;
                else if (flags == 0)
                    return NetworkStatus.NotReachable;
                return NetworkStatus.ReachableViaWiFiNetwork;
            }

            public static NetworkStatus LocalWifiConnectionStatus ()
            {
                NetworkReachabilityFlags flags;
                if (IsAdHocWiFiNetworkAvailable (out flags)){
                    if ((flags & NetworkReachabilityFlags.IsDirect) != 0)
                        return NetworkStatus.ReachableViaWiFiNetwork;
                }
                return NetworkStatus.NotReachable;
            }
        }

        public override NetworkConnectionType ConnectionType {
            get {
                switch (Reachability.InternetConnectionStatus()) {
                    case Reachability.NetworkStatus.NotReachable:
                        return NetworkConnectionType.None;
                    case Reachability.NetworkStatus.ReachableViaCarrierDataNetwork:
                        return NetworkConnectionType.Carrier;
                    case Reachability.NetworkStatus.ReachableViaWiFiNetwork:
                        return NetworkConnectionType.WiFi;
                    default:
                    throw new NotSupportedException ();
                }
            }
        }

        #endregion



    }

    public static class IosExtensions  {


        public static BuddyGeoLocation ToBuddyGeoLocation(this MonoTouch.CoreLocation.CLLocation loc) {
            return new BuddyGeoLocation (loc.Coordinate.Latitude, loc.Coordinate.Longitude);
        }

        public static CLLocation ToCLLocation(this BuddyGeoLocation loc) {
            var clLoc = new CLLocation (loc.Latitude, loc.Longitude);
            return clLoc;
        }
    }
#else
    // default
    internal class DotNetPlatformAccess : PlatformAccess
    {

        public DotNetPlatformAccess()
        {
            _context = SynchronizationContext.Current;
        }
        public override string Platform
        {
            get { return ".NET"; }
        }

        public override string Model
        {
            get {

#if WINDOWS_PHONE
            return Microsoft.Phone.Info.DeviceStatus.DeviceName;
#else
                return "DeviceType not found";
#endif
            
            }
        }

        public override string DeviceUniqueId
        {
            get {
                var uniqueId = GetUserSetting("UniqueId");
#if WINDOWS_PHONE
                byte[] id = (byte[])Microsoft.Phone.Info.DeviceExtendedProperties.GetValue("DeviceUniqueId");
               
                if (id != null) {
                    return Convert.ToBase64String(myDeviceID);
                }
                
#else

                // default do nothing
#endif
                if (uniqueId == null)
                {
                    uniqueId = Guid.NewGuid().ToString();
                    SetUserSetting("UniqueId", uniqueId);
                }
                return uniqueId;
           }
        }

        public override string OSVersion
        {
            get {
#if WINDOWS_PHONE
            return System.Environment.OSVersion.Version.ToString();
#else
                try
                {
                    // .NET
                    var osVersionProperty = typeof(Environment).GetRuntimeProperty("OSVersion");
                    object osVersion = osVersionProperty.GetValue(null, null);
                    var versionStringProperty = osVersion.GetType().GetRuntimeProperty("VersionString");
                    var versionString = versionStringProperty.GetValue(osVersion, null);
                    return (string)versionString;
                }
                catch
                {
                }

                return "DeviceOSVersion not found";
#endif
            
            
            }
        }

        public override bool IsEmulator
        {
            get {
#if WINDOWS_PHONE
                 return Environment.DeviceType == DeviceType.Emulator;
#else
                return false;
#endif
                }
        }

        public override string ApplicationID
        {
            get {
#if WINDOWS_PHONE
            var xml = XElement.Load("WMAppManifest.xml");
            var prodId = (from app in xml.Descendants("App")
                            select app.Attribute("ProductID").Value).FirstOrDefault();
            if (string.IsNullOrEmpty(prodId)) return string.Empty;
            return new Guid(prodId).ToString();
#else
                try
                {
                    // .NET
                    var assemblyFullName = typeof(System.Diagnostics.Debug).GetTypeInfo().Assembly.FullName;
                    var process = Type.GetType("System.Diagnostics.Process, " + assemblyFullName);
                    var getCurrentProcessMethod = process.GetRuntimeMethod("GetCurrentProcess", new Type[0]);
                    var currentProcess = getCurrentProcessMethod.Invoke(null, null);
                    var processNameProperty = currentProcess.GetType().GetRuntimeProperty("ProcessName");
                    var processName = processNameProperty.GetValue(currentProcess, null);
                    return (string)processName;
                }
                catch
                {
                }

                try
                {
                    // Windows Store
                    var loadMethod = typeof(XDocument).GetRuntimeMethod("Load", new Type[] {
                    typeof(string),
                    typeof(LoadOptions)
                });
                    var xDocument = loadMethod.Invoke(null, new object[] {
                    "AppxManifest.xml",
                    LoadOptions.None
                });

                    var xNamespace = XNamespace.Get("http://schemas.microsoft.com/appx/2010/manifest");

                    var identityElement = ((XDocument)xDocument).Descendants(xNamespace + "Identity").First();

                    return identityElement.Attribute("Name").Value;
                }
                catch
                {
                }

                return "ApplicationId not found";
#endif
            
            
            }
        }

        public override string AppVersion
        {
            get {

                // TODO: Capture calling assmbly from Buddy.Init call.
                var entryAssembly = Assembly.GetEntryAssembly();

                if (entryAssembly != null) {
                    var attr = entryAssembly.GetCustomAttribute<AssemblyFileVersionAttribute>();
                    return attr.Version;
                }

                return "1.0";
            
            }
        }

        public override PlatformAccess.NetworkConnectionType ConnectionType
        {
            get { throw new NotImplementedException(); }
        }

        public override string GetConfigSetting(string key)
        {
		    return System.Configuration.ConfigurationManager.AppSettings[key];
        }

        private IDictionary<string, string> LoadSettings(IsolatedStorageFile isoStore)
        {

            string existing = "";
            if (isoStore.FileExists("_buddy"))
            {
                using (var sr = new StreamReader(isoStore.OpenFile("_buddy", FileMode.Open)))
                {
                    existing = sr.ReadToEnd();
                }
            }
            var d = new Dictionary<string, string>();
            var parts = Regex.Match(existing, "(?<key>\\w+)=(?<value>.*?);");

            while (parts.Success)
            {
                d[parts.Groups["key"].Value] = parts.Groups["value"].Value;

                parts = parts.NextMatch();
            }
            return d;
        }

        private void SaveSettings(IsolatedStorageFile isoStore, IDictionary<string, string> values)
        {

            
            StringBuilder sb = new StringBuilder();
            foreach (var kvp in values)
            {
                sb.AppendFormat("{0}={1};", kvp.Key, kvp.Value ?? "");
            }
            using (var fs = isoStore.OpenFile("_buddy", FileMode.Create))
            {
                var sw = new StreamWriter(fs);

                sw.WriteLine(sb.ToString());

                sw.Flush();
                fs.Flush();

            }
        }
        public override void SetUserSetting(string key, string value, DateTime? expires = null)
        {
            IsolatedStorageFile isoStore = IsolatedStorageFile.GetStore(IsolatedStorageScope.User | IsolatedStorageScope.Assembly, null, null);

            

            // parse it
            var parsed = LoadSettings(isoStore);
            parsed[key] = value;

            SaveSettings(isoStore, parsed);
            
        }

        public override string GetUserSetting(string key)
        {
            IsolatedStorageFile isoStore = IsolatedStorageFile.GetStore(IsolatedStorageScope.User | IsolatedStorageScope.Assembly, null, null);

            var parsed = LoadSettings(isoStore);

            if (parsed.ContainsKey(key))
            {
                return parsed[key];
            }
            return null;
        }

        public override void ClearUserSetting(string key)
        {
            IsolatedStorageFile isoStore = IsolatedStorageFile.GetStore(IsolatedStorageScope.User | IsolatedStorageScope.Assembly, null, null);

            var parsed = LoadSettings(isoStore);

            if (parsed.ContainsKey(key))
            {
                parsed.Remove(key);
                SaveSettings(isoStore, parsed);
            }
           
        }


        private SynchronizationContext _context;

        public override void InvokeOnUiThread(Action a)
        {
            if (_context != null)
            {
                _context.Post((s) => { a(); }, null);
            }
            else
            {
                a();
            }
        }
    }
    #endif
}

