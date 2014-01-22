using System;
using System.Collections.Generic;

using System.Linq;
using System.Text;
using System.Threading;
using System.ComponentModel;
using System.Collections.ObjectModel;
using System.Globalization;
using BuddyServiceClient;
using System.Reflection;
using System.Collections;


#if WINDOWS_PHONE
using System.Net;
#else


#endif
using System.Xml.Linq;
using System.Threading.Tasks;
using System.IO;



namespace BuddySDK
{

    /// <summary>
    /// Represents the main class and entry point to the Buddy platform. Use this class to interact with the platform, create and login users and modify general
    /// application level properties like Devices and Metadata.
    /// <example>
    /// <code>
    ///     BuddyClient client = new BuddyClient("APPNAME", "APPPASS");
    ///     client.PingAsync(null);
    /// </code>
    /// </example>
    /// </summary>
    public class BuddyClient
    {

        public event EventHandler<ServiceExceptionEventArgs> ServiceException;
        public event EventHandler<ConnectivityLevelChangedArgs> ConnectivityLevelChanged;
        public event EventHandler<CurrentUserChangedEventArgs> CurrentUserChanged;
        public event EventHandler LastLocationChanged;
        public event EventHandler AuthorizationLevelChanged;
        public event EventHandler AuthorizationFailure; 


        private string _userToken = null;
        private bool _gettingToken = false;
        private AuthenticatedUser _user;
        private static bool _crashReportingSet = false;
        private BuddyClientFlags _flags;



        /// <summary>
        /// The service we use to call the Buddy backend.
        /// </summary>
        /// 
        private BuddyServiceClientBase _service;
        internal BuddyServiceClientBase Service { 
            get {
                OnEnsureService();
                return _service;
            }
        }

        private static string _WebServiceUrl;
        protected static string WebServiceUrl {
            get {
                return _WebServiceUrl ?? "http://10.211.55.3:50800";
            }
            set {
                _WebServiceUrl = value;
            }

        }

        /// <summary>
        /// Gets the application ID for this client.
        /// </summary>
        public string AppId { get; protected set; }

        /// <summary>
        /// Gets the application secret key for this client.
        /// </summary>
        public string AppKey { get; protected set; }



        protected string AccessToken
        {
            get
            {
                return GetAccessToken ().Result;

            }
        }

        public AuthenticationLevel AuthLevel {
            get;
            private set;
        }

        private string DeviceToken { get; set; }

        private string UserToken { 
            get { return _userToken; } 
            set { 
                _userToken = value; 
            }
        }

        public AuthenticatedUser User
        {
            get
            { 
                return GetUser();
            }
            private set
            {
                _user = value;
                if (_user != null)
                {
                    UserToken = _user.AccessToken;
                    PlatformAccess.Current.SetUserSetting ("UserID", _user.ID);


                    var lastUserID = PlatformAccess.Current.GetUserSetting ("LastUserID");

                    if (lastUserID != _user.ID) {

                        OnCurrentUserChanged (lastUserID);

                        // update last user
                        PlatformAccess.Current.SetUserSetting ("LastUserID",_user.ID);
                    }

                }
                else
                {
                    PlatformAccess.Current.ClearUserSetting("UserID");
                    UserToken = null;
                }
                OnAccessTokenChanged (UserToken, AccessTokenType.User);
            }
        }

       

       


        /// <summary>
        /// The last location value for this device.  Location tracking
        /// must be enabled to use this property.
        /// </summary>
        /// <value>The last location.</value>
        public BuddyGeoLocation LastLocation {
            get {
                if (!ShouldTrackLocation) {
                    throw new InvalidOperationException ("Location tracking must be enabled.");
                }
                return PlatformAccess.Current.LastLocation;
            }
        }


