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
    
    [Buddy initClient:@"92538700814090257" appKey:@"55E419A6-C732-4C5A-9778-0B62F66323FE" complete:^{
        [Buddy login:@"erik4" password:@"password" completed:^(BPUser *loggedInsUser) {
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
                            tempCheckinId = [checkin.identifier stripBuddyId];
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
        XCTAssert([newBuddyObject.identifier isEqualToString:tempCheckinId], @"Did not retrive old identifier.");
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
