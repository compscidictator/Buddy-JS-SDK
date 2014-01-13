//
//  BPDescriptors.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/12/14.
//
//

#import "BPDescriptors.h"

@implementation BPUser (Description)
/*
 
 @property (nonatomic, copy) NSString *name;
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
 */

- (NSString *)description
{
    return [NSString stringWithFormat:@"%@\r%@\r%@\r%i\r%i\r%@\r%@\r%@\r%@\r%@\r%@\r%i\r%i\r%i\r%i\r", self.firstName, self.lastName, self.userName, self.celebMode, self.gender, self.dateOfBirth, self.lastLogin, self.lastModified, self.created, self.profilePicture, self.profilePictureId, self.age, self.relationshipStatus, self.friendRequestPending, self.isMe];

}

@end

//@implementation BPCheckin (Description)
//
//- (NSString *)description
//{
//    return [NSString stringWithFormat:@"%@\r%@\r%@\r%@\r%@\r%@\r%@\r%@\r", nil, nil, nil, nil, nil, nil, nil, nil, nil];
//}
//
//@end

@implementation BPPhoto (Description)

- (NSString *)description
{
    return [NSString stringWithFormat:@"%@; Comment: %@", [super description], self.caption];
}

@end