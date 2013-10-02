using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using StartupExplorer.Models;
using StartupExplorer.Helper;

namespace StartupExplorer.Controllers
{
    public class HomeController : Controller
    {
        #region View section Starts

        /// <summary>
        /// GET: /Index
        /// </summary>
        /// <returns></returns>
        public ActionResult Index()
        {
            return View();
        }

        /// <summary>
        /// GET: /SubmitStartup
        /// </summary>
        /// <returns></returns>
        public ActionResult SubmitStartup()
        {
            return View();
        }

        [HttpPost]
        public ActionResult SubmitStartup(HomeViewModel model)
        {
            if (ModelState.IsValid)
            {
                Utility.SendEmail(model.StartUpContactEmail, "New Startup has been submitted!!", Utility.MailFormat(model.StartUpName, model.StartUpCity, model.StartUpCategory, model.StartUpStreet, model.StartUpContactName, model.StartUpZip, model.StartUpContactEmail), true);
                Utility.SendEmail("startupexplorer@buddy.com", "New Startup has been submitted!!", Utility.MailFormat(model.StartUpName, model.StartUpCity, model.StartUpCategory, model.StartUpStreet, model.StartUpContactName, model.StartUpZip, model.StartUpContactEmail), true);

                TempData["ViewMessage"] = "Mail Sent Successfully.";
                return RedirectToAction("SubmitStartup", "Home");
                //return View(new HomeViewModel { ViewMessage = "Mail Sent successfully." });
            }
            return View(new HomeViewModel { ViewMessage = "Please fill all mandatory fields." });
        }

        //public ActionResult ClearAllFields(HomeViewModel model)
        //{
        //    return model;
        //}
        #endregion


    }
}
