using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BuddySDK
{
    public class PhotoCollection : BuddyCollectionBase<Photo>
    {

        internal PhotoCollection(BuddyClient client)
            : base(null, client)
        {

        }


        public Task<Photo> AddAsync(string caption, Stream photoData, string contentType, BuddyGeoLocation location, BuddyPermissions read = BuddyPermissions.User, BuddyPermissions write = BuddyPermissions.User)
        {
            Task<Photo> ct = new Task<Photo>(() =>
            {
                var c = new Photo(null, this.Client)
                {
                    Caption = caption,
                    Location = location,
                    Data = new BuddyServiceClient.BuddyFile()
                    {
                         ContentType = contentType,
                         Data = photoData,
                         Name = "data"
                    }
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
