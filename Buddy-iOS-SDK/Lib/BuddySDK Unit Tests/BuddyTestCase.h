//
//  BuddyTestHelper.h
//  BuddySDK
//
//  Created by Erik Kerber on 12/6/13.
//
//

#import <XCTest/XCTest.h>
#import "TestHelper.h"

@interface BuddyTestCase : XCTestCase
@property (nonatomic, strong) TestHelper *tester;
@end