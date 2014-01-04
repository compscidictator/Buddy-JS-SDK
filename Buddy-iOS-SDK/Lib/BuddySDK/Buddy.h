//
//  Buddy.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <Foundation/Foundation.h>

#import "BuddyDevice.h"
#import "BPClient.h"
#import "BPCheckin.h"
#import "BPCheckinCollection.h"
#import "BPPhoto.h"
#import "BPUser.h"
#import "BPGameBoards.h"
#import "BPSounds.h"
#import "BPAppMetadata.h"
#import "BPClient.h"
#import "BPPhotoCollection.h"
#import "BPCoordinate.h"

@interface Buddy : NSObject

+ (BPUser *)user;

+ (BuddyDevice *)device;

+ (BPGameBoards *)gameBoards;

+ (BPAppMetadata *)metadata;

+ (BPSounds *)sounds;

+ (BPCheckinCollection *) checkins;

+ (BPPhotoCollection *) photos;

+ (BOOL) locationEnabled;

+ (void) setLocationEnabled:(BOOL)enabled;

+ (void)initClient:(NSString *)appID
            appKey:(NSString *)appKey
          complete:(void (^)())complete;

+ (void) initClient:(NSString *)appID
             appKey:(NSString *)appKey
        withOptions:(NSDictionary *)options
           complete:(void (^)())complete;

+ (void)   initClient:(NSString *)appID
               appKey:(NSString *)appKey
 autoRecordDeviceInfo:(BOOL)autoRecordDeviceInfo
   autoRecordLocation:(BOOL)autoRecordLocation
          withOptions:(NSDictionary *)options
             complete:(void (^)())complete;


// TODO - document options
+ (void)createUser:(NSString *)username password:(NSString *)password options:(NSDictionary *)options completed:(BuddyObjectCallback)callback;

+ (void)login:(NSString *)username password:(NSString *)password completed:(BuddyObjectCallback)callback;

+ (void)socialLogin:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BPBuddyObjectCallback) callback;

@end
