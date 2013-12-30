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


- (void)logout:(BuddyCompletionCallback)callback
{
    NSString *resource = @"users/logout";
    
    [[[BPClient defaultClient] restService] POST:resource parameters:nil callback:^(id json) {
        callback(nil);
    }];
}

- (void)requestPasswordReset:(BuddyObjectCallback)callback
{
    NSString *resource = @"users/password";
    NSDictionary *parameters = @{@"UserName": self.userName};
                                 

    [[[BPClient defaultClient] restService] POST:resource parameters:parameters callback:^(id json) {
        callback(json, nil);
    }];
}

- (void)resetPassword:(NSString *)resetCode newPassword:(NSString *)newPassword callback:(BuddyCollectionCallback)callback
{
    NSString *resource = @"users/password";
    NSDictionary *parameters = @{@"UserName": self.userName,
                                 @"ResetCode": resetCode,
                                 @"NewPassword": newPassword};
    
    [[[BPClient defaultClient] restService] PATCH:resource parameters:parameters callback:^(id json) {
        callback(nil);
    }];
}

- (void)addIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
{
    NSString *resource = @"users/identity";
    NSDictionary *parameters = @{@"Identity": identityValue};
    
    [[[BPClient defaultClient] restService] PATCH:resource parameters:parameters callback:^(id json) {
        callback(json);
    }];
}

- (void)removeIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
{
    NSString *resource = [@"users/identity/" stringByAppendingString:identityValue];
    NSDictionary *parameters = @{@"Identity": identityValue};
    
    [[[BPClient defaultClient] restService] DELETE:resource parameters:parameters callback:^(id json) {
        
    }];
}

@end
