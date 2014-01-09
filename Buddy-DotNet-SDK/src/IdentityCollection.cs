using System.Collections.Generic;
using System.Threading.Tasks;

namespace BuddySDK
{
    public class IdentityCollection : BuddyCollectionBase<Identity>
    {
        internal IdentityCollection(BuddyClient client)
            : base(null, client)
        {
        }

        protected override string Path
        {
            get
            {
                return "/users/identities";
            }
        }

        public Task<Identity> AddAsync(string identityProviderName, string identityID, BuddyGeoLocation location = null, BuddyPermissions read = BuddyPermissions.User, BuddyPermissions write = BuddyPermissions.User)
        {
            return AddAsyncCore(new Identity(null, this.Client)
            {
                IdentityProviderName = identityProviderName,
                IdentityID = identityID,
                Location = location,
                //  ReadPermissions = read,
                //  WritePemissions = write
            });
        }

        public Task<SearchResult<Identity>> FindAsync(string identityProviderName = null, IEnumerable<string> identityIDs = null)
        {
            return base.FindAsync(parameterCallback: (obj) =>
            {
                obj.Add("IdentityProviderName", identityProviderName);
                obj.Add("IdentityIDs", identityIDs == null ? null : string.Join(",", identityIDs));
            });
        }
    }
}
