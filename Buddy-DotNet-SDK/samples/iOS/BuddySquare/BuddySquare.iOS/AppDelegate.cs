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
           
            Buddy.Init("bbbbbc.gLdbblFMNhwg", "00AD8C51-6D95-4DA1-89ED-D5FEC5083901");

            bool showingError = false;

            Func<string, string, UIAlertView> showDialog = (title, message) => {

                if (!showingError) {
                    showingError = true;

                   
                    UIAlertView uav =  
                        new UIAlertView(title, 
                            message, 
                            null, "OK");

                    uav.Dismissed += (sender, e) => {

                        showingError = false;
                    };             
                    uav.Show();
                    return uav;
                }
                return null;
            };
                                            

            Buddy.ServiceException += (client, args) => {


                showDialog("Buddy Error", String.Format("{0}\r\n{1}", args.Exception.Error, args.Exception.Message));

                args.ShouldThrow = false;

            };
           
            Buddy.AuthorizationFailure += HandleAuthorizationFailure;

            Buddy.AuthorizationLevelChanged += (sender, e) =>  {

                if (homeController == null && Buddy.CurrentUser != null) {

                        SetupNavController();
                }
                else if (Buddy.CurrentUser == null) {
                    HandleAuthorizationFailure(this, EventArgs.Empty);
                }

            };

            UIAlertView connectivityAlert = null;

            Buddy.ConnectivityLevelChanged += (sender, e) => {

               
                if (e.ConnectivityLevel == ConnectivityLevel.None) {

                    connectivityAlert = showDialog("Network", "No Connection Available");

                }
                else if(connectivityAlert != null) {
                    connectivityAlert.Hidden = true;
                    connectivityAlert = null;
                }

            };

            return true;
        }

        void HandleAuthorizationFailure (object sender, EventArgs e)
        {

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

