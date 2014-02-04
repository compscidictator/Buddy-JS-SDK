//
//  AppDelegate.m
//  PhotoGallery
//
//  Created by Nick Ambrose on 1/22/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import "AppDelegate.h"
#import "BuddySDK/Buddy.h"

#import "Constants.h"
#import "PhotoList.h"

#import "MainViewController.h"
#import "LoginViewController.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    
    self.window.backgroundColor = [UIColor whiteColor];
    
    // Create/Clear all caches/data etc.
    [self clearDownloadedData];
    
    
    self.loginPresented=FALSE;
    
    MainViewController *mainVC = [[MainViewController alloc]
                                  initWithNibName:@"MainViewController" bundle:nil];
    
    mainVC.title = @"Gallery";
    
    self.topController =mainVC;
    
    self.navController=[[UINavigationController alloc] initWithRootViewController:self.topController];
    
    [self.navController setNavigationBarHidden:TRUE];
    self.window.rootViewController=self.navController;
    
    
    [Buddy initClient: APP_ID appKey: APP_KEY callback:
     ^(NSError *error)
     {
         if(error==nil)
         {
             NSLog(@"InitClient Callback Called - Success");
         }
         else
         {
             NSLog(@"InitClient Callback Called - Failed");
         }
     }];

    
    [self.window makeKeyAndVisible];
    return YES;

}

-(void) clearDownloadedData
{
    self.userPhotos = [[PhotoList alloc] init];
    
    [self.userPhotos clearAndImages:YES];
}

/* UserName */

-(void) storeUsername:(NSString*)userName;
{
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    
    [prefs setObject:userName forKey:USERNAME_PREF_KEY];
    [prefs synchronize];
}
-(NSString*) fetchUsername
{
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    
    return [prefs stringForKey:USERNAME_PREF_KEY];
}
-(BOOL) isUsernameSet
{
    NSString *userName = [self fetchUsername];
    if(userName==nil)
    {
        return FALSE;
    }
    if([userName length]==0)
    {
        return FALSE;
    }
    return TRUE;
}

/* Password - Store this somewhere more secure in a real App */

-(void) storePassword:(NSString*)userName;
{
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    
    [prefs setObject:userName forKey:PASSWORD_PREF_KEY];
    [prefs synchronize];
}
-(NSString*) fetchPassword
{
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    
    return [prefs stringForKey:PASSWORD_PREF_KEY];
}
-(BOOL) isPasswordSet
{
    NSString *password = [self fetchPassword];
    if(password==nil)
    {
        return FALSE;
    }
    if([password length]==0)
    {
        return FALSE;
    }
    return TRUE;
}

-(void)doLogout
{
    [Buddy logout:^(NSError *error)
     {
         NSLog(@"Logout Callback Called");
     }];
    
    [self.navController popToRootViewControllerAnimated:TRUE];
    [self clearDownloadedData];
    [self authorizationFailed];
}

-(void)authorizationFailed
{
    NSLog(@"auth Failed delegate called");
    
    if(self.loginPresented==TRUE)
    {
        return;
    }
    self.loginPresented=TRUE;
    LoginViewController *loginVC = [[LoginViewController alloc]
                                    initWithNibName:@"LoginViewController" bundle:nil];
    
    // Get the "topmost" VC (may need to be careful here if that VC has already presented a VC?
    [self.navController.topViewController presentViewController:loginVC animated:NO completion:nil];
    
    
}
-(void) storeUsername:(NSString *)userName andPassword:(NSString*)password
{
    [self storeUsername:userName];
    [self storePassword:password];
}


@end
