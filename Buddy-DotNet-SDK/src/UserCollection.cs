using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Threading.Tasks;

namespace BuddySDK
{
    public class UserCollection : BuddyCollectionBase<User>
    {
        internal UserCollection(BuddyClient client)
            : base(null, client)
        {
        }

        public Task<IEnumerable<User>> FindByIdentitiesAsync(string identityProviderName, IEnumerable<string> identityIDs = null)
        {
            var t = new Task<IEnumerable<User>>(() =>
            {
                var r = Client.Service.CallMethodAsync<IEnumerable<string>>("GET", Path + "/identities", new
                        {
                            IdentityProviderName = identityProviderName,
                            IdentityIDs = identityIDs == null ? null : string.Join("\t", identityIDs)
                        });

                r.Wait();

                var users = new ObservableCollection<User>();
                foreach (var userID in r.Result)
                {
                    var user = new User(Client, userID);

                    users.Add(user);
                }
                return users;
            });
            t.Start();
            return t;
        }
    }
}
