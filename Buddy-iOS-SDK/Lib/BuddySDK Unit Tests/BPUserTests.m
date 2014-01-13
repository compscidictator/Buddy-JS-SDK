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

SPEC_BEGIN(BPUserSpec)

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
            [Buddy user].dateOfBirth = [NSDate date];
            //[Buddy user].name = @"Don'tBeThisString";

            [[expectFutureValue([Buddy user].firstName) shouldEventually] equal:randomName];
            [[expectFutureValue([Buddy user].dateOfBirth) shouldEventually] equal:randomDate];
            
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
        
        pending_(@"Should allow the user to logout", ^{
            [[Buddy user] removeIdentityValue:@"SomeValue" callback:^(NSError *error) {
                [[Buddy user] deleteMe:^(NSError *error){
#pragma messsage("TODO - Check error when I implement them. Ensure error exists for logged out user.")
                }];
            }];
        });
        

    });
});

SPEC_END
