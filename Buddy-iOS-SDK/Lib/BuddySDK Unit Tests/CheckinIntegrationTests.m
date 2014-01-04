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
            
            [[expectFutureValue(theValue(fin)) shouldEventuallyBeforeTimingOutAfter(4.0)] beTrue];
        });
        
        afterAll(^{
            
        });
        
        it(@"Should allow you to checkin.", ^{
            
            __block BPCheckin *newCheckin;
            BPCoordinate *coordinate = [BPCoordinate new];
            coordinate.latitude = 2.3;
            coordinate.longitude = 4.4;
            
            [[Buddy checkins] checkinWithComment:@"Checking in!"
                                     description:@"Description"
                                        complete:^(BPCheckin *checkin, NSError *error) {
                                            newCheckin = checkin;
                                            tempCheckinId = checkin.id;
                                            tempCheckin = checkin;
                                        }];

            
            [[expectFutureValue(newCheckin.comment) shouldEventually] equal:@"Checking in!"];
            [[expectFutureValue(newCheckin.description) shouldEventually] equal:@"Description"];
        });
        
        it(@"Should allow you to retrieve a list of checkins.", ^{
            __block NSArray *checkins;
            [[Buddy checkins] getCheckins:^(NSArray *buddyObjects) {
                checkins = buddyObjects;
                [[theValue([checkins count]) should] beGreaterThan:theValue(0)];
                //int a = [checkins count];
            }];
            
            [[expectFutureValue(theValue([checkins count])) shouldEventuallyBeforeTimingOutAfter(3.0)] beGreaterThan:theValue(0)];
        });
        
        it(@"Should allow you to retrieve a specific checkin.", ^{
            __block BPCheckin *retrievedCheckin;
            [BPCheckin queryFromServerWithId:tempCheckinId callback:^(BPCheckin *newBuddyObject, NSError *error) {
                retrievedCheckin = newBuddyObject;
            }];

            [[expectFutureValue(retrievedCheckin.id) shouldEventually] equal:tempCheckin.id];
            [[expectFutureValue(retrievedCheckin.comment) shouldEventually] equal:tempCheckin.comment];
            [[expectFutureValue(retrievedCheckin.description) shouldEventually] equal:tempCheckin.description];
        });
    });
});

SPEC_END
