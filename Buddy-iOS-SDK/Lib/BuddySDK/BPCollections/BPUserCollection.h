//
//  BPUserCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 1/20/14.
//
//

#import "BuddyCollection.h"

@interface BPUserCollection : BuddyCollection

-(void)getUsers:(BuddyCollectionCallback)callback;

-(void)searchUsers:(NSDictionary *)parameters callback:(BuddyCollectionCallback)callback;

@end
