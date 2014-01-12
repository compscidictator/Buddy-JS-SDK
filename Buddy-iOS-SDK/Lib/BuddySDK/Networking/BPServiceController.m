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
#import "NSError+BuddyError.h"

typedef void (^AFFailureCallback)(AFHTTPRequestOperation *operation, NSError *error);
typedef void (^AFSuccessCallback)(AFHTTPRequestOperation *operation, id responseObject);

@interface BPServiceController()
@property (nonatomic, strong) AFHTTPRequestOperationManager *manager;
@property (nonatomic, strong) NSString *token;
@end

@implementation BPServiceController

- (instancetype)initWithBuddyUrl:(NSString *)url
{
    self = [super init];
    if(self)
    {
        [self setupManagerWithBaseUrl:url withToken:nil];

    }
    return self;
}

#pragma mark - Token Management
- (void)setupManagerWithBaseUrl:(NSString *)baseUrl withToken:(NSString *)token
{
    assert([baseUrl length] > 0);
    self.manager = [[AFHTTPRequestOperationManager alloc] initWithBaseURL:[NSURL URLWithString:baseUrl]];
    
    AFJSONRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
    AFJSONResponseSerializer *responseSerializer = [AFJSONResponseSerializer serializer];

    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    [requestSerializer setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];

    if(token){
        NSLog(@"Setting token: %@", token);
        // Tell our serializer our new Authorization string.
        NSString *authString = [@"Buddy " stringByAppendingString:token];
        [requestSerializer setValue:authString forHTTPHeaderField:@"Authorization"];
    }

    self.token = token;
    self.manager.responseSerializer = responseSerializer;
    self.manager.requestSerializer = requestSerializer;
}


- (void)updateConnectionWithResponse:(id)result
{
    if(!result || ![result isKindOfClass:[NSDictionary class]])return;
    // Grab the access token
    NSString *newToken = result[@"accessToken"];
    // Grab the potentially different base url.
    NSString *newBaseUrl = result[@"serviceRoot"];
    
    if (newToken && ![newToken isEqualToString:self.token]) {
        [self setupManagerWithBaseUrl:(newBaseUrl ?: self.manager.baseURL.absoluteString) withToken:newToken];
    }
}

- (void)setAppID:(NSString *)appID withKey:(NSString *)appKey complete:(RESTCallback)complete
{
    NSDictionary *getTokenParams = @{
                                     @"appId": appID,
                                     @"appKey": appKey,
                                     @"Platform": @"iOS",
                                     @"UniqueId": [BuddyDevice identifier],
                                     @"Model": [BuddyDevice deviceModel],
                                     @"OSVersion": [BuddyDevice osVersion]
                                     };
    
    [self POST:@"devices" parameters:getTokenParams callback:complete];
}


#pragma mark - BPRestProvider

- (void)GET:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self.manager GET:servicePath
           parameters:parameters
              success:[self handleSuccess:callback]
              failure:[self handleFailure:callback]];
}

- (void)POST:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self.manager POST:servicePath
            parameters:parameters
               success:[self handleSuccess:callback]
               failure:[self handleFailure:callback]];
}

- (void)MULTIPART_POST:(NSString *)servicePath parameters:(NSDictionary *)parameters data:(NSDictionary *)data callback:(RESTCallback)callback
{
    void (^constructBody)(id <AFMultipartFormData> formData) =^(id<AFMultipartFormData> formData){
        for(NSString *name in [data allKeys]){
#pragma messsage("TODO - somehow pass in mime type for blob/image POSTs")
            [formData appendPartWithFileData:data[name] name:name fileName:name mimeType:@"image/png"];
        }
    };
    
    [self.manager       POST:servicePath
                  parameters:parameters
   constructingBodyWithBlock:constructBody
                     success:[self handleSuccess:callback]
                     failure:[self handleFailure:callback]];
}

- (void)PATCH:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self.manager PATCH:servicePath
             parameters:parameters
                success:[self handleSuccess:callback]
                failure:[self handleFailure:callback]];
}

- (void)DELETE:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self.manager DELETE:servicePath
              parameters:parameters
                 success:[self handleSuccess:callback]
                 failure:[self handleFailure:callback]];
}

#pragma mark Response Handlers

- (AFSuccessCallback) handleSuccess:(RESTCallback)callback
{
    return ^(AFHTTPRequestOperation *operation, id responseObject){
        id result = responseObject[@"result"];
        [self updateConnectionWithResponse:result];
        callback(result, nil);
    };
}

- (AFFailureCallback) handleFailure:(RESTCallback)callback
{
    return ^(AFHTTPRequestOperation *operation, NSError *error){
        //        + (NSError *)noInternetError:(NSInteger)code;
        //        + (NSError *)noAuthenticationError:(NSInteger)code;
        //        + (NSError *)tokenExpiredError:(NSInteger)code;
        //        + (NSError *)badDataError:(NSInteger)code;
        
        NSInteger responseCode = operation.response.statusCode;
        
        NSError *buddyError;
        switch (responseCode) {
            case 400:
                buddyError = [NSError badDataError:error.code message:operation.responseString];
                break;
            case 403:
                if (YES) {
                    buddyError = [NSError noAuthenticationError:error.code message:operation.responseString];
                } else {
                    // TODO - figure out how to determing token expired.
                    buddyError = [NSError tokenExpiredError:error.code message:operation.responseString];
                }
                break;
            case 500:
                break;
            default:
                buddyError = [NSError noInternetError:error.code message:operation.responseString];
                break;
        }
        
        callback(nil, buddyError);
    };
}


@end
