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


        public async Task<BuddyResult<Location>> AddAsync(
            string name, 
            string description, 
            BuddyGeoLocation location,
            string streetAddress1,
            string streetAddress2,
            string city,
            string subCountryDivision,
            string country,
            string postalCode,
            string phoneNumber,
            string faxNumber,
            string website,
            string category,
            string defaultMetadata = null, 
            BuddyPermissions read = BuddyPermissions.User, 
            BuddyPermissions write = BuddyPermissions.User)
        {

            var c = new Location(null, this.Client)
            {
                Name = name,
                Description = description,
                Location = location,
                StreetAddress1 = streetAddress1,
                StreetAddress2 = streetAddress2,
                City = city,
                SubCountryDivision = subCountryDivision,
                Country = country,
                PhoneNumber = phoneNumber,
                FaxNumber = faxNumber,
                PostalCode = postalCode,
                Category = category,
                DefaultMetadata = defaultMetadata,
                Website = new Uri(website)
            };

            var r = await c.SaveAsync();
            return r.Convert(b => c);
        }

        public Task<SearchResult<Location>> FindAsync(BuddyGeoLocationRange location, string name = null, string category = null, int maxResults = 100)
        {
            return base.FindAsync (null, null, null, location, maxResults, null, (p) => {
                p["name"] = name;
                p["category"] = category;
            });
        }
    }
}
