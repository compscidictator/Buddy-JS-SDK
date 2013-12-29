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
        __block NSString *testCreateDeleteName = @"ItPutsTheLotionOnItsSkin";
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
            
            [Buddy createUser:testCreateDeleteName password:TEST_PASSWORD options:options completed:^(BPUser *newBuddyObject, NSError *error) {
                newUser = newBuddyObject;
            }];
            
            [[expectFutureValue(newUser.userName) shouldEventuallyBeforeTimingOutAfter(4.0)] equal:testCreateDeleteName];
        });
        
        it(@"Should allow you to login.", ^{
            __block BPUser *newUser;
            
            [Buddy login:testCreateDeleteName password:TEST_PASSWORD completed:^(BPUser *loggedInsUser, NSError *error) {
                newUser = loggedInsUser;
            }];
            
            [[expectFutureValue(newUser.userName) shouldEventuallyBeforeTimingOutAfter(4.0)] equal:testCreateDeleteName];
            //[[expectFutureValue(theValue(newUser.relationshipStatus)) shouldEventually] equal:theValue(BPUserRelationshipStatusOnTheProwl)];

        });
        
        it(@"Should allow you to perform a social login.", ^{
            //pending(@"Social login", ^{
            //});
        });
        
        it(@"Should allow you to delete a user.", ^{
            __block BOOL deleted = NO;
            
            [Buddy login:testCreateDeleteName password:TEST_PASSWORD completed:^(BPUser *loggedInsUser, NSError *error) {
                [loggedInsUser deleteMe:^{
                    deleted = YES;
                }];
            }];
            
            [[expectFutureValue(theValue(deleted)) shouldEventuallyBeforeTimingOutAfter(4.0)] beYes];
            
        });
    });
});

SPEC_END
