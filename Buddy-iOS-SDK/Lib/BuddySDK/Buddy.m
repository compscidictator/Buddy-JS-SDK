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
#import "BuddyObject+Private.h"

/// <summary>
/// TODO
/// </summary>

@implementation Buddy

+ (BPUser *)user{
    return [[BPSession currentSession] user];
}

+ (BuddyDevice *)device{
    return [[BPSession currentSession] device];
}

+ (BPCheckinCollection *) checkins{
    return [[BPSession currentSession] checkins];
}

+ (BPPhotoCollection *) photos{
    return [[BPSession currentSession] photos];
}

+ (BPBlobCollection *) blobs{
    return [[BPSession currentSession] blobs];
}

+ (BOOL) locationEnabled{
    @synchronized(self){
        return [[BPSession currentSession] locationEnabled];
    }
}

+ (void) setLocationEnabled:(BOOL)val
{
    @synchronized(self){
        [[BPSession currentSession] setLocationEnabled:val];
    }
}

+ (void)initClient:(NSString *)appID
       appKey:(NSString *)appKey
          callback:(void (^)())callback
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
           callback:(void (^)())callback
{
    [[BPSession currentSession] setupWithApp:appID
                                     appKey:appKey
                                      options:options
                                  callback:callback];
}

+ (void)   initClient:(NSString *)appID
          appKey:(NSString *)appKey
 autoRecordDeviceInfo:(BOOL)autoRecordDeviceInfo
   autoRecordLocation:(BOOL)autoRecordLocation
          withOptions:(NSDictionary *)options
             callback:(void (^)())callback
{
    
    NSDictionary *defaultOptions = @{@"autoRecordLocation": @(autoRecordLocation),
                                     @"autoRecordDeviceInfo": @(autoRecordDeviceInfo)};
    
    NSMutableDictionary *combined = [NSMutableDictionary dictionaryWithDictionary:defaultOptions];
    // TODO - merge options
    
    [[BPSession currentSession] setupWithApp:appID
                                    appKey:appKey
                                   options:combined
                                  callback:callback];
}

#pragma mark User

+ (void)createUser:(NSString *)username password:(NSString *)password options:(NSDictionary *)options callbackd:(BuddyObjectCallback)callback
{
    NSDictionary *parameters = @{@"username": username,
                                 @"password": password };
    
    parameters = [NSDictionary dictionaryByMerging:parameters with:options];
    
    // On BPUser for now for consistency. Probably will move.
    [BPUser createFromServerWithParameters:parameters callback:callback];
}

+ (void)login:(NSString *)username password:(NSString *)password callbackd:(BuddyObjectCallback)callback
{
    [[BPSession currentSession] login:username password:password success:^(id json, NSError *error) {
        
        if(error) {
            callback(nil, error);
            return;
        }
        
        BPUser *user = [[BPUser alloc] initBuddyWithResponse:json];
        user.isMe = YES;
        
        [user refresh:^(NSError *error){
#pragma messsage("TODO - Error")
            [[BPSession currentSession] initializeCollectionsWithUser:user];
            callback(user, nil);
        }];
    }];
}

+ (void)socialLogin:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BuddyObjectCallback) callback;
{
    [[BPSession currentSession] socialLogin:provider providerId:providerId token:token success:^(id json, NSError *error) {

        if (error) {
            callback(nil, error);
            return;
        }
        
        BPUser *user = [[BPUser alloc] initBuddyWithResponse:json];
        user.isMe = YES;

        [user refresh:^(NSError *error){
            callback(user, error);
        }];
    }];
}

@end
