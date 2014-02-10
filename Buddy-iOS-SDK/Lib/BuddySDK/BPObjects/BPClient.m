///
//  BPClient.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPClient.h"
#import "AFNetworking.h"
#import "BPServiceController.h"
#import "AFNetworking.h"
#import "BPCheckinCollection.h"
#import "BPGameBoards.h"
#import "BPSounds.h"
#import "BPPhotoCollection.h"
#import "BPAlbumCollection.h"
#import "BPBlobCollection.h"
#import "BPRestProvider.h"
#import "BPUser.h"
#import "BuddyObject+Private.h"
#import "BuddyLocation.h"
#import "BuddyDevice.h"
#import "BPAppSettings.h"
#import "BuddyAppDelegateDecorator.h"
#import <CoreFoundation/CoreFoundation.h>

#define BuddyServiceURL @"BuddyServiceURL"

#define BuddyDefaultURL @"http://api.buddyplatform.com"

#define HiddenArgumentCount 2

@interface BPClient()<BPRestProvider>

@property (nonatomic, strong) BPServiceController *service;
@property (nonatomic, strong) BPAppSettings *appSettings;
@property (nonatomic, strong) BuddyLocation *location;
@property (nonatomic, strong) BuddyAppDelegateDecorator *decorator;

@end

@implementation BPClient

@synthesize user=_user;
@synthesize checkins=_checkins;
@synthesize photos =_photos;
@synthesize blobs = _blobs;
@synthesize albums = _albums;

#pragma mark - Init

- (instancetype)init
{
    self = [super self];
    if(self)
    {
        // Nothing for now.
    }
    return self;
}

- (void)initializeCollectionsWithUser:(BPUser *)user
{
    _user = user;
    
    _location = [BuddyLocation new];
}

- (void)resetOnLogout
{
    _user = nil;
    
    _checkins = nil;
    _photos = nil;
    _blobs = nil;
    _albums = nil;
    _location = nil;
}

-(void)setupWithApp:(NSString *)appID
             appKey:(NSString *)appKey
            options:(NSDictionary *)options
           delegate:(id<BPClientDelegate>) delegate

{
    
#if DEBUG
    // Annoying nuance of running a unit test "bundle".
    NSString *serviceUrl = [[NSBundle bundleForClass:[self class]] infoDictionary][BuddyServiceURL];
#else
    NSString *serviceUrl = [[NSBundle mainBundle] infoDictionary][BuddyServiceURL];
#endif
    
    serviceUrl = serviceUrl ?: BuddyDefaultURL;
    
    _appSettings = [[BPAppSettings alloc] initWithBaseUrl:serviceUrl];
    _service = [[BPServiceController alloc] initWithAppSettings:_appSettings];
    
    // TODO - Does the client need a copy? Do users need to read back key/id?
    _appSettings.appKey = appKey;
    _appSettings.appID = appID;
    
    if(!([options hasKey:@"disablePush"] && ((BOOL)[options objectForKey:@"disablePush"]) == NO)){
        [self registerForPushes];
    }
}


-(void) registerForPushes {
    UIApplication* app = [UIApplication sharedApplication];
    //wrap the app delegate in a buddy decorator
    self.decorator = [BuddyAppDelegateDecorator  appDelegateDecoratorWithAppDelegate:app.delegate client:self andSettings:_appSettings];
    app.delegate = self.decorator;
    [app registerForRemoteNotificationTypes:UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeNewsstandContentAvailability | UIRemoteNotificationTypeNone | UIRemoteNotificationTypeSound];
    
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

#pragma mark - Collections


- (BPUser *) user
{
    if(!_user)
    {
        [self raiseNeedsLoginError];
    }
    return _user;
}

-(BPCheckinCollection *)checkins
{
    if(!_checkins)
    {
        _checkins = [[BPCheckinCollection alloc] initWithClient:self];;
    }
    return _checkins;
}

-(BPPhotoCollection *)photos
{
    if(!_photos)
    {
        _photos = [[BPPhotoCollection alloc] initWithClient:self];
    }
    return _photos;
}

-(BPBlobCollection *)blobs
{
    
    if(!_blobs)
    {
        _blobs = [[BPBlobCollection alloc] initWithClient:self];
    }
    return _blobs;
}

-(BPAlbumCollection *)albums
{
    
    if(!_albums)
    {
        _albums = [[BPAlbumCollection alloc] initWithClient:self];
    }
    return _albums;
}

#pragma mark - Login

-(void)loginWorker:(NSString *)username password:(NSString *)password success:(BuddyObjectCallback) callback
{
    NSDictionary *parameters = @{@"username": username,
                                 @"password": password};
    [self POST:@"users/login" parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(json, error) : nil;
    }];
}

