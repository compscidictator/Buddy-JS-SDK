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
	BPUserGender_Any = 3
} BPUserGender;

/// <summary>
/// Represents the status of the user.
/// </summary>
typedef enum
{
    Single = 1,
    Dating = 2,
    Engaged = 3,
    Married = 4,
    Divorced = 5,
    Widowed = 6,
    OnTheProwl = 7,
    Any = -1
} BPUserStatus;

@interface BPUser : BuddyObject

@property (nonatomic, copy) NSString *name;

@property (nonatomic, copy) NSString *username;

@property (nonatomic, assign) BPUserGender gender;

@property (nonatomic, copy) NSString *applicationTag;

// TODO - method?
//@property (nonatomic, assign) double latitude;
//@property (nonatomic, assign) double longitude;

//@property (nonatomic, assign) double distanceInMeters;

@property (nonatomic, strong) NSDate *lastLogin;

@property (nonatomic, strong) NSDate *createdOn;

@property (nonatomic, strong) NSURL *profilePicture;

@property (nonatomic, copy) NSString *profilePictureId;

@property (nonatomic, assign) NSInteger age;

@property (nonatomic, assign) BPUserStatus userStatus;

@property (nonatomic, assign) BOOL friendRequestPending;




@end
