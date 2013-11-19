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

/// <summary>
/// TODO
/// </summary>

@implementation Buddy

+ (BPAuthenticatedUser *)user{
    return [[BPClient defaultClient] user];
}

+ (BuddyDevice *)device{
    return [[BPClient defaultClient] device];
    
}

+ (BPGameBoards *)gameBoards{
    return [[BPClient defaultClient] gameBoards];
    
}

+ (BPAppMetadata *)metadata{
    return [[BPClient defaultClient] metadata];
    
}

+ (BPSounds *)sounds{
    return [[BPClient defaultClient] sounds];
}

+ (BOOL) locationEnabled{
    @synchronized(self){
        return [[BPClient defaultClient] locationEnabled];
    }
}

+ (void) setLocationEnabled:(BOOL)val
{
    @synchronized(self){
        [[BPClient defaultClient] setLocationEnabled:val];
    }
}

+ (void)initClient:(NSString *)appID
       appKey:(NSString *)appKey
{
	[Buddy initClient:appID
          appKey:appKey
 autoRecordDeviceInfo:NO
   autoRecordLocation:NO
          withOptions:nil];
}

+ (void)    initClient:(NSString *)appID
           appKey:(NSString *)appKey
           withOptions:(NSDictionary *)options
{
    [[BPClient defaultClient] setupWithApp:appID
                                     appKey:appKey
                                      options:options];
}

+ (void)   initClient:(NSString *)appID
          appKey:(NSString *)appKey
 autoRecordDeviceInfo:(BOOL)autoRecordDeviceInfo
   autoRecordLocation:(BOOL)autoRecordLocation
          withOptions:(NSDictionary *)options
{
    
    NSDictionary *defaultOptions = @{@"autoRecordLocation": @(autoRecordLocation),
                                     @"autoRecordDeviceInfo": @(autoRecordDeviceInfo)};
    
    NSMutableDictionary *combined = [NSMutableDictionary dictionaryWithDictionary:defaultOptions];
    // TODO - merge options
    
    [[BPClient defaultClient] setupWithApp:appID
                                     appKey:appKey
                                      options:combined];
}



@end
