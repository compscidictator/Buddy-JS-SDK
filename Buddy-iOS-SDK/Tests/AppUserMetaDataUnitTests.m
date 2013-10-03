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

#import "AppUserMetaDataUnitTests.h"
#import "TestBuddySDK.h"
#import <BuddySDK/Buddy.h>


@implementation AppUserMetaDataUnitTests


static NSString *AppName = @"Buddy iOS SDK test app";
static NSString *AppPassword = @"8C9E044D-7DB7-42DE-A376-16460B58008E";
static bool bwaiting = false;
static NSString *Token = @"UT-76444f9f-4a4b-4d3d-ba5c-7a82b5dbb5a5";

- (void)setUp
{
	[super setUp];
    
	[Buddy initClient:AppName
                                   appPassword:AppPassword];
    
	STAssertNotNil([BuddyClient defaultClient], @"AppUserMetaDataUnitTests failed buddyClient nil");
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
	[[BuddyClient defaultClient] login:Token
				   callback:[^(BuddyAuthenticatedUserResponse *response)
							 {
								 if (response.isCompleted)
								 {
									 NSLog(@"alogin OK user: %@", [Buddy user].toString);
								 }
								 else
								 {
                                     NSLog(@"alogin %@", response.stringResult);
									 STFail(@"alogin failed");
								 }
								 bwaiting = false;
							 } copy]];
}

- (void)testUserMetaData
{
    bwaiting = true;
    [self alogin];
    [self waitloop];

    int icount = 1;
    while(icount != 0)
    {
        bwaiting = true;
        [self addData];
        [self waitloop];
        
        bwaiting = true;
        [self addData2];
        [self waitloop];
        
        bwaiting = true;
        [self sumData];
        [self waitloop];
        
        bwaiting = true;
        [self batchSumData];
        [self waitloop];
        
        icount--;
    }
}

- (void)addData
{
    [[Buddy user].metadata set:@"TestKey1" value:@"124" latitude:0.0 longitude:0.0 appTag:@"AppTag" callback:[^(BuddyBoolResponse *response)
            {
                if(response.isCompleted)
                {
                    NSLog(@"addUserMetaData OK");
                }
                else
                {
                    STFail(@"addUserMetaData failed !response.isCompleted");
                }
                bwaiting = false;
            } copy]];
}

- (void)addData2
{
    [[Buddy user].metadata set:@"TestKey2" value:@"5235" latitude:0.0 longitude:0.0 appTag:@"AppTag" callback:[^(BuddyBoolResponse *response)
         {
             if(response.isCompleted)
             {
                 NSLog(@"addUserMetaData2 OK");
             }
             else
             {
                 STFail(@"addUserMetaData2 failed !response.isCompleted");
             }
             bwaiting = false;
         } copy]];
}

- (void)batchSumData
{
    [[Buddy user].metadata batchSum:@"TestKey%;TestKey1" callback:[^(BuddyArrayResponse *response)
         {
             if(response.isCompleted)
             {
                  [response.result objectAtIndex:0];
             }else
             {
                 STFail(@"BatchSumData failed !response.isCompleted");
             }
         } copy]];
}

- (void)sumData
{
    [[Buddy user].metadata sum:@"TestKey%" callback:[^(BuddyMetadataSumResponse *response)
       {
           if(response.isCompleted)
           {
               if(response.result.total != 5359)
               {
                   STFail(@"sumUserMetaData should have had 5359 as total");
               }
           }
           else
           {
               STFail(@"sumUserMetaData failed !response.isCompleted");
           }
       } copy]];
}

- (void)testApplicationMetadataParseGoodDataTest
{
	NSArray *resArray = [TestBuddySDK GetTextFileData:@"Test_AppSearchMetadata"];
    
	NSDictionary *dict = [[BuddyClient defaultClient].metadata performSelector:@selector(makeMetadataItemDictionary:) withObject:resArray];
    
	if ([dict count] != 3)
	{
		STFail(@"testApplicationMetadataParseGoodDataTest failed dict should have 3 items");
	}
    
	BuddyMetadataItem *metaItem = (BuddyMetadataItem *)[dict objectForKey:@"MyValue"];
	BuddyMetadataItem *metaItem1 = (BuddyMetadataItem *)[dict objectForKey:@"MyValue1"];
	BuddyMetadataItem *metaItem2 = (BuddyMetadataItem *)[dict objectForKey:@"MyValue2"];
    
	if (metaItem == nil)
	{
		STFail(@"testApplicationMetadataParseGoodDataTest failed  metaItem == nil ");
	}
    
	if (metaItem1 == nil)
	{
		STFail(@"testApplicationMetadataParseGoodDataTest failed  metaItem1 == nil ");
	}
    
	if (metaItem2 == nil)
	{
		STFail(@"testApplicationMetadataParseGoodDataTest failed  metaItem2 == nil ");
	}
    
	if (![metaItem1.value isEqualToString:metaItem2.value])
	{
		STFail(@"testApplicationMetadataParseGoodDataTest failed ![metaItem1.value isEqualToString:metaItem2.value]");
	}
    
	if ([metaItem.value isEqualToString:metaItem2.value])
	{
		STFail(@"testApplicationMetadataParseGoodDataTest failed [metaItem.value isEqualToString:metaItem2.value]");
	}
}

