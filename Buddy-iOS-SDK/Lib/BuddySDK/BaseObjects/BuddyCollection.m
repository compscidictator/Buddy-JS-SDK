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

@synthesize session=_session;

-(instancetype)initWithSession:(BPSession*)session
{
    self = [super init];
    if(self)
    {
        _session = session;
    }
    return self;
}

-(BPSession*)session
{
    if(_session!=nil)
    {
        return _session;
    }
    
    return [BPSession currentSession];
}

-(void)getAll:(BuddyCollectionCallback)callback
{
    [self search:nil callback:callback];
}

-(void)search:(NSDictionary *)searchParmeters callback:(BuddyCollectionCallback)callback
{
    [[[BPSession currentSession] restService] GET:[[self type] requestPath] parameters:searchParmeters callback:^(id json, NSError *error) {
        NSArray *results = [json[@"pageResults"] map:^id(id object) {
            return [[self.type alloc] initBuddyWithResponse:object andSession:self.session];
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
        id buddyObject = [[self.type alloc] initBuddyWithResponse:json andSession:self.session];
        callback(buddyObject, error);
    }];
}

@end
