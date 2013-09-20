/*
 * Copyright (C) 2013 Buddy Platform, Inc.
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

#import "Buddy.h"
#import "BuddyClient.h"

/// <summary>
/// TODO
/// </summary>

@implementation Buddy

+ (BuddyAuthenticatedUser *)user{
    return [[BuddyClient defaultClient] user];
}

+ (BuddyDevice *)device{return nil;}

+ (BuddyGameBoards *)gameBoards{return nil;}

+ (BuddyAppMetadata *)metadata{return nil;}

+ (BuddySounds *)sounds{return nil;}

+ (void)initClient:(NSString *)name
       appPassword:(NSString *)password
{
	[Buddy initClient:name
          appPassword:password
 autoRecordDeviceInfo:NO
   autoRecordLocation:NO
          withOptions:nil];
}

+ (void)    initClient:(NSString *)name
           appPassword:(NSString *)password
           withOptions:(NSDictionary *)options
{
    [[BuddyClient defaultClient] setupWithApp:name
                                     password:password
                                      options:options];
}

+ (void)   initClient:(NSString *)name
          appPassword:(NSString *)password
 autoRecordDeviceInfo:(BOOL)autoRecordDeviceInfo
   autoRecordLocation:(BOOL)autoRecordLocation
          withOptions:(NSDictionary *)options
{
    
    NSDictionary *defaultOptions = @{@"autoRecordLocation": @(autoRecordLocation),
                                     @"autoRecordDeviceInfo": @(autoRecordDeviceInfo)};
    
    NSMutableDictionary *combined = [NSMutableDictionary dictionaryWithDictionary:defaultOptions];
    // TODO - merge options
    
    [[BuddyClient defaultClient] setupWithApp:name
                                     password:password
                                      options:combined];
}

@end
