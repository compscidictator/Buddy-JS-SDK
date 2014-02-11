using System;
using Newtonsoft.Json;

namespace BuddySDK
{
    [JsonConverter(typeof(BuddyLocationGeoConverter))]
    public class BuddyGeoLocation
    {
        [JsonProperty("latitude")]
        public double Latitude { get; set; }

        [JsonProperty("longitude")]
        public double Longitude { get; set; }

        public string LocationID { get; private set; }

        public BuddyGeoLocation()
        {
        }

		public static BuddyGeoLocation Parse(object latLng)
		{
			var regex = new System.Text.RegularExpressions.Regex(
				"^([-+]?\\d{1,2}(?:[.]\\d+)?),\\s*([-+]?\\d{1,3}(?:[.]\\d+)?)$");

			var matches = regex.Match(latLng.ToString());

			double latitude, longitude;
			if (matches.Success && matches.Groups.Count == 3 && double.TryParse(matches.Groups [1].Value, out latitude) &&
				double.TryParse(matches.Groups [2].Value, out longitude))
			{
				return new BuddyGeoLocation (latitude, longitude);
			}
			else
			{
				return JsonConvert.DeserializeObject<BuddyGeoLocation>(latLng.ToString());
			}
		}

        public BuddyGeoLocation(string locationId)
        {
            LocationID = locationId;
        }

        public BuddyGeoLocation(double lat, double lng)
        {
            Latitude = lat;
            Longitude = lng;
        }
        public override string ToString()
        {
            if (LocationID == null) {
                return String.Format ("{0},{1}", Latitude, Longitude);
            }
            return LocationID;
        }
    }



    public class BuddyGeoLocationRange : BuddyGeoLocation 
    {
        public int DistanceInMeters {get;set;}

        public BuddyGeoLocationRange(double lat, double lng, int distanceInMeters) : base(lat, lng)
        {
            DistanceInMeters = distanceInMeters;
        } 

        public override string ToString()
        {
            return String.Format("{0},{1},{2}", Latitude, Longitude, DistanceInMeters);
        }
    }
}

