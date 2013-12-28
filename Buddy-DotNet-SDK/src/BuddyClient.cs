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
#if IOS
using MonoTouch.Foundation;
#endif

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


        /// <summary>
        /// Gets the BuddyServiceClient interface
        /// </summary>
        /// 
        private BuddyServiceClientBase _service;
        internal BuddyServiceClientBase Service { 
            get {
                OnEnsureService();
                return _service;
            }
        }

        /// <summary>
        /// Gets the application name for this client.
        /// </summary>
        public string AppId { get; protected set; }

        /// <summary>
        /// Gets the application password for this client.
        /// </summary>
        public string AppKey { get; protected set; }



        private BuddyClientFlags _flags;
        private static string _WebServiceUrl;

        public static string WebServiceUrl {
            get {
                return _WebServiceUrl ?? "http://craig.buddyservers.net:8080/api";
            }
            set {
                _WebServiceUrl = value;
            }

        }
        
<<<<<<< HEAD
        /// <summary>
        /// Initializes a new instance of the BuddyClient class. To get an application username and password, go to http://BuddySDK.com, create a new
        /// developer account and create a new application.
        /// </summary>
        /// <param name="appid">The name of the application to use with this client. Can't be null or empty.</param>
        /// <param name="appkey">The password of the application to use with this client. Can't be null or empty.</param>
