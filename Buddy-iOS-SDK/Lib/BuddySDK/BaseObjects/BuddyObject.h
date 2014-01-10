//
//  BuddyObject.h
//  BuddySDK
//
//  Created by Erik Kerber on 9/11/13.
//
//

#import <Foundation/Foundation.h>

typedef enum{
    BuddyPermissionsOwner,
    BuddyPermissionsApp,
    BuddyPermissionsDefult = BuddyPermissionsOwner
}BuddyPermissions;

typedef void (^BuddyObjectCallback)(id newBuddyObject, NSError *error);
typedef void (^BuddyCompletionCallback)(NSError *error);


@interface BuddyObject : NSObject

@property (nonatomic, readonly, assign) BOOL isDirty;
@property (nonatomic, strong) NSDate *created;
@property (nonatomic, strong) NSDate *lastModified;
@property (nonatomic, copy) NSString *defaultMetadata;
@property (nonatomic, strong) NSNumber *userId;
@property (nonatomic, copy) NSString *id;

- (instancetype) init __attribute__((unavailable("init not available")));
+ (instancetype) new __attribute__((unavailable("new not available")));


+ (NSString *)requestPath;

+ (void)createFromServerWithParameters:(NSDictionary *)parameters complete:(BuddyObjectCallback)callback;
+ (void)queryFromServerWithId:(NSString *)identifier callback:(BuddyObjectCallback)callback;
- (void)deleteMe:(void(^)())complete;
- (void)deleteMe;
- (void)refresh;
- (void)refresh:(BuddyCompletionCallback)complete;
- (void)save:(BuddyCompletionCallback)callback;

- (void)registerProperty:(SEL)property;


@end
