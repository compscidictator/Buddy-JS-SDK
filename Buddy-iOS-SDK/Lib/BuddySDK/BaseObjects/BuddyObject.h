//
//  BuddyObject.h
//  BuddySDK
//
//  Created by Erik Kerber on 9/11/13.
//
//

#import <Foundation/Foundation.h>

@interface BuddyObject : NSObject

@property (nonatomic, readonly, assign) BOOL isDirty;
@property (nonatomic, strong) NSDate *created;
@property (nonatomic, strong) NSDate *lastModified;
@property (nonatomic, strong) NSString *tag;
@property (nonatomic, strong) NSNumber *userId;
@property (nonatomic, assign) NSInteger identifier;

-(instancetype) init __attribute__((unavailable("init not available")));
+(instancetype) new __attribute__((unavailable("new not available")));


+(NSString *)requestPath;

+(instancetype)create;
//+(void)createFromServer:(BuddyObjectCallback)callback;
//+(void)queryFromServerWithId:(NSInteger)identifier callback:(BuddyObjectCallback)callback;
-(void)deleteMe;
-(void)refresh;
-(void)update;

// "Abstracts" meant to be overidden.
-(NSDictionary *)buildUpdateDictionary;

-(void)registerProperty:(SEL)property;


@end
