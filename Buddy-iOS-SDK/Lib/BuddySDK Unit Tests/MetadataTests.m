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

SPEC_BEGIN(MetadataSpec)

describe(@"Metadata", ^{
    context(@"When a user is logged in", ^{
        
        __block BPCheckin *checkin1;
        __block BPCheckin *checkin2;
        beforeAll(^{
            
            DescribeCheckin d1 = ^(id<BPCheckinProperties> checkinProperties) {
                checkinProperties.comment = @"Test checkin";
                checkinProperties.description = @"Test checkin description";
                checkinProperties.location = BPCoordinateMake(1.2, 3.4);
            };

            DescribeCheckin d2 = ^(id<BPCheckinProperties> checkinProperties) {
                checkinProperties.comment = @"Second checkin";
                checkinProperties.description = @"Test checkin description";
                checkinProperties.location = BPCoordinateMake(1.2, 3.4);
            };
            
            [BuddyIntegrationHelper bootstrapLogin:^{
                
                [[Buddy checkins] checkin:d1
                 callback:^(id newBuddyObject, NSError *error) {
                    checkin1 = newBuddyObject;
                }];
                
                [[Buddy checkins] checkin:d2
                callback:^(id newBuddyObject, NSError *error) {
                     checkin2 = newBuddyObject;
                 }];
            }];
            
            [[expectFutureValue(checkin1) shouldEventually] beNonNil];
            [[expectFutureValue(checkin2) shouldEventually] beNonNil];
        });
        
        afterAll(^{
            [Buddy logout:nil];
        });
        
        it(@"Should allow setting string metadata", ^{
            
            __block NSString *targetString = nil;
            __block NSString *targetString2 = nil;
            __block NSString *targetString3 = @"No Change";
            __block NSString *targetString4 = @"No Change";
            
            
            NSDictionary *kvp = @{@"Hakuna": @"Matata"};
            
            // App-level Metadata with Key Value Pairs
            [Buddy setMetadataWithKeyValues:kvp permissions:BuddyPermissionsDefault callback:^(NSError *error) {
                [[error should] beNil];
                [Buddy getMetadataWithKey:@"Hakuna" permissions:BuddyPermissionsDefault callback:^(id newBuddyObject, NSError *error) {
                    targetString = newBuddyObject;
                }];
            }];
            
            // App-level MetaData with Key/String
            [Buddy setMetadataWithKey:@"Hey" andString:@"There" permissions:BuddyPermissionsApp callback:^(NSError *error) {
                [[error should] beNil];
                [Buddy getMetadataWithKey:@"Hey" permissions:BuddyPermissionsApp callback:^(id newBuddyObject, NSError *error) {
                    targetString2 = newBuddyObject;
                }];
            }];
            
            // App-level Metadata - Check permissions (write as User, Get as App should fail)
            [Buddy setMetadataWithKey:@"HeyHey" andString:@"There" permissions:BuddyPermissionsUser callback:^(NSError *error) {
                [[error should] beNil];
                [Buddy getMetadataWithKey:@"HeyHey" permissions:BuddyPermissionsApp callback:^(id newBuddyObject, NSError *error) {
                    if(error==nil)
                    {
                        targetString3 = newBuddyObject;
                    }
                }];
                [Buddy getMetadataWithKey:@"HeyHey" permissions:BuddyPermissionsUser callback:^(id newBuddyObject, NSError *error) {
                    if(error==nil)
                    {
                        targetString4 = newBuddyObject;
                    }
                }];
            }];
            
            [[expectFutureValue(targetString) shouldEventually] equal:@"Matata"];
            [[expectFutureValue(targetString2) shouldEventually] equal:@"There"];
            [[expectFutureValue(targetString3) shouldEventually] equal:@"No Change"];
            [[expectFutureValue(targetString4) shouldEventually] equal:@"There"];
            
        });
        
        it(@"Should be able to set nil  metadata", ^{
            __block id targetString1 = @"Stuff";
            
            __block BPCheckin *c1 = checkin1;

            [checkin1 setMetadataWithKey:@"StringlyMetadata" andString:@"REMOVE" permissions:BuddyPermissionsDefault callback:^(NSError *error) {
                [[error should] beNil];
                [c1 getMetadataWithKey:@"StringlyMetadata" permissions:BuddyPermissionsDefault callback:^(id newBuddyObject, NSError *error) {
                    targetString1 = newBuddyObject;
                }];
            }];
            
            [[expectFutureValue(targetString1) shouldEventually] equal:@"REMOVE"];
        });
        
        it(@"Should be able to set string based metadata", ^{
            __block id targetString1 = @"Stuff";
            __block id targetString2 = @"Stuff";
            
            
            __block BPCheckin *c1 = checkin1;
            __block BPCheckin *c2 = checkin2;
            
            [checkin1 setMetadataWithKey:@"StringlyMetadata" andString:@"Test String" permissions:BuddyPermissionsDefault callback:^(NSError *error) {
                [[error should] beNil];
                [c1 getMetadataWithKey:@"StringlyMetadata" permissions:BuddyPermissionsDefault callback:^(id newBuddyObject, NSError *error) {
                    targetString1 = newBuddyObject;
                }];
                [c2 getMetadataWithKey:@"StringlyMetadata" permissions:BuddyPermissionsDefault callback:^(id newBuddyObject, NSError *error) {
                    targetString2 = newBuddyObject;
                }];
            }];
            
            [[expectFutureValue(targetString1) shouldEventually] equal:@"Test String"];
            [[expectFutureValue(targetString2) shouldNotEventually] equal:@"Test String"];
        });
        
        it(@"Should be able to set integer based metadata", ^{
            NSInteger testInteger = 42;
            __block NSInteger targetInteger = -1;
            
            __block BPCheckin *c = checkin1;
            [checkin1 setMetadataWithKey:@"IntlyMetadata" andInteger:testInteger permissions:BuddyPermissionsDefault callback:^(NSError *error) {
                [[error should] beNil];
                [c getMetadataWithKey:@"IntlyMetadata" permissions:BuddyPermissionsDefault callback:^(id newBuddyObject, NSError *error) {
                    targetInteger = [newBuddyObject integerValue];
                }];
            }];
            
            [[expectFutureValue(theValue(targetInteger)) shouldEventually] equal:theValue(testInteger)];
        });
        
        it(@"Should be able to delete metadata", ^{
            __block BPCheckin *c = checkin1;
            __block BOOL fin = NO;
            
            [c getMetadataWithKey:@"StringlyMetadata" permissions:BuddyPermissionsDefault callback:^(id newBuddyObject, NSError *error) {
                [[newBuddyObject shouldNot] beNil];
                fin = YES;
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
            fin = NO;
            
            [checkin1 deleteMetadataWithKey:@"StringlyMetadata" permissions:BuddyPermissionsDefault callback:^(NSError *error) {
                [c getMetadataWithKey:@"StringlyMetadata" permissions:BuddyPermissionsDefault callback:^(id newBuddyObject, NSError *error) {
                    [[newBuddyObject should] beNil];
                    fin = YES;
                }];
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
        });
        
        it(@"Should be able to set multi-valued metadata", ^{
            __block id targetString1 = @"Stuff";
            __block id targetString2 = @"Stuff";
            
            NSDictionary *keysValues = @{@"foo": @"bar"};
            
            __block BPCheckin *c1 = checkin1;
            __block BPCheckin *c2 = checkin2;
            
            [checkin1 setMetadataWithKeyValues:keysValues permissions:BuddyPermissionsDefault callback:^(NSError *error) {
                [[error should] beNil];
                [c1 getMetadataWithKey:@"foo" permissions:BuddyPermissionsDefault callback:^(id newBuddyObject, NSError *error) {
                    if(error==nil)
                    {
                        targetString1 = newBuddyObject;
                    }
                    else
                    {
                        targetString1=nil;
                    }
                }];
                [c2 getMetadataWithKey:@"foo" permissions:BuddyPermissionsDefault callback:^(id newBuddyObject, NSError *error) {
                    if(error==nil)
                    {
                        targetString2 = newBuddyObject;
                    }
                    else
                    {
                        targetString2 = nil;
                    }
                }];
            }];
            
            [[expectFutureValue(targetString1) shouldEventually] equal:@"bar"];
            [[expectFutureValue(targetString2) shouldNotEventually] equal:@"bar"];
        });
    });
});

SPEC_END
