using System.IO;
using System.Net;
using System.Reflection;
using System.Threading.Tasks;

namespace BuddySDK
{
	[BuddyObjectPath("/items")]
	public class AlbumItem : BuddyBase
	{
        public AlbumItem()
            : base()
        {
        }

        public AlbumItem(string path, BuddyClient client)
            : base(client)
        {
            this.path = path;
        }
        
        [Newtonsoft.Json.JsonProperty("comment")]
		public string Comment
		{
			get
			{
				return GetValueOrDefault<string>("Comment");
			}
			set
			{
				SetValue<string>("Comment", value, checkIsProp: false);
			}
		}

		[Newtonsoft.Json.JsonProperty("itemId")]
		public string ItemId
		{
			get
			{
				return GetValueOrDefault<string>("ItemId");
			}
			set
			{
				SetValue<string>("ItemId", value, checkIsProp: false);
			}
		}

        private readonly string path;
        protected override string Path
        {
            get
            {

                return this.path;
            }
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

    public class AlbumItemCollection : BuddyCollectionBase<AlbumItem>
    {
		internal AlbumItemCollection(string parentObjectPath, BuddyClient client)
			: base(parentObjectPath + typeof(AlbumItem).GetCustomAttribute<BuddyObjectPathAttribute>(true).Path, client)
        {
        }
    }
}