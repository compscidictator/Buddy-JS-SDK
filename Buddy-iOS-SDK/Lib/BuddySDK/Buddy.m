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

+ (void)initClient:(NSString *)name
       appPassword:(NSString *)password
{
	[Buddy initClient:name appPassword:password withOptions:nil];
}

+ (void) initClient:(NSString *)name
        appPassword:(NSString *)password
        withOptions:(NSDictionary *)options
{
    NSString *version = options[@"appVersion"] ? options[@"appVersion"] : @"1.0";
    BOOL autoRecordDeviceInfo = [options[@"autoRecordDeviceInfo"] boolValue];
    
    [[BuddyClient defaultClient] initClient:name
                                appPassword:password
                                 appVersion:version
                       autoRecordDeviceInfo:autoRecordDeviceInfo];
}

@end
