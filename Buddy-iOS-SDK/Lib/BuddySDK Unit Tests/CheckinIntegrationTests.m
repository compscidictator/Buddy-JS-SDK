//
//  CheckinIntegrationTests.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/2/13.
//
//

#import "CheckinIntegrationTests.h"
#import "Buddy.h"


@implementation CheckinIntegrationTests

static NSString *tempCheckinId;
static BPCheckin *tempCheckin;

-(void)setUp{
    [super setUp];
    
    [Buddy initClient:APP_NAME appKey:APP_KEY complete:^{
        [Buddy login:TEST_USERNAME password:TEST_PASSWORD completed:^(BPUser *loggedInsUser) {
            [self.tester signalDone];
        }];
    }];
    
    [self.tester wait];
}

-(void)tearDown
{
    
}

-(void)testCreateCheckin
{
    BPCoordinate *coordinate = [BPCoordinate new];
    coordinate.latitude = 2.3;
    coordinate.longitude = 4.4;

    self.tester = [TestHelper new];
    
    [[Buddy checkins] checkinWithComment:@"Checking in!"
                             description:@"Description"
                        complete:^(BPCheckin *checkin) {
                            XCTAssert([checkin.comment isEqualToString:@"Checking in!"], @"Didn't get the response back");
                            tempCheckinId = [checkin.id stripBuddyId];
                                [self.tester signalDone];
                        }];
    [self.tester wait];
}

-(void)testCreateAlternateCheckin
{
    BPCoordinate *coordinate = [BPCoordinate new];
    coordinate.latitude = 2.3;
    coordinate.longitude = 4.4;
    
    BPCheckin *checkin = [BPCheckin checkin];
    checkin.comment = @"Checking in 2!";
    checkin.description = @"Description 2";
    checkin.defaultMetadata = @"LOL I don't know what this is?";
    
    [[Buddy checkins] addCheckin:checkin];
}

-(void)testGetCheckinList
{
    [[Buddy checkins] getCheckins:^(NSArray *buddyObjects) {
       //TODO
        [self.tester signalDone];
    }];
    
    [self.tester wait];
}

-(void)testGetCheckin
{
    [BPCheckin queryFromServerWithId:tempCheckinId callback:^(BPCheckin *newBuddyObject) {
        XCTAssert([newBuddyObject.id isEqualToString:tempCheckinId], @"Did not retrive old identifier.");
    }];
}

-(void)testDeleteCheckin
{
    [tempCheckin deleteMe];

}

-(void)testGetCheckinByRadius
{
    
}

@end
