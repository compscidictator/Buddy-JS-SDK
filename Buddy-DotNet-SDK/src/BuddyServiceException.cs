using System;
using System.Net;

namespace BuddySDK
{
    /// <summary>
    /// Occurs when there is an error processing the service request.
    /// </summary>
    public class BuddyServiceException : Exception
    {
        /// <summary>
        /// The error that occured.
        /// </summary>
        public string Error { get; protected set; }

      

        internal BuddyServiceException(string error, string message): base(message)
        {
            this.Error = error;
        }
    }

    public class BuddyUnauthorizedException : BuddyServiceException {

        internal BuddyUnauthorizedException(string e, string m): base(e,m) {


        }
    }

    public class BuddyNoInternetException : BuddyServiceException {

        internal BuddyNoInternetException(string e): base(e, null) {

        }
    }
}