        /// <summary>
        /// Enables or disables tracking of device location.
        /// </summary>
        /// <value><c>true</c> if should track location; otherwise, <c>false</c>.</value>
        public bool ShouldTrackLocation {
            get {
                return _flags.HasFlag(BuddyClientFlags.AutoTrackLocation);
            }
            set {
                if (value != ShouldTrackLocation) {
                    if (value) {
                        _flags |= BuddyClientFlags.AutoTrackLocation;
                    } else {
                        _flags &= ~BuddyClientFlags.AutoTrackLocation;
                    }
                    PlatformAccess.Current.TrackLocation (value);
                }
            }
        }


    

       

        public BuddyClient(string appid, string appkey, BuddyClientFlags flags = BuddyClientFlags.Default)
        {
            if (String.IsNullOrEmpty(appid))
                throw new ArgumentException("Can't be null or empty.", "appName");
            if (String.IsNullOrEmpty(appkey))
                throw new ArgumentException("Can't be null or empty.", "AppKey");

            this.AppId = appid;
            this.AppKey = appkey;
            //this._flags = flags;

            LoadState ();
            UpdateAccessLevel();

            if (flags.HasFlag (BuddyClientFlags.AutoCrashReport)) {
                InitCrashReporting ();
            }
            _flags = flags;
            if (ShouldTrackLocation) {
                PlatformAccess.Current.TrackLocation (true);
            }

            PlatformAccess.Current.LocationUpdated += (sender, e) => {

                if (LastLocationChanged != null) {
                    LastLocationChanged(this,e);
                }
            };
            
        }



        public class DeviceRegistration
        {
            public string AccessToken { get; set; }
            public string ServiceRoot { get; set; }
        }


        internal async Task<string> GetAccessToken() {

            if (!_gettingToken)
            {
                try
                {
                    _gettingToken = true;
                    if (UserToken != null)
                    {
                        return UserToken;
                    }
                    else if (DeviceToken != null)
                    {
                        return DeviceToken;
                    }
                    DeviceToken = await GetDeviceToken();
                    OnAccessTokenChanged(DeviceToken, AccessTokenType.Device);
                    return DeviceToken;
                }
                finally
                {
                    _gettingToken = false;
                }
            }
            else
            {
                return UserToken ?? DeviceToken;
            }
        }


        private async Task<string> GetDeviceToken()
        {

            var dr = await CallServiceMethodHelper<DeviceRegistration, DeviceRegistration> (
                "POST",
                "/devices",
                new
                {
                    AppId = AppId,
                    AppKey = AppKey,
                    ApplicationId = PlatformAccess.Current.ApplicationID,
                    Platform = PlatformAccess.Current.Platform,
                    UniqueID = PlatformAccess.Current.DeviceUniqueId,
                    Model = PlatformAccess.Current.Model,
                    OSVersion = PlatformAccess.Current.OSVersion
                },
                completed: (r1, r2) => { 
                    if (r2.IsSuccess && r2.Value.ServiceRoot != null)
                    {
                        Service.ServiceRoot = r2.Value.ServiceRoot;
                        PlatformAccess.Current.SetUserSetting ("ServiceRoot", r2.Value.ServiceRoot);
                    }
                    else {
                        ClearCredentials();
                    }
                });

           
            if (!dr.IsSuccess) {
                return null;
            }
            return dr.Value.AccessToken;
        }

        private AuthenticatedUser GetUser() {


            if (_user == null && AuthLevel != AuthenticationLevel.User) {
                this.OnAuthorizationFailure (null);
            } else if (_user != null && !_user.IsPopulated) {
                // make sure the user exists.
                //
                _user.FetchAsync ().ContinueWith ((r) => {
                });

               
            }
            return _user;
        }

        protected virtual void OnCurrentUserChanged (string lastUserId)
        {
            User lastUser = null;

            if (lastUserId != null) {
                lastUser = new User (lastUserId);
            }
            if (CurrentUserChanged != null) {
                CurrentUserChanged(this, new CurrentUserChangedEventArgs(this.User, lastUser));
            }
        }

        private string GetRootUrl() {
            string setting = PlatformAccess.Current.GetConfigSetting("RootUrl");
            var userSetting = PlatformAccess.Current.GetUserSetting ("ServiceRoot");
            return userSetting ?? setting ?? WebServiceUrl;
        }

