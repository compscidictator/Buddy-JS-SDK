//
//  BPUserTests.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "Buddy.h"
#import "BuddyIntegrationHelper.h"
#import <Kiwi/Kiwi.h>

#ifdef kKW_DEFAULT_PROBE_TIMEOUT
#undef kKW_DEFAULT_PROBE_TIMEOUT
#endif
#define kKW_DEFAULT_PROBE_TIMEOUT 4.0

SPEC_BEGIN(BPUserIntegrationSpec)

describe(@"BPUser", ^{
    context(@"When a user is logged in", ^{
        __block NSString *resetCode;    

        beforeAll(^{
            __block BOOL fin = NO;
            
            [BuddyIntegrationHelper bootstrapLogin:^{
                fin = YES;
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
        });
        
        it(@"Should be allow modifying and saving", ^{
        
            NSDate *randomDate = [BuddyIntegrationHelper randomDate];
            NSString *randomName = [BuddyIntegrationHelper randomString:10];

            NSLog(@"1111%@", [Buddy user].dateOfBirth);

            [Buddy user].dateOfBirth = randomDate;
            [Buddy user].firstName = @"Test";
            [Buddy user].relationshipStatus = BPUserRelationshipStatusOnTheProwl;
            
            NSLog(@"2222%@", randomDate);

            [[Buddy user] save:^(NSError *error) {
                if (error) {
                    fail(@"Save was unsuccessful");
                }
                [[Buddy user] refresh:^(NSError *error) {
                    NSDate *f = [Buddy user].dateOfBirth;
                    NSLog(@"3333%@", f);
                }];
            }];
            
            // Hack to set it up to something that will change.
            [Buddy user].relationshipStatus = BPUserRelationshipStatusMarried;
            [Buddy user].dateOfBirth = [NSDate date];
            [Buddy user].firstName = @"Don'tBeThisString";

            [[expectFutureValue([Buddy user].firstName) shouldEventually] equal:randomName];
            [[expectFutureValue([Buddy user].dateOfBirth) shouldEventually] equal:randomDate];
            
            
            [[expectFutureValue(theValue([Buddy user].relationshipStatus)) shouldEventually] equal:theValue(BPUserRelationshipStatusOnTheProwl)];

            
        });

        pending_(@"Should provide a method to request a password reset.", ^{
//            {"status":400,
//            "error":"PasswordResetNotConfigured",
//            "message":"Password Reset values must be configured in the Developer Dashboard->Security",
//            "request_id":"7dc04781-41e0-483f-850c-186324a9cb29"}
            
#pragma messsage("TODO - This can be turned on/off in the dev portal (response above). Look into it.")
            [[Buddy user] requestPasswordReset:^(id newBuddyObject, NSError *error) {
                resetCode = newBuddyObject;
            }];
            
            [[expectFutureValue(resetCode) shouldEventually] beNonNil];
        });
        
        pending_(@"Should then a method to reset the password with a reset code.", ^{
            [[Buddy user] resetPassword:resetCode newPassword:@"TODO" callback:^(id buddyObject) {
                
            }];
        });
        
        pending_(@"Should allow the user to set a profile picture", ^{
            
        });
        
        pending_(@"Should allow the user to delete the profile picture", ^{
            
        });
    
        pending_(@"Should allow adding identity values.", ^{
            __block BOOL done = NO;
            [[Buddy user] addIdentityValue:@"SomeValue" callback:^(NSError *error) {
                done = YES;
            }];
            
            [[expectFutureValue(theValue(done)) shouldEventually] beYes];
        });
        
        pending_(@"Should then allow deleting identity values.", ^{
            
            [[Buddy user] removeIdentityValue:@"SomeValue" callback:^(NSError *error) {
                
            }];
        });
        
        it(@"Should allow the user to logout", ^{
            __block BOOL done = NO;
            [[Buddy user] logout:^(NSError *error) {
                [[error should] beNil];
                done = YES;
            }];
            
            [[expectFutureValue(theValue(done)) shouldEventually] beYes];

        });
        

    });
});

SPEC_END
