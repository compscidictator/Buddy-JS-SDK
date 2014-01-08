using System;
using System.Collections.Generic;

namespace BuddySDK
{

    public class SearchResult<T>
    {
        public string NextToken { get; set; }
        public string CurrentToken { get; set; }
        public string PreviousToken { get; set; }

        public IEnumerable<T> PageResults {
            get;
            set;
        }
    }

}

