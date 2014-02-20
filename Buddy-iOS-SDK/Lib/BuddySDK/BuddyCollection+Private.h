//
//  BuddyCollection+Private.h
//  BuddySDK
//
//  Created by Erik Kerber on 12/2/13.
//
//

#import "BuddyCollection.h"
#import "BPRestProvider.h"

@interface BuddyCollection (Private)

@property (nonatomic, readonly, copy) NSString *requestPrefix;

-(void)search:(NSDictionary *)searchParmeters callback:(BuddyCollectionCallback)callback;

@end