//
//  BuddyCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 10/7/13.
//
//

#import "BuddyCollection.h"
#import "BPSession.h"
#import "BuddyObject+Private.h"

@implementation BuddyCollection

-(void)getAll:(BuddyCollectionCallback)complete
{
    [[[BPSession currentSession] restService] GET:[[self type] requestPath] parameters:nil callback:^(id json, NSError *error) {

        NSArray *f = [json[@"pageResults"] map:^id(id object) {
            return [[self.type alloc] initBuddyWithResponse:object];
        }];
        complete(f);
    }];
}

@end
