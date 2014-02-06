//
//  MainViewController.m
//  registerlogin
//
//  Created by Nick Ambrose on 1/15/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>

#import "MBProgressHUD.h"

#import "Constants.h"
#import "AppDelegate.h"
#import "MainViewController.h"

#import "BuddySDK/Buddy.h"

@interface MainViewController ()
@property (nonatomic,strong) MBProgressHUD *HUD;
@end

@implementation MainViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [[CommonAppDelegate navController] setNavigationBarHidden:YES];
    
    UIBarButtonItem *backButton =[[UIBarButtonItem alloc] initWithTitle:@"Logout"  style:UIBarButtonItemStylePlain target:self action:@selector(doLogout)];
    
    
    self.navigationItem.leftBarButtonItem = backButton;
    
    self.refreshBut.layer.cornerRadius = DEFAULT_BUT_CORNER_RAD;
    self.refreshBut.layer.borderWidth = DEFAULT_BUT_BORDER_WIDTH;
    self.refreshBut.layer.borderColor = [UIColor blackColor].CGColor;
    self.refreshBut.clipsToBounds = YES;

    self.clearUserBut.layer.cornerRadius = DEFAULT_BUT_CORNER_RAD;
    self.clearUserBut.layer.borderWidth = DEFAULT_BUT_BORDER_WIDTH;
    self.clearUserBut.layer.borderColor = [UIColor blackColor].CGColor;
    self.clearUserBut.clipsToBounds = YES;
}

-(void) updateFields
{
    [[CommonAppDelegate navController] setNavigationBarHidden:YES];
    self.mainLabel.text = [NSString stringWithFormat:@"Hi %@ %@",
                           Buddy.user.firstName, Buddy.user.lastName];
    
}

-(BuddyCompletionCallback) getRefreshCallback
{
    MainViewController * __weak weakSelf = self;
    
    return ^(NSError *error)
    {
        [weakSelf.HUD hide:TRUE afterDelay:0.1];
        self.HUD=nil;
    };
}

-(BuddyCompletionCallback) getClearUserCallback
{
    MainViewController * __weak weakSelf = self;
    
    return ^(NSError *error)
    {
        [weakSelf.HUD hide:TRUE afterDelay:0.1];
        self.HUD=nil;
    };
}

-(void)doLogout
{
    [Buddy logout:^(NSError *error)
     {
         NSLog(@"Logout Callback Called");
     }];
    
    [CommonAppDelegate authorizationNeedsUserLogin];
}

-(IBAction)doRefresh:(id)sender
{
    self.HUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    self.HUD.labelText= @"Refresh";
    self.HUD.dimBackground = YES;
    self.HUD.delegate=self;
    
    [Buddy.user refresh:[self getRefreshCallback]];
    
}

-(IBAction)doClearUser:(id)sender
{
    self.HUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    self.HUD.labelText= @"Clear User";
    self.HUD.dimBackground = YES;
    self.HUD.delegate=self;

    [Buddy logout:[self getClearUserCallback]];
}
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    [self updateFields];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    if(self.HUD!=nil)
    {
        [self.HUD hide:TRUE afterDelay:0.1];
        self.HUD=nil;
    }
    if (self.isMovingFromParentViewController)
    {
        [Buddy logout:nil];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
