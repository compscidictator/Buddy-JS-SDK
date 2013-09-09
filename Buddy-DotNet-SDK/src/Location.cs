using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Buddy.BuddyService;

namespace Buddy
{
    /// <summary>
    /// Represents a single, named location in the Buddy system that's not a user. Locations are related to stores, hotels, parks, etc.
    /// <example>
    /// <code>
    ///     BuddyClient client = new BuddyClient("APPNAME", "APPPASS");
    ///     AuthenticatedUser user = client.Login("username2", "password2");
    ///     List&lt;Location&gt; locations = user.Locations.Find(1000000, 0.0, 0.0);
    /// </code>
    /// </example>
    /// </summary>
    public class Location
    {
        protected BuddyClient Client { get; set; }
        protected AuthenticatedUser User { get; set; }

        /// <summary>
        /// Gets the address of the location.
        /// </summary>
        public string Address { get; protected set; }

        /// <summary>
        /// Gets the custom application tag data for the location.
        /// </summary>
        public string AppTagData { get; protected set; }

        /// <summary>
        /// Gets the category ID of the location (i.e. Hotels).
        /// </summary>
        public int CategoryID { get; protected set; }
        
        /// <summary>
        /// Gets the category name for the location.
        /// </summary>
        public string CategoryName { get; protected set; }

        /// <summary>
        /// Gets the city for the location.
        /// </summary>
        public string City { get; protected set; }
        
        /// <summary>
        /// Gets the date the location was created in the system.
        /// </summary>
        public DateTime CreatedDate { get; protected set; }

        /// <summary>
        /// If this user profile was returned from a search, gets the distance in kilometers from the search origin.
        /// </summary>
        public double DistanceInKiloMeters { get; protected set; }

        /// <summary>
        /// If this user profile was returned from a search, gets the distance in meters from the search origin.
        /// </summary>
        public double DistanceInMeters { get; protected set; }

        /// <summary>
        /// If this user profile was returned from a search, gets the distance in miles from the search origin.
        /// </summary>
        public double DistanceInMiles { get; protected set; }

        /// <summary>
        /// If this user profile was returned from a search, gets the distance in yards from the search origin.
        /// </summary>
        public double DistanceInYards { get; protected set; }

        /// <summary>
        /// Gets the fax number of the location.
        /// </summary>
        public string Fax { get; protected set; }
        
        /// <summary>
        /// Gets the globaly unique ID of the location.
        /// </summary>
        public int ID { get; protected set; }
        
        /// <summary>
        /// Gets the latitude of the location.
        /// </summary>
        public double Latitude { get; protected set; }

        /// <summary>
        /// Gets the longitude of the location.
        /// </summary>
        public double Longitude { get; protected set; }
        
        /// <summary>
        /// Gets the name of the location.
        /// </summary>
        public string Name { get; protected set; }

        /// <summary>
        /// Gets the postal state of the location.
        /// </summary>
        public string PostalState { get; protected set; }

        /// <summary>
        /// Gets the postal ZIP of the location.
        /// </summary>
        public string PostalZip { get; protected set; }

        /// <summary>
        /// Gets the region of the location.
        /// </summary>
        public string Region { get; protected set; }

        /// <summary>
        /// Gets the ShortID of the location.
        /// </summary>
        public string ShortID { get; protected set; }

        /// <summary>
        /// Gets the telephone number of the location.
        /// </summary>
        public string Telephone { get; protected set; }

        /// <summary>
        /// Gets the last update date of the location.
        /// </summary>
        public DateTime TouchedDate { get; protected set; }

        /// <summary>
        /// Gets the user tag data of the location.
        /// </summary>
        public string UserTagData { get; protected set; }

        /// <summary>
        /// Gets the website of the location.
        /// </summary>
        public string Website { get; protected set; }

        internal Location(BuddyClient client, AuthenticatedUser user, DataContract_SearchPlaces place)
        {
            if (client == null) throw new ArgumentNullException("client");
            if (user == null) throw new ArgumentNullException("user");
            this.Client = client;
            this.User = user;

            this.Address = place.Address;
            this.AppTagData = place.AppTagData;
            this.CategoryID = Int32.Parse(place.CategoryID);
            this.CategoryName = place.CategoryName;
            this.City = place.City;
            this.CreatedDate = DateTime.Parse(place.CreatedDate);
            this.DistanceInKiloMeters = Double.Parse(place.DistanceInKilometers);
            this.DistanceInMeters = Double.Parse(place.DistanceInMeters);
            this.DistanceInMiles = Double.Parse(place.DistanceInMiles);
            this.DistanceInYards = Double.Parse(place.DistanceInYards);
            this.Fax = place.Fax;
            this.ID = Int32.Parse(place.GeoID);
            this.Latitude = Double.Parse(place.Latitude);
            this.Longitude = Double.Parse(place.Longitude);
            this.Name = place.Name;
            this.PostalState = place.PostalState;
            this.PostalZip = place.PostalZip;
            this.Region = place.Region;
            this.ShortID = place.ShortID;
            this.Telephone = place.Telephone;
            this.TouchedDate = DateTime.Parse(place.TouchedDate);
            this.UserTagData = place.UserTagData;
            this.Website = place.WebSite;
        }
    }
}
