using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BuddySDK
{

    [Flags]
    public enum BuddyClientFlags
    {
        AutoTrackLocation =  0x0000001,
        AutoRegisterDevice = 0x00000002,
        Default = AutoTrackLocation | AutoRegisterDevice
    }

    public static class Buddy
    {
        static BuddyClient _client;
        static Tuple<string, string, BuddyClientFlags> _creds;

        public static BuddyClient Instance
        {
            get
            {
                if (_creds == null)
                {
                    throw new InvalidOperationException("Init must be called before accessing Instance.");
                }
                if (_client == null)
                {
                    _client = new BuddyClient(_creds.Item1, _creds.Item2, _creds.Item3);
                }
                return _client;
            }
        }

        public static AuthenticatedUser CurrentUser
        {
            get
            {
                return Instance.User;
            }
        }

        public static void Init(string appId, string appKey, BuddyClientFlags flags = BuddyClientFlags.Default)
        {
            if (_creds != null)
            {
                throw new InvalidOperationException("Already initalized.");
            }
            _creds = new Tuple<string, string, BuddyClientFlags>(appId, appKey, flags);

        }

        public static Task<AuthenticatedUser> CreateUserAsync(string username, string password, string name = null, string email = null, UserGender? gender = null, DateTime? dateOfBirth = null, string defaultMetadata = null) {
            return Instance.CreateUserAsync (username, password, name, email, gender, dateOfBirth, defaultMetadata : defaultMetadata);
        }

        public static Task<AuthenticatedUser> LoginUserAsync(string username, string password)
        {
            var t = Instance.LoginUserAsync(username, password);

            return t;
        }

        public static Task<SocialAuthenticatedUser> SocialLoginUserAsync(string identityProviderName, string identityID, string identityAccessToken)
        {
            var t = Instance.SocialLoginUserAsync(identityProviderName, identityID, identityAccessToken);

            return t;
        }

        // 
        // Metrics
        //

        public static Task<string> RecordMetricAsync(string key, IDictionary<string, object> value = null, TimeSpan? timeout = null) {
            return Instance.RecordMetricAsync (key, value, timeout);
        }

        public static Task<TimeSpan?> RecordTimedMetricEndAsync(string timedMetricId) {
            return Instance.RecordTimedMetricEndAsync (timedMetricId);
        }

        // 
        // Collections.
        //

        private static CheckinCollection _checkins;
        public static CheckinCollection Checkins
        {
            get
            {
                if (_checkins == null)
                {
                    _checkins = new CheckinCollection(Instance);
                }
                return _checkins;
            }
        }


        private static LocationCollection _locations;

        public static LocationCollection Locations
        {
            get
            {
                if (_locations == null)
                {
                    _locations = new LocationCollection(Instance);
                }
                return _locations;
            }
        }

        private static PhotoCollection _photos;

        public static PhotoCollection Photos
        {
            get
            {
                if (_photos == null)
                {
                    _photos = new PhotoCollection(Instance);
                }
                return _photos;
            }
        }

        private static AlbumCollection _albums;

        public static AlbumCollection Albums
        {
            get
            {
                if (_albums == null)
                {
                    _albums = new AlbumCollection(Instance);
                }

                return _albums;
            }
        }
    }
}
