//
//  Buddy.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <Foundation/Foundation.h>

#import "BuddyDevice.h"

#import "BPAuthenticatedUser.h"
#import "BPCheckin.h"
#import "BPCheckinCollection.h"
#import "BPPhoto.h"
#import "BPUser.h"
#import "BPGameBoards.h"
#import "BPSounds.h"
#import "BPAppMetadata.h"
#import "BPClient.h"
#import "BPPhotoCollection.h"

@interface Buddy : NSObject

+ (BPAuthenticatedUser *)user;

+ (BuddyDevice *)device;

+ (BPGameBoards *)gameBoards;

+ (BPAppMetadata *)metadata;

+ (BPSounds *)sounds;

+ (BPCheckinCollection *) checkins;

+ (BPPhotoCollection *) photos;

+ (BOOL) locationEnabled;

+ (void) setLocationEnabled:(BOOL)enabled;

+ (void)initClient:(NSString *)appID
            appKey:(NSString *)appKey;

+ (void) initClient:(NSString *)appID
             appKey:(NSString *)appKey
        withOptions:(NSDictionary *)options;

+ (void)   initClient:(NSString *)appID
               appKey:(NSString *)appKey
 autoRecordDeviceInfo:(BOOL)autoRecordDeviceInfo
   autoRecordLocation:(BOOL)autoRecordLocation
          withOptions:(NSDictionary *)options;
@end
