using System;
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


        protected Task<T> AddAsyncCore(T item)
        {
            Task<T> task = new Task<T>( () =>
            {
                var innerTask = item.SaveAsync();
                innerTask.Wait();
                return item;
            });
            task.Start();
            return task;
        }
      
        public Task<IEnumerable<T>> FindAsync(string ownerId = null, DateTime? startDate = null, DateTime? endDate = null)
        {

            Task<IEnumerable<T>> t = new Task<IEnumerable<T>>(() =>
            {
                var r = Client.Service.CallMethodAsync < IEnumerable<IDictionary<string, object>>>("GET",
                    Path,
                    new
                    {
                        accessToken = Client.AccessToken,
                        userId = ownerId,
                        startDate=startDate,
                        endDate = endDate
                    });

                r.Wait();
                var items = new ObservableCollection<T>();
                foreach (var d in r.Result)
                {
                    T item = new T();
                    item.Update(d);
                    items.Add(item);
                }
                return items;
            });
            t.Start();
            return t;
        }
    }
}
