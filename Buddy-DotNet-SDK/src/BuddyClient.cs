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
        internal BuddyServiceClientBase Service { get; private set; }

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
                return _WebServiceUrl ?? "https://webservice.buddyplatform.com";
            }
            set {
                _WebServiceUrl = value;
            }

        }
        
        /// <summary>
        /// Initializes a new instance of the BuddyClient class. To get an application username and password, go to http://BuddySDK.com, create a new
        /// developer account and create a new application.
        /// </summary>
        /// <param name="appid">The name of the application to use with this client. Can't be null or empty.</param>
        /// <param name="appkey">The password of the application to use with this client. Can't be null or empty.</param>
        /// <param name="appVersion">Optional string that describes the version of the app you are building. This string will then be used when uploading
        /// device information to buddy or submitting crash reports.</param>
        /// <param name="autoRecordDeviceInfo">If true automatically records the current device profile with the Buddy Service (device type, os version, etc.). Note that this
        /// only works for Windows Phone clients.</param>
        public BuddyClient(string appid, string appkey, string appVersion = "1.0", BuddyClientFlags flags = BuddyClientFlags.Default)
        {


           
          
            var root = System.Configuration.ConfigurationManager.AppSettings["rootUrl"] ?? WebServiceUrl;



            this.Service = BuddyServiceClientBase.CreateServiceClient(root);

            if (String.IsNullOrEmpty(appid))
                throw new ArgumentException("Can't be null or empty.", "appName");
            if (String.IsNullOrEmpty(appkey))
                throw new ArgumentException("Can't be null or empty.", "AppKey");

            this.AppId = appid;
            this.AppKey = appkey;
            this.AppVersion = String.IsNullOrEmpty(appVersion) ? "1.0" : appVersion;
            //this.Metadata = new AppMetadata(this);
            //this.Device = new Device(this);
            //this.GameBoards = new GameBoards(this);
            //this.Sounds = new Sounds(this);

            this._flags = flags;
        }


        private string _deviceToken = null;
        private string _userToken = null;
        
        public string AccessToken
        {
            get
            {
                if (_userToken != null)
                {
                    return _userToken;
                }
                else if (_deviceToken != null)
                {
                    return _deviceToken;
                }
                return _deviceToken = GetDeviceToken();
                
            }
        }

        AuthenticatedUser _user;
        public AuthenticatedUser User
        {
            get
            {
                return _user;
            }
            private set
            {
                _user = value;
                if (_user != null)
                {
                    _userToken = _user.AccessToken;
                }
                else
                {
                    _userToken = null;
                }
            }
        }


        public class DeviceRegistration
        {
            public string AccessToken { get; set; }
            public string ServiceRoot { get; set; }
        }

        private static string DeviceUniqueId
        {
            get
            {
                var isoStore = System.IO.IsolatedStorage.IsolatedStorageFile.GetMachineStoreForAssembly();
                System.IO.IsolatedStorage.IsolatedStorageFileStream stream = null;
                if (!isoStore.FileExists("uniqueid"))
                {
                    stream = isoStore.CreateFile("uniqueid");
                    var writer = new StreamWriter(stream);
                    writer.Write(Guid.NewGuid().ToString());
                    writer.Flush();
                    stream.Seek(0, SeekOrigin.Begin);
                }
                else
                {
                    stream = isoStore.OpenFile("uniqueid", FileMode.Open);
                }
                var reader = new StreamReader(stream);
                var guid = Guid.Parse(reader.ReadToEnd());
                stream.Close();

                return guid.ToString();
            }
        }

        private string GetDeviceToken()
        {
            var result = Service.CallMethodAsync<DeviceRegistration>("POST", "/devices",
                new
                {
                    AppId = AppId,
                    AppKey = AppKey,
                    Platform = GetDeviceName(),
                    UniqueID = DeviceUniqueId,
                    Model = GetDeviceName(),
                    OSVersion = GetOSVersion()
                }
            );
            result.Wait();
            if (result.Result.ServiceRoot != null)
            {
                Service.ServiceRoot = result.Result.ServiceRoot;
            }
            return result.Result.AccessToken;
        }


               private string GetOSVersion()
        {
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

        private string GetDeviceName()
        {
#if WINDOWS_PHONE
            return Microsoft.Phone.Info.DeviceStatus.DeviceName;
#else
            return "DeviceType not found";
#endif
        }

        private string GetApplicationId()
        {

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
        }

        // service
        //
        public Task<string> PingAsync()
        {
            return Service.CallMethodAsync<string>("GET", "/service/ping", new { });
        }

        // User auth.
        //


        /// <summary>
        /// Create a new Buddy user. Note that this method internally does two web-service calls, and the IAsyncResult object
        /// returned is only valid for the first one.
        /// </summary>
        /// <param name="name">The name of the new user. Can't be null or empty.</param>
        /// <param name="password">The password of the new user. Can't be null.</param>
        /// <param name="gender">An optional gender for the user.</param>
        /// <param name="age">An optional age for the user.</param>
        /// <param name="email">An optional email for the user.</param>
        /// <param name="status">An optional status for the user.</param>
        /// <param name="fuzzLocation">Optionally set location fuzzing for this user. When enabled user location is randomized in searches.</param>
        /// <param name="celebrityMode">Optionally set the celebrity mode for this user. When enabled this user will be absent from all searches.</param>
        /// <param name="defaultMetadata">An optional custom tag for this user.</param>
        /// <returns>A Task&lt;AuthenticatedUser&gt;that can be used to monitor progress on this call.</returns>
        public System.Threading.Tasks.Task<AuthenticatedUser> CreateUserAsync(string name, string password, string username, BuddySDK.UserGender gender = UserGender.Any, DateTime? dateOfBirth = null, string email = "", BuddySDK.UserStatus status = UserStatus.Any, bool fuzzLocation = false, bool celebrityMode = false, string defaultMetadata = "")
        {

            if (String.IsNullOrEmpty(name))
                throw new ArgumentException("Can't be null or empty.", "name");
            if (password == null)
                throw new ArgumentNullException("password");
            if (dateOfBirth > DateTime.Now)
                throw new ArgumentException("dateOfBirth must be in the past.", "dateOfBirth");


            var task = new Task<AuthenticatedUser>(() =>
            {

                var r = this.Service.CallMethodAsync<IDictionary<string, object>>("POST", "/users", new
                {
                    name = name,
                    username = username,
                    password = password,
                    email = email,
                    gender = gender.ToString(),
                    defaultMetadata = defaultMetadata,
                    relationshipStatus = status
                });

                r.Wait();

                var user = new AuthenticatedUser(this, (string)r.Result["ID"], (string)r.Result["accessToken"]);

                this.User = user;
                return user;
            });

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
                var r = Service.CallMethodAsync<IDictionary<string, object>>("POST", "/users/login", new
                {
                    AccessToken = AccessToken,
                    Username = username,
                    password = password
                });

                r.Wait();

                var user = new AuthenticatedUser(this, (string)r.Result["ID"], (string)r.Result["accessToken"]);

                this.User = user;

                return user;

            });
            task.Start();
            return task;
        }














        /// <summary>
        /// Gets the optional string that describes the version of the app you are building. This string is used when uploading
        /// device information to buddy or submitting crash reports. It will default to 1.0.
        /// </summary>
        public string AppVersion { get; protected set; }

