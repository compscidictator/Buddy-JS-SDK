//
//  BPUser.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <UIKit/UIKit.h>
#import "BuddyObject.h"

typedef NS_ENUM(NSInteger, BPUserGender)
{
	BPUserGender_Male = 1,
	BPUserGender_Female = 2,
} ;

/// <summary>
/// Represents the status of the user.
/// </summary>
typedef NS_ENUM(NSInteger, BPUserRelationshipStatus)
{
    BPUserRelationshipStatusSingle = 1,
    BPUserRelationshipStatusDating = 2,
    BPUserRelationshipStatusEngaged = 3,
    BPUserRelationshipStatusMarried = 4,
    BPUserRelationshipStatusDivorced = 5,
    BPUserRelationshipStatusWidowed = 6,
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

@end
