//
//  AppDelegate.h
//  PhotoAlbum
//
//  Created by Nick Ambrose on 1/27/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "BuddySDK/BPClientDelegate.h"

#define CommonAppDelegate (AppDelegate*)[[UIApplication sharedApplication] delegate]

@class MainViewController;

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic,strong) UINavigationController *navController;
@property (nonatomic,strong) MainViewController *topController;
@property (nonatomic,assign) BOOL loginPresented;

-(void) storeUsername:(NSString*)userName;
-(NSString*) fetchUsername;
-(BOOL) isUsernameSet;

// Used on logout and also if user changes after authFailure
-(void) clearDownloadedData;

// Executes Buddy Logout and clears downloaded data
-(void) doLogout;

/* Store password in a more secure place than User Preferences in a real App */
-(void) storePassword:(NSString*)userName;
-(NSString*) fetchPassword;
-(BOOL) isPasswordSet;

-(void) storeUsername:(NSString *)userName andPassword:(NSString*)password;

@end
