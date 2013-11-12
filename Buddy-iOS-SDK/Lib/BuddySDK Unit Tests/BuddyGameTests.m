//
//  BuddyGameTests.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/11/13.
//
//

#import <XCTest/XCTest.h>
#import "BuddyGamePlayers.h"
#import "BuddyGamePlayer.h"

@interface BuddyGameTests : XCTestCase

@end

@implementation BuddyGameTests

-(void)setUp
{
    [super setUp];
}

-(void)tearDown
{
    [super tearDown];
}

-(void)testModel
{
    BuddyGamePlayer *p = [[BuddyGamePlayer alloc] init];
    
    [p refresh];
    
    XCTFail(@"Test");
}

@end
