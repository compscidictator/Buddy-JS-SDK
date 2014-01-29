//
//  NSError+BuddyError.h
//  BuddySDK
//
//  Created by Erik Kerber on 12/29/13.
//
//

#import <Foundation/Foundation.h>

@interface NSError (BuddyError)

/*
 AuthFailed =                        0x100,
 AuthAPICallDisabledByDeveloper =    0x101,
 AuthSignatureInvalid =              0x102,
 AuthRegionAccessKeyInvalid =        0x103,
 AuthAccessTokenInvalid =            0x104,
 AuthAppCredentialsInvalid =         0x105,
 AuthBadUsernameOrPassword =         0x106,
 AuthUserAccessTokenRequired =       0x107,
 AuthUserAccountDisabled =           0x108,
 
 
 // Params
 ParameterMissingRequiredValue =     0x201,
 ParameterOutOfRange =               0x202,
 ParameterIncorrectFormat =          0x203,
 
 // Common
 OperationNotFound =                 0x204,
 
 //Internal
 InvalidObjectType =                 0x205,
 
 
 // Item
 ItemDoesNotExist =                  0x301,
 ItemAlreadyExists =                 0x302,
 ObjectPermissionDenied =            0x303,
 
 
 
 // Binary
 FileUploadFailed =                  0x401,
 */

+ (NSError *)noInternetError:(NSInteger)code message:(NSString *)message;
+ (NSError *)buildBuddyError:(id)buddyJSON;

- (BOOL)isAuthError;

@end
