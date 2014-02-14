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

@interface BPUser()

@end

@implementation BPUser

@synthesize firstName, lastName, userName, gender, dateOfBirth, profilePicture, profilePictureId, email;

- (void)registerProperties
{
    [super registerProperties];
    
    [self registerProperty:@selector(firstName)];
    [self registerProperty:@selector(lastName)];
    [self registerProperty:@selector(userName)];
    [self registerProperty:@selector(gender)];
    [self registerProperty:@selector(dateOfBirth)];
    [self registerProperty:@selector(profilePicture)];
    [self registerProperty:@selector(profilePictureId)];
}

+ (NSDictionary *)enumMap
{
    return [[[self class] baseEnumMap] dictionaryByMergingWith: @{
                                                                  NSStringFromSelector(@selector(gender)) : @{
                                                                          @(BPUserGender_Male) : @"Male",
                                                                          @(BPUserGender_Female) : @"Female",
                                                                          },
                                                                  }];
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
                                 

    [self.client POST:resource parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(json, nil) : nil;
    }];
}

- (void)resetPassword:(NSString *)resetCode newPassword:(NSString *)newPassword callback:(BuddyCompletionCallback)callback
{
    NSString *resource = @"users/password";
    NSDictionary *parameters = @{@"UserName": self.userName,
                                 @"ResetCode": resetCode,
                                 @"NewPassword": newPassword};
    
    [self.client PATCH:resource parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(nil) : nil;
    }];
}

#pragma mark - Identity Value

- (void)addIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
{
    NSString *resource = @"users/identity";
    NSDictionary *parameters = @{@"Identity": identityValue};
    
    [self.client PATCH:resource parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(error) : nil;
    }];
}

- (void)removeIdentityValue:(NSString *)identityValue callback:(BuddyCompletionCallback)callback;
{
    NSString *resource = [@"users/identity/" stringByAppendingString:identityValue];
    NSDictionary *parameters = @{@"Identity": identityValue};
    [self.client DELETE:resource parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(error) : nil;
    }];
}

#pragma mark - Profile Picture

- (void)setUserProfilePicture:(UIImage *)picture caption:(NSString *)caption callback:(BuddyCompletionCallback)callback
{
    NSString *resource = [NSString stringWithFormat:@"user/%@/profilepicture", self.id];
    NSDictionary *parameters = @{@"caption": caption};

    NSDictionary *data = @{@"data": UIImagePNGRepresentation(picture)};
    
    [self.client MULTIPART_POST:resource parameters:parameters data:data callback:^(id json, NSError *error) {
        callback ? callback(error) : nil;
    }];
}

- (void)deleteUserProfilePicture:(BuddyCompletionCallback)callback
{
    NSString *resource = [NSString stringWithFormat:@"user/%@/profilepicture", self.id];
    
    [self.client DELETE:resource parameters:nil callback:^(id json, NSError *error) {
        callback ? callback(error) : nil;
    }];
}

@end
