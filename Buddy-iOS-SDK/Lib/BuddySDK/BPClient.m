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

-(void)setupWithApp:(NSString *)appID appKey:(NSString *)appKey options:(NSDictionary *)options
{
    self.service = [[BPServiceController alloc] initWithBuddyUrl:@"buddy.com"];
    
    // TODO - Does the client need a copy? Do users need to read back key/id?
    _appKey = appKey;
    _appID = appID;
    
    
    [self.service setAppID:appKey withKey:appKey];
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



-(void)createObjectWithPath:(NSString *)path parameters:(NSDictionary *)parameters withCallback:(BPBuddyObjectCallback) callback
{
    [self.service getBuddyObject:path parameters:parameters callback:^(id json) {
        callback(json);
    }];
    
}

-(void)refreshObjectWithPath:(NSString *)path parameters:(NSDictionary *)parameters withCallback:(BPBuddyObjectCallback) callback
{
    [self.service getBuddyObject:path parameters:parameters callback:^(id json) {
        callback(json);
    }];
}

-(void)updateObjectWithPath:(NSString *)path parameters:(NSDictionary *)parameters withCallback:(BPBuddyObjectCallback) callback
{

    [self.service updateBuddyObject:path parameters:parameters callback:^(id json) {
        callback(json);
    }];
}

-(void)deleteObjectWithPath:(NSString *)path parameters:(NSDictionary *)parameters withCallback:(BPBuddyObjectCallback) callback
{
    [self.service deleteBuddyObject:path parameters:parameters callback:^(id json) {
        callback(json);
    }];
}

#pragma mark Non-BuddyObject Requests

-(void)ping:(BPPingCallback)callback
{
//    [self.service GET:@"/ping" parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
//        callback([NSDecimalNumber decimalNumberWithString:@"2.0"]);
//    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
//        callback([NSDecimalNumber decimalNumberWithString:@"2.0"]);
//    }];
}


@end
