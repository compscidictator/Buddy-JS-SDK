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
    assert([baseUrl length] > 0);
    self.manager = [[AFHTTPRequestOperationManager alloc] initWithBaseURL:[NSURL URLWithString:baseUrl]];
    
    AFJSONRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
    AFJSONResponseSerializer *responseSerializer = [AFJSONResponseSerializer serializer];

    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    [requestSerializer setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [requestSerializer setValue:@"craig.buddyservers.net:8080" forHTTPHeaderField:@"Host"];


    if(token){
        // Tell our serializer our new Authorization string.
        NSString *authString = [@"Buddy " stringByAppendingString:token];
        [requestSerializer setValue:authString forHTTPHeaderField:@"Authorization"];
    }

    self.token = token;
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
        
        id result = responseObject[@"result"];
        
        if(operation.response.statusCode == 200){
            
            [self updateConnectionWithResponse:result];
            
            complete();
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        complete();// TODO NSError?
    }];
}

-(void)updateConnectionWithResponse:(id)result
{
    if(!result || [result isKindOfClass:[NSArray class]])return;
    // Grab the access token
    NSString *newToken = result[@"accessToken"];
    // Grab the potentially different base url.
    NSString *newBaseUrl = result[@"serviceRoot"];
    
    if (newToken && ![newToken isEqualToString:self.token]) {
        [self setupManagerWithBaseUrl:(newBaseUrl ?: self.manager.baseURL.absoluteString) withToken:newToken];
    }
}

-(void)createBuddyObject:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback
{
    [self.manager POST:servicePath parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        //[self updateConnectionWithResponse:responseObject];
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}

-(void)getBuddyObject:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback
{
    [self.manager GET:servicePath parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [self updateConnectionWithResponse:responseObject];
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}

-(void)deleteBuddyObject:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback
{
    [self.manager DELETE:servicePath parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [self updateConnectionWithResponse:responseObject];
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}

-(void)updateBuddyObject:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback
{
    [self.manager POST:servicePath parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [self updateConnectionWithResponse:responseObject];
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}


#pragma mark AFNetworking by composisition. Not sure if I want to keep these.
-(void)GET:(NSString *)servicePath parameters:(NSDictionary *)parameters success:(AFNetworkingCallback)callback
{
    [self.manager GET:servicePath parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        id result = responseObject[@"result"];
        
        [self updateConnectionWithResponse:result];
        callback(responseObject[@"result"]);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}

-(void)POST:(NSString *)servicePath parameters:(NSDictionary *)parameters success:(AFNetworkingCallback)callback
{
    [self.manager POST:servicePath parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        id result = responseObject[@"result"];

        [self updateConnectionWithResponse:result];
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}


@end
