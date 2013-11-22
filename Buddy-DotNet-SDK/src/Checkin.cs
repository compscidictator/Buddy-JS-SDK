using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BuddySDK
{
    [BuddyObjectPath("/checkins")]
    public class Checkin : BuddyBase
    {

       
        [Newtonsoft.Json.JsonProperty("comment")]
        public string Comment
        {
            get
            {
                return GetValueOrDefault<string>("Comment");
            }
            set
            {
                SetValue<string>("Comment", value,checkIsProp:false);
            }
        }

        [Newtonsoft.Json.JsonProperty("description")]
        public string Description
        {
            get
            {
                return GetValueOrDefault<string>("Description");
            }
            set
            {
                SetValue<string>("Description", value, checkIsProp: false);
            }
        }


        public Checkin()
        {

        }
       
        public Checkin(string id= null, BuddyClient client = null)
            : base(id, client)
        {

        }

        public override Task SaveAsync()
        {
            if (Location == null)
            {
                throw new ArgumentException("Location is required.");
            }
            return base.SaveAsync();
        }
    }

    
}
