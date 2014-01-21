using System;
using System.Collections.Generic;
using System.Linq;
using MonoTouch.Foundation;
using MonoTouch.UIKit;
using BuddySDK;

namespace BuddySquare.iOS
{
    // The UIApplicationDelegate for the application. This class is responsible for launching the
    // User Interface of the application, as well as listening (and optionally responding) to
    // application events from iOS.
    [Register ("AppDelegate")]
    public partial class AppDelegate : UIApplicationDelegate
    {
        public static AppDelegate Current {
            get;
            set;
        }

        // class-level declarations
        UIWindow window;
        HomeScreenViewController homeController;
        UINavigationController navController;

        bool showingLoginView;

        public AppDelegate() {
            Current = this;
        }

       
        public override bool WillFinishLaunching (UIApplication application, NSDictionary launchOptions)
        {
           
            Buddy.Init("bbbbbc.CPkbvKkpqJtg", "06A06E4A-D816-48D5-B07C-9F2E07C9A9F7");

            bool showingError = false;

            Buddy.ServiceException += (client, args) => {

                if (!showingError) {
                    showingError = true;

                    var msg = args.Exception.Message;
                    var err = args.Exception.Error;

                    if (args.Exception is BuddyNoInternetException) {

                        msg = "Warning - no connectivity!";
                    }

                    UIAlertView uav =  
                        new UIAlertView("Buddy Error", 
                            String.Format("{0}:{1}", msg,err  ), 
                            null, "OK");

                    uav.Dismissed += (sender, e) => {

                        showingError = false;
                    };             
                    uav.Show();
                }

                args.ShouldThrow = false;

            };
           
            Buddy.Instance.AuthorizationFailure += (obj, e) => {

                if (showingLoginView) {
                    return;
                }

                // create a login view.
                // 
                var lv = new LoginViewController(window.RootViewController, () => {
                    showingLoginView = false;
                }
                );

                showingLoginView = true;

                window.RootViewController.PresentViewController(lv, true,null);


            };

            Buddy.Instance.AuthLevelChanged += (sender, e) =>  {

                if (homeController == null && Buddy.CurrentUser != null) {

                        SetupNavController();
                }

            };

            return true;
        }


        internal void SetupNavController() {

            if (homeController != null)
                return;

            homeController = new HomeScreenViewController ();

            navController = new UINavigationController (homeController);

            window.RootViewController = navController;
        }

        //
        // This method is invoked when the application has loaded and is ready to run. In this
        // method you should instantiate the window, load the UI into it and then make the window
        // visible.
        //
        // You have 17 seconds to return from this method, or iOS will terminate your application.
        //
        public override bool FinishedLaunching (UIApplication app, NSDictionary options)
        {

           

             
            window = new UIWindow (UIScreen.MainScreen.Bounds);

            var loadingController = new LoadingViewController ();

            window.RootViewController = loadingController;
            window.MakeKeyAndVisible ();


            return true;
        }
    }
}

