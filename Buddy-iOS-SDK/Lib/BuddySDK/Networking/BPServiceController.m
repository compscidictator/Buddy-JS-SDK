//
//  BPServiceController.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPServiceController.h"
#import "AFNetworking.h"

@interface BPServiceController()
@property (nonatomic, strong) AFHTTPRequestOperationManager *manager;
@property (nonatomic, strong) NSString *token;
@end

@implementation BPServiceController

-(instancetype)initWithBuddyUrl:(NSString *)url
{
    self = [super init];
    if(self)
    {
        self.manager = [[AFHTTPRequestOperationManager alloc] initWithBaseURL:[NSURL URLWithString:url]];
        
        self.manager.responseSerializer = [AFJSONResponseSerializer serializer];
        self.manager.requestSerializer = [AFJSONRequestSerializer serializer];

    }
    return self;
}

-(NSDictionary *)buildGetParameters
{
    return @{@"appKey": self.appKey,
             @"appID": self.appID};
}

-(void)setAppID:(NSString *)appID withKey:(NSString *)appKey
{
    NSDictionary *getTokenParams = @{
                                     @"appId": appID,
                                     @"appKey": appKey,
                                     @"Platform": @"TODO",
                                     @"UniqueId": @"TODO - unique id",
                                     @"Model": @"TODO - device name",
                                     @"OSVersion": @"TODO - os version"
                                     };
    
    [self.manager POST:@"/devices" parameters:getTokenParams success:^(AFHTTPRequestOperation *operation, id responseObject) {
        // TODO - JSON WAG
        self.token = responseObject[@"AccessToken"];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
       // TODO - Bad login - let the caller know.
    }];
}

-(void)getBuddyObject:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback
{
    [self.manager GET:servicePath parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}

-(void)deleteBuddyObject:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback
{
    [self.manager DELETE:servicePath parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}

-(void)updateBuddyObject:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback
{
    [self.manager POST:servicePath parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}

@end
