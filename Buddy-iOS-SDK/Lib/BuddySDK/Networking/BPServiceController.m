//
//  BPServiceController.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPServiceController.h"
#import "AFNetworking.h"
#import "BuddyDevice.h"

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
        [self setupManagerWithBaseUrl:url withToken:nil];

    }
    return self;
}

-(void)setupManagerWithBaseUrl:(NSString *)baseUrl withToken:(NSString *)token
{
    self.manager = [[AFHTTPRequestOperationManager alloc] initWithBaseURL:[NSURL URLWithString:baseUrl]];
    
    AFJSONRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
    AFJSONResponseSerializer *responseSerializer = [AFJSONResponseSerializer serializer];

    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    if(token){
        // Tell our serializer our new Authorization string.
        NSString *authString = [@"Buddy " stringByAppendingString:self.token];
        [requestSerializer setValue:authString forHTTPHeaderField:@"Authorization"];
    }

    
    self.manager.responseSerializer = responseSerializer;
    self.manager.requestSerializer = requestSerializer;
}

-(void)setAppID:(NSString *)appID withKey:(NSString *)appKey complete:(void (^)())complete
{
    NSDictionary *getTokenParams = @{
                                     @"appId": appID,
                                     @"appKey": appKey,
                                     @"Platform": @"iOS",
                                     @"UniqueId": [BuddyDevice identifier],
                                     @"Model": [BuddyDevice deviceModel],
                                     @"OSVersion": [BuddyDevice osVersion]
                                     };
    
    [self.manager POST:@"/api/devices" parameters:getTokenParams success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if(operation.response.statusCode == 200){
            
            // Grab the potentially different base url.
            NSString *baseUrl = responseObject[@"result"][@"serviceRoot"];
            
            // Grab the access token
            self.token = responseObject[@"result"][@"accessToken"];
            
            [self setupManagerWithBaseUrl:baseUrl withToken:self.token];
            
            complete();
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
       // TODO - Bad login -    let the caller know.
    }];
}

-(void)createBuddyObject:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback
{
    [self.manager POST:servicePath parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
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


#pragma mark AFNetworking by composisition. Not sure if I want to keep these.
-(void)GET:(NSString *)servicePath parameters:(NSDictionary *)parameters success:(AFNetworkingCallback)callback
{
    [self.manager GET:servicePath parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}

-(void)POST:(NSString *)servicePath parameters:(NSDictionary *)parameters success:(AFNetworkingCallback)callback
{
    [self.manager POST:servicePath parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}


@end