        private void InitCrashReporting() {

            if (!_crashReportingSet) {

                _crashReportingSet = true;
                AppDomain.CurrentDomain.UnhandledException += (sender, e) => {
                    var ex = e.ExceptionObject as Exception;

                    // need to do this synchrously or the OS won't wait for us.
                    var t = Buddy.Instance.AddCrashReportAsync (ex);
                   
                    // wait up to a second to let it go out
                    t.Wait(TimeSpan.FromSeconds(2));

                };
            }

        }

        internal Task<BuddyResult<T>> CallServiceMethod<T>(string verb, string path, object parameters = null, bool allowThrow = true) {


            return Task.Run<BuddyResult<T>> (() => {

                var dictionary = BuddyServiceClientBase.ParametersToDictionary(parameters);
                var loc = PlatformAccess.Current.LastLocation;
                if (!dictionary.ContainsKey("location") && loc != null) {
                    dictionary["location"] = loc.ToString();
                }

                var bcrTask = Service.CallMethodAsync<T> (verb, path, dictionary);

                var bcr = bcrTask.Result;

                var result = new BuddyResult<T> ();
                result.RequestID = bcr.RequestID;

                if (bcr.Error != null) {
                    BuddyServiceException buddyException = null;

                    switch (bcr.StatusCode) {
                        case 0: 
                            buddyException = new BuddyNoInternetException (bcr.Error);
                            break;
                        case 403:
                            buddyException = new BuddyUnauthorizedException (bcr.Error, bcr.Message);
                            break;
                        default:
                            buddyException = new BuddySDK.BuddyServiceException (bcr.Error, bcr.Message);
                            break;
                    }

                    var tsc = new TaskCompletionSource<bool>();

                   
                    PlatformAccess.Current.InvokeOnUiThread(() => {

                        var r = false;
                        if (OnServiceException(this, buddyException)) {
                            r = true;
                        }
                        tsc.TrySetResult(r);
                    });

                    if (tsc.Task.Result && allowThrow) {
                        throw buddyException;
                    }

                    buddyException.StatusCode = bcr.StatusCode;
                    result.Error = buddyException;

                } else {
                    result.Value = bcr.Result;
                }
                return result;
            });

        }


        internal async Task<BuddyResult<T2>> CallServiceMethodHelper<T1, T2>(
            string verb, 
            string path, 
            object parameters = null, 
            Func<T1, T2> map = null, 
            Action<BuddyResult<T1>, BuddyResult<T2>> completed = null) {

            BuddyResult<T1> r1 = null;
            BuddyResult<T2> r2 = null;

            if (typeof(T1) == typeof(T2)) {
                r2 = await CallServiceMethod<T2> (verb, path, parameters);
            } else {
                r1 = await CallServiceMethod<T1> (verb, path, parameters);

               
                if (map == null) {
                    map = (t1) => {
                        return (T2)(object)r1.Value;
                    };
                }

                r2 = r1.Convert<T2> (map);

            }

            if (completed != null) {
                PlatformAccess.Current.InvokeOnUiThread (() => completed (r1, r2));
            }
            return r2;
        }

      

     

        private void ClearCredentials(bool clearUser = true, bool clearDevice = true) {

            if (clearDevice) {
                PlatformAccess.Current.ClearUserSetting ("ServiceRoot");
                PlatformAccess.Current.ClearUserSetting (this.AppId + "-DeviceAccessToken");
                DeviceToken = null;
                Service.ServiceRoot = GetRootUrl ();
            }

            if (clearUser) {
                PlatformAccess.Current.ClearUserSetting ("UserID");
                PlatformAccess.Current.ClearUserSetting (this.AppId + "-UserAccessToken");

                UserToken = null;
            }

            UpdateAccessLevel ();
        }