- (void)testApplicationMetadataParseBadDataTest
{
	NSArray *resArray = [TestBuddySDK GetTextFileData:@"Test_AppSearchMetadataBad"];
    
	NSDictionary *dict = [[BuddyClient defaultClient].metadata performSelector:@selector(makeMetadataItemDictionary:) withObject:resArray];
    
	if ([dict count] != 1)
	{
		STFail(@"testApplicationMetadataParseBadDataTest failed dict should have 1 valid item");
	}
    
	BuddyMetadataItem *metaItem = (BuddyMetadataItem *)[dict objectForKey:@"MyValue"];
	BuddyMetadataItem *metaItem1 = (BuddyMetadataItem *)[dict objectForKey:@"MyValue1"];
	BuddyMetadataItem *metaItem2 = (BuddyMetadataItem *)[dict objectForKey:@"MyValue2"];
    
	if (metaItem != nil)
	{
		STFail(@"testApplicationMetadataParseBadDataTest failed  metaItem == nil ");
	}
    
	if (metaItem1 == nil)
	{
		STFail(@"testApplicationMetadataParseBadDataTest failed  metaItem1 == nil ");
	}
    
	if (metaItem2 != nil)
	{
		STFail(@"testApplicationMetadataParseBadDataTest failed  metaItem2 == nil ");
	}
}

- (void)testApplicationMetadataNoData
{
	NSArray *resArray = [TestBuddySDK GetTextFileData:@"Test_NoData"];
    
	NSDictionary *dict = [[BuddyClient defaultClient].metadata performSelector:@selector(makeMetadataItemDictionary:) withObject:resArray];
    
	if ([dict count] != 0)
	{
		STFail(@"testApplicationMetadataNoData failed dict should have 0 items");
	}
    
	resArray = [TestBuddySDK GetTextFileData:@"Test_EmptyData"];
	dict = [[BuddyClient defaultClient].metadata performSelector:@selector(makeMetadataItemDictionary:) withObject:resArray];
	if ([dict count] != 0)
	{
		STFail(@"testApplicationMetadataNoData failed dict Test_EmptyData should have 0 items");
	}
}

- (void)testApplicationMetadataSumTest
{
	NSArray *resArray = [TestBuddySDK GetTextFileData:@"Test_MetadataSum"];
    
	NSArray *dict = [[BuddyClient defaultClient].metadata performSelector:@selector(makeMetadataSumArray:) withObject:resArray];
    
	if ([dict count] != 2)
	{
		STFail(@"testApplicationMetadataSumTest failed dict should have 2 item2");
	}
    
	resArray = [TestBuddySDK GetTextFileData:@"Test_NoData"];
	dict = [[BuddyClient defaultClient].metadata performSelector:@selector(makeMetadataSumArray:) withObject:resArray];
	if ([dict count] != 0)
	{
		STFail(@"ttestApplicationMetadataSumTest failed dict should have 0 items");
	}
    
    
	resArray = [TestBuddySDK GetTextFileData:@"Test_EmptyData"];
	dict = [[BuddyClient defaultClient].metadata performSelector:@selector(makeMetadataSumArray:) withObject:resArray];
	if ([dict count] != 0)
	{
		STFail(@"testApplicationMetadataSumTest failed dict should have 0 items");
	}
}

- (void)testUserMetadataParseGoodDataTest
{
	NSArray *resArray = [TestBuddySDK GetTextFileData:@"Test_AppSearchMetadata"];
    
	bwaiting = true;
	[self alogin];
	[self waitloop];
	if (![Buddy user])
	{
		STFail(@"testUserMetadataParseGoodDataTest login failed.");
		return;
	}
    
	NSDictionary *dict = [[Buddy user].metadata performSelector:@selector(makeMetadataItemDictionary:) withObject:resArray];
    
	if ([dict count] != 3)
	{
		STFail(@"testUserMetadataParseGoodDataTest failed dict should have 3 items");
	}
    
	BuddyMetadataItem *metaItem = (BuddyMetadataItem *)[dict objectForKey:@"MyValue"];
	BuddyMetadataItem *metaItem1 = (BuddyMetadataItem *)[dict objectForKey:@"MyValue1"];
	BuddyMetadataItem *metaItem2 = (BuddyMetadataItem *)[dict objectForKey:@"MyValue2"];
    
	if (metaItem == nil)
	{
		STFail(@"testUserMetadataParseGoodDataTest failed  metaItem == nil");
	}
    
	if (metaItem1 == nil)
	{
		STFail(@"testUserMetadataParseGoodDataTest failed  metaItem1 == nil");
	}
    
	if (metaItem2 == nil)
	{
		STFail(@"testUserMetadata_ParseGoodDataTest failed  metaItem2 == nil");
	}
    
	if (![metaItem1.value isEqualToString:metaItem2.value])
	{
		STFail(@"testUserMetadataParseGoodDataTest failed ![metaItem1.value isEqualToString:metaItem2.value]");
	}
    
	if ([metaItem.value isEqualToString:metaItem2.value])
	{
		STFail(@"testUserMetadata_ParseGoodDataTest failed [metaItem.value isEqualToString:metaItem2.value]");
	}
    
	if ([metaItem2 compareTo:metaItem1] == 0)
	{
		STFail(@"[metaItem2 compareTo: metaItem1] == 0 ");
	}
	else
	{
		NSLog(@"[metaItem2 compareTo: metaItem1] != 0 ");
	}
    
	if ([metaItem2 compareTo:metaItem2] == 0)
	{
		NSLog(@"[metaItem2 compareTo: metaItem2] == 0 ");
	}
	else
	{
		STFail(@"[metaItem2 compareTo: metaItem2] != 0 ");
	}
}

