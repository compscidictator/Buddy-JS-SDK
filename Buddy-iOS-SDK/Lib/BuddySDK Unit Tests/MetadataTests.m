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
#define kKW_DEFAULT_PROBE_TIMEOUT 8.0

SPEC_BEGIN(MetadataSpec)

describe(@"Metadata", ^{
    context(@"When a user is logged in", ^{
        
        __block BPCheckin *checkin;
        beforeAll(^{
            [BuddyIntegrationHelper bootstrapLogin:^{
                [[Buddy checkins] checkinWithComment:@"Test checkin." description:@"Test checkin description" callback:^(id newBuddyObject, NSError *error) {
                    checkin = newBuddyObject;
                }];
            }];
            
            [[expectFutureValue(checkin) shouldEventually] beNonNil];
        });
        
        afterAll(^{
            [Buddy logout:nil];
        });
        
        it(@"Should be able to set nil  metadata", ^{
            __block id targetString = @"Stuff";
            
            __block BPCheckin *c = checkin;
            [checkin setMetadataWithKey:@"StringlyMetadata" andString:nil callback:^(NSError *error) {
                [[error should] beNil];
                [c getMetadataWithKey:@"StringlyMetadata" callback:^(id newBuddyObject, NSError *error) {
                    targetString = newBuddyObject;
                }];
            }];
            
            [[expectFutureValue(targetString) shouldEventually] beNil];
        });
        
        it(@"Should be able to set string based metadata", ^{
            NSString *testString = @"Hakuna matata";
            __block NSString *targetString = nil;
            
            __block BPCheckin *c = checkin;
            [checkin setMetadataWithKey:@"StringlyMetadata" andString:testString callback:^(NSError *error) {
                [[error should] beNil];
                [c getMetadataWithKey:@"StringlyMetadata" callback:^(id newBuddyObject, NSError *error) {
                    targetString = newBuddyObject;
                }];
            }];

            [[expectFutureValue(targetString) shouldEventually] equal:testString];
        });
        
        it(@"Should be able to set integer based metadata", ^{
            NSInteger testInteger = 42;
            __block NSInteger targetInteger = -1;
            
            __block BPCheckin *c = checkin;
            [checkin setMetadataWithKey:@"IntlyMetadata" andInteger:testInteger callback:^(NSError *error) {
                [[error should] beNil];
                [c getMetadataWithKey:@"IntlyMetadata" callback:^(id newBuddyObject, NSError *error) {
                    targetInteger = [newBuddyObject integerValue];
                }];
            }];
            
            [[expectFutureValue(theValue(targetInteger)) shouldEventually] equal:theValue(testInteger)];
        });
    });
});

SPEC_END
