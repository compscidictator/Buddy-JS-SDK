//
//  BuddyClientProvider.m
//  BuddySDK
//
//  Created by Erik Kerber on 10/7/13.
//
//

#import "BuddyClientProvider.h"
#import "BuddyRequests.h"

@interface BuddyClientProvider()

@property (strong, nonatomic) id<BuddyRequests> client;

@end

@implementation BuddyClientProvider

+(id<BuddyRequests>)sharedClient
{
    return [[self sharedProvider] client];
}

+(void)setClientClass:(Class)client
{
    [[self sharedProvider] setClient:[[client alloc] init]];
}


+(BuddyClientProvider *)sharedProvider
{
    static BuddyClientProvider *sharedProvider = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedProvider = [[self alloc] init];
    });
    
    return sharedProvider;
}

@end
