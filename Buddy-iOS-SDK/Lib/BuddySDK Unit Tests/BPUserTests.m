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
        
        pending_(@"Should be updatable", ^{
            
            NSDate *today = [[NSDate alloc] init];
            NSLog(@"%@", today);
            NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
            NSDateComponents *offsetComponents = [[NSDateComponents alloc] init];
            [offsetComponents setYear:-100]; // note that I'm setting it to -1
            NSDate *endOfWorldWar3 = [gregorian dateByAddingComponents:offsetComponents toDate:today options:0];
            NSLog(@"%@", endOfWorldWar3);
            
            
            NSCalendar *currentCalendar = [NSCalendar currentCalendar];
            NSDateComponents *comps = [currentCalendar components:NSYearCalendarUnit | NSMonthCalendarUnit | NSDayCalendarUnit fromDate:endOfWorldWar3];
            
            [comps setMonth:arc4random_uniform(12)];
            
            NSRange range = [[NSCalendar currentCalendar] rangeOfUnit:NSDayCalendarUnit inUnit:NSMonthCalendarUnit forDate:[currentCalendar dateFromComponents:comps]];
            
            [comps setDay:arc4random_uniform(range.length)];
            
            // Normalise the time portion
            [comps setHour:0];
            [comps setMinute:0];
            [comps setSecond:0];
            [comps setTimeZone:[NSTimeZone timeZoneWithName:@"GMT"]];
            
            NSDate *randomDate = [currentCalendar dateFromComponents:comps];
            
            [Buddy user].dateOfBirth = randomDate;
            
            [[Buddy user] update];
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
            [[Buddy user] resetPassword:resetCode newPassword:@"TODO" callback:^(NSArray *buddyObjects) {
                
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
                [[Buddy user] deleteMe:^{
#pragma messsage("TODO - Check error when I implement them. Ensure error exists for logged out user.")
                }];
            }];
        });
        

    });
});

SPEC_END
