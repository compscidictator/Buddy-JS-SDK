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

@implementation BuddyIntegrationTests

-(void)setUp
{
    [super setUp];
    

    [Buddy initClient:@"92538700814090257" appKey:@"55E419A6-C732-4C5A-9778-0B62F66323FE" complete:^{
        [self.tester signalDone];
    }];
    
    [self.tester wait];
}

-(void)tearDown
{
    [super tearDown];
}

//-(void (^)())startTest
//{
//    dispatch_semaphore_t semaphore = dispatch_semaphore_create(0);
//
//    void (^testFinished)() = ^void(){
//        dispatch_semaphore_signal(semaphore);
//    };
//}

-(void)testBuddyAppIdPassword
{
    __block bool passed = NO;
    
    [[BPClient defaultClient] ping:^(NSDecimalNumber *ping) {
        NSLog(@"Hello!");
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
    
    [Buddy createUser:@"erik4" password:@"password" options:nil completed:^(BPUser *newBuddyObject) {
        //XCTAssertTrue([newBuddyObject.name isEqualToString:@"Erik"], @"Yay!");
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
    
    [Buddy login:@"erik4" password:@"password" completed:^(BPUser *loggedInsUser) {
        XCTAssertTrue([loggedInsUser.name isEqualToString:@"erik4"], @"Buddy object did not contain correct name");
        [self.tester signalDone];
        passed = YES;
    }];
    
    [self.tester wait];
    
    if(!passed)
        XCTFail(@"No callback received");
}

@end
