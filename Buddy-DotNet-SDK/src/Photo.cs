using BuddyServiceClient;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace BuddySDK
{
    [BuddyObjectPath("/photos")]
    public class Photo : BuddyBase
    {
        [Newtonsoft.Json.JsonProperty("caption")]
        public string Caption
        {
            get
            {
                return GetValueOrDefault<string>("Caption");
            }
            set
            {
                SetValue<string>("Caption", value, checkIsProp: false);
            }
        }

        [JsonIgnore]
        [System.ComponentModel.EditorBrowsable(System.ComponentModel.EditorBrowsableState.Never)]
        internal BuddyFile Data
        {
            get
            {
                return GetValueOrDefault<BuddyFile>("Data");
            }
            set
            {
                SetValue<BuddyFile>("Data", value, checkIsProp: false);
            }
        }

        public Photo()
        {

        }

        public Photo(string id = null, BuddyClient client = null)
            : base(id, client)
        {

        }

        public Task<Stream> GetFileAsync(int? size = null)
        {
            var t = new Task<Stream>(() =>
            {
                var r = Client.Service.CallMethodAsync<HttpWebResponse>(
                                  "GET", GetObjectPath() + "/file", new
                                  {
                                      Size =size
                                  });

                r.Wait();
                var response = r.Result;

                if (response == null) {
                    return null;
                }
                return response.GetResponseStream();
            });
            t.Start();
            return t;

        }

       
    }

}