        private void LoadState() {


            var deviceToken = PlatformAccess.Current.GetUserSetting (this.AppId + "-DeviceAccessToken");
            var userToken = PlatformAccess.Current.GetUserSetting(this.AppId + "-UserAccessToken");
            if (deviceToken != null) {
                    OnAccessTokenChanged (deviceToken, AccessTokenType.Device);
            }
            var id = PlatformAccess.Current.GetUserSetting ("UserID");
            if (userToken != null && id != null) {
                User = new AuthenticatedUser (id, userToken, this);
            }
        }

        private async void OnEnsureService()
        {
            if (this._service != null) return;



            var root = GetRootUrl ();

            this._service = BuddyServiceClientBase.CreateServiceClient(this, root);

            this._service.ServiceException += (object sender, ExceptionEventArgs e) => {

                if (e.Exception is BuddyUnauthorizedException) {
                    ClearCredentials(true, true);
                }

            };

            var token = await GetAccessToken ();
            if (token == null)
            {
                throw new UnauthorizedAccessException("Failed to register device, check AppID/AppKey");
            }

        }

        protected enum AccessTokenType {
            Device,
            User
        }

        protected virtual void OnAccessTokenChanged(string token, AccessTokenType tokenType, DateTime? expires = null) {

            string key = null;
            switch (tokenType) {
            case AccessTokenType.Device:
                DeviceToken = token;
                key = this.AppId + "-DeviceAccessToken";
                break;
            case AccessTokenType.User:
                UserToken = token;
                key = this.AppId + "-UserAccessToken";
                break;
            }
            if (token != null) {
                
                PlatformAccess.Current.SetUserSetting (key, token, expires ?? DateTime.Now.AddDays (7));

                if (expires != null) {
                    key = key + ".Expires";
                    PlatformAccess.Current.SetUserSetting (key, expires.Value.ToString (), expires);
                }
            } else {
                PlatformAccess.Current.ClearUserSetting (key);
            }

            UpdateAccessLevel();
        }

        ConnectivityLevel? _connectivity;
        public ConnectivityLevel ConnectivityLevel {
            get {
                if (_connectivity == null) {
                    return PlatformAccess.Current.ConnectionType;
                }
                return _connectivity.GetValueOrDefault(ConnectivityLevel.None);
            }
            private set {
                _connectivity = value;
            }
        }

       
        private void CheckConnectivity(TimeSpan waitTime) {
            Service.Client.CallServiceMethod<string>("GET", "/service/ping", allowThrow:false)
                .ContinueWith(r=> {

                    if (r.Result != null && r.Result.IsSuccess) {
                        PlatformAccess.Current.InvokeOnUiThread(() => {
                            OnConnectivityChanged(PlatformAccess.Current.ConnectionType);
                        });
                    }
                    else
                    {
                        // wait a second and try again
                        //
                        Thread.Sleep(waitTime);
                        CheckConnectivity(waitTime);
                    }
                }
             );

        }

        protected virtual void OnConnectivityChanged(ConnectivityLevel level) {
            if (level == _connectivity) {
                return;
            }
            _connectivity = level;

            switch (level) {
            case ConnectivityLevel.None:
                    CheckConnectivity (TimeSpan.FromSeconds (1));
                    break;
              
            }


            if (ConnectivityLevelChanged != null) {
                ConnectivityLevelChanged (this, new ConnectivityLevelChangedArgs  {
                    ConnectivityLevel =  level
                });
            }

        }
      
        protected bool OnServiceException(BuddyClient client, BuddyServiceException buddyException) {


            // first see if it's an auth failure.
            //
            if (buddyException is BuddyUnauthorizedException) {
                client.OnAuthorizationFailure ((BuddyUnauthorizedException)buddyException);
                return false;
            } else if (buddyException is BuddyNoInternetException) {
                OnConnectivityChanged (ConnectivityLevel.None);
                return false;
            }

            bool result = false;

            if (ServiceException != null) {
                var args = new ServiceExceptionEventArgs (buddyException);
                ServiceException (this, args);
                result = args.ShouldThrow;
            } 
            return result;
        }

