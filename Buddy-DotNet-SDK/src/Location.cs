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

        [Newtonsoft.Json.JsonProperty("streetAddress1")]
        public string StreetAddress1
        {
            get
            {
                return GetValueOrDefault<string>("StreetAddress1");
            }
            set
            {
                SetValue<string>("StreetAddress1", value, checkIsProp: false);
            }
        }

        [Newtonsoft.Json.JsonProperty("streetAddress2")]
        public string StreetAddress2
        {
            get
            {
                return GetValueOrDefault<string>("StreetAddress2");
            }
            set
            {
                SetValue<string>("StreetAddress2", value, checkIsProp: false);
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

        [Newtonsoft.Json.JsonProperty("subCountryDivision")]
        public string SubCountryDivision
        {
            get
            {
                return GetValueOrDefault<string>("SubCountryDivision");
            }
            set
            {
                SetValue<string>("SubCountryDivision", value, checkIsProp: false);
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

        [Newtonsoft.Json.JsonProperty("postalCode")]
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
                SetValue<Uri>("Website", value, checkIsProp:false);
            }
        }

        [Newtonsoft.Json.JsonProperty("category")]
        public string Category
        {
            get
            {
                return GetValueOrDefault<string>("Category");
            }
            set
            {
                SetValue<string>("Category", value,checkIsProp:false);
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

        public override Task<BuddyResult<bool>> SaveAsync()
        {
            if (Location == null)
            {
                throw new ArgumentException("Location is required.");
            }
            return base.SaveAsync();
        }
    }
}
