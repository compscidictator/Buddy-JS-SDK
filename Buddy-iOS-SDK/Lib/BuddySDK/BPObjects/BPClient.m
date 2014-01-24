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
#import "BPBlobCollection.h"
#import "BPRestProvider.h"
#import "BPUser.h"
#import "BuddyObject+Private.h"
#import "BuddyLocation.h"
#import "BuddyDevice.h"
#import <CoreFoundation/CoreFoundation.h>

#define BuddyServiceURL @"BuddyServiceURL"



@interface BPClient()<BPRestProvider>
@property (nonatomic, strong) BPServiceController *service;

- (void)loginWorker:(NSString *)username password:(NSString *)password success:(BuddyObjectCallback) callback;
- (void)socialLoginWorker:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BuddyObjectCallback) callback;
- (void)initializeCollectionsWithUser:(BPUser *)user;



@property (nonatomic, strong) BuddyLocation *location;

@end

@implementation BPClient

@synthesize user=_user;
@synthesize checkins=_checkins;
@synthesize photos =_photos;
@synthesize blobs = _blobs;

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
    _location = nil;
}

-(void)setupWithApp:(NSString *)appID
             appKey:(NSString *)appKey
            options:(NSDictionary *)options
           delegate:(id<BPClientDelegate>) delegate
           callback:(BuddyCompletionCallback)callback

{
    
#if DEBUG
    // Annoying nuance of running a unit test "bundle".
    NSString *serviceUrl = [[NSBundle bundleForClass:[self class]] infoDictionary][BuddyServiceURL];
#else
    NSString *serviceUrl = [[NSBundle mainBundle] infoDictionary][BuddyServiceURL];
#endif

    self.service = [[BPServiceController alloc] initWithBuddyUrl:serviceUrl];
    
    // TODO - Does the client need a copy? Do users need to read back key/id?
    _appKey = appKey;
    _appID = appID;
    
    NSDictionary *getTokenParams = @{
                                     @"appId": appID,
                                     @"appKey": appKey,
                                     @"Platform": @"iOS",
                                     @"UniqueId": [BuddyDevice identifier],
                                     @"Model": [BuddyDevice deviceModel],
                                     @"OSVersion": [BuddyDevice osVersion]
                                     };
    
    [self POST:@"devices" parameters:getTokenParams callback:^(id json, NSError *error) {
        callback ? callback(error) : nil;
    }];
}

- (id<BPRestProvider>)restService
{
    return self;
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
        [self raiseAuthError];
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

-(BPPhotoCollection*)photos
{
    if(!_photos)
    {
        _photos = [[BPPhotoCollection alloc] initWithClient:self];
    }
    return _photos;
}

-(BPBlobCollection*)blobs
{
    
    if(!_blobs)
    {
        _blobs = [[BPBlobCollection alloc] initWithClient:self];
    }
    return _blobs;
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
            callback(nil, error);
            return;
        }
        
        BPUser *user = [[BPUser alloc] initBuddyWithResponse:json andClient:self];
        user.isMe = YES;
        
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
    [self.service GET:servicePath parameters:parameters callback:[self handleResponse:callback]];
}

- (void)POST:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self.service POST:servicePath parameters:parameters callback:[self handleResponse:callback]];
}

- (void)MULTIPART_POST:(NSString *)servicePath parameters:(NSDictionary *)parameters data:(NSDictionary *)data callback:(RESTCallback)callback
{
    [self.service MULTIPART_POST:servicePath parameters:parameters data:data callback:[self handleResponse:callback]];
}

- (void)PATCH:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self.service PATCH:servicePath parameters:parameters callback:[self handleResponse:callback]];
}

- (void)PUT:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self.service PUT:servicePath parameters:parameters callback:[self handleResponse:callback]];
}

- (void)DELETE:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(RESTCallback)callback
{
    [self.service DELETE:servicePath parameters:parameters callback:[self handleResponse:callback]];
}

#pragma mark - Response Handlers

- (ServiceResponse) handleResponse:(RESTCallback)callback
{
    return ^(NSInteger responseCode, id response, NSError *error) {
        //        + (NSError *)noInternetError:(NSInteger)code;
        //        + (NSError *)noAuthenticationError:(NSInteger)code;
        //        + (NSError *)tokenExpiredError:(NSInteger)code;
        //        + (NSError *)badDataError:(NSInteger)code;
        
        BOOL authError = FALSE;
        
        NSLog (@"Framework: handleResponse");
        
        NSError *buddyError;
        
        response = response ?: @"Unknown";
        id responseObject = nil;
        
        switch (responseCode) {
            case 200:
            case 201:
                responseObject = response[@"result"];
                break;
            case 400:
                buddyError = [NSError badDataError:error.code message:response];
                break;
            case 403:
                authError=TRUE;
                if (YES) {
                    buddyError = [NSError noAuthenticationError:error.code message:response];
                } else {
                    // TODO - figure out how to determing token expired.
                    buddyError = [NSError tokenExpiredError:error.code message:response];
                }
                break;
            case 500:
                buddyError = [NSError badDataError:error.code message:response];
                break;
            default:
                buddyError = [NSError noInternetError:error.code message:response];
                break;
        }
        if(authError) {
            [self raiseAuthError];
        }
        callback(responseObject, buddyError);

    };
}

- (void)raiseAuthError
{
    id<UIApplicationDelegate> app =[[UIApplication sharedApplication] delegate];
    
    if (!self.delegate) { // First check our delegate
        if (app && [app respondsToSelector:@selector(authorizationFailed)])  {
            [app performSelector:@selector(authorizationFailed)];
        }
    } else { // If no delegate, see if we've implemented delegate methods on the AppDelegate.
        if ([self.delegate respondsToSelector:@selector(authorizationFailed)]) {
            [self.delegate authorizationFailed];
        }
    }
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

@end
