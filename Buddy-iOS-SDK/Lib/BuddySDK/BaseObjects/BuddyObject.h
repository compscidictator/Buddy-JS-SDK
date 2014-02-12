//
//  BuddyObject.h
//  BuddySDK
//
//  Created by Erik Kerber on 9/11/13.
//
//

#import <Foundation/Foundation.h>
#import "BPRestProvider.h"
#import "BPBase.h"

@protocol BuddyObjectProperties <NSObject>

@end

@interface BuddyObject : BPBase

@property (nonatomic, readonly, assign) BOOL isDirty;
@property (nonatomic, strong) NSDate *created;
@property (nonatomic, strong) NSDate *lastModified;
@property (nonatomic, copy) NSString *defaultMetadata;
@property (nonatomic, assign) BuddyPermissions readPermissions;
@property (nonatomic, assign) BuddyPermissions writePermissions;
@property (nonatomic, copy) NSString *id;

- (instancetype) init __attribute__((unavailable("init not available")));
+ (instancetype) new __attribute__((unavailable("new not available")));

- (void)registerProperty:(SEL)property;

+ (NSString *)requestPath;

+ (void)createFromServerWithParameters:(NSDictionary *)parameters client:(id<BPRestProvider>)client callback:(BuddyObjectCallback)callback;
+ (void)queryFromServerWithId:(NSString *)identifier client:(id<BPRestProvider>)client callback:(BuddyObjectCallback)callback;
- (void)deleteMe:(BuddyCompletionCallback)callback;
- (void)deleteMe;
- (void)refresh;
- (void)refresh:(BuddyCompletionCallback)callback;
- (void)save:(BuddyCompletionCallback)callback;

@end
