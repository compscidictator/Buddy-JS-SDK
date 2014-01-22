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

#import <CoreFoundation/CoreFoundation.h>

#define BuddyServiceURL @"BuddyServiceURL"



@interface BPClient()
@property (nonatomic, strong) BPServiceController *service;

- (void)loginWorker:(NSString *)username password:(NSString *)password success:(BuddyObjectCallback) callback;
- (void)socialLoginWorker:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BuddyObjectCallback) callback;
- (void)initializeCollectionsWithUser:(BPUser *)user;



@property (nonatomic, strong) BuddyLocation *location;

@end

@implementation BPClient

@synthesize user=_user;

#pragma mark Initializer

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
    
    _checkins = [[BPCheckinCollection alloc] initWithClient:self];
    _photos = [[BPPhotoCollection alloc] initWithClient:self];
    _blobs = [[BPBlobCollection alloc] initWithClient:self];
    _location = [BuddyLocation new];
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

    self.service = [[BPServiceController alloc] initWithBuddyUrl:serviceUrl client:self];
    
    // TODO - Does the client need a copy? Do users need to read back key/id?
    _appKey = appKey;
    _appID = appID;
    
    
    [self.service setAppID:appID withKey:appKey callback:^(id json, NSError *error) {
        callback ? callback(error) : nil;
    }];
}

- (id<BPRestProvider>)restService
{
    return self.service;
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


-(void) setUser:(BPUser*) user
{
    _user=user;
}

-(BPUser*) user
{
    if(_user==nil)
    {
        [self raiseAuthError];
    }
    return _user;
}



#pragma mark Login

-(void)loginWorker:(NSString *)username password:(NSString *)password success:(BuddyObjectCallback) callback
{
    NSDictionary *parameters = @{@"username": username,
                                 @"password": password};
    [self.service POST:@"users/login" parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(json, error) : nil;
    }];
}

-(void)socialLoginWorker:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BuddyObjectCallback) callback
{
    NSDictionary *parameters = @{@"identityProviderName": provider,
                                 @"identityId": providerId,
                                 @"identityAccessToken": token};
    
    [self.service POST:@"users/login/social" parameters:parameters callback:^(id json, NSError *error) {
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
        
        [user refresh:^(NSError *error){
         #pragma messsage("TODO - Error")
         [self initializeCollectionsWithUser:user];
         callback ? callback(user, nil) : nil;
         }];
        
    }];
}

- (void)socialLogin:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BuddyObjectCallback) callback;
{
    [self socialLogin:provider providerId:providerId token:token success:^(id json, NSError *error) {
        
        if (error) {
            if (callback)
                callback(nil, error);
            return;
        }
        
        BPUser *user = [[BPUser alloc] initBuddyWithResponse:json andClient:self];
        user.isMe = YES;
        
        [user refresh:^(NSError *error){
            callback ? callback(user, error) : nil;
        }];
    }];
}


#pragma mark Non-BuddyObject Requests

-(void)ping:(BPPingCallback)callback
{
    [self.service GET:@"ping" parameters:nil callback:^(id json, NSError *error) {
        callback ? callback([NSDecimalNumber decimalNumberWithString:@"2.0"]) : nil;
    }];
}

#pragma mark Response Handlers


-(void) raiseAuthError
{
    id<UIApplicationDelegate> app =[[UIApplication sharedApplication] delegate];
    
    if (self.delegate==nil)
    {
        if ( (app!=nil) &&[ app respondsToSelector:@selector(authorizationFailed)])
        {
            [app performSelector:@selector(authorizationFailed)];
        }
    }
    else
    {
        if ([self.delegate respondsToSelector:@selector(authorizationFailed)])
        {
            [self.delegate authorizationFailed];
        }
    }
}

#pragma mark Location

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
