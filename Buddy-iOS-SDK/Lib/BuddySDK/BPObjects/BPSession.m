///
//  BPClient.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPSession.h"
#import "AFNetworking.h"
#import "BPServiceController.h"
#import "AFNetworking.h"
#import "BPCheckinCollection.h"
#import "BPGameBoards.h"
#import "BPSounds.h"
#import "BPPhotoCollection.h"
#import "BPBlobCollection.h"
#import "BPRestProvider.h"
#import "BuddyLocation.h"

#define BuddyServiceURL @"BuddyServiceURL"

@interface BPSession() <BuddyLocationDelegate>
@property (nonatomic, strong) BPServiceController *service;
@property (nonatomic, strong) BuddyLocation *location;
@end

@implementation BPSession

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
    _checkins = [BPCheckinCollection new];
    _photos = [BPPhotoCollection new];
    _blobs = [BPBlobCollection new];
    _location = [BuddyLocation new];
}

-(void)setupWithApp:(NSString *)appID appKey:(NSString *)appKey options:(NSDictionary *)options callback:(BuddyCompletionCallback)callback
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
+(instancetype)currentSession
{
    static BPSession *sharedClient = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedClient = [[self alloc] init];
    });
    return sharedClient;
}

#pragma mark Login

-(void)login:(NSString *)username password:(NSString *)password success:(BuddyObjectCallback) callback
{
    NSDictionary *parameters = @{@"username": username,
                                 @"password": password};
    [self.service POST:@"users/login" parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(json, error) : nil;
    }];
}

-(void)socialLogin:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BuddyObjectCallback) callback
{
    NSDictionary *parameters = @{@"identityProviderName": provider,
                                 @"identityId": providerId,
                                 @"identityAccessToken": token};
    
    [self.service POST:@"users/login/social" parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(json, error) : nil;
    }];
}

#pragma mark Non-BuddyObject Requests

-(void)ping:(BPPingCallback)callback
{
    [self.service GET:@"ping" parameters:nil callback:^(id json, NSError *error) {
        callback ? callback([NSDecimalNumber decimalNumberWithString:@"2.0"]) : nil;
    }];
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
