//
//  BPUser.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPUser.h"

@implementation BPUser

-(instancetype)init
{
    self = [super init];
    if(self)
    {
        [self registerProperty:@selector(name)];
        [self registerProperty:@selector(username)];
        [self registerProperty:@selector(gender)];
        [self registerProperty:@selector(applicationTag)];
        [self registerProperty:@selector(lastLogin)];
        [self registerProperty:@selector(createdOn)];
        [self registerProperty:@selector(profilePicture)];
        [self registerProperty:@selector(profilePictureId)];
        [self registerProperty:@selector(age)];
        [self registerProperty:@selector(userStatus)];
        [self registerProperty:@selector(friendRequestPending)];
        
        // TODO - if we make distance objects properties, add them.
        
    }
    return self;
}
-(NSString *)requestPath
{
    return @"/users";
}



@end
