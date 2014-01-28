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
#import "BPClient.h"
#import "BPClient+Private.h"

typedef void (^AFFailureCallback)(AFHTTPRequestOperation *operation, NSError *error);
typedef void (^AFSuccessCallback)(AFHTTPRequestOperation *operation, id responseObject);


@interface BPServiceController()

- (AFFailureCallback) handleFailure:(ServiceResponse)callback;
- (AFSuccessCallback) handleSuccess:(ServiceResponse)callback;

@property (nonatomic, strong) AFJSONRequestSerializer *jsonRequestSerializer;
@property (nonatomic, strong) AFJSONResponseSerializer *jsonResponseSerializer;
@property (nonatomic, strong) AFHTTPRequestSerializer *httpRequestSerializer;
@property (nonatomic, strong) AFHTTPResponseSerializer *httpResponseSerializer;

@property (nonatomic, strong) AFHTTPRequestOperationManager *manager;
@property (nonatomic, strong) NSString *token;

@end

@implementation BPServiceController

- (instancetype)initWithBuddyUrl:(NSString *)url
{
    self = [super init];
    if(self)
    {
        _jsonRequestSerializer = [AFJSONRequestSerializer serializer];
        _jsonResponseSerializer = [AFJSONResponseSerializer serializer];
        _httpRequestSerializer = [AFHTTPRequestSerializer serializer];
        _httpResponseSerializer = [AFHTTPResponseSerializer serializer];
        
        [_jsonRequestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
        [_jsonRequestSerializer setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
        [_httpRequestSerializer setValue:@"*/*" forHTTPHeaderField:@"Accept"];
        //[_httpRequestSerializer setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
        
        
        [self setupManagerWithBaseUrl:url withToken:nil];

    }
    return self;
}

#pragma mark - Token Management
- (void)setupManagerWithBaseUrl:(NSString *)baseUrl withToken:(NSString *)token
{
    assert([baseUrl length] > 0);
    self.manager = [[AFHTTPRequestOperationManager alloc] initWithBaseURL:[NSURL URLWithString:baseUrl]];

    if(token){
        NSLog(@"Setting token: %@", token);
        // Tell our serializer our new Authorization string.
        NSString *authString = [@"Buddy " stringByAppendingString:token];
        [self.jsonRequestSerializer setValue:authString forHTTPHeaderField:@"Authorization"];
        [self.httpRequestSerializer setValue:authString forHTTPHeaderField:@"Authorization"];
    }

    self.token = token;
    self.manager.responseSerializer = self.jsonResponseSerializer;
    self.manager.requestSerializer = self.jsonRequestSerializer;
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


#pragma mark - BPRestProvider

- (void)GET_FILE:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(ServiceResponse)callback
{
    self.manager.requestSerializer = self.httpRequestSerializer;
    self.manager.responseSerializer = self.httpResponseSerializer;
    
    [self.manager GET:servicePath
           parameters:parameters
              success:[self handleSuccess:callback json:NO]
              failure:[self handleFailure:callback]];
}

- (void)GET:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(ServiceResponse)callback
{
    self.manager.responseSerializer = self.jsonResponseSerializer;
    self.manager.requestSerializer = self.jsonRequestSerializer;
    
    [self.manager GET:servicePath
           parameters:parameters
              success:[self handleSuccess:callback]
              failure:[self handleFailure:callback]];

}

- (void)POST:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(ServiceResponse)callback
{
    [self.manager POST:servicePath
            parameters:parameters
               success:[self handleSuccess:callback]
               failure:[self handleFailure:callback]];
}

- (void)MULTIPART_POST:(NSString *)servicePath parameters:(NSDictionary *)parameters data:(NSDictionary *)data callback:(ServiceResponse)callback
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

- (void)PATCH:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(ServiceResponse)callback
{
    [self.manager PATCH:servicePath
             parameters:parameters
                success:[self handleSuccess:callback]
                failure:[self handleFailure:callback]];
}

- (void)PUT:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(ServiceResponse)callback
{
    [self.manager PUT:servicePath
           parameters:parameters
              success:[self handleSuccess:callback]
              failure:[self handleFailure:callback]];
}

- (void)DELETE:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(ServiceResponse)callback
{
    [self.manager DELETE:servicePath
              parameters:parameters
                 success:[self handleSuccess:callback]
                 failure:[self handleFailure:callback]];
}

- (AFSuccessCallback) handleSuccess:(ServiceResponse)callback
{
    return [self handleSuccess:callback json:YES];
}

- (AFSuccessCallback) handleSuccess:(ServiceResponse)callback json:(BOOL)json
{
    return ^(AFHTTPRequestOperation *operation, id responseObject){

        id result = responseObject;
        if (json) {
            // json is a weird parameter I know, it basically means it's
            // a Buddy service call and can have token information etc..
            result = responseObject[@"result"];
            [self updateConnectionWithResponse:result];
        }
        
        callback([operation response].statusCode, result, nil);
    };
}

- (AFFailureCallback) handleFailure:(ServiceResponse)callback
{
    return ^(AFHTTPRequestOperation *operation, NSError *error){

        NSInteger statusCode = operation.response ? operation.response.statusCode : 0;
        callback(statusCode, operation.responseString, error);
    };
}

@end
