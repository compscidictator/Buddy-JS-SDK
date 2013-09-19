/*
 * Copyright (C) 2012 Buddy Platform, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

#import "TestBuddySDK.h"
#import "GamesUnitTests.h"
#import <BuddySDK/Buddy.h>

@implementation GamesUnitTests

static NSString *AppName = @"Buddy iOS SDK test app";
static NSString *AppPassword = @"8C9E044D-7DB7-42DE-A376-16460B58008E";
static bool bwaiting = false;
static NSString *Token = @"UT-76444f9f-4a4b-4d3d-ba5c-7a82b5dbb5a5";

- (void)setUp
{
    [super setUp];
    
    [Buddy initClient:AppName
                appPassword:AppPassword];
    
    STAssertNotNil([BuddyClient defaultClient], @"GamesUnitTests setUp failed buddyClient nil");
}

- (void)tearDown
{
    [super tearDown];
}

- (void)waitloop
{
    NSDate *loopTil = [NSDate dateWithTimeIntervalSinceNow:30];
    
    while (bwaiting && [loopTil timeIntervalSinceNow] > 0)
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:loopTil];
}

- (void)alogin
{
    [[BuddyClient defaultClient] login:Token callback:[^(BuddyAuthenticatedUserResponse *response)
                                                      {
                                                          if (response.isCompleted)
                                                          {
                                                              NSLog(@"alogin OK user: %@", [Buddy user].toString);
                                                          }
                                                          else
                                                          {
                                                              STFail(@"alogin failed !response.isCompleted");
                                                          }
                                                          bwaiting = false;
                                                      } copy]];
}

- (void)testGamesScoresFromSearch
{
    NSArray *resArray = [TestBuddySDK GetTextFileData:@"Test_GameScoreSearch"];
    
    NSArray *scores = [[BuddyClient defaultClient].gameBoards performSelector:@selector(makeScores:) withObject:resArray];
    
    if (scores == nil || [scores count] != 3)
    {
        STFail(@"testGamesScoresFromSearch failed scores == nil || [scores count] != 3");
    }
    
    BuddyGameScore *gs1 = (BuddyGameScore *) [scores objectAtIndex:0];
    BuddyGameScore *gs2 = (BuddyGameScore *) [scores objectAtIndex:1];
    BuddyGameScore *gs3 = (BuddyGameScore *) [scores objectAtIndex:1];
    
    if (gs1.score != gs2.score || gs1.score != gs3.score || gs2.score != gs3.score)
    {
        STFail(@"testGamesScoresFromSearch failed scores should be the same");
    }
}

- (void)testGamesScoresFromSearchNoData
{
    NSArray *resArray = [TestBuddySDK GetTextFileData:@"Test_NoData"];
    
    NSArray *scores = [[BuddyClient defaultClient].gameBoards performSelector:@selector(makeScores:) withObject:resArray];
    
    if (scores == nil || [scores count] != 0)
    {
        STFail(@"testGamesScoresFromSearchNoData failed");
    }
    
    resArray = [TestBuddySDK GetTextFileData:@"Test_EmptyData"];
    scores = [[BuddyClient defaultClient].gameBoards performSelector:@selector(makeScores:) withObject:resArray];
    
    if (scores == nil || [scores count] != 0)
    {
        STFail(@"testGamesScoresFromSearchNoDataJson failed EmptyData");
    }
}

// test the creation of a BuddyPlayers list from fixed json data
- (void)testGamePlayersFixedDataJson
{
    bwaiting = true;
    [self alogin];
    [self waitloop];
    
    if (![Buddy user])
    {
        STFail(@"testGamePlayersFixedDataJson login failed");
        return;
    }
    
    NSArray *resArray = [TestBuddySDK GetTextFileData:@"Test_NoData"];
    
    NSArray *data = [[Buddy user].gamePlayers performSelector:@selector(makePlayerList:) withObject:resArray];
    
    if (data == nil || [data count] != 0)
    {
        STFail(@"atestGamePlayersFixedDataJson _Test_NoData");
    }
    
    resArray = [TestBuddySDK GetTextFileData:@"Test_EmptyData"];
    
    data = [[Buddy user].gamePlayers performSelector:@selector(makePlayerList:) withObject:resArray];
    if (data == nil || [data count] != 0)
    {
        STFail(@"atestGamePlayersFixedDataJson Test_EmptyData");
    }
    
    resArray = [TestBuddySDK GetTextFileData:@"Test_PlayerDataBad"];
    data = [[Buddy user].gamePlayers performSelector:@selector(makePlayerList:) withObject:resArray];
    if (data == nil)
    {
        STFail(@"atestGamePlayersFixedDataJson Test_PlayerDataBad");
    }
    
    resArray = [TestBuddySDK GetTextFileData:@"Test_PlayerDataGood"];
    data = [[Buddy user].gamePlayers performSelector:@selector(makePlayerList:) withObject:resArray];
    
    if (data == nil || [data count] != 2)
    {
        STFail(@"atestGamePlayersFixedDataJson Test_PlayerDataGood");
    }
}

@end
