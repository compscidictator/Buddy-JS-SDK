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
                [[Buddy checkins] checkin:^(id<BPCheckinProperties> checkinProperties) {
                    checkinProperties.comment = @"Test checkin";
                    checkinProperties.description = @"Test checkin description";
                    checkinProperties.location = BPCoordinateMake(1.2, 3.4);
                } callback:^(id newBuddyObject, NSError *error) {
                    checkin = newBuddyObject;
                }];
            }];
            
            [[expectFutureValue(checkin) shouldEventually] beNonNil];
        });
        
        afterAll(^{
            [Buddy logout:nil];
        });
        
        it(@"Should allow setting string metadata", ^{
            
            __block NSString *targetString = nil;
            
            NSDictionary *kvp = @{@"Hakuna": @"Matata"};
            
            [Buddy setMetadataWithKeyValues:kvp permissions:BuddyPermissionsDefault callback:^(NSError *error) {
                [[error should] beNil];
                [Buddy getMetadataWithKey:@"Hakuna" callback:^(id newBuddyObject, NSError *error) {
                    targetString = newBuddyObject;
                }];
            }];
            
            [[expectFutureValue(targetString) shouldEventually] equal:@"Matata"];
        });
        
        it(@"Should be able to set nil  metadata", ^{
            __block id targetString = @"Stuff";
            
            __block BPCheckin *c = checkin;
            [checkin setMetadataWithKey:@"StringlyMetadata" andString:@"Test String" permissions:BuddyPermissionsDefault callback:^(NSError *error) {
                [[error should] beNil];
                [c getMetadataWithKey:@"StringlyMetadata" callback:^(id newBuddyObject, NSError *error) {
                    targetString = newBuddyObject;
                }];
            }];
            
            [[expectFutureValue(targetString) shouldEventually] equal:@"Test String"];
        });
        
        it(@"Should be able to set string based metadata", ^{
            NSString *testString = @"Hakuna matata";
            __block NSString *targetString = nil;
            
            __block BPCheckin *c = checkin;
            [checkin setMetadataWithKey:@"StringlyMetadata" andString:testString permissions:BuddyPermissionsDefault callback:^(NSError *error) {
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
            [checkin setMetadataWithKey:@"IntlyMetadata" andInteger:testInteger permissions:BuddyPermissionsDefault callback:^(NSError *error) {
                [[error should] beNil];
                [c getMetadataWithKey:@"IntlyMetadata" callback:^(id newBuddyObject, NSError *error) {
                    targetInteger = [newBuddyObject integerValue];
                }];
            }];
            
            [[expectFutureValue(theValue(targetInteger)) shouldEventually] equal:theValue(testInteger)];
        });
        
        it(@"Should be able to delete metadata", ^{
            __block BPCheckin *c = checkin;
            __block BOOL fin = NO;
            
            [c getMetadataWithKey:@"StringlyMetadata" callback:^(id newBuddyObject, NSError *error) {
                [[newBuddyObject shouldNot] beNil];
                fin = YES;
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
            fin = NO;
            
            [checkin deleteMetadataWithKey:@"StringlyMetadata" callback:^(NSError *error) {
                [c getMetadataWithKey:@"StringlyMetadata" callback:^(id newBuddyObject, NSError *error) {
                    [[newBuddyObject should] beNil];
                    fin = YES;
                }];
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
        });
    });
});

SPEC_END
