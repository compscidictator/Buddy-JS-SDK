﻿using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Reflection;

namespace BuddySDK
{
    public abstract class BuddyCollectionBase<T> where T: BuddyBase, new()
    {
        private BuddyClient _client;
        private string _path;

        protected BuddyClient Client {
            get {
                return _client ?? Buddy.Instance;
            }
        }

        protected virtual string Path
        {
            get
            {
                if (_path == null)
                {
                    var attr = typeof(T).GetCustomAttribute<BuddyObjectPathAttribute>(true);
                    if (attr != null)
                    {
                        _path = attr.Path;
                    }
                }
                return _path;
            }
        }

        protected BuddyCollectionBase(string path = null, BuddyClient client = null)
        {
            this._path = path;
            this._client = client;
        }


        protected async Task<BuddyResult<T>> AddAsyncCore(T item)
        {
            var r = await item.SaveAsync ();
           

            return r.Convert (b => item);
        }

        protected Task<SearchResult<T>> FindAsync(
            string userId = null,
            DateTime? startDate = null, 
            DateTime? endDate = null, 
            BuddyGeoLocationRange location = null, int maxItems = 100, string pagingToken = null, Action<IDictionary<string, object>> parameterCallback = null)
        {
            Task<SearchResult<T>> t = new Task<SearchResult<T>>(() =>
            {
                    IDictionary<string,object> obj = new Dictionary<string, object>(StringComparer.InvariantCultureIgnoreCase){
                        {"userID", userId},
                        {"startDate", startDate},
                        {"endDate", endDate},
                        {"location", location},
                        {"limit", maxItems}
                    };

                    if (pagingToken != null) {
                        obj.Clear();
                        obj["token"] = pagingToken;
                    }

                    if (parameterCallback != null) {
                        parameterCallback(obj);
                    }

                    var r = Client.CallServiceMethod<SearchResult<IDictionary<string, object>>>("GET",
                            Path, obj
                            ).Result;


                    var sr = new SearchResult<T>();

                    sr.Error = r.Error;
                    sr.RequestID = r.RequestID;

                    if (r.IsSuccess) {

                        sr.NextToken = r.Value.NextToken;
                        sr.PreviousToken = r.Value.PreviousToken;
                        sr.CurrentToken = r.Value.CurrentToken;

                        var items = new ObservableCollection<T>();
                        foreach (var d in r.Value.PageResults)
                        {
							var parameters = new List<object>();
							if (typeof(T).GetConstructor(new Type[] { typeof(string), typeof(BuddyClient) }) != null)
							{
								parameters.Add(Path);
							}
							parameters.Add(Client);
							T item = (T)Activator.CreateInstance(typeof(T), parameters.ToArray());
                            item.Update(d);
                            items.Add(item);
                        }
                        sr.PageResults = items;
                    }
                    return sr;
            });
            t.Start();
            return t;
        }
    }
}