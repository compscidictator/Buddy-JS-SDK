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
    
    dispatch_semaphore_t semaphore = dispatch_semaphore_create(0);

    [Buddy initClient:@"78766986829496375" appKey:@"783C82AE-5E11-4EEF-8A14-388EA1848060" complete:^{
        dispatch_semaphore_signal(semaphore);
    }];
    
    while (dispatch_semaphore_wait(semaphore, DISPATCH_TIME_NOW))
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode
                                 beforeDate:[NSDate dateWithTimeIntervalSinceNow:10]];}

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
    dispatch_semaphore_t semaphore = dispatch_semaphore_create(0);
    __block bool passed = NO;
    
    [[BPClient defaultClient] ping:^(NSDecimalNumber *ping) {
        NSLog(@"Hello!");
        passed = YES;
        dispatch_semaphore_signal(semaphore);
    }];
    
    // Run loop
    while (dispatch_semaphore_wait(semaphore, DISPATCH_TIME_NOW))
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode
                                 beforeDate:[NSDate dateWithTimeIntervalSinceNow:10]];
    
    if(!passed)
        XCTFail(@"No ping callback received");
}

-(void)testUserCreation
{
    dispatch_semaphore_t semaphore = dispatch_semaphore_create(0);
    __block bool passed = NO;
    
    [Buddy createUser:@"erik4" password:@"password" options:nil completed:^(BPUser *newBuddyObject) {
        //XCTAssertTrue([newBuddyObject.name isEqualToString:@"Erik"], @"Yay!");
        passed = YES;
        dispatch_semaphore_signal(semaphore);
    }];
    
    // Run loop
    while (dispatch_semaphore_wait(semaphore, DISPATCH_TIME_NOW))
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode
                                 beforeDate:[NSDate dateWithTimeIntervalSinceNow:10]];
    
    if(!passed)
        XCTFail(@"No callback received");
}

-(void)testUserLogin
{
    dispatch_semaphore_t semaphore = dispatch_semaphore_create(0);
    __block bool passed = NO;
    
    [Buddy login:@"erik4" password:@"password" completed:^(BPUser *loggedInsUser) {
        XCTAssertTrue([loggedInsUser.name isEqualToString:@"erik4"], @"Buddy object did not contain correct name");
        dispatch_semaphore_signal(semaphore);
        passed = YES;
    }];
    
    // Run loop
    while (dispatch_semaphore_wait(semaphore, DISPATCH_TIME_NOW))
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode
                                 beforeDate:[NSDate dateWithTimeIntervalSinceNow:10]];
    
    if(!passed)
        XCTFail(@"No callback received");
}

@end
