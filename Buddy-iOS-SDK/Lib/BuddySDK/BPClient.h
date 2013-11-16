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

@interface BPClient : NSObject

/// <summary>
/// Gets the application name for this client.
/// </summary>
@property (readonly, nonatomic, assign) NSString *appName;

/// <summary>
/// Gets the application password for this client.
/// </summary>
@property (readonly, nonatomic, assign) NSString *appPassword;

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
/// TODO
/// </summary>
@property (nonatomic, assign) BOOL locationEnabled;

/// <summary>
/// Singleton instance of the client.
/// </summary>
+ (instancetype)defaultClient;

/// <summary>
/// Current BuddyAuthenticatedUser as of the last login
/// </summary>
- (BPAuthenticatedUser *)user;

/// TODO
-(void)     setupWithApp:(NSString *)appName
                password:(NSString *)appPassword
                 options:(NSDictionary *)options;

@end