-(void)socialLoginWorker:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BuddyObjectCallback) callback
{
    NSDictionary *parameters = @{@"identityProviderName": provider,
                                 @"identityId": providerId,
                                 @"identityAccessToken": token};
    
    [self POST:@"users/login/social" parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(json, error) : nil;
    }];
}

- (void)login:(NSString *)username password:(NSString *)password callback:(BuddyObjectCallback)callback
{
    [self loginWorker:username password:password success:^(id json, NSError *error) {
        
        if(error) {
            callback ? callback(nil, error) : nil;
            return;
        }
        
        BPUser *user = [[BPUser alloc] initBuddyWithResponse:json andClient:self];
        user.isMe = YES;
        
        // Grab the potentially different base url.
        if ([json hasKey:@"accessToken"] && ![json[@"accessToken"] isEqualToString:self.appSettings.token]) {
            self.appSettings.userToken = json[@"accessToken"];
        }
        
        [user refresh:^(NSError *error) {
            [self initializeCollectionsWithUser:user];
            callback ? callback(user, error) : nil;
        }];
        
    }];
}

- (void)socialLogin:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BuddyObjectCallback) callback;
{
    [self socialLoginWorker:provider providerId:providerId token:token success:^(id json, NSError *error) {
        
        if (error) {
            callback ? callback(nil, error) : nil;
            return;
        }
        
        BPUser *user = [[BPUser alloc] initBuddyWithResponse:json andClient:self];
        user.isMe = YES;
        
        [user refresh:^(NSError *error){
            callback ? callback(user, error) : nil;
        }];
    }];
}


- (void)logout:(BuddyCompletionCallback)callback
{
    NSString *resource = @"users/me/logout";
    
    [self POST:resource parameters:nil callback:^(id json, NSError *error) {
        if (!error) {
            [self resetOnLogout];
        }
        
        callback ? callback(error) : nil;
    }];
}

-(void) registerPushToken:(NSString *)token callback:(BuddyObjectCallback)callback{
    NSString *resource = @"devices";
    [self checkDeviceToken:^(void){
    
        [self PATCH:resource parameters:@{@"pushToken": token} callback:callback];
    }];
}


#pragma mark - Utility

-(void)ping:(BPPingCallback)callback
{
    [self GET:@"ping" parameters:nil callback:^(id json, NSError *error) {
        callback ? callback([NSDecimalNumber decimalNumberWithString:@"2.0"]) : nil;
    }];
}

#pragma mark - BPRestProvider

- (void)GET:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self checkDeviceToken:^{
        [self.service GET:servicePath parameters:parameters callback:[self handleResponse:callback]];
    }];
}

- (void)GET_FILE:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self checkDeviceToken:^{
        [self.service GET_FILE:servicePath parameters:parameters callback:[self handleResponse:callback]];
    }];
}

- (void)POST:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self checkDeviceToken:^{
        [self.service POST:servicePath parameters:parameters callback:[self handleResponse:callback]];
    }];
}

- (void)MULTIPART_POST:(NSString *)servicePath parameters:(NSDictionary *)parameters data:(NSDictionary *)data callback:(RESTCallback)callback
{
    [self checkDeviceToken:^{
        [self.service MULTIPART_POST:servicePath parameters:parameters data:data callback:[self handleResponse:callback]];
    }];
}

- (void)PATCH:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self checkDeviceToken:^{
        [self.service PATCH:servicePath parameters:parameters callback:[self handleResponse:callback]];
    }];
}

- (void)PUT:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self checkDeviceToken:^{
        [self.service PUT:servicePath parameters:parameters callback:[self handleResponse:callback]];
    }];
}

- (void)DELETE:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self checkDeviceToken:^{
        [self.service DELETE:servicePath parameters:parameters callback:[self handleResponse:callback]];
    }];
}

// Data struct to keep track of requests waiting on device token.
NSMutableArray *queuedRequests;
- (void)checkDeviceToken:(void(^)())method
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        queuedRequests = [NSMutableArray array];
    });
    
    if ([self.appSettings.deviceToken length] > 0) {
        method();
    } else {
        @synchronized (self) {
            if ([queuedRequests count] > 0) {
                [queuedRequests addObject:[method copy]];
                return;
            }
            else {
                [queuedRequests addObject:[method copy]];
                
                NSDictionary *getTokenParams = @{
                                                 @"appId": self.appSettings.appID,
                                                 @"appKey": self.appSettings.appKey,
                                                 @"Platform": @"iOS",
                                                 @"UniqueId": [BuddyDevice identifier],
                                                 @"Model": [BuddyDevice deviceModel],
                                                 @"OSVersion": [BuddyDevice osVersion],
                                                 @"DeviceToken": [BuddyDevice pushToken]
                                                 };
                [self.service POST:@"devices" parameters:getTokenParams callback:[self handleResponse:^(id json, NSError *error) {
                    // Grab the potentially different base url.
                    if ([json hasKey:@"accessToken"] && ![json[@"accessToken"] isEqualToString:self.appSettings.token]) {
                        self.appSettings.deviceToken = json[@"accessToken"];
                        
                        for (void(^block)() in queuedRequests) {
                            block();
                        }
                    }
                    [queuedRequests removeAllObjects];
                }]];
            }
        }
    }
}

