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
#import "BPRestProvider.h"
#import <CoreFoundation/CoreFoundation.h>

@interface BPClient()
@property (nonatomic, strong) BPServiceController *service;
@end



@implementation BPClient

#pragma mark Initializer

-(instancetype) init
{
    self = [super self];
    if(self)
    {
        _checkins = [BPCheckinCollection new];
        // Mostly empty init. Use setupWithApp to help facilitate singleton BPClient.
    }
    return self;
}

-(void)setupWithApp:(NSString *)appID appKey:(NSString *)appKey options:(NSDictionary *)options complete:(void (^)())complete

{
    self.service = [[BPServiceController alloc] initWithBuddyUrl:BUDDY_SERVER];
    
    // TODO - Does the client need a copy? Do users need to read back key/id?
    _appKey = appKey;
    _appID = appID;
    
    
    [self.service setAppID:appID withKey:appKey complete:^(id json, NSError *error) {
        complete();
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



-(void)login:(NSString *)username password:(NSString *)password success:(BPBuddyObjectCallback) callback
{
    NSDictionary *parameters = @{@"username": username,
                                 @"password": password};
    [self.service POST:@"users/login" parameters:parameters callback:^(id json, NSError *error) {
        callback(json);
    }];
}

-(void)socialLogin:(NSString *)provider providerId:(NSString *)providerId token:(NSString *)token success:(BPBuddyObjectCallback) callback
{
    // providerName
    // providerUniqueId
    // providerAccessToken
    // pageSize
    // pageNumber
    
    // SocialProviderTokenMismatch
    // NoSuchSocialProvider
    // AccessTokenInvalid
    // NoNameOrEmail

    NSDictionary *parameters = @{@"providerName": provider,
                                 @"providerUniqueId": providerId,
                                 @"providerAccessToken": token};
    
    [self.service POST:@"users/login/social" parameters:parameters callback:^(id json, NSError *error) {
        callback(json);
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
