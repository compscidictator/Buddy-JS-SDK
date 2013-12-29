//
//  BPUser.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <UIKit/UIKit.h>
#import "BuddyObject.h"

typedef enum
{
	BPUserGender_Male = 1,
	BPUserGender_Female = 2,
	//BPUserGender_Any = 3
} BPUserGender;

/// <summary>
/// Represents the status of the user.
/// </summary>
typedef enum
{
    BPUserRelationshipStatusSingle = 1,
    BPUserRelationshipStatusDating = 2,
    BPUserRelationshipStatusEngaged = 3,
    BPUserRelationshipStatusMarried = 4,
    BPUserRelationshipStatusDivorced = 5,
    BPUserRelationshipStatusWidowed = 6,
    BPUserRelationshipStatusOnTheProwl = 7,
//    Any = -1
} BPUserRelationshipStatus;

@interface BPUser : BuddyObject

@property (nonatomic, copy) NSString *name;

@property (nonatomic, copy) NSString *userName;

@property (nonatomic, assign) BOOL celebMode;

@property (nonatomic, assign) BPUserGender gender;

@property (nonatomic, assign) NSDate *dateOfBirth;

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

- (void)requestPasswordReset;
- (void)resetPassword;
- (void)addIdentityValue:(NSString *)identityValue;
- (void)removeIdentityValue:(NSString *)identityValue;

@end
