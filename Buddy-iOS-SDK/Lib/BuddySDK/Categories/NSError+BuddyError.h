//
//  NSError+BuddyError.h
//  BuddySDK
//
//  Created by Erik Kerber on 12/29/13.
//
//

#import <Foundation/Foundation.h>

@interface NSError (BuddyError)

+ (NSError *)noInternetError:(NSInteger)code message:(NSString *)message;
+ (NSError *)noAuthenticationError:(NSInteger)code message:(NSString *)message;
+ (NSError *)tokenExpiredError:(NSInteger)code message:(NSString *)message;
+ (NSError *)badDataError:(NSInteger)code message:(NSString *)message;

@end
