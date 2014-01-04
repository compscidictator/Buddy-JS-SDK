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
		Unknown,
		Male,
        Female
    }

    /// <summary>
    /// Represents the status of the user.
    /// </summary>
    public enum UserRelationshipStatus
    {
		Unknown = 0,
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
    /// 
    [BuddyObjectPath("/users")]
    public class User : BuddyBase
    {
		[JsonProperty("firstName")]
		public string FirstName
        {
            get
            {
				return GetValueOrDefault<string>("FirstName");
            }
            set
            {
				SetValue("FirstName", value);
            }
        }

		[JsonProperty("lastName")]
		public string LastName
		{
			get
			{
				return GetValueOrDefault<string>("LastName");
			}
			set
			{
				SetValue("LastName", value);
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

       
        public string ProfilePictureID
        {
            get
            {
                return GetValueOrDefault<string>("ProfilePictureID");
            }
            set
            {
                SetValue<string>("ProfilePictureID", value, checkIsProp: false);
            }
        }

		
        internal User(BuddyClient client, string id)
            : base(client, id)
        {
        }
    }
}
