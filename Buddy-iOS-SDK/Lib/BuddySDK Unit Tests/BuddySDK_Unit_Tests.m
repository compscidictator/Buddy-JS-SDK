//
//  BuddySDK_Unit_Tests.m
//  BuddySDK Unit Tests
//
//  Created by Erik Kerber on 11/11/13.
//
//

#import <XCTest/XCTest.h>

@interface BuddySDK_Unit_Tests : XCTestCase

@end

@implementation BuddySDK_Unit_Tests

- (void)setUp
{
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown
{
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testExample
{
    NSString *d = [NSString stringWithFormat:@"%@", [NSNumber numberWithInteger:2]];
    NSLog(d);
    //XCTFail(@"No implementation for \"%s\"", __PRETTY_FUNCTION__);
}

@end
