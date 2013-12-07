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

typedef void (^BuddyObjectCallback)(id newBuddyObject);
typedef void (^BuddyCompletionCallback)();


@interface BuddyObject : NSObject

@property (nonatomic, readonly, assign) BOOL isDirty;
@property (nonatomic, strong) NSDate *created;
@property (nonatomic, strong) NSDate *lastModified;
@property (nonatomic, copy) NSString *defaultMetadata;
@property (nonatomic, strong) NSNumber *userId;
@property (nonatomic, copy) NSString *identifier;

-(instancetype) init __attribute__((unavailable("init not available")));
+(instancetype) new __attribute__((unavailable("new not available")));


+(NSString *)requestPath;

+(instancetype)create;
+(void)createFromServerWithParameters:(NSDictionary *)parameters complete:(BuddyObjectCallback)callback;
+(void)queryFromServerWithId:(NSString *)identifier callback:(BuddyObjectCallback)callback;
-(void)deleteMe:(void(^)())complete;
-(void)deleteMe;
-(void)refresh;
-(void)refresh:(BuddyCompletionCallback)complete;
-(void)update;

// "Abstracts" meant to be overidden.
-(NSDictionary *)buildUpdateDictionary;

-(void)registerProperty:(SEL)property;


@end
