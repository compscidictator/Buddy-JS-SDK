using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Linq;
using System.Reflection;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace BuddySDK
{
    [AttributeUsage(AttributeTargets.Class)]
    public class BuddyObjectPathAttribute : Attribute
    {
        public string Path { get; set; }
        public BuddyObjectPathAttribute(string path)
        {
            if (String.IsNullOrEmpty(path)) throw new ArgumentNullException("path");
            this.Path = path;
        }
    }

    public class BuddyLocationGeoConverter : JsonConverter {

        public override bool CanConvert(Type objectType)
        {
            return objectType == typeof(string);
        }

        public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
        {
            var obj = (JObject)serializer.Deserialize(reader);
            return new BuddyGeoLocation()
            {
                Latitude = (double)obj.Property("latitude").Value,
                Longitude = (double)obj.Property("longitude").Value
            };
        }

        public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
        {
            BuddyGeoLocation bcl = (BuddyGeoLocation)value;

            if (bcl.LocationID == null)
            {
                writer.WriteValue(String.Format("{0},{1}", bcl.Latitude, bcl.Longitude));
            }
            else
            {
                writer.WriteValue(String.Format("{0}", bcl.LocationID));
            }
        }
    }

    public enum BuddyPermissions
    {
        Owner,
        App,
        Default = Owner
    }

    [JsonConverter(typeof(BuddyLocationGeoConverter))]
    public class BuddyGeoLocation
    {
        [JsonProperty("latitude")]
        public double Latitude { get; set; }

        [JsonProperty("longitude")]
        public double Longitude { get; set; }


       
        public string LocationID { get; set; }

        public BuddyGeoLocation()
        {
        }


        public BuddyGeoLocation(double lat, double lng)
        {
            Latitude = lat;
            Longitude = lng;
        }
        public override string ToString()
        {
            return String.Format("{0},{1}", Latitude, Longitude);
        }
    }
   
    public abstract class BuddyBase : System.ComponentModel.INotifyPropertyChanged
    {

        private static Dictionary<Type, List<Tuple<string, string>>> _propMappings = new Dictionary<Type, List<Tuple<string, string>>>();

        protected static void EnsureMappings(object t)
        {
            if (_propMappings.ContainsKey(t.GetType()))
            {
                return;
            }
            var l = new List<Tuple<string, string>>();
            foreach (var prop in t.GetType().GetProperties())
            {
                string jsonName = prop.Name;

                var attr = prop.GetCustomAttribute<Newtonsoft.Json.JsonPropertyAttribute>();

                if (attr != null && attr.PropertyName != null)
                {
                    jsonName = attr.PropertyName;
                }

                l.Add(new Tuple<string, string>(prop.Name, jsonName));
            }
            _propMappings[t.GetType()] = l;
        }

        private BuddyClient _client;
        protected BuddyClient Client {
            get {
                return _client ?? Buddy.Instance;
            }
        }

        protected bool IsDeleted
        {
            get;
            private set;
        }

        [JsonProperty("id")]
        public string ID {
            get
            {
                return GetValueOrDefault<string>("ID");
            }
        }


        [JsonProperty("location")]
        public BuddyGeoLocation Location
        {
            get
            {
                return GetValueOrDefault<BuddyGeoLocation>("Location");
            }
            set
            {
                SetValue<BuddyGeoLocation>("Location", value, checkIsProp:false);
            }
        }

        [JsonProperty("created")]
        public DateTime Created {
            get {
                return GetValueOrDefault<DateTime>("Created");
            }
            set
            {
                SetValue<DateTime>("Created", value, checkIsProp:false);
            }
        }

        [JsonProperty("lastModified")]
        public DateTime LastModified {
            get {
                return GetValueOrDefault<DateTime>("LastModified");
            }
            set
            {
                SetValue<DateTime>("LastModified", value, checkIsProp:false);
            }
        }

        [JsonProperty("defaultMetadata")]
        public string DefaultMetadata { 

            get {
                return GetValueOrDefault<string>("DefaultMetadata");
            }
            set
            {
                SetValue<string>("DefaultMetadata", value, checkIsProp:false);
            }
        }



        protected virtual string Path
        {
            get
            {
                var attr = this.GetType().GetCustomAttribute<BuddyObjectPathAttribute>(true);
                if (attr == null)
                {
                    throw new NotImplementedException("BuddyObjectPathAttribute required");
                }
                return attr.Path;
            }
        }
        

        private bool _isPopulated = false;

        protected BuddyBase()
        {
            EnsureMappings(this);
        }

        protected BuddyBase(BuddyClient client = null, string id = null)
            : this(id, client)
        {

        }

        protected BuddyBase(string id = null, BuddyClient client = null)  : this()
        {
           
            if (client != null) {
                this._client = client;
            }
            if (id != null)
            {
                SetValue<string>("ID", id);
            }
        }

        protected virtual void EnsureValid()
        {
            if (IsDeleted)
            {
                throw new ObjectDisposedException("This object has been deleted.");
            }
        }

        protected BuddyBase(BuddyClient client, AuthenticatedUser user):this(client)
        {
            throw new NotImplementedException();
           
            
        }

        public virtual Task DeleteAsync()
        {
            EnsureValid();
            Task t = new Task(() =>
            {
                var r = Client.Service.CallMethodAsync<bool>("DELETE", GetObjectPath(), new
                {
                    Client.AccessToken
                });

                r.Wait();
                this.IsDeleted = true;
            });

            t.Start();
            return t;


        }

        protected virtual string GetObjectPath()
        {
            if (ID == null) throw new InvalidOperationException("ID required.");
           
            var id = Regex.Match(ID, "\\d+").Value;
            return String.Format("{0}/{1}", Path, id);
        }

        Task _pendingRefresh;
        public virtual Task FetchAsync(Action updateComplete = null)
        {
            EnsureValid();
            lock (this)
            {
                if (_pendingRefresh != null)
                {
                    _pendingRefresh.Wait();
                }
                else
                {
                    _pendingRefresh = new Task( () =>
                   {

                       var r =  Client.Service.CallMethodAsync<IDictionary<string, object>>(
                              "GET", GetObjectPath());

                        r.Wait();
                        Update(r.Result);
                       _pendingRefresh = null;
                       
                   });
                    _pendingRefresh.Start();
                }
            }     
            return _pendingRefresh;
        }

        public void Invalidate()
        {
            if (null == _pendingRefresh)
            {
                _isPopulated = false;
            }
        }


        public bool IsDirty
        {
            get
            {
                return (from t in _values.Values where t.IsDirty select t).Any();
            }
        }

        private class ValueEntry
        {
            public object Value { get; set; }
            public bool IsDirty { get; set; }

            public ValueEntry(object value, bool isDirty = false)
            {
                Value = value;
                IsDirty = isDirty;
            }
        }

        private IDictionary<string, ValueEntry> _values = new Dictionary<string, ValueEntry>(StringComparer.InvariantCultureIgnoreCase);

        protected T GetValueOrDefault<T>(string key, T defaultValue = default(T), bool autoPopulate = true)
        {
            EnsureValid();
            ValueEntry  v;
            if (_values.TryGetValue(key, out v))
            {
                return (T)Convert.ChangeType(v.Value, typeof(T));
            }
            else if (autoPopulate && !_isPopulated)
            {
                var r = FetchAsync();
                r.Wait();
                return GetValueOrDefault<T>(key, defaultValue, false);
            }
            return defaultValue;
        }

        protected virtual void SetValueCore<T>(string key, T value)
        {
            if (key == "Location" && !(value is BuddyGeoLocation))
            {
                value = (T)(object) JsonConvert.DeserializeObject<BuddyGeoLocation>(value.ToString());
               
            }
            _values[key] = new ValueEntry(value, true);
        }

        protected void SetValue<T>(string key, T value, bool notify = true, bool checkIsProp = true)
        {
            EnsureValid();
            if (checkIsProp && this.GetType().GetProperty(key) == null)
            {
                throw new ArgumentException(String.Format("{0} is not a property on {1}", key, GetType().Name));
            }

            var oldValue = GetValueOrDefault<T>(key, autoPopulate:false);
            if (Object.Equals(value, oldValue))
            {
                return;
            }
            SetValueCore<T>(key, value);
            if (notify)
            {
                OnPropertyChanged(key);
            }
        }

        public virtual Task SaveAsync()
        {
            EnsureValid();
            Task t = new Task(() => { });

            var isNew = String.IsNullOrEmpty(GetValueOrDefault<string>("ID", null,false));
            if (IsDirty || isNew)
            {
                // gather dirty props.
                var d = new Dictionary<string, object>(StringComparer.InvariantCultureIgnoreCase);
                var mappings = _propMappings[GetType()];
                foreach (var kvp in _values)
                {
                    if (kvp.Value.IsDirty)
                    {
                        var name = mappings.Where(m => m.Item1 == kvp.Key).Select(m => m.Item2).FirstOrDefault();
                        d[name ?? kvp.Key] = kvp.Value.Value;
                    }
                }
                t = new Task(() =>
                {
                    IDictionary<string, object> updateDict = null;
                    if (isNew)
                    {
                        var r = Client.Service.CallMethodAsync<string>("POST", Path, d);
                        r.Wait();
                        updateDict = new Dictionary<string, object>(StringComparer.InvariantCultureIgnoreCase) {
                           { "ID", r.Result }
                        };
                    }
                    else
                    {
                        var r = Client.Service.CallMethodAsync<IDictionary<string, object>>("PATCH", Path, d);
                        r.Wait();
                        updateDict = r.Result;
                    }

                    Client.Service.CallOnUiThread(() =>
                    {
                        Update(updateDict);
                    });
                });
            }
            t.Start();
            return t;
        }

        internal void UpdateFrom(BuddyBase other)
        {
            EnsureValid();
            if (this.GetType().IsInstanceOfType(other))
            {
                throw new ArgumentException();
            }

            var d = new Dictionary<string, object>(StringComparer.InvariantCultureIgnoreCase);
            foreach (var kvp in other._values)
            {
                d[kvp.Key] = kvp.Value.Value;
            }
            Update(d);
        }

        internal void Update(IDictionary<string, object> values)
        {
            _isPopulated = true;
            var mappings = _propMappings[this.GetType()];

            foreach (var kvp in values)
            {
                string name = mappings.Where(m => m.Item2 == kvp.Key).Select(i => i.Item1).FirstOrDefault();

                SetValue(name ?? kvp.Key, kvp.Value, false, false);
            }
            foreach (var kvp in _values)
            {
                kvp.Value.IsDirty = false;
            }
            OnPropertyChanged (null);
        }

        // Metadata stuff

        private string GetMetadataPath(string key) {
            return string.Format ("/metadata/{0}/{1}", ID, key);
        }

        private Task SetMetadataCore(string key, object value, BuddyPermissions permissions) {
            var t = Client.Service.CallMethodAsync<bool> ("PUT", 
                                                          GetMetadataPath (key), 
                                                          new  {
                value = value,
                permissions = permissions
            });

            Task retTask = new Task (() => {
                t.Wait ();
            });
            retTask.Start ();
            return retTask;
        }

        public Task SetMetadataAsync(string key, string value, BuddyPermissions permissions = BuddyPermissions.Default) {
            return SetMetadataCore (key, value, permissions);
        }

        public Task SetMetadataAsync(string key, int value, BuddyPermissions permissions = BuddyPermissions.Default) {
            return SetMetadataCore (key, value, permissions);
        }

        private Task<T> GetMetatadataCore<T>(string key) {
            return Client.Service.CallMethodAsync<T> ("GET", GetMetadataPath (key));                                               
        }

        public Task<int> GetMetadataIntAsync(string key) {
            return GetMetatadataCore<int> (key);
        }

        public Task<string> GetMetadataStringAsync(string key) {
            return GetMetatadataCore<string> (key);
        }

        public Task<int> IncrementMetadataAsync(string key, int delta) {
            var path = String.Format ("/metadata/{0}/{1}/increment", ID, key);

            var r = Client.Service.CallMethodAsync<int> ("POST",path,
                                               new {
                accessToken = Client.AccessToken,
                delta = delta
            });    
            r.Start ();
            return r;
        }

        public Task<bool> DeleteMetadataAsync(string key) {

            var t = Client.Service.CallMethodAsync<bool> ("DELETE", 
                                                          GetMetadataPath (key)
                                                         );

            t.Start ();
            return t;
        }

        #region IPropertyNotifyChangedStuff

        public event System.ComponentModel.PropertyChangedEventHandler PropertyChanged;

        protected virtual void OnPropertyChanged(string prop) {
            if (PropertyChanged != null)
            {
                PlatformAccess.Current.InvokeOnUiThread (() => {
                    PropertyChanged (this, new System.ComponentModel.PropertyChangedEventArgs (prop));
                });
            }
         }


        #endregion

    }
}
