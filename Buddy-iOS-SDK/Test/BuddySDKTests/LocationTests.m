//
//  LocationTests.m
//  BuddySDKTests
//
//  Created by Erik Kerber on 9/15/13.
//
//

#import "LocationTests.h"
#import <BuddySDK/Buddy.h>

@implementation LocationTests

static NSString *AppName = @"Buddy iOS SDK test app";
static NSString *AppPassword = @"8C9E044D-7DB7-42DE-A376-16460B58008E";
//static bool bwaiting = false;
//static NSString *Token = @"UT-76444f9f-4a4b-4d3d-ba5c-7a82b5dbb5a5";

-(void)setUp
{
    [super setUp];
    
    [Buddy initClient:AppName
                appPassword:AppPassword];
    
    
    STAssertNotNil([BuddyClient defaultClient], @"TestFriendRequest failed buddyClient nil");
}

-(void)tearDown
{
    [super tearDown];
}

-(void)testLocationEnabled
{
    
}

@end
