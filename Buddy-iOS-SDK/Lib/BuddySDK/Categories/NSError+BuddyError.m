//
//  NSError+BuddyError.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/29/13.
//
//

#import "NSError+BuddyError.h"

@implementation NSError (BuddyError)

static NSString *BuddyErrorDomain = @"BuddyError";

+ (NSError *)noAuthenticationError:(NSInteger)code
{
    return [NSError errorWithDomain:BuddyErrorDomain
                               code:code
                           userInfo:@{@"message": @"Call requires app to be authenticated."}];
}

+ (NSError *)noInternetError:(NSInteger)code
{
    return [NSError errorWithDomain:BuddyErrorDomain
                               code:code
                           userInfo:@{@"message": @"There is no internet connection."}];
}

+ (NSError *)badDataError:(NSInteger)code
{
    return [NSError errorWithDomain:BuddyErrorDomain
                               code:code
                           userInfo:@{@"message": @"Missing data."}];
}

+ (NSError *)tokenExpiredError:(NSInteger)code
{
    return [NSError errorWithDomain:BuddyErrorDomain
                               code:code
                           userInfo:@{@"message": @"The user token has expired."}];
}

@end
