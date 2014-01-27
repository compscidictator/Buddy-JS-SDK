//
//  MainViewController.h
//  registerlogin
//
//  Created by Nick Ambrose on 1/15/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MBProgressHUD.h"
#import "BuddySDK/BuddyObject.h"

@interface MainViewController : UIViewController <MBProgressHUDDelegate>

@property (nonatomic,weak) IBOutlet UILabel *mainLabel;

@property (nonatomic,weak) IBOutlet UILabel *emailLabel;

@property (nonatomic,weak) IBOutlet UIButton *refreshBut;
@property (nonatomic,weak) IBOutlet UIButton *clearUserBut;

- (void)updateFields;

-(IBAction)doRefresh:(id)sender;
-(IBAction)doClearUser:(id)sender;
-(void)doLogout;

-(BuddyCompletionCallback) getRefreshCallback;
-(BuddyCompletionCallback) getClearUserCallback;
@end
