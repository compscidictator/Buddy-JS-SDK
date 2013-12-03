//
//  BPClient.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPClient.h"
#import "AFNetworking.h"
#import "BPServiceController.h"

@interface BPClient()
@property (nonatomic, strong) BPServiceController *service;
@end



@implementation BPClient

#pragma mark Initializer

-(instancetype) init
{
    self = [super self];
    if(self)
    {
        // Mostly empty init. Use setupWithApp to help facilitate singleton BPClient.
    }
    return self;
}

-(void)setupWithApp:(NSString *)appID appKey:(NSString *)appKey options:(NSDictionary *)options complete:(void (^)())complete

{
    self.service = [[BPServiceController alloc] initWithBuddyUrl:@"http://craig.buddyservers.net:8080/api"];
    
    // TODO - Does the client need a copy? Do users need to read back key/id?
    _appKey = appKey;
    _appID = appID;
    
    
    [self.service setAppID:appID withKey:appKey complete:^{
        complete();
    }];
}

# pragma mark -
# pragma mark Singleton
+(instancetype)defaultClient
{
    static BPClient *sharedClient = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedClient = [[self alloc] init];
    });
    return sharedClient;
}

#pragma mark BuddyObject



-(void)createObjectWithPath:(NSString *)path parameters:(NSDictionary *)parameters complete:(BPBuddyObjectCallback) callback
{
    [self.service createBuddyObject:path parameters:parameters callback:^(id json) {
        callback(json);
    }];
    
}

-(void)refreshObjectWithPath:(NSString *)path parameters:(NSDictionary *)parameters complete:(BPBuddyObjectCallback) callback
{
    [self.service getBuddyObject:path parameters:parameters callback:^(id json) {
        callback(json);
    }];
}

-(void)updateObjectWithPath:(NSString *)path parameters:(NSDictionary *)parameters complete:(BPBuddyObjectCallback) callback
{

    [self.service updateBuddyObject:path parameters:parameters callback:^(id json) {
        callback(json);
    }];
}

-(void)deleteObjectWithPath:(NSString *)path parameters:(NSDictionary *)parameters complete:(BPBuddyObjectCallback) callback
{
    [self.service deleteBuddyObject:path parameters:parameters callback:^(id json) {
        callback(json);
    }];
}

-(void)login:(NSString *)username password:(NSString *)password success:(BPBuddyObjectCallback) callback
{
    NSDictionary *parameters = @{@"username": username,
                                 @"password": password};
    [self.service POST:@"users/login" parameters:parameters success:^(id json) {
        callback(json);
    }];
}

#pragma mark Non-BuddyObject Requests

-(void)ping:(BPPingCallback)callback
{
    [self.service GET:@"ping" parameters:nil success:^(id json) {
        callback([NSDecimalNumber decimalNumberWithString:@"2.0"]);
    }];
}


@end
