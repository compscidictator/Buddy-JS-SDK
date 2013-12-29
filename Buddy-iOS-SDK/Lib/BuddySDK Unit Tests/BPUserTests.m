//
//  BPUserTests.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPUserTests.h"
#import "Buddy.h"
#import <Kiwi/Kiwi.h>

SPEC_BEGIN(BPUser)

describe(@"BPUser", ^{
    context(@"When a user is logged in", ^{
        
        __block BPCheckin *tempCheckin;
        __block NSString *tempCheckinId;
        
        beforeAll(^{
            __block BOOL fin = NO;
            
            [BuddyIntegrationHelper bootstrapLogin:^{
                fin = YES;
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
        });
    });
});

SPEC_END
