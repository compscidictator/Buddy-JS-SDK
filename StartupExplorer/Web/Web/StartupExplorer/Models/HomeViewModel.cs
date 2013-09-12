using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations;

namespace StartupExplorer.Models
{
    public class HomeViewModel
    {
        /// <summary>
        /// Constructor with no parameters
        /// </summary>
        public HomeViewModel() { }

        #region Submit Your StartUp Starts
        [Required(ErrorMessage = "Must supply a Satrtup Name")]        
        public string StartUpName { get; set; }

        [Required(ErrorMessage = "Must supply a Satrtup City")]        
        public string StartUpCity { get; set; }

        [Required(ErrorMessage = "Must supply a Satrtup Category")]        
        public string StartUpCategory { get; set; }

        //[Required(ErrorMessage = "Must supply a Satrtup Street")]        
        public string StartUpStreet { get; set; }


        [Required(ErrorMessage = "Must supply a Satrtup Conatct Name")]        
        public string StartUpContactName { get; set; }

        //[Required(ErrorMessage = "Must supply a Satrtup Zip ")]        
        public string StartUpZip { get; set; }

        [Required(ErrorMessage = "Must supply a Satrtup Contact Email")]        
        public string StartUpContactEmail { get; set; }

        public string ViewMessage = string.Empty;
        #endregion
    }
}