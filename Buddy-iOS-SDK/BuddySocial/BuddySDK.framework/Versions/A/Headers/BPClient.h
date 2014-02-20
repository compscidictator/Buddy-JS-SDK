//
//  BPClient.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <Foundation/Foundation.h>


#import "BPRestProvider.h"
#import "BPClientDelegate.h"
#import "BuddyCollection.h" // TODO - remove dependency
#import "BPMetricCompletionHandler.h"
#import "BPBase.h"

@class BuddyDevice;
@class BPGameBoards;
@class BPAppMetadata;
@class BPSounds;
@class BPUser;
@class BPCheckinCollection;
@class BPPhotoCollection;
@class BPBlobCollection;
@class BPAlbumCollection;
@class BPCoordinate;

/**
 Enum specifying the current authentication level.
 */
typedef NS_ENUM(NSInteger, BPAuthenticationLevel) {
    /** No authentication */
    BPAuthenticationLevelNone,
    /** App/Device level authentication */
    BPAuthenticationLevelDevice,
    /** User level authentication */
    BPAuthenticationLevelUser
};

@interface BPClient : BPBase


/** Callback signature for the BuddyClientPing function. BuddyStringResponse.result field will be "Pong" if the server responds correctly. If there was an exception or error (e.g. unknown server response or invalid data) the response.exception field will be set to an exception instance and the raw response from the server, if any, will be held in the response.dataResult field.
 */
typedef void (^BPPingCallback)(NSDecimalNumber *ping);

/// <summary>
/// Gets the application name for this client.
/// </summary>
@property (readonly, nonatomic, assign) NSString *appID;

/// <summary>
/// Gets the application password for this client.
/// </summary>
@property (readonly, nonatomic, assign) NSString *appKey;

/// <summary>
/// Gets the optional string that describes the version of the app you are building. This string is used when uploading
/// device information to Buddy or submitting crash reports. It will default to 1.0.
/// </summary>
@property (readonly, nonatomic, assign) NSString *appVersion;

/// <summary>
/// Gets an object that can be used to record device information about this client or upload crashes.
/// </summary>
@property (readonly, nonatomic, strong) BuddyDevice *device;

/// <summary>
/// Gets an object that can be used to retrieve high score rankings or search for game boards in this application.
/// </summary>
@property (readonly, nonatomic, strong) BPGameBoards *gameBoards;


/// <summary>
/// Gets an object that can be used to manipulate application-level metadata. Metadata is used to store custom values on the platform.
/// </summary>
@property (readonly, nonatomic, strong) BPAppMetadata *metadata;

/// <summary>
/// Gets an object that can be used to retrieve sounds.
/// </summary>
@property (readonly, nonatomic, strong) BPSounds *sounds;

/// <summary>
/// Gets an object that can be used to retrieve sounds.
/// </summary>
@property (readonly, nonatomic, strong) BPCheckinCollection *checkins;

/// <summary>
/// TODO
/// </summary>
@property (readonly, nonatomic, strong) BPPhotoCollection *photos;

/// <summary>
/// TODO
/// </summary>
@property (readonly, nonatomic, strong) BPBlobCollection *blobs;

/// <summary>
/// TODO
/// </summary>
@property (readonly, nonatomic, strong) BPAlbumCollection *albums;

/// <summary>
/// TODO
/// </summary>
@property (nonatomic, assign) BOOL locationEnabled;

/**
  * Most recent BPCoordinate.
  */
@property (nonatomic, readonly, strong) BPCoordinate *lastLocation;

/// <summary>
/// Current BuddyAuthenticatedUser as of the last login
/// </summary>
@property (nonatomic, readonly, strong) BPUser *user;

@property (nonatomic,weak) id<BPClientDelegate> delegate;

/// <summary>
/// Singleton instance of the client.
/// </summary>
+ (instancetype)defaultClient;


@property (nonatomic, readonly, strong) id <BPRestProvider> restService;
/// TODO
-(void)setupWithApp:(NSString *)appID
                appKey:(NSString *)appKey
                options:(NSDictionary *)options
                delegate:(id<BPClientDelegate>) delegate;

- (void)login:(NSString *)username password:(NSString *)password callback:(BuddyObjectCallback)callback;

- (void)socialLogin:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BuddyObjectCallback) callback;

- (void)logout:(BuddyCompletionCallback)callback;

- (void)ping:(BPPingCallback)callback;

- (void)recordMetric:(NSString *)key andValue:(NSDictionary *)value callback:(BuddyCompletionCallback)callback;

- (void)recordTimedMetric:(NSString *)key andValue:(NSDictionary *)value timeout:(NSInteger)seconds callback:(BuddyMetricCallback)callback;

- (void)registerPushToken:(NSString *)token callback:(BuddyObjectCallback)callback;

- (void) registerForPushes;

@end
