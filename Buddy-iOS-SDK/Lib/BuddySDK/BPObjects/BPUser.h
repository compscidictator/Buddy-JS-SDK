//
//  BPUser.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <UIKit/UIKit.h>
#import "BuddyObject.h"

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

/**
 Enum for specifying relationship status.
 */
typedef NS_ENUM(NSInteger, BPUserRelationshipStatus)
{
    /** Single */
    BPUserRelationshipStatusSingle = 1,
    /** Dating */
    BPUserRelationshipStatusDating = 2,
    /** Engaged */
    BPUserRelationshipStatusEngaged = 3,
    /** Married */
    BPUserRelationshipStatusMarried = 4,
    /** Divorced */
    BPUserRelationshipStatusDivorced = 5,
    /** Widowed */
    BPUserRelationshipStatusWidowed = 6,
    /** On the Prowl */
    BPUserRelationshipStatusOnTheProwl = 7,
};

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
@property (nonatomic, assign) BPUserRelationshipStatus relationshipStatus;
@property (nonatomic, assign) BOOL friendRequestPending;

@property (nonatomic, assign) BOOL isMe;

- (void)requestPasswordReset:(BuddyObjectCallback)callback;
- (void)resetPassword:(NSString *)resetCode newPassword:(NSString *)newPassword callback:(BuddyCompletionCallback)callback;
- (void)addIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
- (void)removeIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
- (void)setUserProfilePicture:(BuddyCompletionCallback)callback;
- (void)deleteUserProfilePicture:(BuddyCompletionCallback)callback;

@end
