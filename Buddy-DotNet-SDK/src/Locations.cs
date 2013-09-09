using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Buddy.BuddyService;

namespace Buddy
{
    /// <summary>
    /// Represents an object that can be used to search for physical locations around the user.
    /// <example>
    /// <code>
    ///     BuddyClient client = new BuddyClient("APPNAME", "APPPASS");
    ///     AuthenticatedUser user = client.Login("username2", "password2");
    ///     List&lt;Location&gt; locations = user.Locations.Find(1000000, 0.0, 0.0);
    /// </code>
    /// </example>
    /// </summary>
    public class Locations
    {
        protected BuddyClient Client { get; set; }
        protected AuthenticatedUser User { get; set; }

        internal Locations(BuddyClient client, AuthenticatedUser user)
        {
            if (client == null) throw new ArgumentNullException("client");
            if (user == null) throw new ArgumentNullException("user");
            this.Client = client;
            this.User = user;
        }

        /// <summary>
        /// Find a location close to a given latitude and logitude.
        /// </summary>
        /// <param name="searchDistanceInMeters">The radius of the location search.</param>
        /// <param name="latitude">The latitude where the search should start.</param>
        /// <param name="longitude">The longitude where the search should start.</param>
        /// <param name="numberOfResults">Optional number of result to return, defaults to 10.</param>
        /// <param name="searchForName">Optional search string, for example: "Star*" to search for all place that start with the string "Star"</param>
        /// <param name="searchCategoryId">Optional search category ID to narrow down the search with.</param>
        /// <returns>A list of location that were found.</returns>
        public List<Location> Find(int searchDistanceInMeters, double latitude, double longitude, int numberOfResults = 10, string searchForName = "", int searchCategoryId = -1)
        { return FindInteral(null, searchDistanceInMeters, latitude, longitude, numberOfResults, searchForName, searchCategoryId);  }

        /// <summary>
        /// Find a location close to a given latitude and logitude.
        /// </summary>
        /// <param name="callback">The async callback to call on success or error. The first parameter is a list of locations that were found.</param>
        /// <param name="searchDistanceInMeters">The radius of the location search.</param>
        /// <param name="latitude">The latitude where the search should start.</param>
        /// <param name="longitude">The longitude where the search should start.</param>
        /// <param name="numberOfResults">Optional number of result to return, defaults to 10.</param>
        /// <param name="searchForName">Optional search string, for example: "Star*" to search for all place that start with the string "Star"</param>
        /// <param name="searchCategoryId">Optional search category ID to narrow down the search with.</param>
        public void FindAsync(Action<List<Location>, Exception> callback, int searchDistanceInMeters, double latitude, double longitude, int numberOfResults = 10, string searchForName = "", int searchCategoryId = -1)
        { FindInteral(callback == null ? (r, ex) => { } : callback, searchDistanceInMeters, latitude, longitude, numberOfResults, searchForName, searchCategoryId);  }

        protected List<Location> FindInteral(Action<List<Location>, Exception> callback, int searchDistanceInMeters, double latitude, double longitude, int numberOfResults = 10, string searchForName = "", int searchCategoryId = -1)
        {
            if (searchDistanceInMeters <= 0) throw new ArgumentException("Can't be smaller or equal to zero.", "searchDistanceInMeters");
            if (latitude > 90.0 || latitude < -90.0) throw new ArgumentException("Can't be bigger than 90.0 or smaller than -90.0.", "atLatitude");
            if (longitude > 180.0 || longitude < -180.0) throw new ArgumentException("Can't be bigger than 180.0 or smaller than -180.0.", "atLongitude");
            if (numberOfResults <= 0) throw new ArgumentException("Can't be smaller or equal to zero.", "numberOfResults");
            if (searchForName == null) searchForName = "";

            return this.Client.InvokeServiceMethod<List<Location>, GeoLocation_Location_SearchCompletedEventArgs>(callback != null,
                (state) => this.Client.Service.GeoLocation_Location_SearchAsync(this.Client.AppName, this.Client.AppPassword, this.User.Token, searchDistanceInMeters.ToString(),
                    latitude.ToString(), longitude.ToString(), numberOfResults.ToString(), searchForName, searchCategoryId >= 0 ? searchCategoryId.ToString() : "", state),
                (result) => {
                    if (result.Result == null) throw new ServiceUnkownErrorException();

                    List<Location> lst = new List<Location>();
                    foreach (var d in result.Result) lst.Add(new Location(this.Client, this.User, d));
                    return lst;
                },
                callback);
        }
    }
}
