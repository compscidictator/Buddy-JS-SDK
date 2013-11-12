//
//  BuddyObject.h
//  BuddySDK
//
//  Created by Erik Kerber on 9/11/13.
//
//

#import <Foundation/Foundation.h>
#import "BuddyRequests.h"

@interface BuddyObject : NSObject

@property (nonatomic, copy) NSString *resourceString;
@property (nonatomic, strong) id<BuddyRequests> client;

@property (nonatomic, readonly, assign) BOOL isDirty;
@property (nonatomic, strong) NSDate *created;
@property (nonatomic, strong) NSDate *lastModified;
@property (nonatomic, strong) NSString *tag;
@property (nonatomic, strong) NSNumber *userId;
@property (nonatomic, assign) NSInteger identifier;


+(instancetype)create;
-(void)deleteMe;
-(void)refresh;
-(void)update;

// "Abstracts" meant to be overidden.
-(NSDictionary *)buildUpdateDictionary;

@end
