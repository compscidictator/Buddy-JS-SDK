﻿using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using Newtonsoft.Json.Linq;
using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Reflection;
using System.Threading;
using System.Threading.Tasks;

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

    [JsonConverter(typeof(StringEnumConverter))]
    public enum BuddyPermissions
    {
        App,
        User,
        Default = User
    }

    public abstract class BuddyMetadataBase
    {
        private BuddyClient _client;
        protected BuddyClient Client
        {
            get
            {
                return _client ?? Buddy.Instance;
            }
        }

        protected BuddyMetadataBase(BuddyClient client = null)
        {
            if (client != null)
            {
                this._client = client;
            }
        }

        private string GetMetadataPath(string key = null)
        {
            var path = string.Format("/metadata/{0}", GetMetadataID());

            if (!string.IsNullOrEmpty(key))
            {
                path += string.Format("/{0}", key);
            }

            return path;
        }

        protected abstract string GetMetadataID();

        private Task<BuddyResult<bool>> SetMetadataCore(string key, object value, BuddyPermissions permissions)
        {
            return Client.CallServiceMethod<bool>("PUT", GetMetadataPath(key), new
                {
                    value = value,
                    permissions = permissions
                });
        }

        private Task<BuddyResult<bool>> SetMetadataCore(IDictionary keyValuePairs, BuddyPermissions permissions)
        {
            return Client.CallServiceMethod<bool>("PUT", GetMetadataPath(), new
                {
                    keyValuePairs = keyValuePairs,
                    permissions = permissions
                });
        }

        public Task<BuddyResult<bool>> SetMetadataAsync(string key, string value, BuddyPermissions permissions = BuddyPermissions.Default)
        {
            return SetMetadataCore(key, value, permissions);
        }

        public Task<BuddyResult<bool>> SetMetadataAsync(string key, int value, BuddyPermissions permissions = BuddyPermissions.Default)
        {
            return SetMetadataCore(key, value, permissions);
        }

        public Task<BuddyResult<bool>> SetMetadataAsync(IDictionary<string, string> keyValues, BuddyPermissions permissions = BuddyPermissions.Default)
        {
            return SetMetadataCore((IDictionary)keyValues, permissions);
        }

        public Task<BuddyResult<bool>> SetMetadataAsync(IDictionary<string, int> keyValues, BuddyPermissions permissions = BuddyPermissions.Default)
        {
            return SetMetadataCore((IDictionary)keyValues, permissions);
        }

        public Task<BuddyResult<int>> GetMetadataIntAsync(string key)
        {
            return GetMetatadataCore<int>(key);
        }

        public Task<BuddyResult<string>> GetMetadataStringAsync(string key)
        {
            return GetMetatadataCore<string>(key);
        }

        private Task<BuddyResult<T>> GetMetatadataCore<T>(string key)
        {
            return Task.Run<BuddyResult<T>>(() =>
            {
                return Client.CallServiceMethod<MetadataItem>("GET", GetMetadataPath(key)).Result.Convert<T>(mdi => (T)mdi.Value);
            });
        }

        public Task<BuddyResult<MetadataItem>> GetMetadataAsync(string key)
        {
            return GetMetatadataCore<MetadataItem>(key);
        }

        public Task<BuddyResult<int>> IncrementMetadataAsync(string key, int delta)
        {
            var path = GetMetadataPath(key) + "/increment";

            var r = Client.CallServiceMethod<int>("POST", path,
                     new
                     {
                         delta = delta
                     });

            return r;
        }

        public Task<BuddyResult<bool>> DeleteMetadataAsync(string key)
        {
            var t = Client.CallServiceMethod<bool>("DELETE", GetMetadataPath(key));

            return t;
        }
    }


    public abstract class BuddyBase : BuddyMetadataBase, System.ComponentModel.INotifyPropertyChanged
    {
        private static Dictionary<Type, List<Tuple<string, string>>> _propMappings = new Dictionary<Type, List<Tuple<string, string>>>();

        protected static List<Tuple<string, string>> EnsureMappings(object t)
        {
            if (_propMappings.ContainsKey(t.GetType()))
            {
                return _propMappings[t.GetType()];
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
            return l;
        }

        protected bool IsDeleted
        {
            get;
            private set;
        }

        public bool IsPopulated {
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
        public virtual BuddyGeoLocation Location
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

        [JsonProperty("readPermissions")]
        public BuddyPermissions ReadPermissions
        {
            get
            {
                return GetValueOrDefault<BuddyPermissions>("ReadPermissions");
            }
            set
            {
                SetValue<BuddyPermissions>("ReadPermissions", value, checkIsProp: false);
            }
        }

        [JsonProperty("writePermissions")]
        public BuddyPermissions WritePermissions
        {
            get
            {
                return GetValueOrDefault<BuddyPermissions>("WritePermissions");
            }
            set
            {
                SetValue<BuddyPermissions>("WritePermissions", value, checkIsProp: false);
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

        protected BuddyBase(BuddyClient client)
            : base(client)
        {
            EnsureMappings(this);
        }

        protected BuddyBase(string id = null, BuddyClient client = null)
            : this(client)
        {
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

        public virtual Task<BuddyResult<bool>> DeleteAsync()
        {
            EnsureValid();

           
            var r = Client.CallServiceMethod<bool>("DELETE", GetObjectPath());


            r.ContinueWith((rt) => {
                if (rt.Result.IsSuccess) {
                    this.IsDeleted = true;
                }
            });

            return r;        
        }

        protected virtual string GetObjectPath()
        {
            if (ID == null) throw new InvalidOperationException("ID required.");
           
            return String.Format("{0}/{1}", Path, ID);
        }

        protected override string GetMetadataID()
        {
            return ID;
        }

        private Task<BuddyResult<bool>> _pendingRefresh;
        public virtual async Task<BuddyResult<bool>> FetchAsync(Action updateComplete = null)
        {
            EnsureValid();

            if (_pendingRefresh != null)
            {
                return await _pendingRefresh;
            }
            else
            {
                _pendingRefresh = Client.CallServiceMethodHelper<IDictionary<string,object>, bool> (
                    "GET", 
                    GetObjectPath (), 
                    map: (d => true),
                    completed: (r1, r2) => {

                        if (r2.IsSuccess) {
                            Update(r1.Value);
                        }
                        _pendingRefresh = null;

                    });



                return await _pendingRefresh;
            }
          
        }

        public void Invalidate()
        {
            if (null == _pendingRefresh)
            {
                IsPopulated = false;
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
                return (T)ChangeType<T>(v.Value);
            }
            else if (autoPopulate && !IsPopulated)
            {
                // kick off a fetch, which will come back and update the value
                // and fire an IPNC event.
                FetchAsync().ContinueWith((r) => {});

            }
            return defaultValue;
        }

        private object ChangeType<T>(object value)
        {

            if (typeof(T).IsInstanceOfType(value)) {
                return (T)value;
            }

			var enumType = GetEnumType<T> ();

			if (enumType != null)
            {                  
                if (value is string)
                {
					// Enum.Parse doesn't handle null values
					if (value == null) {
						return default(T);
					}

					try // needed because Enum.IsDefined is case-sensitive, and passing in a non-enum string causes Enum.Parse to throw
                    {
						return Enum.Parse(enumType, (string)value, true);
                    }
                    catch
                    {
                    }
                }
                else
                {
                    if (value is long)
                    {
                        value = Convert.ToInt32(value);
                    }

					if (Enum.IsDefined(enumType, value))
                    {
						return Enum.ToObject(enumType, value);
                    }
                }
            }

			// Convert.ChangeType doesn't handle null values
			if (GetIsNullable<T>() && value == null) {
				return default(T);
			}

			// Convert.ChangeType doesn't handle nullable types
			return Convert.ChangeType(value, GetNonNullableType<T> ());
        }

		private Type GetEnumType<T>()
		{
			Type type = GetNonNullableType<T>();

			return type.IsEnum ? type : null;
		}
		
		private Type GetNonNullableType<T>()
		{
			return GetIsNullable<T>() ? Nullable.GetUnderlyingType(typeof(T)) : typeof(T);
		}

		private bool GetIsNullable<T>()
		{
			return typeof(T).IsGenericType && typeof(T).GetGenericTypeDefinition () == typeof(Nullable<>);
		}

		protected virtual void SetValueCore<T>(string key, T value)
        {
            if (key == "Location" && !(value is BuddyGeoLocation))
            {
				value = (T)(object)BuddyGeoLocation.Parse (value);
            }
            else if (key == "Website" && !(value is Uri))
            {
                value = (T)(object)new Uri((string)(object)value);
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

        public virtual Task<BuddyResult<bool>> SaveAsync()
        {
            EnsureValid();
            var isNew = String.IsNullOrEmpty(GetValueOrDefault<string>("ID", null,false));
            if (IsDirty || isNew)
            {
                // gather dirty props.
                var d = new Dictionary<string, object>(StringComparer.InvariantCultureIgnoreCase);
                var mappings = EnsureMappings (this);
                foreach (var kvp in _values)
                {
                    if (kvp.Value.IsDirty)
                    {
                        var name = mappings.Where(m => m.Item1 == kvp.Key).Select(m => m.Item2).FirstOrDefault();
                        d[name ?? kvp.Key] = kvp.Value.Value;
                    }
                }
                BuddyServiceException error = null;
                string requestId;
                return Task.Run<BuddyResult<bool>>(() =>
                {
                    IDictionary<string, object> updateDict = null;
                    if (isNew)
                    {
                        var r = Client.CallServiceMethod<IDictionary<string, object>>("POST", Path, d).Result;

                        if (r.IsSuccess)
                        {
                            updateDict = r.Value;
                        }

                        error = r.Error;
                        requestId = r.RequestID;
                    }
                    else
                    {
                        var r = Client.CallServiceMethod<IDictionary<string, object>>("PATCH", GetObjectPath(), d).Result;

                        if (r.IsSuccess)
                        {
                            updateDict = r.Value;
                        }
                        error = r.Error;
                        requestId = r.RequestID;
                    }

                    if (updateDict != null)
                    {
                        var service = Client.Service();
						service.Wait();
						service.Result.CallOnUiThread(() =>
                        {
                            Update(updateDict);
                        });
                    }

                    return new BuddyResult<bool>
                    {
                        Error = error,
                        RequestID = requestId,
                        Value = error == null
                    };
                });
            }
            else
            {
                return Task.Run<BuddyResult<bool>>(() => { return new BuddyResult<bool> { Value = true }; });
            }
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
            IsPopulated = true;
            var mappings = EnsureMappings (this);

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

        protected Task<BuddyResult<Stream>> GetFileCoreAsync(string url, object parameters) {



            return Task.Run<BuddyResult<Stream>>(async () =>
                    {

                        // need to pass accessToken as a param for these
                        // so our auth doesn't get sent through to the redirect
                        //
                        var parameterDictionary = BuddyServiceClient.BuddyServiceClientBase.ParametersToDictionary(parameters);
                        parameterDictionary["accessToken"] = await Client.GetAccessToken();

                        var r = Client.CallServiceMethod<HttpWebResponse>(
                                "GET", 
                                url, 
                            parameterDictionary
                        );

                        var result = r.Result;

                        if (result.IsSuccess && result.Value != null) {
                            var response = result.Convert(hwr => hwr.GetResponseStream());

                            return response;
                        }
                        else {
                            return result.Convert(hwr => (Stream)null);
                        }
                        
                    
                    });
               

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

    // From http://www.hanselman.com/blog/ComparingTwoTechniquesInNETAsynchronousCoordinationPrimitives.aspx
    public sealed class AsyncLock
    {
        private readonly SemaphoreSlim m_semaphore = new SemaphoreSlim(1, 1);
        private readonly Task<IDisposable> m_releaser;

        public AsyncLock()
        {
            m_releaser = Task.FromResult((IDisposable)new Releaser(this));
        }

        public Task<IDisposable> LockAsync()
        {
            var wait = m_semaphore.WaitAsync();
            return wait.IsCompleted ?
                        m_releaser :
                        wait.ContinueWith((_, state) => (IDisposable)state,
                            m_releaser.Result, CancellationToken.None,
            TaskContinuationOptions.ExecuteSynchronously, TaskScheduler.Default);
        }

        private sealed class Releaser : IDisposable
        {
            private readonly AsyncLock m_toRelease;
            internal Releaser(AsyncLock toRelease) { m_toRelease = toRelease; }
            public void Dispose() { m_toRelease.m_semaphore.Release(); }
        }
    }
}
