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

-(void)getAll:(BuddyCollectionCallback)callback
{
    [self search:nil callback:callback];
}

-(void)search:(NSDictionary *)searchParmeters callback:(BuddyCollectionCallback)callback
{
    [[[BPSession currentSession] restService] GET:[[self type] requestPath] parameters:searchParmeters callback:^(id json, NSError *error) {
        NSArray *results = [json[@"pageResults"] map:^id(id object) {
            return [[self.type alloc] initBuddyWithResponse:object];
        }];
        callback(results, error);
    }];
}

- (void)getItem:(NSString *)identifier callback:(BuddyObjectCallback)callback
{
    NSString *resource = [NSString stringWithFormat:@"%@/%@",
                          [[self type] requestPath],
                          identifier];
    
    [[[BPSession currentSession] restService] GET:resource parameters:nil callback:^(id json, NSError *error) {
        id buddyObject = [[self.type alloc] initBuddyWithResponse:json];
        callback(buddyObject, error);
    }];
}

@end
