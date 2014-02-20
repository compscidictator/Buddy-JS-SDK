//
//  BPUser.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <UIKit/UIKit.h>
#import "BuddyObject.h"

#define BPUserGenderField @"gender"
#define BPUserEmailField @"email"
#define BPUserFirstNameField @"firstName"
#define BPUserLastNameField @"lastName"
#define BPUserDateOfBirthField @"dateOfBirth"

#define BPUserCelebrityModeField @"celebrityMode"
#define BPUserFuzzLocationField @"fuzzLocation"

/**
 Enum for specifying gender.
 */
typedef NS_ENUM(NSInteger, BPUserGender)
{
    /** Unknown */
    BPUserGender_Unknown = 0,
    /** Male */
	BPUserGender_Male = 1,
    /** Female */
	BPUserGender_Female = 2,
} ;

@protocol BPUserProperties <BuddyObjectProperties>

@property (nonatomic, copy) NSString *firstName;
@property (nonatomic, copy) NSString *lastName;
@property (nonatomic, copy) NSString *userName;
@property (nonatomic, copy) NSString *email;
@property (nonatomic, assign) BPUserGender gender;
@property (nonatomic, strong) NSDate *dateOfBirth;
@property (nonatomic, strong) NSURL *profilePicture;
@property (nonatomic, copy) NSString *profilePictureId;

@end

typedef void(^DescribeUser)(id<BPUserProperties> userProperties);

@interface BPUser : BuddyObject<BPUserProperties>

- (NSInteger)age;

- (void)requestPasswordReset:(BuddyObjectCallback)callback;
- (void)resetPassword:(NSString *)resetCode newPassword:(NSString *)newPassword callback:(BuddyCompletionCallback)callback;
- (void)addIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
- (void)removeIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
- (void)setUserProfilePicture:(UIImage *)picture caption:(NSString *)comment callback:(BuddyCompletionCallback)callback;
- (void)deleteUserProfilePicture:(BuddyCompletionCallback)callback;

@end
