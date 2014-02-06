//
//  BPAppSettings.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/30/14.
//
//

#import "BPAppSettings.h"

@interface BPAppSettings()

@property (nonatomic, strong) NSString *defaultUrl;

@end

@implementation BPAppSettings

@synthesize serviceUrl = _serviceUrl;

- (instancetype)initWithBaseUrl:(NSString *)baseUrl
{
    self = [super init];
    if (self) {
        _defaultUrl = baseUrl;
    }
    return self;
}

#pragma mark - Custom accessors

- (NSString *)serviceUrl
{
    return _serviceUrl ?: self.defaultUrl;
}

- (void)setServiceUrl:(NSString *)serviceUrl
{
    _serviceUrl = serviceUrl;
}

- (NSString *)token
{
    return self.userToken ?: self.deviceToken ?: nil;
}
#pragma mark - BPAppSettings

- (void)clear
{
    self.serviceUrl = nil;
    self.deviceToken = nil;
    self.deviceTokenExpires = nil;
    [self clearUser];
}

- (void)clearUser
{
    self.userToken = nil;
    self.userTokenExpires = nil;
}

@end
