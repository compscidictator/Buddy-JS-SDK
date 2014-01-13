//
//  BPUser.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPUser.h"
#import "BuddyObject+Private.h"
#import "BPSession.h"
#import "BPEnumMapping.h"

@interface BPUser()<BPEnumMapping>

@end

@implementation BPUser

- (instancetype)initBuddyWithResponse:(id)response
{
    self = [super initBuddyWithResponse:response];
    if(self)
    {
        [self registerProperty:@selector(firstName)];
        [self registerProperty:@selector(lastName)];
        [self registerProperty:@selector(userName)];
        [self registerProperty:@selector(gender)];
        [self registerProperty:@selector(dateOfBirth)];
        [self registerProperty:@selector(profilePicture)];
        [self registerProperty:@selector(profilePictureId)];
        [self registerProperty:@selector(lastLogin)];
        [self registerProperty:@selector(created)];
        [self registerProperty:@selector(profilePicture)];
        [self registerProperty:@selector(profilePictureId)];
        [self registerProperty:@selector(relationshipStatus)];
    }
    return self;
}

+ (NSDictionary *)mapForProperty:(NSString *)key
{
    return [self enumMap][key];
}

+ (NSDictionary *)enumMap
{
    return @{
             NSStringFromSelector(@selector(relationshipStatus)) : @{
                     @(BPUserRelationshipStatusSingle) : @"Single",
                     @(BPUserRelationshipStatusDating) : @"Dating",
                     @(BPUserRelationshipStatusEngaged) : @"Engaged",
                     @(BPUserRelationshipStatusMarried) : @"Married",
                     @(BPUserRelationshipStatusDivorced) : @"Divorced",
                     @(BPUserRelationshipStatusWidowed) : @"Widowed",
                     @(BPUserRelationshipStatusOnTheProwl) : @"OnTheProwl",
                    },
             NSStringFromSelector(@selector(gender)) : @{
                     @(BPUserGender_Male) : @"Male",
                     @(BPUserGender_Female) : @"Female",
                     },
             };
}

static NSString *users = @"users";
+(NSString *)requestPath
{
    return users;
}

- (NSString *)id
{
    return [self isMe] ? @"me" : [super id];
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
    
    [[[BPSession currentSession] restService] POST:resource parameters:nil callback:^(id json, NSError *error) {
        callback(nil);
    }];
}

- (void)requestPasswordReset:(BuddyObjectCallback)callback
{
    NSString *resource = @"users/password";
    NSDictionary *parameters = @{@"UserName": self.userName};
                                 

    [[[BPSession currentSession] restService] POST:resource parameters:parameters callback:^(id json, NSError *error) {
        callback(json, nil);
    }];
}

- (void)resetPassword:(NSString *)resetCode newPassword:(NSString *)newPassword callback:(BuddyCompletionCallback)callback
{
    NSString *resource = @"users/password";
    NSDictionary *parameters = @{@"UserName": self.userName,
                                 @"ResetCode": resetCode,
                                 @"NewPassword": newPassword};
    
    [[[BPSession currentSession] restService] PATCH:resource parameters:parameters callback:^(id json, NSError *error) {
        callback(nil);
    }];
}

- (void)addIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
{
    NSString *resource = @"users/identity";
    NSDictionary *parameters = @{@"Identity": identityValue};
    
    [[[BPSession currentSession] restService] PATCH:resource parameters:parameters callback:^(id json, NSError *error) {
        callback(json);
    }];
}

- (void)removeIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
{
    NSString *resource = [@"users/identity/" stringByAppendingString:identityValue];
    NSDictionary *parameters = @{@"Identity": identityValue};
    
    [[[BPSession currentSession] restService] DELETE:resource parameters:parameters callback:^(id json, NSError *error) {
        callback(json);
    }];
}

@end