=======
>>>>>>> origin
        public BuddyClient(string appid, string appkey, BuddyClientFlags flags = BuddyClientFlags.Default)
        {
            if (String.IsNullOrEmpty(appid))
                throw new ArgumentException("Can't be null or empty.", "appName");
            if (String.IsNullOrEmpty(appkey))
                throw new ArgumentException("Can't be null or empty.", "AppKey");

            this.AppId = appid;
            this.AppKey = appkey;
            this._flags = flags;

            LoadState ();
            UpdateAccessLevel();
        }


        private string _deviceToken = null;
        private string _userToken = null;

        private bool _gettingToken = false;
        
        public string AccessToken
        {
            get
            {
                if (!_gettingToken)
                {
                    try
                    {
                        _gettingToken = true;
                        if (_userToken != null)
                        {
                            return _userToken;
                        }
                        else if (_deviceToken != null)
                        {
                            return _deviceToken;
                        }
                        _deviceToken = GetDeviceToken();
                        OnAccessTokenChanged(_deviceToken, AccessTokenType.Device);
                        return _deviceToken;
                    }
                    finally
                    {
                        _gettingToken = false;
                    }
                }
                else
                {
                    return _userToken ?? _deviceToken;
                }
                
            }
        }

       

        public AuthenticationLevel AuthLevel {
            get;
            private set;
        }

        public event EventHandler AuthLevelChanged;

        AuthenticatedUser _user;
        public AuthenticatedUser User
        {
            get
            {
                if (_user == null && AuthLevel != AuthenticationLevel.User) {
                    this.OnAuthorizationFailure();
                }
                return _user;
            }
            private set
            {
                _user = value;
                if (_user != null)
                {
                    _userToken = _user.AccessToken;
                    PlatformAccess.Current.SetUserSetting ("UserID", _user.ID);
                }
                else
                {
                    PlatformAccess.Current.ClearUserSetting("UserID");
                    _userToken = null;
                }
                OnAccessTokenChanged (_userToken, AccessTokenType.User);
            }
        }

        public event EventHandler AuthorizationFailure; 


        public class DeviceRegistration
        {
            public string AccessToken { get; set; }
            public string ServiceRoot { get; set; }
        }



        private string GetDeviceToken()
        {
            var result = Service.CallMethodAsync<DeviceRegistration>("POST", "/devices",
                new
                {
                    AppId = AppId,
                    AppKey = AppKey,
                    ApplicationId = PlatformAccess.Current.ApplicationID,
                    Platform = PlatformAccess.Current.Platform,
                    UniqueID = PlatformAccess.Current.DeviceUniqueId,
                    Model = PlatformAccess.Current.Model,
                    OSVersion = PlatformAccess.Current.OSVersion
                }
            );
            result.Wait();
            if (result.Result.ServiceRoot != null)
            {
                Service.ServiceRoot = result.Result.ServiceRoot;
                PlatformAccess.Current.SetUserSetting ("ServiceRoot", result.Result.ServiceRoot);
            }
            return result.Result.AccessToken;
        }


        private string GetRootUrl() {
            string setting = PlatformAccess.Current.GetConfigSetting("RootUrl");
            var userSetting = PlatformAccess.Current.GetUserSetting ("ServiceRoot");
            return userSetting ?? setting ?? WebServiceUrl;
        }

        private void ClearCredentials() {
            PlatformAccess.Current.ClearUserSetting ("ServiceRoot");
            PlatformAccess.Current.ClearUserSetting ("UserID");
            PlatformAccess.Current.ClearUserSetting (this.AppId + "-DeviceAccessToken");
            PlatformAccess.Current.ClearUserSetting (this.AppId + "-UserAccessToken");
            _userToken = _deviceToken = null;
            Service.ServiceRoot = GetRootUrl ();
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
                User = new AuthenticatedUser (this, id, userToken);
            }
        }

        private void OnEnsureService()
        {
            if (this._service != null) return;



            var root = GetRootUrl ();

            this._service = BuddyServiceClientBase.CreateServiceClient(this, root);

            this._service.ServiceException += (object sender, ExceptionEventArgs e) => {

                if (e.Exception is BuddyUnauthorizedException) {
                    ClearCredentials();
                }

            };

            if (AccessToken == null)
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
                _deviceToken = token;
                key = this.AppId + "-DeviceAccessToken";
                break;
            case AccessTokenType.User:
                _userToken = token;
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

        protected virtual void OnAuthorizationFailure() {

            PlatformAccess.Current.InvokeOnUiThread (() => {

                if (this.AuthorizationFailure != null) {
                    this.AuthorizationFailure(this, new EventArgs());
                }
            });

          
        }

        protected virtual void OnAuthLevelChanged() {
           
            PlatformAccess.Current.InvokeOnUiThread (() => {

                if (this.AuthLevelChanged != null) {
                    this.AuthLevelChanged (this, EventArgs.Empty);
                }
            });
        }

        private void UpdateAccessLevel() {

            var old = AuthLevel;
            AuthenticationLevel authLevel = AuthenticationLevel.None;
            if (_deviceToken != null) authLevel = AuthenticationLevel.Device;
            if (_userToken != null) authLevel = AuthenticationLevel.User;
            AuthLevel = authLevel;

            if (old != authLevel) {
                OnAuthLevelChanged ();
            }

        }

        // service
        //
        public Task<string> PingAsync()
        {
            return Service.CallMethodAsync<string>("GET", "/service/ping", new { });
        }

        // User auth.
      public System.Threading.Tasks.Task<AuthenticatedUser> CreateUserAsync(
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

            var task = new Task<AuthenticatedUser>(() =>
            {

                var r = this.Service.CallMethodAsync<IDictionary<string, object>>("POST", "/users", new
                {
                    name = name,
                    username = username,
                    password = password,
                    email = email,
                    gender = gender,
                    defaultMetadata = defaultMetadata,
                    relationshipStatus = status
                });

                r.Wait();

                var user = new AuthenticatedUser(this, (string)r.Result["ID"], (string)r.Result["accessToken"]);

                this.User = user;
                return user;
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
        public System.Threading.Tasks.Task<AuthenticatedUser> LoginUserAsync(string username, string password)
        {
            var task = new Task<AuthenticatedUser>(() =>
            {
                    try {
                        var r = Service.CallMethodAsync<IDictionary<string, object>>("POST", "/users/login", new
                        {
                            Username = username,
                            password = password
                        });

                        r.Wait();

                        var user = new AuthenticatedUser(this, (string)r.Result["ID"], (string)r.Result["accessToken"]);

                        this.User = user;

                        return user;

                    }
                    catch(AggregateException aex) {
                        throw aex.InnerException;
                    }

            });
            task.Start();
            return task;
        }

        public Task LogoutUserAsync() {
            var t = new Task (() => {

                if (_userToken != null) {
                    _userToken = null;
                    PlatformAccess.Current.ClearUserSetting(this.AppId + "-UserAccessToken");
                }
            });
            t.Start ();
            return t;
        }

    }

    public enum AuthenticationLevel {
        None = 0,
        Device,
        User
    }
}
    














       




