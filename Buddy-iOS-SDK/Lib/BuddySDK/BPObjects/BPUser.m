//
//  BPUser.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPUser.h"
#import "BuddyObject+Private.h"
#import "BPClient.h"

@implementation BPUser

-(instancetype)init
{
    self = [super init];
    if(self)
    {
        [self registerProperty:@selector(name)];
        [self registerProperty:@selector(userName)];
        [self registerProperty:@selector(gender)];
        [self registerProperty:@selector(dateOfBirth)];
        [self registerProperty:@selector(profilePicture)];
        [self registerProperty:@selector(profilePictureId)];
        [self registerProperty:@selector(lastLogin)];
        [self registerProperty:@selector(created)];
        [self registerProperty:@selector(profilePicture)];
        [self registerProperty:@selector(profilePictureId)];
        [self registerProperty:@selector(age)];
        [self registerProperty:@selector(relationshipStatus)];
        [self registerProperty:@selector(friendRequestPending)];
        
        // TODO - if we make distance objects properties, add them.
        
    }
    return self;
}



static NSString *users = @"users";
+(NSString *)requestPath
{
    return users;
}

-(NSInteger)age
{
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
    NSDateComponents *components = [calendar components:NSYearCalendarUnit
                                               fromDate:self.dateOfBirth
                                                 toDate:[NSDate date]
                                                options:0];
    
    return components.year;
}

- (void)logout
{
    //NSString *resource = @"users/logout";
}

- (void)requestPasswordReset
{
    //NSString *resource = @"users/password";
    //NSDictionary *parameters = @{@"Creds": @"TODO",
    //                             @"UserName": @"TODO"};
                                 

                                     
}

- (void)resetPassword
{
    //NSString *resource = @"users/password";
    //NSDictionary *parameters = @{@"UserName": @"TODO",
    //                             @"ResetCode": @"TODO",
    //                             @"NewPassword": @"TODO"};
    
    //[BPClient defaultClient]
}

- (void)addIdentityValue:(NSString *)identityValue
{
    //NSString *resource = @"users/identity";

}

- (void)removeIdentityValue:(NSString *)identityValue
{
    //NSString *resource = @"users/identity";

}

@end
