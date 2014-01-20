//
//  BuddyObject+Private.h
//  BuddySDK
//
//  Created by Erik Kerber on 12/2/13.
//
//

#import "BuddyObject.h"

@class BPSession;

@interface BuddyObject (Private)

- (instancetype)initBuddyWithSession:(BPSession*)session;
- (instancetype)initBuddyWithResponse:(id)response andSession:(BPSession*)session;

- (NSDictionary *)buildUpdateDictionary;

@end
