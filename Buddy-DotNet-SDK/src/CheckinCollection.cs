using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BuddySDK
{
    public class CheckinCollection : BuddyCollectionBase<Checkin>
    {

        internal CheckinCollection(BuddyClient client)
            : base(null, client)
        {

        }


        public Task<Checkin> AddAsync(string comment, string description, BuddyGeoLocation location, BuddyPermissions read = BuddyPermissions.Owner, BuddyPermissions write = BuddyPermissions.Owner)
        {
            Task<Checkin> ct = new Task<Checkin>(() =>
            {
                var c = new Checkin(null, this.Client)
                {
                    Comment = comment,
                    Description = description,
                    Location = location,
                    //  ReadPermissions = read,
                    //  WritePemissions = write
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
