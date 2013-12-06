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
    struct BPCoordinate coordinate;
    coordinate.lattitude = 2.3;
    coordinate.longitude = 4.4;

    self.tester = [TestHelper new];
    
    [[Buddy checkins] checkinWithComment:@"Checking in!"
                             description:@"Description"
                        complete:^(BPCheckin *checkin) {
                            XCTAssert([checkin.comment isEqualToString:@"Checking in!"], @"Didn't get the response back");
                                [self.tester signalDone];
                        }];
    [self.tester wait];
}

-(void)testCreateAlternateCheckin
{
    struct BPCoordinate coordinate;
    coordinate.lattitude = 2.3;
    coordinate.longitude = 4.4;
    
    BPCheckin *checkin = [BPCheckin checkin];
    checkin.comment = @"Checking in 2!";
    checkin.description = @"Description 2";
    checkin.defaultMetadata = @"LOL I don't know what this is?";
    
    [[Buddy checkins] addCheckin:checkin];
}

-(void)testGetCheckinList
{
//    [Buddy checkins]
}

-(void)testGetCheckin
{
    
}

-(void)testGetCheckinByRadius
{
    
}

@end
