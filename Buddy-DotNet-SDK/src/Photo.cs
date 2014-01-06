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
    [BuddyObjectPath("/pictures")]
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

        [Newtonsoft.Json.JsonProperty("signedUrl")]
        public string SignedUrl
        {
            get
            {
                return GetValueOrDefault<string>("signedUrl");
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
                if (string.IsNullOrEmpty(SignedUrl))
                {
                    FetchAsync().Wait();
                }

                    var r = Client.Service.CallMethodAsync<HttpWebResponse>(
                                  "GET", SignedUrl.ToString(), new
                                      {
                                      Size = size
                                      });

                    r.Wait();
                    var response = r.Result;

                    if (response == null) {
                        return null;
                    }
                    return response.GetResponseStream();
                  /* Introduced by 889c2c76f3940fedc68d5d27851ea913982bdc21 }
                    catch (Exception ex) {
                        System.Diagnostics.Debug.WriteLine("Failed to get photo: {0}: ", ex.ToString());
                        return null;
                    }*/
            });
            t.Start();
            return t;
        }
    }
}
