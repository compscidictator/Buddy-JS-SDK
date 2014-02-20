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

typedef void (^AFFailureCallback)(AFHTTPRequestOperation *operation, NSError *error);
typedef void (^AFSuccessCallback)(AFHTTPRequestOperation *operation, id responseObject);


@interface BPServiceController()

- (AFFailureCallback) handleFailure:(ServiceResponse)callback;
- (AFSuccessCallback) handleSuccess:(ServiceResponse)callback;

@property (nonatomic, strong) BPAppSettings *appSettings;

@property (nonatomic, strong) AFJSONRequestSerializer *jsonRequestSerializer;
@property (nonatomic, strong) AFJSONResponseSerializer *jsonResponseSerializer;
@property (nonatomic, strong) AFHTTPRequestSerializer *httpRequestSerializer;
@property (nonatomic, strong) AFHTTPResponseSerializer *httpResponseSerializer;

@property (nonatomic, strong) AFHTTPRequestOperationManager *manager;
@property (nonatomic, strong) NSString *token;

@end

@implementation BPServiceController

- (instancetype)initWithAppSettings:(BPAppSettings *)appSettings
{
    self = [super init];
    if(self)
    {
        _appSettings = appSettings;
        _jsonRequestSerializer = [AFJSONRequestSerializer serializer];
        _jsonResponseSerializer = [AFJSONResponseSerializer serializer];
        _httpRequestSerializer = [AFHTTPRequestSerializer serializer];
        _httpResponseSerializer = [AFHTTPResponseSerializer serializer];
        
        [_jsonRequestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
        [_jsonRequestSerializer setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
        [_httpRequestSerializer setValue:@"*/*" forHTTPHeaderField:@"Accept"];

        [self addObserver:self forKeyPath:@"appSettings.userToken" options:NSKeyValueObservingOptionNew context:nil];
        [self addObserver:self forKeyPath:@"appSettings.deviceToken" options:NSKeyValueObservingOptionNew context:nil];
        [self addObserver:self forKeyPath:@"appSettings.serviceUrl" options:NSKeyValueObservingOptionNew context:nil];
        
        [self setupManagerWithNewSettings];
    }
    return self;
}

- (void)dealloc
{
    [self removeObserver:self forKeyPath:@"appSettings.userToken"];
    [self removeObserver:self forKeyPath:@"appSettings.deviceToken"];
    [self removeObserver:self forKeyPath:@"appSettings.serviceUrl"];
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
    [self setupManagerWithNewSettings];
}

#pragma mark - Token Management
- (void)setupManagerWithNewSettings
{
    assert([self.appSettings.serviceUrl length] > 0);
    self.manager = [[AFHTTPRequestOperationManager alloc] initWithBaseURL:[NSURL URLWithString:self.appSettings.serviceUrl]];

    if(self.appSettings.token){
        NSLog(@"Setting token: %@", self.appSettings.token);
        // Tell our serializer our new Authorization string.
        NSString *authString = [@"Buddy " stringByAppendingString:self.appSettings.token];
        [self.jsonRequestSerializer setValue:authString forHTTPHeaderField:@"Authorization"];
        [self.httpRequestSerializer setValue:authString forHTTPHeaderField:@"Authorization"];
    }

    self.manager.responseSerializer = self.jsonResponseSerializer;
    self.manager.requestSerializer = self.jsonRequestSerializer;
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
    
    self.manager.responseSerializer = self.jsonResponseSerializer;
    self.manager.requestSerializer = self.jsonRequestSerializer;
}

- (void)GET:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(ServiceResponse)callback
{
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
        
        callback([operation response].statusCode, responseObject, nil);
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