#if false
        /// <summary>
        /// Gets an object that can be used to manipulate application-level metadata. Metadata is used to store custom values on the platform.
        /// </summary>
        public AppMetadata Metadata { get; protected set; }

        /// <summary>
        /// Gets an object that can be used to record device information about this client or upload crashes.
        /// </summary>
        public Device Device { get; protected set; }

        /// <summary>
        /// Gets an object that can be used to retrieve high score rankings or search for game boards in this application.
        /// </summary>
        public GameBoards GameBoards { get; protected set; }

        /// <summary>
        /// Gets an object that can be used to retrieve sounds.
        /// </summary>
        public Sounds Sounds { get; protected set; }
       

        internal void PingInternal(Action<BuddyCallResult<string>> callback)
        {
            this.Service.Service_Ping_Get(this.AppId, this.AppKey, (bcr) =>
            {
                var result = bcr.Result;
                if (bcr.Error != null)
                {
                    callback(BuddyResultCreator.Create<string>(null, bcr.Error));
                    return;
                }
                {
                    callback(BuddyResultCreator.Create(result, bcr.Error));
                    return;
                }
                ;
            });
            return;

        }



        internal void GetServiceTimeInternal(Action<BuddyCallResult<DateTime>> callback)
        {
            this.Service.Service_DateTime_Get(this.AppId, this.AppKey, (bcr) =>
            {
                var result = bcr.Result;
                if (bcr.Error != null)
                {
                    callback(BuddyResultCreator.Create(default(DateTime), bcr.Error));
                    return;
                }
                {
                    callback(BuddyResultCreator.Create(Convert.ToDateTime(result, CultureInfo.InvariantCulture), bcr.Error));
                    return;
                }
                ;
            });
            return;

        }



        internal void GetServiceVersionInternal(Action<BuddyCallResult<string>> callback)
        {
            this.Service.Service_Version_Get(this.AppId, this.AppKey, (bcr) =>
            {
                var result = bcr.Result;
                if (bcr.Error != null)
                {
                    callback(BuddyResultCreator.Create<string>(null, bcr.Error));
                    return;
                }
                {
                    callback(BuddyResultCreator.Create(result, bcr.Error));
                    return;
                }
                ;
            });
            return;

        }



        internal void GetUserEmailsInternal(int fromRow, int pageSize, Action<BuddyCallResult<List<string>>> callback)
        {
            this.Service.Application_Users_GetEmailList(this.AppId, this.AppKey, fromRow.ToString(),
                    (fromRow + pageSize).ToString(), (bcr) =>
            {
                var result = bcr.Result;
                if (bcr.Error != null)
                {
                    callback(BuddyResultCreator.Create<List<string>>(null, bcr.Error));
                    return;
                }
                List<string> emails = new List<string>();
                foreach (var d in result)
                    emails.Add(d.UserEmail);
                {
                    callback(BuddyResultCreator.Create(emails, bcr.Error));
                    return;
                }
                ;
            });
            return;

        }

       

   

        internal void GetUserProfilesInternal(int fromRow, int pageSize, Action<BuddyCallResult<List<User>>> callback)
        {
            this.Service.Application_Users_GetProfileList(this.AppId, this.AppKey, fromRow.ToString(),
                    (fromRow + pageSize).ToString(), (bcr) =>
            {
                var result = bcr.Result;
                if (bcr.Error != null)
                {
                    callback(BuddyResultCreator.Create<List<User>>(null, bcr.Error));
                    return;
                }
                var userProfiles = new List<User>();
                foreach (var userProfile in result)
                {
                    userProfiles.Add(new User(this, userProfile));
                }
                {
                    callback(BuddyResultCreator.Create(userProfiles, bcr.Error));
                    return;
                }
                ;
            });
            return;

        }

        internal void GetApplicationStatisticsInternal(Action<BuddyCallResult<List<ApplicationStatistics>>> callback)
        {
            this.Service.Application_Metrics_GetStats(this.AppId, this.AppKey, (bcr) =>
            {
                var result = bcr.Result;
                if (bcr.Error != null)
                {
                    callback(BuddyResultCreator.Create<List<ApplicationStatistics>>(null, bcr.Error));
                    return;
                }
                List<ApplicationStatistics> stats = new List<ApplicationStatistics>();
                foreach (var d in result)
                    stats.Add(new ApplicationStatistics(d, this));
                {
                    callback(BuddyResultCreator.Create(stats, bcr.Error));
                    return;
                }
                ;
            });
            return;

        }


     

        internal void LoginInternal(string token, Action<BuddyCallResult<AuthenticatedUser>> callback)
        {

            throw new NotImplementedException();


        }

        public Task<AuthenticatedUser> SocialLoginAsync(string providerName, string providerUserId, string accessToken) 
        {
            var tcs = new TaskCompletionSource<AuthenticatedUser>();

            this.SocialLoginInternal(providerName, providerUserId, accessToken, (bcr) =>
                {
                    if (bcr.Error != BuddyServiceClient.BuddyError.None)
                    {
                        tcs.TrySetException(new BuddyServiceException(bcr.Error));
                    }
                    else
                    {
                        tcs.TrySetResult(bcr.Result);
                    }
                });
            return tcs.Task;
        }

        internal void SocialLoginInternal(string providerName, string providerUserId, string accessToken, Action<BuddyServiceClient.BuddyCallResult<AuthenticatedUser>> callback)
        {
            Dictionary<string, object> parameters = new Dictionary<string, object>();

            parameters.Add("BuddyApplicationName", this.AppId);
            parameters.Add("BuddyApplicationPassword", this.AppKey);
            parameters.Add("ProviderName", providerName);
            parameters.Add("ProviderUserId", providerUserId);
            parameters.Add("AccessToken", accessToken);

            this.Service.CallMethodAsync<InternalModels.DataContract_SocialLoginReply[]>("UserAccount_Profile_SocialLogin", parameters, (bcr) =>
                {
                    if (bcr.Result != null)
                    {
                        this.LoginInternal(bcr.Result.First().UserToken, (bdr) =>
                            {
                                callback(bdr);
                            });
                    }
                });
        }

       

 

        



        

      
        internal void LoginInternal(string username, string password, Action<BuddyCallResult<AuthenticatedUser>> callback)
        {
            if (String.IsNullOrEmpty("username"))
                throw new ArgumentException("Can't be null or empty.", "username");
            if (password == null)
                throw new ArgumentNullException("password");

            this.Service.UserAccount_Profile_Recover(this.AppId, this.AppKey, username, password, (bcr) =>
            {
                var result = bcr.Result;
                if (bcr.Error != null)
                {
                    callback(BuddyResultCreator.Create<AuthenticatedUser>(null, bcr.Error));
                    return;
                }
                LoginInternal(result, callback);

            });
            return;

        }

       

     

        internal void CheckIfEmailExistsInternal(string email, Action<BuddyCallResult<bool>> callback)
        {
            if (String.IsNullOrEmpty(email))
                throw new ArgumentException("Can't be null or empty.", "email");

            this.Service.UserAccount_Profile_CheckUserEmail(this.AppId, this.AppKey, email, (bcr) =>
            {

                callback(BuddyResultCreator.Create(bcr.Error == BuddyError.UserEmailTaken, BuddyError.None));
            });
            return;

        }

      

      

        internal void CheckIfUsernameExistsInternal(string username, Action<BuddyCallResult<bool>> callback)
        {
            if (String.IsNullOrEmpty(username))
                throw new ArgumentException("Can't be null or empty.", "username");

            this.Service.UserAccount_Profile_CheckUserName(this.AppId, this.AppKey, username, (bcr) =>
            {
                if (bcr.Error != null && bcr.Error != BuddyError.UserNameAvailble && bcr.Error != BuddyError.UserNameAlreadyInUse)
                {
                    callback(BuddyResultCreator.Create(default(bool), bcr.Error));
                    return;
                }
                callback(BuddyResultCreator.Create(bcr.Error == BuddyError.UserNameAlreadyInUse, BuddyError.None));

            });
            return;

        }


        /// <summary>
      

        internal void CreateUserInternal(string name, string password, UserGender gender, int age,
                    string email, UserStatus status, bool fuzzLocation, bool celebrityMode, string appTag, Action<BuddyCallResult<AuthenticatedUser>> callback)
        {
            if (String.IsNullOrEmpty(name))
                throw new ArgumentException("Can't be null or empty.", "name");
            if (password == null)
                throw new ArgumentNullException("password");
            if (age < 0)
                throw new ArgumentException("Can't be less than 0.", "age");

            this.Service.UserAccount_Profile_Create(this.AppId, this.AppKey, name, password,
                            gender == UserGender.Female ? "female" : "male", age, email == null ? "" : email, (int)status, fuzzLocation ? 1 : 0,
                            celebrityMode ? 1 : 0, appTag, (bcr) =>
            {
                if (bcr.Error != null)
                {
                    callback(BuddyResultCreator.Create<AuthenticatedUser>(null, bcr.Error));
                    return;
                }
                this.LoginInternal(bcr.Result, callback);
            });
            return;

        }


        //
        // Analytics_Session methods
        //

      



     

     


        internal void RecordSessionMetricInternal(AuthenticatedUser user, int sessionId, string metricKey, string metricValue, string appTag, Action<BuddyCallResult<bool>> callback)
        {
            if (user == null || user.AccessToken == null)
                throw new ArgumentNullException("user", "An AuthenticatedUser value is required for parmaeter user");
            if (String.IsNullOrEmpty(metricKey))
                throw new ArgumentException("metricKey", "metricKey must not be null or empty.");
            if (metricValue == null)
                throw new ArgumentNullException("metricValue", "metrickValue must not be null.");

            this.Service.Analytics_Session_RecordMetric(this.AppId, this.AppKey, user.AccessToken, sessionId.ToString(), metricKey, metricValue, appTag, (bcr) =>
            {
                var result = bcr.Result;
                if (bcr.Error != null)
                {
                    callback(BuddyResultCreator.Create(default(bool), bcr.Error));
                    return;
                }
                {
                    callback(BuddyResultCreator.Create(result == "1", bcr.Error));
                    return;
                }
                ;
            });
            return;

        }

        internal double TryParseDouble(string value)
        {
            double result;
            if (value == "")
            {
                return 0;
            }
            Double.TryParse(value, out result);
            if (value == "")
            {
                return result;
            }
            else
                return Convert.ToDouble(value, CultureInfo.InvariantCulture);
        }


   #if AWAIT_SUPPORTED

      /// <summary>
        /// Check if another user with the same name already exists in the system.
        /// </summary>
        /// <param name="username">The name to check for, can't be null or empty.</param>
        /// <returns>A Task&lt;Boolean&gt;that can be used to monitor progress on this call.</returns>
        public System.Threading.Tasks.Task<Boolean> CheckIfUsernameExistsAsync( string username)
        {
            var tcs = new System.Threading.Tasks.TaskCompletionSource<Boolean>();
            CheckIfUsernameExistsInternal(username, (bcr) =>
            {
                if (bcr.Error != BuddyServiceClient.BuddyError.None)
                {
                    tcs.TrySetException(new BuddyServiceException(bcr.Error));
                }
                else
                {
                    tcs.TrySetResult(bcr.Result);
                }
            });
            return tcs.Task;
        }

     

      

       

        /// <summary>
        /// Records a session metric value
        /// </summary>
        /// <param name="user">The user that is starting this session</param>
        /// <param name="sessionId">The id of the session, returned from StartSessionAsync.</param>
        /// <param name="metricKey">A custom key describing the metric.</param>
        /// <param name="metricValue">The value to set.</param>
        /// <param name="appTag">An optional custom tag to include with the metric.</param>
        /// <returns></returns>
        public System.Threading.Tasks.Task<Boolean> RecordSessionMetricAsync( BuddySDK.AuthenticatedUser user, int sessionId, string metricKey, string metricValue, string appTag = null)
        {
            var tcs = new System.Threading.Tasks.TaskCompletionSource<Boolean>();
            RecordSessionMetricInternal(user, sessionId, metricKey, metricValue, appTag, (bcr) =>
            {
                if (bcr.Error != BuddyServiceClient.BuddyError.None)
                {
                    tcs.TrySetException(new BuddyServiceException(bcr.Error));
                }
                else
                {
                    tcs.TrySetResult(bcr.Result);
                }
            });
            return tcs.Task;
        }

        /// <summary>
        /// Ping the service.
        /// </summary>
        /// <returns>A Task&lt;String&gt;that can be used to monitor progress on this call.</returns>
        public System.Threading.Tasks.Task<String> PingAsync()
        {
            var tcs = new System.Threading.Tasks.TaskCompletionSource<String>();
            PingInternal((bcr) =>
            {
                if (bcr.Error != BuddyServiceClient.BuddyError.None)
                {
                    tcs.TrySetException(new BuddyServiceException(bcr.Error));
                }
                else
                {
                    tcs.TrySetResult(bcr.Result);
                }
            });
            return tcs.Task;
        }

        /// <summary>
        /// Get the current Buddy web-service date/time.
        /// </summary>
        /// <returns>A Task&lt;DateTime&gt;that can be used to monitor progress on this call.</returns>
        public System.Threading.Tasks.Task<DateTime> GetServiceTimeAsync()
        {
            var tcs = new System.Threading.Tasks.TaskCompletionSource<DateTime>();
            GetServiceTimeInternal((bcr) =>
            {
                if (bcr.Error != BuddyServiceClient.BuddyError.None)
                {
                    tcs.TrySetException(new BuddyServiceException(bcr.Error));
                }
                else
                {
                    tcs.TrySetResult(bcr.Result);
                }
            });
            return tcs.Task;
        }

        /// <summary>
        /// Get the current version of the service that is being used by this SDK.
        /// </summary>
        /// <returns>A Task&lt;String&gt;that can be used to monitor progress on this call.</returns>
        public System.Threading.Tasks.Task<String> GetServiceVersionAsync()
        {
            var tcs = new System.Threading.Tasks.TaskCompletionSource<String>();
            GetServiceVersionInternal((bcr) =>
            {
                if (bcr.Error != BuddyServiceClient.BuddyError.None)
                {
                    tcs.TrySetException(new BuddyServiceException(bcr.Error));
                }
                else
                {
                    tcs.TrySetResult(bcr.Result);
                }
            });
            return tcs.Task;
        }

        /// <summary>
        /// Gets a list of emails for all registered users for this app.
        /// </summary>
        /// <param name="fromRow">Used for paging, retrieve only records starting fromRow.</param>
        /// <param name="pageSize">Used for paginig, specify page size.</param>
        /// <returns>A Task&lt;IEnumerable&lt;String&gt; &gt;that can be used to monitor progress on this call.</returns>
        public System.Threading.Tasks.Task<IEnumerable<String>> GetUserEmailsAsync( int fromRow, int pageSize = 10)
        {
            var tcs = new System.Threading.Tasks.TaskCompletionSource<IEnumerable<String>>();
            GetUserEmailsInternal(fromRow, pageSize, (bcr) =>
            {
                if (bcr.Error != BuddyServiceClient.BuddyError.None)
                {
                    tcs.TrySetException(new BuddyServiceException(bcr.Error));
                }
                else
                {
                    tcs.TrySetResult(bcr.Result);
                }
            });
            return tcs.Task;
        }

        /// <summary>
        /// Gets a list of all user profiles for this app.
        /// </summary>
        /// <param name="fromRow">Used for paging, retrieve only records starting fromRow.</param>
        /// <param name="pageSize">Used for paginig, specify page size.</param>
        /// <returns>A Task&lt;IEnumerable&lt;User&gt; &gt;that can be used to monitor progress on this call.</returns>
        public System.Threading.Tasks.Task<IEnumerable<User>> GetUserProfilesAsync( int fromRow, int pageSize = 10)
        {
            var tcs = new System.Threading.Tasks.TaskCompletionSource<IEnumerable<User>>();
            GetUserProfilesInternal(fromRow, pageSize, (bcr) =>
            {
                if (bcr.Error != BuddyServiceClient.BuddyError.None)
                {
                    tcs.TrySetException(new BuddyServiceException(bcr.Error));
                }
                else
                {
                    tcs.TrySetResult(bcr.Result);
                }
            });
            return tcs.Task;
        }

        /// <summary>
        /// This method will return a list of statistics for the application covering items such as total users, photos, etc. 
        /// </summary>
        /// <returns>A Task&lt;IEnumerable&lt;ApplicationStatistics&gt; &gt;that can be used to monitor progress on this call.</returns>
        public System.Threading.Tasks.Task<IEnumerable<ApplicationStatistics>> GetApplicationStatisticsAsync()
        {
            var tcs = new System.Threading.Tasks.TaskCompletionSource<IEnumerable<ApplicationStatistics>>();
            GetApplicationStatisticsInternal((bcr) =>
            {
                if (bcr.Error != BuddyServiceClient.BuddyError.None)
                {
                    tcs.TrySetException(new BuddyServiceException(bcr.Error));
                }
                else
                {
                    tcs.TrySetResult(bcr.Result);
                }
            });
            return tcs.Task;
        }




        /// <summary>
        /// Login an existing user with their secret token. Each user is assigned a token on creation, you can store it instead of a
        /// username/password combination.
        /// </summary>
        /// <param name="accessToken">The private token of the user to login.</param>
        /// <returns>A Task&lt;AuthenticatedUser&gt;that can be used to monitor progress on this call.</returns>
        public System.Threading.Tasks.Task<AuthenticatedUser> LoginAsync( string accessToken)
        {


            var tcs = new System.Threading.Tasks.TaskCompletionSource<AuthenticatedUser>();
            LoginInternal(accessToken, (bcr) =>
            {
                if (bcr.Error != BuddyServiceClient.BuddyError.None)
                {
                    tcs.TrySetException(new BuddyServiceException(bcr.Error));
                }
                else
                {
                    tcs.TrySetResult(bcr.Result);
                }
            });
            return tcs.Task;
        }

       

        /// <summary>
        /// Check if another user with the same email already exists in the system.
        /// </summary>
        /// <param name="email">The email to check for, can't be null or empty.</param>
        /// <returns>A Task&lt;Boolean&gt;that can be used to monitor progress on this call.</returns>
        public System.Threading.Tasks.Task<Boolean> CheckIfEmailExistsAsync( string email)
        {
            var tcs = new System.Threading.Tasks.TaskCompletionSource<Boolean>();
            CheckIfEmailExistsInternal(email, (bcr) =>
            {
                if (bcr.Error != BuddyServiceClient.BuddyError.None)
                {
                    tcs.TrySetException(new BuddyServiceException(bcr.Error));
                }
                else
                {
                    tcs.TrySetResult(bcr.Result);
                }
            });
            return tcs.Task;
        }
