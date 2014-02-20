//
//  BPMetadataBase.h
//  BuddySDK
//
//  Created by Erik Kerber on 2/11/14.
//
//

#import <Foundation/Foundation.h>

/**
 Permissions scope for Buddy objects.
 */
typedef NS_ENUM(NSInteger, BuddyPermissions){
    /** Accessible by App. */
    BuddyPermissionsApp,
    /** Accessible by owner. */
    BuddyPermissionsUser,
    /** Default (Accessible by Owner). */
    BuddyPermissionsDefault = BuddyPermissionsUser
};

typedef void (^BuddyCompletionCallback)(NSError *error);
typedef void (^BuddyObjectCallback)(id newBuddyObject, NSError *error);

@interface BPBase : NSObject

- (NSString *) metadataPath:(NSString *)key;

- (void)setMetadataWithKey:(NSString *)key andString:(NSString *)value permissions:(BuddyPermissions)permissions callback:(BuddyCompletionCallback)callback;
- (void)setMetadataWithKey:(NSString *)key andInteger:(NSInteger)value permissions:(BuddyPermissions)permissions callback:(BuddyCompletionCallback)callback;
- (void)setMetadataWithKeyValues:(NSDictionary *)keyValuePaths permissions:(BuddyPermissions)permissions callback:(BuddyCompletionCallback)callback;
- (void)getMetadataWithKey:(NSString *)key permissions:(BuddyPermissions) permissions callback:(BuddyObjectCallback) callback;

/*- (void)getMetadataWithPermissions:(BuddyPermissions)permissions callback:(BuddyObjectCallback)callback; */

- (void)deleteMetadataWithKey:(NSString *)key permissions:(BuddyPermissions) permissions callback:(BuddyCompletionCallback)callback;

@property (nonatomic, readonly, strong) id<BPRestProvider> client;

@end
