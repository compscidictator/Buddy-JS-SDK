using System;

namespace BuddySDK
{
    public class Identity : BuddyBase
    {
        [Newtonsoft.Json.JsonProperty("identityProviderName")]
        public string IdentityProviderName
        {
            get
            {
                return GetValueOrDefault<string>("IdentityProviderName");
            }
            set
            {
                SetValue<string>("IdentityProviderName", value, checkIsProp: false);
            }
        }

        [Newtonsoft.Json.JsonProperty("identityID")]
        public string IdentityID
        {
            get
            {
                return GetValueOrDefault<string>("IdentityID");
            }
            set
            {
                SetValue<string>("IdentityID", value, checkIsProp: false);
            }
        }

        public Identity()
        {
        }

        internal Identity(string id = null, BuddyClient client = null)
            : base(id, client)
        {
        }

        protected override string Path
        {
            get
            {
                return string.Format("/users/{0}/identities", Client.User.ID);
            }
        }
    }
}
