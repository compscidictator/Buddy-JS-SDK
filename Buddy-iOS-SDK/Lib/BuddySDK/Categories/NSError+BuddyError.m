//
//  NSError+BuddyError.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/29/13.
//
//

#import "NSError+BuddyError.h"

@implementation NSError (BuddyError)

static NSString *NoInternetError = @"NoInternetError";

+ (NSError *)noInternetError:(NSInteger)code message:(NSString *)message
{
    return [NSError errorWithDomain:NoInternetError
                               code:code
                           userInfo:@{@"message": message}];
}

+ (NSError *)buildBuddyError:(id)buddyJSON
{
    id jsonData = [buddyJSON dataUsingEncoding:NSUTF8StringEncoding]; //if input is NSString
    id json = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
    
    NSInteger buddyErrorCode = [json[@"errorNumber"] integerValue];
    id buddyErrorDomain = json[@"error"];
    id message = json[@"message"];
    //id status = [json[@"status"] integerValue];
    
    return [NSError errorWithDomain:buddyErrorDomain code:buddyErrorCode userInfo:@{@"message": message}];
}

- (BOOL)isAuthError
{
    return (self.code & 0x100) > 0;
}

@end
