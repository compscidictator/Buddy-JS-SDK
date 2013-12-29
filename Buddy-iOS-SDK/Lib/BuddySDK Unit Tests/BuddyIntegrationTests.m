//
//  BuddyIntegrationTests.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/25/13.
//
//

#import "Buddy.h"
#import <Kiwi/Kiwi.h>

SPEC_BEGIN(BuddyIntegrationSpec)

describe(@"BPUser", ^{
    context(@"A clean boot of your app", ^{
        
        beforeAll(^{
            __block BOOL fin = NO;

            [Buddy initClient:APP_NAME appKey:APP_KEY complete:^{
                fin = YES;
            }];
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
        });
        
        afterAll(^{

        });
        
        it(@"Should allow you to create a user.", ^{
            
            __block BPUser *newUser;
            NSDictionary *options = @{@"name": @"Erik Kerber",
                                      @"gender": @(BPUserGender_Male),
                                      @"email": @"erik.kerber@gmail.com",
                                      @"dateOfBirth": [NSNull null],
                                      @"relationshipStatus": @(BPUserRelationshipStatusOnTheProwl),
                                      @"celebrityMode": @(YES),
                                      @"fuzzLocation": @(NO)
                                      };
            
            //NSString *tmpUsername = @"AB";
            [Buddy createUser:TEST_USERNAME password:TEST_PASSWORD options:options completed:^(BPUser *newBuddyObject) {
                newUser = newBuddyObject;
            }];
            
            [[expectFutureValue(newUser.userName) shouldEventuallyBeforeTimingOutAfter(2.0)] equal:TEST_USERNAME];
        });
        
        it(@"Should allow you to login.", ^{
            __block BPUser *newUser;
            
            [Buddy login:TEST_USERNAME password:TEST_PASSWORD completed:^(BPUser *loggedInsUser) {
                newUser = loggedInsUser;
            }];
            
            [[expectFutureValue(newUser.userName) shouldEventually] equal:TEST_USERNAME];
            //[[expectFutureValue(theValue(newUser.relationshipStatus)) shouldEventually] equal:theValue(BPUserRelationshipStatusOnTheProwl)];

        });
        
        it(@"Should allow you to delete a user.", ^{
            __block BOOL deleted = NO;
            
            [Buddy login:TEST_USERNAME password:TEST_PASSWORD completed:^(BPUser *loggedInsUser) {
                [loggedInsUser deleteMe:^{
                    deleted = YES;
                }];
            }];
            
            [[expectFutureValue(theValue(deleted)) shouldEventually] beYes];
            
        });
    });
});

SPEC_END
