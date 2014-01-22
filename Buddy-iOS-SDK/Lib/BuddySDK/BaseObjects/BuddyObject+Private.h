//
//  BuddyObject+Private.h
//  BuddySDK
//
//  Created by Erik Kerber on 12/2/13.
//
//

#import "BuddyObject.h"

@class BPClient;

@interface BuddyObject (Private)

- (instancetype)initBuddyWithClient:(BPClient*)client;
- (instancetype)initBuddyWithResponse:(id)response andClient:(BPClient*)client;

- (NSDictionary *)buildUpdateDictionary;

@end