- (void)testUserMetadataParseBadDataTest
{
	NSArray *resArray = [TestBuddySDK GetTextFileData:@"Test_AppSearchMetadataBad"];
    
	bwaiting = true;
	[self alogin];
	[self waitloop];
	if (![Buddy user])
	{
		STFail(@"testUserMetadataNoDataTest login failed.");
		return;
	}
    
	NSDictionary *dict = [[Buddy user].metadata performSelector:@selector(makeMetadataItemDictionary:) withObject:resArray];
    
	if ([dict count] != 1)
	{
		STFail(@"testUserMetadataParseBadDataTest failed dict should have 1 valid item");
	}
    
	BuddyMetadataItem *metaItem = (BuddyMetadataItem *)[dict objectForKey:@"MyValue"];
	BuddyMetadataItem *metaItem1 = (BuddyMetadataItem *)[dict objectForKey:@"MyValue1"];
	BuddyMetadataItem *metaItem2 = (BuddyMetadataItem *)[dict objectForKey:@"MyValue2"];
    
	if (metaItem != nil)
	{
		STFail(@"testUserMetadataParseBadDataTest failed  metaItem == nil ");
	}
    
	if (metaItem1 == nil)
	{
		STFail(@"testUserMetadataParseBadDataTest failed  metaItem1 == nil ");
	}
    
	if (metaItem2 != nil)
	{
		STFail(@"testUserMetadataParseBadDataTest failed  metaItem2 == nil ");
	}
}

- (void)testUserMetadataNoDataTest
{
	NSArray *resArray = [TestBuddySDK GetTextFileData:@"Test_NoData"];
    
	bwaiting = true;
	[self alogin];
	[self waitloop];
	if (![Buddy user])
	{
		STFail(@"testUserMetadataNoDataTest login failed.");
		return;
	}
    
	NSDictionary *dict = [[Buddy user].metadata performSelector:@selector(makeMetadataItemDictionary:) withObject:resArray];
    
	if (dict == nil || [dict count] != 0)
	{
		STFail(@"testUserMetadataNoDataTest failed dict should have 0 items");
	}
    
	resArray = [TestBuddySDK GetTextFileData:@"Test_EmptyData"];
	dict = [[Buddy user].metadata performSelector:@selector(makeMetadataItemDictionary:) withObject:resArray];
    
	if (dict == nil || [dict count] != 0)
	{
		STFail(@"testUserMetadataNoDataTest failed dict Test_EmptyData should have 0 items");
	}
}

- (void)testUserMetadataSumTest
{
	NSArray *resArray = [TestBuddySDK GetTextFileData:@"Test_MetadataSum"];
    
	bwaiting = true;
	[self alogin];
	[self waitloop];
	if (![Buddy user])
	{
		STFail(@"testUserMetadataSumTest login failed.");
		return;
	}
    
	NSArray *dict = [[Buddy user].metadata performSelector:@selector(makeMetadataSumArray:) withObject:resArray];
    
	if ([dict count] != 2)
	{
		STFail(@"testUserMetadataSumTest failed dict should have 2 items");
	}
    
	resArray = [TestBuddySDK GetTextFileData:@"Test_NoData"];
	dict = [[Buddy user].metadata performSelector:@selector(makeMetadataSumArray:) withObject:resArray];
    
	if ([dict count] != 0)
	{
		STFail(@"testUserMetadataSumTest failed dict should have 0 items");
	}
    
	resArray = [TestBuddySDK GetTextFileData:@"Test_EmptyData"];
	dict = [[Buddy user].metadata performSelector:@selector(makeMetadataSumArray:) withObject:resArray];
	if ([dict count] != 0)
	{
		STFail(@"testUserMetadataSumTest failed dict should have 0 items");
	}
}


@end


