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
          complete:(void (^)())complete
{
	[Buddy initClient:appID
          appKey:appKey
 autoRecordDeviceInfo:NO
   autoRecordLocation:NO
          withOptions:nil
             complete:complete];
}

+ (void) initClient:(NSString *)appID
             appKey:(NSString *)appKey
        withOptions:(NSDictionary *)options
           complete:(void (^)())complete
{
    [[BPSession currentSession] setupWithApp:appID
                                     appKey:appKey
                                      options:options
                                  complete:complete];
}

+ (void)   initClient:(NSString *)appID
          appKey:(NSString *)appKey
 autoRecordDeviceInfo:(BOOL)autoRecordDeviceInfo
   autoRecordLocation:(BOOL)autoRecordLocation
          withOptions:(NSDictionary *)options
             complete:(void (^)())complete
{
    
    NSDictionary *defaultOptions = @{@"autoRecordLocation": @(autoRecordLocation),
                                     @"autoRecordDeviceInfo": @(autoRecordDeviceInfo)};
    
    NSMutableDictionary *combined = [NSMutableDictionary dictionaryWithDictionary:defaultOptions];
    // TODO - merge options
    
    [[BPSession currentSession] setupWithApp:appID
                                    appKey:appKey
                                   options:combined
                                  complete:complete];
}

#pragma mark User

+ (void)createUser:(NSString *)username password:(NSString *)password options:(NSDictionary *)options completed:(BuddyObjectCallback)callback
{
    NSDictionary *parameters = @{@"username": username,
                                 @"password": password };
    
    parameters = [NSDictionary dictionaryByMerging:parameters with:options];
    
    // On BPUser for now for consistency. Probably will move.
    [BPUser createFromServerWithParameters:parameters complete:callback];
}

+ (void)login:(NSString *)username password:(NSString *)password completed:(BuddyObjectCallback)callback
{
    [[BPSession currentSession] login:username password:password success:^(id json) {
        BPUser *user = [[BPUser alloc] initBuddy];
        user.id = json[@"id"];
        [user refresh:^(NSError *error){
#pragma messsage("TODO - Error")
            [[BPSession currentSession] initializeCollectionsWithUser:user];
            callback(user, nil);
        }];
    }];
}

+ (void)socialLogin:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BPBuddyObjectCallback) callback;
{
    [[BPSession currentSession] socialLogin:provider providerId:providerId token:token success:^(id json) {
        BPUser *user = [[BPUser alloc] initBuddy];
        user.id = json[@"id"];
        [user refresh:^(NSError *error){
            callback(user);
        }];
    }];
}

@end
