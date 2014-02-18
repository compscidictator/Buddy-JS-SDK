//
//  BPUserCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/20/14.
//
//

#import "BPUserCollection.h"
#import "BuddyCollection+Private.h"
#import "BPUser.h"

@implementation BPUserCollection

-(instancetype)init{
    self = [super init];
    if(self){
        self.type = [BPUser class];
    }
    return self;
}

-(void)getUsers:(BuddyCollectionCallback)callback
{
    [self getAll:callback];
}

-(void)searchUsers:(NSDictionary *)parameters callback:(BuddyCollectionCallback)callback
{
    [self search:nil callback:callback];
}


@end
