//
//  BuddyObject.h
//  BuddySDK
//
//  Created by Erik Kerber on 9/11/13.
//
//

#import <Foundation/Foundation.h>

@class BPSession;

/**
 Permissions scope for Buddy objects.
 */
typedef NS_ENUM(NSInteger, BuddyPermissions){
    /** Accessible by owner. */
    BuddyPermissionsOwner,
    /** Accessible by App. */
    BuddyPermissionsApp,
    /** Default (Accessible by Owner). */
    BuddyPermissionsDefault = BuddyPermissionsOwner
};

typedef void (^BuddyObjectCallback)(id newBuddyObject, NSError *error);
typedef void (^BuddyCompletionCallback)(NSError *error);

@class BPSession;

@interface BuddyObject : NSObject

@property (nonatomic, readonly, strong) BPSession* session;

@property (nonatomic, readonly, assign) BOOL isDirty;
@property (nonatomic, strong) NSDate *created;
@property (nonatomic, strong) NSDate *lastModified;
@property (nonatomic, copy) NSString *defaultMetadata;
@property (nonatomic, copy) NSString *id;

- (instancetype) init __attribute__((unavailable("init not available")));
+ (instancetype) new __attribute__((unavailable("new not available")));

+ (NSString *)requestPath;

+ (void)createFromServerWithParameters:(NSDictionary *)parameters session:(BPSession*)session callback:(BuddyObjectCallback)callback;
+ (void)queryFromServerWithId:(NSString *)identifier session:(BPSession*)session callback:(BuddyObjectCallback)callback;
- (void)deleteMe:(BuddyCompletionCallback)callback;
- (void)deleteMe;
- (void)refresh;
- (void)refresh:(BuddyCompletionCallback)callback;
- (void)save:(BuddyCompletionCallback)callback;

- (void)registerProperty:(SEL)property;


@end
