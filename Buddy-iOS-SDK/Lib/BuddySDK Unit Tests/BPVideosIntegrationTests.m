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

SPEC_BEGIN(BuddyVideosSpec)

describe(@"BPVideosIntegrationSpec", ^{
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
        
        it(@"Should allow you create a video.", ^{
            
        });
        
        it(@"Should allow you to retrieve a video.", ^{
        });
        
        it(@"Should allow you to retrieve a specific video.", ^{
        });
        
        it(@"Should allow you to modify a video.", ^{
        });
        
        it(@"Should allow you to delete a video.", ^{
        });
    });
});

SPEC_END
