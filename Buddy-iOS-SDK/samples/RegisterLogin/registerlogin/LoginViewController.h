//
//  LoginViewController.h
//  registerlogin
//
//  Created by Nick Ambrose on 1/14/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "MBProgressHUD.h"

#import "BuddySDK/BuddyObject.h"

@interface LoginViewController : UIViewController <MBProgressHUDDelegate>

@property (nonatomic,weak) IBOutlet UITextField *userNameTextField;
@property (nonatomic,weak) IBOutlet UITextField *passwordTextField;

@property (nonatomic,weak) IBOutlet UIButton *loginBut;
@property (nonatomic,weak) IBOutlet UIButton *goRegisterBut;

-(IBAction) doLogin:(id)sender;
-(IBAction) goRegister:(id)sender;

-(void) populateFields;
-(void) resignTextFields;

@end
