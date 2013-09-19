/*Copyright (C) 2012 Buddy Platform, Inc.
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
#import "StartupsUnitTests.h"
#import <BuddySDK/Buddy.h>


@implementation StartupsUnitTests

static NSString *AppName = @"Buddy iOS SDK test app";
static NSString *AppPassword = @"8C9E044D-7DB7-42DE-A376-16460B58008E";
static bool bwaiting = false;
static NSString *Token = @"UT-76444f9f-4a4b-4d3d-ba5c-7a82b5dbb5a5";

- (void)setUp
{
	[super setUp];
    

    [Buddy initClient:AppName
                appPassword:AppPassword];

	STAssertNotNil([BuddyClient defaultClient], @"StartupsUnitTests failed buddyClient nil");
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

- (void)aLogin
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

- (void)testStartup
{
	NSArray *resArray = [TestBuddySDK GetTextFileData:@"Test_Startup"];

	bwaiting = true;
	[self aLogin];
	[self waitloop];
	if (![Buddy user])
	{
		STFail(@"testStartup login failed.");
		return;
	}

	NSArray *dict = [[Buddy user].startups performSelector:@selector(makeBuddyStartupList:) withObject:resArray];

	if ([dict count] != 2)
	{
		STFail(@"testStartup failed dict should have 2 items");
	}

	resArray = [TestBuddySDK GetTextFileData:@"Test_StartupBad"];
	dict = [[Buddy user].startups performSelector:@selector(makeBuddyStartupList:) withObject:resArray];

	if ([dict count] != 2)
	{
		STFail(@"testStartup failed dict should have 2 items");
	}

	resArray = [TestBuddySDK GetTextFileData:@"Test_EmptyData"];
	dict = [[Buddy user].startups performSelector:@selector(makeBuddyStartupList:) withObject:resArray];
	if ([dict count] != 0)
	{
		STFail(@"testStartup failed dict should have 0 items");
	}
}

@end