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
#import <CoreFoundation/CoreFoundation.h>

#define BuddyServiceURL @"BuddyServiceURL"

@interface BPSession()
@property (nonatomic, strong) BPServiceController *service;
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

}

-(void)setupWithApp:(NSString *)appID appKey:(NSString *)appKey options:(NSDictionary *)options callback:(BuddyCompletionCallback)callback
{
    
#if DEBUG
    NSString *serviceUrl = [[NSBundle bundleForClass:[self class]] infoDictionary][BuddyServiceURL];
#else
    NSString *serviceUrl = [[NSBundle mainBundle] infoDictionary][BuddyServiceURL];
#endif

    self.service = [[BPServiceController alloc] initWithBuddyUrl:serviceUrl];
    
    // TODO - Does the client need a copy? Do users need to read back key/id?
    _appKey = appKey;
    _appID = appID;
    
    
    [self.service setAppID:appID withKey:appKey callback:^(id json, NSError *error) {
        callback(error);
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

#pragma mark BuddyObject



-(void)login:(NSString *)username password:(NSString *)password success:(BuddyObjectCallback) callback
{
    NSDictionary *parameters = @{@"username": username,
                                 @"password": password};
    [self.service POST:@"users/login" parameters:parameters callback:^(id json, NSError *error) {
        callback(json, error);
    }];
}

-(void)socialLogin:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BuddyObjectCallback) callback
{
    NSDictionary *parameters = @{@"identityProviderName": provider,
                                 @"identityId": providerId,
                                 @"identityAccessToken": token};
    
    [self.service POST:@"users/login/social" parameters:parameters callback:^(id json, NSError *error) {
        callback(json, error);
    }];
}

#pragma mark Non-BuddyObject Requests

-(void)ping:(BPPingCallback)callback
{
    [self.service GET:@"ping" parameters:nil callback:^(id json, NSError *error) {
        callback([NSDecimalNumber decimalNumberWithString:@"2.0"]);
    }];
}


@end