        internal virtual void OnAuthorizationFailure(BuddyUnauthorizedException exception) {


            if (exception != null && (exception.Error == "AuthAccessTokenInvalid" || exception.Error == "AuthAppCredentialsInvalid")) {
                ClearCredentials ();
            }
           
            PlatformAccess.Current.InvokeOnUiThread (() => {

                if (this.AuthorizationFailure != null) {
                    this.AuthorizationFailure(this, new EventArgs());
                }
            });

          
        }

        protected virtual void OnAuthLevelChanged() {
           
            PlatformAccess.Current.InvokeOnUiThread (() => {

                if (this.AuthorizationLevelChanged != null) {
                    this.AuthorizationLevelChanged (this, EventArgs.Empty);
                }
            });
        }

        private void UpdateAccessLevel() {

            var old = AuthLevel;
            AuthenticationLevel authLevel = AuthenticationLevel.None;
            if (DeviceToken != null) authLevel = AuthenticationLevel.Device;
            if (UserToken != null) authLevel = AuthenticationLevel.User;
            AuthLevel = authLevel;

            if (old != authLevel) {
                OnAuthLevelChanged ();
            }

        }


        // service
        //
        public Task<BuddyResult<string>> PingAsync()
        {
            return CallServiceMethod<string>("GET", "/service/ping");
        }

        // User auth.
        public System.Threading.Tasks.Task<BuddyResult<AuthenticatedUser>> CreateUserAsync(
            string username, 
            string password, 
            string name = null, 
            string email = null,
            BuddySDK.UserGender? gender = null, 
            DateTime? dateOfBirth = null, 
            BuddySDK.UserRelationshipStatus? status = null, 
            string defaultMetadata = null)
        {

            if (String.IsNullOrEmpty(username))
                throw new ArgumentException("Can't be null or empty.", "username");
            if (password == null)
                throw new ArgumentNullException("password");
            if (dateOfBirth > DateTime.Now)
                throw new ArgumentException("dateOfBirth must be in the past.", "dateOfBirth");

            name = name ?? username;

            var task = new Task<BuddyResult<AuthenticatedUser>>(() =>
            {

                var rt = CallServiceMethod<IDictionary<string, object>>("POST", "/users", new
                {
                    name = name,
                    username = username,
                    password = password,
                    email = email,
                    gender = gender,
					dateOfBirth = dateOfBirth,
                    defaultMetadata = defaultMetadata,
                    relationshipStatus = status
                });

                var r = rt.Result;

                return r.Convert(d => {

                        var user = new AuthenticatedUser( (string)r.Value["ID"], (string)r.Value["accessToken"], this);
                    this.User = user;
                    return user;
                });
            });
            task.Start ();
            return task;
         
        }

        /// <summary>
        /// Login an existing user with their username and password. Note that this method internally does two web-service calls, and the IAsyncResult object
        /// returned is only valid for the first one.
        /// </summary>
        /// <param name="username">The username of the user. Can't be null or empty.</param>
        /// <param name="password">The password of the user. Can't be null.</param>
        /// <returns>A Task&lt;AuthenticatedUser&gt;that can be used to monitor progress on this call.</returns>
        public System.Threading.Tasks.Task<BuddyResult<AuthenticatedUser>> LoginUserAsync(string username, string password)
        {
            return LoginUserCoreAsync<AuthenticatedUser>("/users/login", new
            {
                Username = username,
                Password = password
                }, (result) => new AuthenticatedUser((string)result["ID"], (string)result["accessToken"], this));
        }

        public System.Threading.Tasks.Task<BuddyResult<SocialAuthenticatedUser>> SocialLoginUserAsync(string identityProviderName, string identityID, string identityAccessToken)
        {
            return LoginUserCoreAsync<SocialAuthenticatedUser>("/users/login/social", new
                    {
                        IdentityProviderName = identityProviderName,
                        IdentityID = identityID,
                        IdentityAccessToken = identityAccessToken
                }, (result) => new SocialAuthenticatedUser((string)result["ID"], (string)result["accessToken"], (bool)result["isNew"], this));
        }

