//
//  BPClient.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <Foundation/Foundation.h>
@class BuddyDevice;
@class BPGameBoards;
@class BPAppMetadata;
@class BPSounds;
@class BPAuthenticatedUser;
@class BPCheckinCollection;

typedef enum {
    BPAuthenticationLevelNone,
    BPAuthenticationLevelDevice,
    BPAuthenticationLevelUser
}BPAuthenticationLevel;

@interface BPClient : NSObject


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
@property (nonatomic, assign) BOOL locationEnabled;

/// <summary>
/// Current BuddyAuthenticatedUser as of the last login
/// </summary>
@property (nonatomic, readonly, strong) BPAuthenticatedUser *user;


/// <summary>
/// Singleton instance of the client.
/// </summary>
+ (instancetype)defaultClient;

/// TODO
-(void)setupWithApp:(NSString *)appID appKey:(NSString *)appKey options:(NSDictionary *)options complete:(void (^)())complete;



typedef void (^BPBuddyObjectCallback)(id json);

-(void)createObjectWithPath:(NSString *)path parameters:(NSDictionary *)parameters complete:(BPBuddyObjectCallback) callback;

-(void)refreshObject:(BuddyObject *)object complete:(BPBuddyObjectCallback) callback;

-(void)updateObject:(BuddyObject *)object complete:(BPBuddyObjectCallback) callback;

-(void)deleteObject:(BuddyObject *)object complete:(BPBuddyObjectCallback) callback;

-(void)login:(NSString *)username password:(NSString *)password success:(BPBuddyObjectCallback) callback;

/// <summary>
/// Ping the service.
/// </summary>
/// <param name="callback">The callback to call on success or error. The .result field of BuddyStringResponse will be "Pong" if the server responded. If the BuddyBoolResponse.isCompleted field is FALSE check the BuddyStringResponse.dataResult and/or BuddyStringResponse.exception for error information.</param>
- (void)ping:(BPPingCallback)callback;


@end


