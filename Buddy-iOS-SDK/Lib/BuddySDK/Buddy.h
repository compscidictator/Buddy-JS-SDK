//
//  Buddy.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <Foundation/Foundation.h>

#import "BuddyDevice.h"
#import "BPSession.h"
#import "BPCheckin.h"
#import "BPCheckinCollection.h"
#import "BPPhoto.h"
#import "BPUser.h"
#import "BPGameBoards.h"
#import "BPSounds.h"
#import "BPAppMetadata.h"
#import "BPSession.h"
#import "BPPhotoCollection.h"
#import "BPBlobCollection.h"
#import "BPCoordinate.h"
#import "BPBlob.h"
#import "BPAlbum.h"

@interface Buddy : NSObject

+ (BPUser *)user;
+ (BuddyDevice *)device;
+ (BPCheckinCollection *) checkins;
+ (BPPhotoCollection *) photos;
+ (BPBlobCollection *) blobs;


+ (BOOL) locationEnabled;

+ (void) setLocationEnabled:(BOOL)enabled;

+ (void)initClient:(NSString *)appID
            appKey:(NSString *)appKey
          callback:(void (^)())callback;

+ (void) initClient:(NSString *)appID
             appKey:(NSString *)appKey
        withOptions:(NSDictionary *)options
           callback:(void (^)())callback;

+ (void)   initClient:(NSString *)appID
               appKey:(NSString *)appKey
 autoRecordDeviceInfo:(BOOL)autoRecordDeviceInfo
   autoRecordLocation:(BOOL)autoRecordLocation
          withOptions:(NSDictionary *)options
             callback:(void (^)())callback;


// TODO - document options
+ (void)createUser:(NSString *)username password:(NSString *)password options:(NSDictionary *)options callbackd:(BuddyObjectCallback)callback;

+ (void)login:(NSString *)username password:(NSString *)password callbackd:(BuddyObjectCallback)callback;

+ (void)socialLogin:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BPBuddyObjectCallback) callback;

@end
