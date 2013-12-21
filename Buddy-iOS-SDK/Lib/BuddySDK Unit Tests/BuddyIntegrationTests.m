//
//  BuddyIntegrationTests.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/25/13.
//
//

#import "BuddyIntegrationTests.h"
#import "Buddy.h"


@interface BuddyIntegrationTests()
@end

//#define APP_NAME @"92538700814090257"
//#define APP_KEY @"55E419A6-C732-4C5A-9778-0B62F66323FE"



@implementation BuddyIntegrationTests

-(void)setUp
{
    [super setUp];

    [Buddy initClient:APP_NAME appKey:APP_KEY complete:^{
        [self.tester signalDone];
    }];
    
    [self.tester wait];
}

-(void)tearDown
{
    [super tearDown];
}

-(void)testBuddyAppIdPassword
{
    __block bool passed = NO;
    
    [[BPClient defaultClient] ping:^(NSDecimalNumber *ping) {
        passed = YES;
        [self.tester signalDone];
    }];
    
    [self.tester wait];
    
    if(!passed)
        XCTFail(@"No ping callback received");
}

-(void)testUserCreation
{
    __block bool passed = NO;
    
    NSDictionary *options = @{@"name": @"Erik Kerber",
                              @"gender": @(BPUserGender_Male),
                              @"email": @"erik.kerber@gmail.com"};
    
    [Buddy createUser:TEST_USERNAME password:TEST_PASSWORD options:options completed:^(BPUser *newBuddyObject) {
        
        // Hmm, this will only "pass" if we don't have a user. Maybe change this if I implement a delete test.
        if(newBuddyObject.userName){
            XCTAssertTrue([newBuddyObject.userName isEqualToString:TEST_USERNAME], @"Buddy object did not contain correct name");
        }

        passed = YES;
        [self.tester signalDone];
    }];
    
    [self.tester wait];
    
    if(!passed)
        XCTFail(@"No callback received");
}

-(void)testUserLogin
{
    __block bool passed = NO;
    
    [Buddy login:TEST_USERNAME password:TEST_PASSWORD completed:^(BPUser *loggedInsUser) {
        XCTAssertTrue([loggedInsUser.userName isEqualToString:TEST_USERNAME], @"Buddy object did not contain correct name");
        [self.tester signalDone];
        passed = YES;
    }];
    
    [self.tester wait];
    
    if(!passed)
        XCTFail(@"No callback received");
}

@end
