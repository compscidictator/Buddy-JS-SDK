using System;

namespace BuddySDK
{
    public class MetadataItem
    {
        public string Key { get; set; }
        public object Value { get; set; }
        public BuddyGeoLocation Location { get; set; }
        public DateTime Created { get; set; }
        public DateTime LastModified { get; set; }
    }

    [BuddyObjectPath("/metadata")]
    public class Metadata : BuddyMetadataBase
    {
        public Metadata()
            : base()
        {
        }

        internal Metadata(BuddyClient client)
            : base(client)
        {
        }

        protected override string GetMetadataPath(string key = null)
        {
            var path = "/metadata";

            if (!string.IsNullOrEmpty(key))
            {
                path += string.Format("/{0}", key);
            }

            return path;
        }
    }
}