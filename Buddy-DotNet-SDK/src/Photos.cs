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

        internal static Task<Photo> AddAsync(BuddyClient client, string caption, Stream photoData, string contentType, BuddyGeoLocation location = null, BuddyPermissions read = BuddyPermissions.User, BuddyPermissions write = BuddyPermissions.User)
        {
            Task<Photo> ct = new Task<Photo>(() =>
            {
                var c = new Photo(null, client)
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

        public Task<Photo> AddAsync(string caption, Stream photoData, string contentType, BuddyGeoLocation location, BuddyPermissions read = BuddyPermissions.User, BuddyPermissions write = BuddyPermissions.User)
        {
            return PhotoCollection.AddAsync(this.Client, caption, photoData, contentType, location, read, write);
        }
    }
}
