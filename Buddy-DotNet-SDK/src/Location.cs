using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BuddySDK
{
    [BuddyObjectPath("/locations")]
    public class Location : BuddyBase
    {


        [Newtonsoft.Json.JsonProperty("name")]
        public string Name
        {
            get
            {
                return GetValueOrDefault<string>("Name");
            }
            set
            {
                SetValue<string>("Name", value,checkIsProp:false);
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
                SetValue<string>("Description", value,checkIsProp:false);
            }
        }

        [Newtonsoft.Json.JsonProperty("streetAddress")]
        public string Address
        {
            get
            {
                return GetValueOrDefault<string>("Address");
            }
            set
            {
                SetValue<string>("Address", value,checkIsProp:false);
            }
        }

        [Newtonsoft.Json.JsonProperty("city")]
        public string City
        {
            get
            {
                return GetValueOrDefault<string>("City");
            }
            set
            {
                SetValue<string>("City", value,checkIsProp:false);
            }
        }

        [Newtonsoft.Json.JsonProperty("state")]
        public string State
        {
            get
            {
                return GetValueOrDefault<string>("State");
            }
            set
            {
                SetValue<string>("State", value,checkIsProp:false);
            }
        }

        [Newtonsoft.Json.JsonProperty("country")]
        public string Country
        {
            get
            {
                return GetValueOrDefault<string>("Country");
            }
            set
            {
                SetValue<string>("Country", value,checkIsProp:false);
            }
        }

        [Newtonsoft.Json.JsonProperty("zipcode")]
        public string PostalCode
        {
            get
            {
                return GetValueOrDefault<string>("PostalCode");
            }
            set
            {
                SetValue<string>("PostalCode", value,checkIsProp:false);
            }
        }


      
        [Newtonsoft.Json.JsonProperty("fax")]
        public string FaxNumber
        {
            get
            {
                return GetValueOrDefault<string>("FaxNumber");
            }
            set
            {
                SetValue<string>("FaxNumber", value,checkIsProp:false);
            }
        }

        [Newtonsoft.Json.JsonProperty("phone")]
        public string PhoneNumber
        {
            get
            {
                return GetValueOrDefault<string>("PhoneNumber");
            }
            set
            {
                SetValue<string>("PhoneNumber", value,checkIsProp:false);
            }
        }

        [Newtonsoft.Json.JsonProperty("website")]
        public Uri Website
        {
            get
            {
                return GetValueOrDefault<Uri>("Website");
            }
            set
            {
                SetValue<Uri>("Website", value,checkIsProp:false);
            }
        }


        [Newtonsoft.Json.JsonProperty("categoryId")]
        public string CategoryID
        {
            get
            {
                return GetValueOrDefault<string>("CategoryID");
            }
            set
            {
                SetValue<string>("CategoryID", value,checkIsProp:false);
            }
        }


        [Newtonsoft.Json.JsonProperty("distanceFromSearch")]
        public double Distance
        {
            get
            {
                return GetValueOrDefault<double>("Distance");
            }
            internal set
            {
                SetValue<double>("Distance", value,checkIsProp:false);
            }
        }



        public Location()
        {

        }

        public Location(string id= null, BuddyClient client = null)
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