        private async System.Threading.Tasks.Task<BuddyResult<T>> LoginUserCoreAsync<T>(string path, object parameters, Func<IDictionary<string, object>, T> createUser) where T : AuthenticatedUser
        {

            return await CallServiceMethodHelper<IDictionary<string, object>, T> (
                "POST", 
                path, 
                parameters,
                map: d => createUser (d),
                completed: (r1, r2) => {

                    User = r2.Value;



                });
           
        }

        private async Task<BuddyResult<bool>> LogoutInternal() {

            IDictionary<string,object> dresult = null;

            var r = await CallServiceMethodHelper<IDictionary<string,object>, bool>(
                "POST",
                "/users/me/logout",
                map: (d) => {
                    dresult = d;
                    return d != null;

                });

            if (r.IsSuccess) {

                if (UserToken != null) {
                    UserToken = null;
                    this.User = null;
                    ClearCredentials (true, false);
                   
                }

                if (dresult != null && dresult.ContainsKey("accessToken")) {
                    var token = dresult ["accessToken"] as string;
                    DateTime? expires = null;
                    if (dresult.ContainsKey("accessTokenExpires")) {
                        object dt = dresult ["accessTokenExpires"];
                        expires =  (DateTime)dt;
                    }
                    DeviceToken = token;
                    OnAccessTokenChanged(token, AccessTokenType.Device, expires);
                }
            }
            return r;
        }

        public Task<BuddyResult<bool>> LogoutUserAsync() {
            return LogoutInternal ();
           
        }

        private UserCollection _users;

        public UserCollection Users
        {
            get
            {
                if (_users == null)
                {
                    _users = new UserCollection(this);
                }
                return _users;
            }
        }

        //
        // Metrics
        //

        private class MetricsResult
        {
            public string id { get; set; }
            public bool success { get; set; }
        }


        public Task<BuddyResult<string>> RecordMetricAsync(string key, IDictionary<string, object> value = null, TimeSpan? timeout = null)
        {

            int? timeoutInSeconds = null;

            if (timeout != null)
            {
                timeoutInSeconds = (int)timeout.Value.TotalSeconds;
            }

            return Task.Run<BuddyResult<string>>(() =>
            {

                var r = CallServiceMethod<MetricsResult>("POST", String.Format("/metrics/events/{0}", Uri.EscapeDataString(key)), new
                {
                    value = value,
                    timeoutInSeconds = timeoutInSeconds
                });
               
                
                return r.Result.Convert((mr) => mr.id);

              
            });
        }

        private class CompleteMetricResult
        {
            public long? elaspedTimeInMs { get; set; }
        }

        public Task<BuddyResult<TimeSpan?>> RecordTimedMetricEndAsync(string timedMetricId)
        {
            return Task<TimeSpan?>.Run(() =>
            {

                 var r = CallServiceMethod<CompleteMetricResult>("DELETE", String.Format("/metrics/events/{0}", Uri.EscapeDataString(timedMetricId)));


                    return r.Result.Convert(cmr =>  {
                        TimeSpan? elapsedTime = null;

                        if (cmr.elaspedTimeInMs != null) {
                            elapsedTime = TimeSpan.FromMilliseconds(cmr.elaspedTimeInMs.Value);
                        }

                        return elapsedTime;

                    });
                
            });
        }

        public Task<BuddyResult<bool>> AddCrashReportAsync (Exception ex, string message = null)
        {

            return Task.Run<BuddyResult<bool>> (() => {
                if (ex == null) return new BuddyResult<bool>();



                try {
                    var r = CallServiceMethod<string>(
                        "POST", 
                        "/devices/current/crashreports", 
                            new {
                                stackTrace = ex.ToString(),
                                message = message
                        }, allowThrow:false);
                    return r.Result.Convert(s => true);
                }
                catch {

                }
                return new BuddyResult<bool> {
                    Value = false
                };
            });
        }

    }

    public enum AuthenticationLevel {
        None = 0,
        Device,
        User
    }
}
