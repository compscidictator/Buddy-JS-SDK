using Newtonsoft.Json;
using System.Threading.Tasks;

namespace BuddySDK
{
    [BuddyObjectPath("/albums")]
	public class Album : BuddyBase
	{
        public Album()
            : base()
        {
        }

		internal Album(BuddyClient client)
			: base(null, client)
		{

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

		[Newtonsoft.Json.JsonProperty("name")]
		public string Name
		{
			get
			{
				return GetValueOrDefault<string>("Name");
			}
			set
			{
				SetValue<string>("Name", value, checkIsProp: false);
			}
		}

		private AlbumItemCollection _items;
        public AlbumItemCollection Items
        {
            get
            {
                if (_items == null)
                {
                    _items = new AlbumItemCollection(this, this.Client);
                }

                return _items;
            }
        }

		internal string ObjectPath
        {
            get
            {
                return this.GetObjectPath();
            }
        }
	}

    public class AlbumCollection : BuddyCollectionBase<Album>
    {
        public AlbumCollection()
            : base()
        {
        }

        internal AlbumCollection(BuddyClient client)
            : base(null, client)
        {
        }

        public Task<Album> AddAsync(string name, string comment,
            BuddyGeoLocation location, string defaultMetadata = null, BuddyPermissions readPermissions = BuddyPermissions.User, BuddyPermissions writePermissions = BuddyPermissions.User)
        {
            Task<Album> ct = new Task<Album>(() =>
            {
                var c = new Album(this.Client)
                {
                    Name = name,
                    Comment = comment,
                    Location = location,
                    DefaultMetadata = defaultMetadata,
                    ReadPermissions = readPermissions,
                    WritePermissions = writePermissions
                };

                var t = c.SaveAsync();
                t.Wait();
                return c;
            });
            ct.Start();
            return ct;
        }
    }
}