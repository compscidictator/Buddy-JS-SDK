//
//  BPClient.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <Foundation/Foundation.h>
#import "BPRestProvider.h"
#import "BuddyCollection.h" // TODO - remove dependency

@class BuddyDevice;
@class BPGameBoards;
@class BPAppMetadata;
@class BPSounds;
@class BPUser;
@class BPCheckinCollection;
@class BPPhotoCollection;
@class BPBlobCollection;

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

@interface BPSession : NSObject


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
@property (nonatomic, assign) BOOL locationEnabled;

/// <summary>
/// Current BuddyAuthenticatedUser as of the last login
/// </summary>
@property (nonatomic, readonly, strong) BPUser *user;

/// <summary>
/// Singleton instance of the client.
/// </summary>
+ (instancetype)currentSession;


@property (nonatomic, readonly, strong) id <BPRestProvider> restService;
/// TODO
-(void)setupWithApp:(NSString *)appID appKey:(NSString *)appKey options:(NSDictionary *)options callback:(BuddyCompletionCallback)callback;

- (void)login:(NSString *)username password:(NSString *)password success:(BuddyObjectCallback) callback;
- (void)socialLogin:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BuddyObjectCallback) callback;
- (void)ping:(BPPingCallback)callback;

#pragma message("TODO - Remove this from .h once user creation responsibility is off Buddy.m")
- (void)initializeCollectionsWithUser:(BPUser *)user;

@end


