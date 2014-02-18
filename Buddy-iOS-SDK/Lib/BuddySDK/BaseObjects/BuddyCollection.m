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
#import "BuddyCollection+Private.h"

@interface BuddyCollection()

@property (nonatomic, copy) NSString *requestPrefix;

@end

@implementation BuddyCollection

@synthesize client=_client;

- (instancetype)initWithClient:(id<BPRestProvider>)client {
    self = [super init];
    if(self)
    {
        _client = client;
    }
    return self;
}

- (NSString *)requestPrefix
{
    return @"";
}

-(id<BPRestProvider>)client
{   
    return _client ?: (id<BPRestProvider>)[BPClient defaultClient];
}

-(void)getAll:(BuddyCollectionCallback)callback
{
    [self search:nil callback:callback];
}

-(void)search:(NSDictionary *)searchParmeters callback:(BuddyCollectionCallback)callback
{
    [self.client GET:[[self type] requestPath] parameters:searchParmeters callback:^(id json, NSError *error) {
        NSArray *results = [json[@"pageResults"] map:^id(id object) {
            return [[self.type alloc] initBuddyWithResponse:object andClient:self.client];
        }];
        callback ? callback(results, error) : nil;
    }];
}

- (void)getItem:(NSString *)identifier callback:(BuddyObjectCallback)callback
{
    NSString *resource = [self.requestPrefix stringByAppendingFormat:@"%@/%@",
                                [[self type] requestPath],
                                identifier];
    
    [self.client GET:resource parameters:nil callback:^(id json, NSError *error) {
        id buddyObject = [[self.type alloc] initBuddyWithResponse:json andClient:self.client];
        callback ? callback(buddyObject, error) : nil;
    }];
}

@end
