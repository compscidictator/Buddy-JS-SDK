using BuddyServiceClient;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Globalization;

namespace BuddySDK
{
    /// <summary>
    /// Represents the gender of a user.
    /// </summary>
    public enum UserGender
    {
        Male,
        Female
    }

    /// <summary>
    /// Represents the status of the user.
    /// </summary>
    public enum UserRelationshipStatus
    {
        NotSpecified = 0,
        Single = 1,
        Dating = 2,
        Engaged = 3,
        Married = 4,
        Divorced = 5,
        Widowed = 6,
        OnTheProwl = 7
    }

    /// <summary>
    /// Represents a public user profile. Public user profiles are usually returned when looking at an AuthenticatedUser's friends or making a search with FindUser.
    /// <example>
    /// <code>
    ///     BuddyClient client = new BuddyClient("APPNAME", "APPPASS");
    ///     client.LoginAsync("username", "password", (user, state) => {
    ///     
    ///         // Return all users for this application.
    ///         user.FindUserAsync((users, state2) => { });
    ///     });
    /// </code>
    /// </example>
    /// </summary>
    public class User : BuddyBase
    {

        protected override string Path
        {
            get
            {
                return "/users";
            }
        }

        /// <summary>
        /// Gets the name of the user.
        /// </summary>
        /// 
        [JsonProperty("fullName")]
        public string Name
        {
            get
            {
                return GetValueOrDefault<string>("Name");
            }
            set
            {
                SetValue("Name", value);
            }
        }

      
    

        [JsonProperty("userName")]
        public string Username
        {
            get
            {
                return GetValueOrDefault<string>("Username");
            }
            set
            {
                SetValue<string>("Username", value, checkIsProp: false);
            }
            
        }

       
        /// <summary>
        /// Gets the gender of the user.
        /// </summary>
        public UserGender? Gender {
            get
            {
                return GetValueOrDefault<UserGender?>("Gender");
            }
            set
            {
                SetValue<UserGender?>("Gender", value, checkIsProp: false);
            }
        }
      

        public DateTime? DateOfBirth {
            get
            {
                return GetValueOrDefault<DateTime?>("DateOfBirth");
            }
            set
            {
                SetValue<DateTime?>("DateOfBirth", value, checkIsProp: false);
            }
        }
      
        /// <summary>
        /// Gets the age of this user.
        /// </summary>
        public int? Age { 
            get { 
            
                var dob = this.DateOfBirth;

                if (dob != null) {
                    return (int)(DateTime.Now.Subtract (dob.Value).TotalDays / 365.25);
                }
                return null;
            
            }
        }

       
        /// <summary>
        /// If this user profile was returned from a search, gets the distance in meters from the search origin.
        /// </summary>
        public double DistanceInMeters { get; protected set; }

      
        internal User(BuddyClient client, int id)
            : base(client, id.ToString())
        {
        }



        internal User(BuddyClient client, string id)
            : base(client, id)
        {

        }

    }
}