#endif

#endif



    }

   

#if false  

    /// <summary>
    /// 
    /// <example>
    /// <code>
    ///    
    /// </code>
    /// </example>
    /// </summary>
    /// 
    
    public class ApplicationStatistics
    {
        /// <summary>
        /// 
        /// </summary>
        public string TotalUsers { get; protected set; }

        /// <summary>
        /// This is the combined total of all profile photos and photo album photos for the application
        /// </summary>
        public string TotalPhotos { get; protected set; }

        /// <summary>
        /// 
        /// </summary>
        public string TotalUserCheckins { get; protected set; }

        /// <summary>
        /// 
        /// </summary>
        public string TotalUserMetadata { get; protected set; }

        /// <summary>
        /// 
        /// </summary>
        public string TotalAppMetadata { get; protected set; }

        /// <summary>
        /// 
        /// </summary>
        public string TotalFriends { get; protected set; }

        /// <summary>
        /// 
        /// </summary>
        public string TotalAlbums { get; protected set; }


        /// <summary>
        /// 
        /// </summary>
        public string TotalCrashes { get; protected set; }


        /// <summary>
        /// 
        /// </summary>
        public string TotalMessages { get; protected set; }


        /// <summary>
        /// This is the combined total of all push notifications sent for all platforms supported 
        /// </summary>
        public string TotalPushMessages { get; protected set; }


        /// <summary>
        /// 
        /// </summary>
        public string TotalGamePlayers { get; protected set; }


        /// <summary>
        /// 
        /// </summary>
        public string TotalGameScores { get; protected set; }

        /// <summary>
        /// 
        /// </summary>
        public string TotalDeviceInformation { get; protected set; }

        internal ApplicationStatistics(InternalModels.DataContract_ApplicationStats appStats, BuddyClient client)
        {
            if (client == null)
                throw new ArgumentNullException("client");

            this.TotalUsers = appStats.TotalUsers;

            this.TotalDeviceInformation = appStats.TotalDeviceInformation;

            this.TotalCrashes = appStats.TotalCrashes;

            this.TotalAppMetadata = appStats.TotalAppMetadata;

            this.TotalAlbums = appStats.TotalAlbums;

            this.TotalFriends = appStats.TotalFriends;

            this.TotalGamePlayers = appStats.TotalGamePlayers;

            this.TotalGameScores = appStats.TotalGameScores;

            this.TotalMessages = appStats.TotalMessages;

            this.TotalPhotos = appStats.TotalPhotos;

            this.TotalPushMessages = appStats.TotalPushMessages;

            this.TotalUserCheckins = appStats.TotalUserCheckins;

            this.TotalUserMetadata = appStats.TotalUserMetadata;
        }

#endif
    }


#endif
#if NET40
    public static class ReflectionExtensions
    {
        public static Type GetTypeInfo(this Type type)
        {
            return type;
        }

        public static Attribute[] GetCustomAttributes(this Assembly a)
        {
            var attrs = a.GetCustomAttributes(true);
            if (attrs == null)
            {
                return null;
            }
            Attribute[] attributes = new Attribute[attrs.Length];
            Array.Copy(attrs, attributes, attrs.Length);
            return attributes;
        }

        public static PropertyInfo GetRuntimeProperty(this Type type, string name)
        {
            return type.GetProperty(name);
        }
        public static MethodInfo GetRuntimeMethod(this Type type, string name, Type[] parameterTypes = null)
        {
            return type.GetMethod(name, parameterTypes);
        }
       
    }
#endif



