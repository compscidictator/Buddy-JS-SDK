//
//  BuddyIntegrationTests.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/25/13.
//
//

#import "Buddy.h"
#import <Kiwi/Kiwi.h>

#ifdef kKW_DEFAULT_PROBE_TIMEOUT
#undef kKW_DEFAULT_PROBE_TIMEOUT
#endif
#define kKW_DEFAULT_PROBE_TIMEOUT 4.0

SPEC_BEGIN(BuddyIntegrationSpec)

describe(@"BPUser", ^{
    context(@"A clean boot of your app", ^{
        __block NSString *testCreateDeleteName = @"ItPutsTheLotionOnItsSkin3";
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
            
            [[expectFutureValue(newUser.userName) shouldEventually] equal:testCreateDeleteName];
        });
        
        it(@"Should allow you to login.", ^{
            __block BPUser *newUser;
            
            [Buddy login:testCreateDeleteName password:TEST_PASSWORD completed:^(BPUser *loggedInsUser, NSError *error) {
                newUser = loggedInsUser;
            }];
            
            [[expectFutureValue(newUser.userName) shouldEventually] equal:testCreateDeleteName];
            //[[expectFutureValue(theValue(newUser.relationshipStatus)) shouldEventually] equal:theValue(BPUserRelationshipStatusOnTheProwl)];

        });
        
        it(@"Should allow you to perform a social login.", ^{
            // Social tokens cannot be retrieved programatically
            // See Sample Facebook app.
        });
        
        it(@"Should allow you to delete a user.", ^{
            __block BOOL deleted = NO;
            
            [Buddy login:testCreateDeleteName password:TEST_PASSWORD completed:^(BPUser *loggedInsUser, NSError *error) {
                [loggedInsUser deleteMe:^{
                    deleted = YES;
                }];
            }];
            
            [[expectFutureValue(theValue(deleted)) shouldEventually] beYes];
            
        });
    });
});

SPEC_END
