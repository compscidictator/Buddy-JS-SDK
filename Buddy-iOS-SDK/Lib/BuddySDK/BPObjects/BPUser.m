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
#import "BPEnumMapping.h"

@interface BPUser()<BPEnumMapping>

@end

@implementation BPUser

- (instancetype)initBuddyWithResponse:(id)response andClient:(BPClient*)client;
{
    self = [super initBuddyWithResponse:response andClient:client];
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

-(NSInteger)age
{
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
    NSDateComponents *components = [calendar components:NSYearCalendarUnit
                                               fromDate:self.dateOfBirth
                                                 toDate:[NSDate date]
                                                options:0];
    
    return components.year;
}

#pragma mark - Password

- (void)requestPasswordReset:(BuddyObjectCallback)callback
{
    NSString *resource = @"users/password";
    NSDictionary *parameters = @{@"UserName": self.userName};
                                 

    [[[self client] restService] POST:resource parameters:parameters callback:^(id json, NSError *error) {
        callback(json, nil);
    }];
}

- (void)resetPassword:(NSString *)resetCode newPassword:(NSString *)newPassword callback:(BuddyCompletionCallback)callback
{
    NSString *resource = @"users/password";
    NSDictionary *parameters = @{@"UserName": self.userName,
                                 @"ResetCode": resetCode,
                                 @"NewPassword": newPassword};
    
    [[[self client] restService] PATCH:resource parameters:parameters callback:^(id json, NSError *error) {
        callback(nil);
    }];
}

#pragma mark - Identity Value

- (void)addIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
{
    NSString *resource = @"users/identity";
    NSDictionary *parameters = @{@"Identity": identityValue};
    
    [[[self client] restService] PATCH:resource parameters:parameters callback:^(id json, NSError *error) {
        callback(json);
    }];
}

- (void)removeIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
{
    NSString *resource = [@"users/identity/" stringByAppendingString:identityValue];
    NSDictionary *parameters = @{@"Identity": identityValue};
    
    [[[self client] restService] DELETE:resource parameters:parameters callback:^(id json, NSError *error) {
        callback(json);
    }];
}

#pragma mark - Profile Picture

- (void)setUserProfilePicture:(UIImage *)picture comment:(NSString *)comment callback:(BuddyCompletionCallback)callback
{
    NSString *resource = [NSString stringWithFormat:@"user/%@/profilepicture", self.id];
    NSDictionary *parameters = @{@"comment": comment};

    NSDictionary *data = @{@"data": UIImagePNGRepresentation(picture)};
    
    [[[self client] restService] MULTIPART_POST:resource parameters:parameters data:data callback:^(id json, NSError *error) {
        callback(error);
    }];
}

- (void)deleteUserProfilePicture:(BuddyCompletionCallback)callback
{
    NSString *resource = [NSString stringWithFormat:@"user/%@/profilepicture", self.id];
    
    [[[self client] restService] DELETE:resource parameters:nil callback:^(id json, NSError *error) {
        callback(error);
    }];
}

@end
