//
//  BuddyIntegrationTests.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/25/13.
//
//

#import "Buddy.h"
#import <Kiwi/Kiwi.h>
#import "BuddyIntegrationHelper.h"

#ifdef kKW_DEFAULT_PROBE_TIMEOUT
#undef kKW_DEFAULT_PROBE_TIMEOUT
#endif
#define kKW_DEFAULT_PROBE_TIMEOUT 4.0

SPEC_BEGIN(BuddyIntegrationSpec)

describe(@"Buddy", ^{
    context(@"A clean boot of your app", ^{
        __block NSString *testCreateDeleteName = @"ItPutsTheLotionOnItsSkin";
        __block id mock = nil;
        beforeAll(^{
            mock = [KWMock mockForProtocol:@protocol(BPClientDelegate)];
            // DISABLED AS NOT WORKING [[mock shouldEventually] receive:@selector(connectivityChanged:)];

            [Buddy setClientDelegate:mock];
            [Buddy initClient:APP_NAME appKey:APP_KEY];

        });
        
        afterAll(^{

        });
        
        it(@"Should throw an auth error if they try to access photos.", ^{
            [[mock shouldEventually] receive:@selector(apiErrorOccurred:)];
            [[[mock shouldEventually] receive] authorizationNeedsUserLogin];
            [[Buddy photos] searchPhotos:nil callback:nil];
        });
        
        it(@"Should allow you to create a user.", ^{
            
            __block BPUser *newUser;
            __block NSDate *randomDate = [BuddyIntegrationHelper randomDate];
            [Buddy createUser:testCreateDeleteName password:TEST_PASSWORD describeUser:^(id<BPUserProperties> userProperties) {
                userProperties.firstName = @"Erik";
                userProperties.lastName = @"Kerber";
                userProperties.gender = BPUserGender_Female;
                userProperties.email = @"erik.kerber@gmail.com";
                userProperties.dateOfBirth = randomDate;
            } callback:^(BPUser *newBuddyObject, NSError *error) {
                newUser = newBuddyObject;
            }];
            
            [[expectFutureValue(newUser.userName) shouldEventually] equal:testCreateDeleteName];
            [[expectFutureValue(newUser.firstName) shouldEventually] equal:@"Erik"];
            [[expectFutureValue(newUser.lastName) shouldEventually] equal:@"Kerber"];
            [[expectFutureValue(theValue(newUser.gender)) shouldEventually] equal:theValue(BPUserGender_Female)];
            [[expectFutureValue(newUser.dateOfBirth) shouldEventually] equal:randomDate];
        });
        
        it(@"Should allow you to login.", ^{
            __block BPUser *newUser;
            
            [[mock shouldEventually] receive:@selector(userChangedTo:from:)];

            [Buddy login:testCreateDeleteName password:TEST_PASSWORD callback:^(BPUser *loggedInsUser, NSError *error) {
                newUser = loggedInsUser;
            }];
            
            [[expectFutureValue(newUser.userName) shouldEventually] equal:testCreateDeleteName];
            //[[expectFutureValue(theValue(newUser.relationshipStatus)) shouldEventually] equal:theValue(BPUserRelationshipStatusOnTheProwl)];

        });
        
        it(@"Should raise a notification of changing of a user.", ^{
            __block BOOL fin = NO;
            [Buddy login:testCreateDeleteName password:TEST_PASSWORD callback:^(BPUser *loggedInsUser, NSError *error) {
                fin = YES;
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
        });
        
        pending_(@"Should allow you to perform a social login.", ^{
            // Social tokens cannot be retrieved programatically
            // See Sample Facebook app.
        });
        
        it(@"Should allow you to delete a user.", ^{
            __block BOOL deleted = NO;
            
            [Buddy login:testCreateDeleteName password:TEST_PASSWORD callback:^(BPUser *loggedInsUser, NSError *error) {
                [loggedInsUser deleteMe:^(NSError *error){
                    deleted = YES;
                }];
            }];
            
            [[expectFutureValue(theValue(deleted)) shouldEventually] beYes];

        });
    });
});

SPEC_END
