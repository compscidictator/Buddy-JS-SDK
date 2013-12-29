//
//  NSError+BuddyError.h
//  BuddySDK
//
//  Created by Erik Kerber on 12/29/13.
//
//

#import <Foundation/Foundation.h>

@interface NSError (BuddyError)

+ (NSError *)noInternetError:(NSInteger)code;
+ (NSError *)noAuthenticationError:(NSInteger)code;
+ (NSError *)tokenExpiredError:(NSInteger)code;
+ (NSError *)badDataError:(NSInteger)code;

@end