#pragma mark - Response Handlers

- (ServiceResponse) handleResponse:(RESTCallback)callback
{
    return ^(NSInteger responseCode, id response, NSError *error) {
        NSLog (@"Framework: handleResponse");
        
        NSError *buddyError;
        
        id result = response;
        
        // Is it a JSON response (as opposed to raw bytes)?
        if(result && [result isKindOfClass:[NSDictionary class]]) {
            
            // Grab the result
            result = response[@"result"];
            
            if ([result isKindOfClass:[NSDictionary class]]) {
                
                // Grab the access token
                if ([result hasKey:@"serviceRoot"]) {
                    self.appSettings.serviceUrl = result[@"serviceRoot"];
                }
                
                
            }
        }
        
        response = response ?: @"Unknown";
        id responseObject = nil;
        
        switch (responseCode) {
            case 200:
            case 201:
                responseObject = result;
                break;
            case 400:
            case 401:
            case 402:
            case 403:
            case 404:
            case 405:
            case 500:
                buddyError = [NSError buildBuddyError:result];
                break;
            default:
                buddyError = [NSError noInternetError:error.code message:result];
                break;
        }
        if([buddyError needsLogin]) {
            [self.appSettings clearUser];
            [self raiseNeedsLoginError];
        }
        if([buddyError credentialsInvalid]) {
            [self.appSettings clear];
        }
        
        callback(responseObject, buddyError);
    };
}

- (void)raiseNeedsLoginError
{
    [self tryRaiseDelegate:@selector(authorizationNeedsUserLogin)];
}

- (void)tryRaiseDelegate:(SEL)selector
{
    id<UIApplicationDelegate> app =[[UIApplication sharedApplication] delegate];
    SuppressPerformSelectorLeakWarning(

        if (!self.delegate) {// If no delegate, see if we've implemented delegate methods on the AppDelegate.
            if (app && [app respondsToSelector:selector])  {
                [app performSelector:selector];
            }
        } else { // Try the delegate
            if ([self.delegate respondsToSelector:selector]) {
                [self.delegate performSelector:selector];
            }
        }
                                       
    );
}


#pragma mark - Location

- (void)setLocationEnabled:(BOOL)locationEnabled
{
    _locationEnabled = locationEnabled;
    [self.location beginTrackingLocation:^(NSError *error) {
        // TODO - How do we want users to find out if something went wrong
        // (such as a user slapping the location request)?
    }];
}

- (void)didUpdateBuddyLocation:(BPCoordinate *)newLocation
{
    _lastLocation = newLocation;
}

#pragma mark - Metrics

- (void)recordMetric:(NSString *)key andValue:(NSString *)value callback:(BuddyCompletionCallback)callback
{
    NSString *resource = [NSString stringWithFormat:@"metrics/events/%@", key];
    NSDictionary *parameters = @{@"value": BOXNIL(value)};
    
    [self POST:resource parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(error) : nil;
    }];
}

- (void)recordTimedMetric:(NSString *)key andValue:(NSString *)value timeout:(NSInteger)seconds callback:(BuddyMetricCallback)callback
{
    NSString *resource = [NSString stringWithFormat:@"metrics/events/%@", key];
    NSDictionary *parameters = @{@"value": BOXNIL(value),
                                 @"timeoutInSeconds": @(seconds)};
    
    [self POST:resource parameters:parameters callback:^(id json, NSError *error) {
        BPMetricCompletionHandler *completionHandler;
        if (!error) {
            completionHandler = [[BPMetricCompletionHandler alloc] initWithMetricId:json andClient:self];
        }
        callback ? callback(completionHandler, error) : nil;
    }];
}

#pragma mark - REST workaround

- (id<BPRestProvider>)restService
{
    return self;
}

#pragma mark - Push Notification


-(void)sendApplicationMessage:(SEL)selector withArguments:(NSArray*)args
{
    UIApplication* app = [UIApplication sharedApplication];
    if([app respondsToSelector:selector]){
        NSMethodSignature* messageSig = [UIApplication methodSignatureForSelector:selector];
        NSInvocation* invoke = [NSInvocation invocationWithMethodSignature: messageSig];
        [invoke setSelector:selector];
        [invoke setTarget:app];
        if([args count] != ([messageSig numberOfArguments] - HiddenArgumentCount)){
            return;
        }
       
        for ( int currentArgIndex = 0;currentArgIndex < [args count]; currentArgIndex++){
            [invoke setArgument:(void*)[args objectAtIndex:currentArgIndex] atIndex:currentArgIndex];
        }
        [invoke invoke];
        
    }
    
}

@end
