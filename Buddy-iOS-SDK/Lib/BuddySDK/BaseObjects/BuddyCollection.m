//
//  BuddyCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 10/7/13.
//
//

#import "BuddyCollection.h"
#import "BPClient.h"
#import "BuddyObject+Private.h"

@implementation BuddyCollection

@synthesize client=_client;

-(instancetype)initWithClient:(BPClient*)client
{
    self = [super init];
    if(self)
    {
        _client = client;
    }
    return self;
}

-(BPClient*)client
{
    if(_client!=nil)
    {
        return _client;
    }
    
    return [BPClient defaultClient];
}

-(void)getAll:(BuddyCollectionCallback)callback
{
    [self search:nil callback:callback];
}

-(void)search:(NSDictionary *)searchParmeters callback:(BuddyCollectionCallback)callback
{
    [[[BPClient defaultClient] restService] GET:[[self type] requestPath] parameters:searchParmeters callback:^(id json, NSError *error) {
        NSArray *results = [json[@"pageResults"] map:^id(id object) {
            return [[self.type alloc] initBuddyWithResponse:object andClient:self.client];
        }];
        callback(results, error);
    }];
}

- (void)getItem:(NSString *)identifier callback:(BuddyObjectCallback)callback
{
    NSString *resource = [NSString stringWithFormat:@"%@/%@",
                          [[self type] requestPath],
                          identifier];
    
    [[[BPClient defaultClient] restService] GET:resource parameters:nil callback:^(id json, NSError *error) {
        id buddyObject = [[self.type alloc] initBuddyWithResponse:json andClient:self.client];
        callback(buddyObject, error);
    }];
}

@end
