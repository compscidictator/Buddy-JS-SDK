//
//  NSError+BuddyError.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/29/13.
//
//

#import "NSError+BuddyError.h"

@implementation NSError (BuddyError)

static NSString *BuddyAuthenticationError = @"BuddyAuthenticationError";
static NSString *NoInternetError = @"NoInternetError";
static NSString *BadDataError = @"BadDataError";
static NSString *BuddyTokenExpired = @"BuddyTokenExpired";


+ (NSError *)noAuthenticationError:(NSInteger)code message:(NSString *)message
{
    return [NSError errorWithDomain:BuddyAuthenticationError
                               code:code
                           userInfo:@{@"message": message}];
}

+ (NSError *)noInternetError:(NSInteger)code message:(NSString *)message
{
    return [NSError errorWithDomain:NoInternetError
                               code:code
                           userInfo:@{@"message": message}];
}

+ (NSError *)badDataError:(NSInteger)code message:(NSString *)message
{
    return [NSError errorWithDomain:BadDataError
                               code:code
                           userInfo:@{@"message": message}];
}

+ (NSError *)tokenExpiredError:(NSInteger)code message:(NSString *)message
{
    return [NSError errorWithDomain:BuddyTokenExpired
                               code:code
                           userInfo:@{@"message": @"The user token has expired."}];
}

@end
