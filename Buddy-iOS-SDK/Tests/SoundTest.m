//
//  SoundTest.m
//  BuddySDKTests
//
//  Created by Ryan Brandenburg on 5/28/13.
//
//



#import "SoundTest.h"
#import "TestBuddySDK.h"
#import <BuddySDK/Buddy.h>

@class BuddyAuthenticatedUser;
@class BuddySounds;

@implementation SoundTest 

static NSString *AppName = @"Buddy iOS SDK test app";
static NSString *AppPassword = @"8C9E044D-7DB7-42DE-A376-16460B58008E";
static bool bwaiting = false;
static NSString *Token = @"UT-76444f9f-4a4b-4d3d-ba5c-7a82b5dbb5a5";

- (void)setUp
{
    [super setUp];
    
    [Buddy initClient:AppName
                appPassword:AppPassword];
    
    
    STAssertNotNil([BuddyClient defaultClient], @"TestFriendRequest failed buddyClient nil");
}

- (void)tearDown
{
    [super tearDown];
}

- (void)waitloop
{
    NSDate *loopTil = [NSDate dateWithTimeIntervalSinceNow:20];
    
    while (bwaiting && [loopTil timeIntervalSinceNow] > 0)
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:loopTil];
}

- (void)alogin
{
    [[BuddyClient defaultClient] login:Token  callback:[^(BuddyAuthenticatedUserResponse *response)
         {
             if (response.isCompleted && response.result)
             {
                 NSLog(@"Login OK");
             }
             else
             {
                 STFail(@"alogin failed !response.isCompleted || !response.result");
             }
             bwaiting = false;
         } copy]];
}

-(void)testSound
{
    bwaiting = true;
    [self alogin];
    [self waitloop];
    
    int icount =1;
    while (icount != 0)
    {
        bwaiting = true;
        [self getSound];
        [self waitloop];
        
        icount--;
    }
}

-(void)getSound
{
    [[BuddyClient defaultClient].sounds getSound:@"european_city_ambience_01_120_loop" quality:Low callback:^(NSData *data){
        if(!data || data.length < 1)
        {
            STFail(@"getSound failed");
        }
    }];
}


@end
