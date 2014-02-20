//
//  BPViewController.m
//  BuddySocial
//
//  Created by Erik Kerber on 1/3/14.
//  Copyright (c) 2014 Buddy. All rights reserved.
//

#import "BPViewController.h"
#import <FacebookSDK/FacebookSDK.h>
#import <BuddySDK/Buddy.h>

@interface BPViewController () <FBLoginViewDelegate>

@end

@implementation BPViewController

- (IBAction)checkin:(id)sender {
    __block DescribeCheckin d = ^(id<BPCheckinProperties> checkinProperties) {
        checkinProperties.comment = @"Checkin";
        checkinProperties.description = @"Description";
        checkinProperties.location = BPCoordinateMake(1.2, 3.4);;
    };
    
    [[Buddy checkins] checkin:d callback:^(id newBuddyObject, NSError *error) {
        BPCheckin *c = newBuddyObject;
        
        assert([c.comment isEqualToString:@"Checkin"]);
    }];
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    FBLoginView *loginView = [[FBLoginView alloc] init];
    // Align the button in the center horizontally
    loginView.frame = CGRectOffset(loginView.frame, self.view.center.x - (loginView.frame.size.width / 2), self.view.center.y - (loginView.frame.size.height / 2));
    loginView.delegate = self;
    [self.view addSubview:loginView];
}


- (void)loginViewFetchedUserInfo:(FBLoginView *)loginView user:(id<FBGraphUser>)user
{
    NSLog(@"%@", user);
    NSString *fbAccessToken = [[[FBSession activeSession] accessTokenData] accessToken];

    [Buddy socialLogin:@"Facebook" providerId:user.id token:fbAccessToken success:^(id newBuddyObject, NSError *error) {
        NSLog(@"Hello");
    }];
}

@end
