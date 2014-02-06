//
//  CheckinIntegrationTests.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/2/13.
//
//

#import "Buddy.h"
#import "BuddyIntegrationHelper.h"
#import <Kiwi/Kiwi.h>

#ifdef kKW_DEFAULT_PROBE_TIMEOUT
#undef kKW_DEFAULT_PROBE_TIMEOUT
#endif
#define kKW_DEFAULT_PROBE_TIMEOUT 4.0

SPEC_BEGIN(BuddyMessageSpec)

describe(@"BPMessagesIntegrationSpec", ^{
    context(@"When a user is logged in", ^{
        
        //__block BPAlbum *tempAlbum;
        
        beforeAll(^{
            __block BOOL fin = NO;
            
            [BuddyIntegrationHelper bootstrapLogin:^{
                fin = YES;
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
        });
        
        afterAll(^{
            
        });
        
        it(@"Should allow you create an album.", ^{
            
        });
        
        it(@"Should allow you to retrieve an album.", ^{
        });
        
        it(@"Should allow you to retrieve a specific album.", ^{
        });
        
        it(@"Should allow you to modify an album.", ^{
        });
        
        it(@"Should allow you to delete an album.", ^{
        });
    });
});

SPEC_END
