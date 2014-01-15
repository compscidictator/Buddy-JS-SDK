using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Threading.Tasks;

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
        public User()
        {
        }

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
        public UserGender? Gender
        {
            get
            {
                return GetValueOrDefault<UserGender?>("Gender");
            }
            set
            {
                SetValue<UserGender?>("Gender", value, checkIsProp: false);
            }
        }
      

        public DateTime? DateOfBirth
        {
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
        public int? Age
        {
            get
            {
                var dob = this.DateOfBirth;

                if (dob != null)
                {
                    return (int)(DateTime.Now.Subtract (dob.Value).TotalDays / 365.25);
                }

                return null;
            }
        }

        [JsonProperty("profilePictureID")]
        public string ProfilePictureID
        {
            get
            {
                var profilePictureID = GetValueOrDefault<string>("ProfilePictureID");

                if (string.IsNullOrEmpty(profilePictureID) && profilePicture != null) // this catches the case where a picture has been created manually
                {
                    profilePictureID = profilePicture.ID;
                }

                return profilePictureID;
            }
            set
            {
                ProfilePicture = value == null ? null : new Photo(value);
            }
        }

        [JsonProperty("profilePictureUrl")]
        public string ProfilePictureUrl
        {
            get
            {
                return profilePicture == null ? null : profilePicture.SignedUrl;
            }
        }

        private Photo profilePicture;
        public Photo ProfilePicture
        {
            get
            {
                return profilePicture;
            }
            set
            {
                profilePicture = value;

                SetValue<string>("ProfilePictureID", value == null ? null : value.ID, checkIsProp: false);
            }
        }

        internal User(BuddyClient client, int id)
            : base(client, id.ToString())
        {
        }
		
        internal User(BuddyClient client, string id)
            : base(client, id)
        {
        }

        public Task<BuddyResult<Photo>> AddProfilePictureAsync(string caption, Stream photoData, string contentType, BuddyGeoLocation location = null, BuddyPermissions read = BuddyPermissions.User, BuddyPermissions write = BuddyPermissions.User)
        {
           var tr = PhotoCollection.AddAsync(this.Client, caption, photoData, contentType, location, read, write);


            tr.ContinueWith((t) => {

                if (t.Result.IsSuccess) {
                    profilePicture = t.Result.Value;
                }
            });
        
            return tr;

        }

        public override async Task<BuddyResult<bool>> FetchAsync(Action updateComplete = null)
        {

            var r = await base.FetchAsync(updateComplete);


            if (r.IsSuccess) {

                if (!string.IsNullOrEmpty(ProfilePictureID))
                {
                    profilePicture = new Photo(ProfilePictureID);
                    await profilePicture.FetchAsync();
                }
            }
                  
            return r;
        }

        public override async Task<BuddyResult<bool>> SaveAsync()
        {
            ProfilePictureID = profilePicture.ID;
            Username = Username; // TODO: user name is required on PATCH, so do this to ensure it gets added to the PATCH dictionary.  Remove when user name is optional

            return await Task.Run<BuddyResult<bool>> (async () => {


                var baseResult = await base.SaveAsync();

                var pictureResult = await profilePicture.SaveAsync();


                if (!pictureResult.IsSuccess) {

                    return pictureResult;
                }

                return baseResult;
            });

        }

        public Task<BuddyResult<bool>> AddIdentityAsync(string identityProviderName, string identityID)
        {
            return AddRemoveIdentityCoreAsync("POST", GetObjectPath() + "/identities", new
                    {
                        IdentityProviderName = identityProviderName,
                        IdentityID = identityID
                    });
        }

        public Task<BuddyResult<bool>> RemoveIdentityAsync(string identityProviderName, string identityID)
        {
            return AddRemoveIdentityCoreAsync("DELETE", GetObjectPath() + "/identities/" + identityProviderName, new { IdentityID = identityID });
        }

        private Task<BuddyResult<bool>> AddRemoveIdentityCoreAsync(string verb, string path, object parameters)
        {
            return Task.Run<BuddyResult<bool>>(() =>
            {
                 var r = Client.CallServiceMethod<string>(verb, path, parameters);
                 return r.Result.Convert(s  => r.Result.IsSuccess);
            });
           
        }

        public Task<BuddyResult<IEnumerable<string>>> GetIdentitiesAsync(string identityProviderName)
        {
            return Client.CallServiceMethod<IEnumerable<string>>("GET", GetObjectPath() + "/identities/" + identityProviderName);

        }
    }
}