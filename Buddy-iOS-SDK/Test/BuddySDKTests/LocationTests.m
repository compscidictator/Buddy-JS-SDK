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

-(void)setUp
{
    [super setUp];
    
    [BuddyClient initClient:AppName
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
