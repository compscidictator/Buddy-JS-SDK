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
#import "BPClientDelegate.h"
#import "BuddyObject+Private.h"

/// <summary>
/// TODO
/// </summary>

@implementation Buddy

+ (id<BPRestProvider>)buddyRestProvider {
    return [BPClient defaultClient].restService;
}

+ (BPUser *)user{
    return [[BPClient defaultClient] user];
}

+ (BuddyDevice *)device{
    return [[BPClient defaultClient] device];
}

+ (BPCheckinCollection *) checkins{
    return [[BPClient defaultClient] checkins];
}

+ (BPPhotoCollection *) photos{
    return [[BPClient defaultClient] photos];
}

+ (BPBlobCollection *) blobs{
    return [[BPClient defaultClient] blobs];
}

+ (BPAlbumCollection *) albums{
    return [[BPClient defaultClient] albums];
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

+ (void)setClientDelegate:(id<BPClientDelegate>)delegate
{
    [BPClient defaultClient].delegate = delegate;
}

+ (void)initClient:(NSString *)appID
            appKey:(NSString *)appKey
            callback:(BuddyCompletionCallback)callback
{
    
	[Buddy initClient:appID
            appKey:appKey
            autoRecordDeviceInfo:NO
            autoRecordLocation:NO
            withOptions:nil
            callback:callback];
}

+ (void) initClient:(NSString *)appID
            appKey:(NSString *)appKey
            withOptions:(NSDictionary *)options
            callback:(BuddyCompletionCallback)callback

{
    [[BPClient defaultClient] setupWithApp:appID
            appKey:appKey
            options:options
            delegate:nil
            callback:callback];
}

+ (void) initClient:(NSString *)appID
            appKey:(NSString *)appKey
            autoRecordDeviceInfo:(BOOL)autoRecordDeviceInfo
            autoRecordLocation:(BOOL)autoRecordLocation
            withOptions:(NSDictionary *)options
            callback:(BuddyCompletionCallback)callback
{
    
    NSDictionary *defaultOptions = @{@"autoRecordLocation": @(autoRecordLocation),
                                     @"autoRecordDeviceInfo": @(autoRecordDeviceInfo)};
    
    NSMutableDictionary *combined = [NSMutableDictionary dictionaryWithDictionary:defaultOptions];
    // TODO - merge options
    
    [[BPClient defaultClient] setupWithApp:appID
            appKey:appKey
            options:combined
            delegate:nil
            callback:callback];
}

#pragma mark User

+ (void)createUser:(NSString *)username
                    password:(NSString *)password
                    options:(NSDictionary *)options
                    callback:(BuddyObjectCallback)callback
{
    NSDictionary *parameters = @{@"username": username,
                                 @"password": password };
    
    parameters = [NSDictionary dictionaryByMerging:parameters with:options];
    
    // On BPUser for now for consistency. Probably will move.
    [BPUser createFromServerWithParameters:parameters client:[BPClient defaultClient] callback:callback];
}

+ (void)login:(NSString *)username password:(NSString *)password callback:(BuddyObjectCallback)callback
{
    [[BPClient defaultClient] login:username password:password callback:callback  ];
     
}

+ (void)socialLogin:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BuddyObjectCallback) callback;
{
    [[BPClient defaultClient] socialLogin:provider providerId:providerId token:token success:callback];
}

+ (void)logout:(BuddyCompletionCallback)callback
{
    [[BPClient defaultClient] logout:callback];
}

+ (void)recordMetric:(NSString *)key andValue:(NSString *)value callback:(BuddyCompletionCallback)callback
{
    [[BPClient defaultClient] recordMetric:key andValue:value callback:callback];
}

+ (void)recordTimedMetric:(NSString *)key andValue:(NSString *)value timeout:(NSInteger)seconds callback:(BuddyMetricCallback)callback
{
    [[BPClient defaultClient] recordTimedMetric:key andValue:value timeout:seconds callback:callback];
}

@end
