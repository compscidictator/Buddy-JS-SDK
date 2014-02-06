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
    /** Male */
	BPUserGender_Male = 1,
    /** Female */
	BPUserGender_Female = 2,
} ;


@interface BPUser : BuddyObject

@property (nonatomic, copy) NSString *firstName;
@property (nonatomic, copy) NSString *lastName;
@property (nonatomic, copy) NSString *userName;
@property (nonatomic, assign) BOOL celebMode;
@property (nonatomic, assign) BPUserGender gender;
@property (nonatomic, strong) NSDate *dateOfBirth;

// TODO - method?
//@property (nonatomic, assign) double latitude;
//@property (nonatomic, assign) double longitude;

//@property (nonatomic, assign) double distanceInMeters;

@property (nonatomic, strong) NSDate *lastLogin;
@property (nonatomic, strong) NSDate *lastModified;
@property (nonatomic, strong) NSDate *created;
@property (nonatomic, strong) NSURL *profilePicture;
@property (nonatomic, copy) NSString *profilePictureId;
@property (nonatomic, readonly) NSInteger age;
@property (nonatomic, assign) BOOL friendRequestPending;

@property (nonatomic, assign) BOOL isMe;

- (void)requestPasswordReset:(BuddyObjectCallback)callback;
- (void)resetPassword:(NSString *)resetCode newPassword:(NSString *)newPassword callback:(BuddyCompletionCallback)callback;
- (void)addIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
- (void)removeIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
- (void)setUserProfilePicture:(UIImage *)picture comment:(NSString *)comment callback:(BuddyCompletionCallback)callback;
- (void)deleteUserProfilePicture:(BuddyCompletionCallback)callback;

@end
