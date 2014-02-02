//
//  NSError+BuddyError.h
//  BuddySDK
//
//  Created by Erik Kerber on 12/29/13.
//
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSInteger, BuddyErrorType)
{
    BPErrorAuthFailed =                        0x100,
    BPErrorAuthAPICallDisabledByDeveloper =    0x101,
    BPErrorAuthSignatureInvalid =              0x102,
    BPErrorAuthRegionAccessKeyInvalid =        0x103,
    BPErrorAuthAccessTokenInvalid =            0x104,
    BPErrorAuthAppCredentialsInvalid =         0x105,
    BPErrorAuthBadUsernameOrPassword =         0x106,
    BPErrorAuthUserAccessTokenRequired =       0x107,
    BPErrorAuthUserAccountDisabled =           0x108,
    
    // Params
    BPErrorParameterMissingRequiredValue =     0x201,
    BPErrorParameterOutOfRange =               0x202,
    BPErrorParameterIncorrectFormat =          0x203,
    
    // Common
    BPErrorOperationNotFound =                 0x204,
    
    //Internal
    BPErrorInvalidObjectType =                 0x205,
    
    // Item
    BPErrorItemDoesNotExist =                  0x301,
    BPErrorItemAlreadyExists =                 0x302,
    BPErrorObjectPermissionDenied =            0x303,
    
    // Binary
    BPErrorFileUploadFailed =                  0x401,
};

@interface NSError (BuddyError)

+ (NSError *)noInternetError:(NSInteger)code message:(NSString *)message;
+ (NSError *)buildBuddyError:(id)buddyJSON;

- (BOOL)needsLogin;
- (BOOL)credentialsInvalid;

@end
