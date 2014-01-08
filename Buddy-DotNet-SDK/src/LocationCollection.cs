using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BuddySDK
{
    public class LocationCollection : BuddyCollectionBase<Location>
    {

        internal LocationCollection(BuddyClient client)
            : base(null, client)
        {

        }


        public Task<Location> AddAsync(
            string name, 
            string description, 
            BuddyGeoLocation location, 
            string address,
            string city,
            string state,
            string postalCode,
            string website,
            string categoryId,
            string defaultMetadata = null, 
            BuddyPermissions read = BuddyPermissions.User, 
            BuddyPermissions write = BuddyPermissions.User)
        {
            Task<Location> ct = new Task<Location>(() =>
                {
                    var c = new Location(null, this.Client)
                    {
                        Name = name,
                        Description = description,
                        Location = location,
                        Address =  address,
                        City =  city,
                        State = state,
                        PostalCode = postalCode,
                        CategoryID = categoryId,
                        DefaultMetadata = defaultMetadata,
                        Website = new Uri(website)
                    };

                    var t = c.SaveAsync();
                    t.Wait();
                    return c;
                });
            ct.Start();
            return ct;
        }

        public Task<SearchResult<Location>> FindAsync(string name, BuddyGeoLocationRange location, int maxResults = 100) {

            return base.FindAsync (null, null, null, location, maxResults,null, (p) => {

                p["name"] = name;



            });

        }
    }
}
