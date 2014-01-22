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

SPEC_BEGIN(BuddyCheckinSpec)

describe(@"BPCheckinIntegrationSpec", ^{
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
        
        afterAll(^{
            
        });
        
        it(@"Should allow you to checkin.", ^{
            BPCoordinate *coordinate = [BPCoordinate new];
            coordinate.latitude = 2.3;
            coordinate.longitude = 4.4;
            
            [[Buddy checkins] checkinWithComment:@"Checking in!"
                                     description:@"Description"
                                        callback:^(BPCheckin *checkin, NSError *error) {
                                            tempCheckinId = checkin.id;
                                            tempCheckin = checkin;
                                        }];

            
            [[expectFutureValue(tempCheckin.comment) shouldEventually] equal:@"Checking in!"];
            [[expectFutureValue(tempCheckin.description) shouldEventually] equal:@"Description"];
        });
        
        it(@"Should allow you to retrieve a list of checkins.", ^{
            __block NSArray *checkins;
            [[Buddy checkins] getCheckins:^(NSArray *buddyObjects, NSError *error) {
                checkins = buddyObjects;
                [[theValue([checkins count]) should] beGreaterThan:theValue(0)];
            }];
            
            [[expectFutureValue(theValue([checkins count])) shouldEventually] beGreaterThan:theValue(0)];
        });
        
        it(@"Should allow you to retrieve a specific checkin.", ^{
            __block BPCheckin *retrievedCheckin;
            [BPCheckin queryFromServerWithId:tempCheckinId client:[BPClient defaultClient ] callback:^(BPCheckin *newBuddyObject, NSError *error) {
                retrievedCheckin = newBuddyObject;
            }];

            [[expectFutureValue(retrievedCheckin.id) shouldEventually] equal:tempCheckin.id];
            [[expectFutureValue(retrievedCheckin.comment) shouldEventually] equal:tempCheckin.comment];
            [[expectFutureValue(retrievedCheckin.description) shouldEventually] equal:tempCheckin.description];
        });
        
        it(@"Should allow modifying the comment of a checkin.", ^{
            
            tempCheckin.comment = @"My new comment";
            
            [tempCheckin save:^(NSError *error) {
                [tempCheckin refresh:^(NSError *error) {
                    NSLog(@"Checkin saved");
                }];
            
            }];
            
            tempCheckin.comment = @"";
            
            [[expectFutureValue(tempCheckin.comment) shouldEventually] equal:@"My new comment"];
        });
    });
});

SPEC_END
