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
        [self registerProperty:@selector(username)];
        [self registerProperty:@selector(gender)];
        [self registerProperty:@selector(dateOfBirth)];
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

+(void)createUserWithName:(NSString *)name password:(NSString *)password completed:(BuddyObjectCallback)callback
{
    NSDictionary *parameters = @{@"username": name,
                                 @"password": password,
                                 @"email": @"erik.kerber@gmail.com"};
    
    [self createFromServerWithParameters:parameters complete:^(id newBuddyObject) {
        callback(newBuddyObject);
    }];
}

+(void)login:(NSString *)name password:(NSString *)password completed:(BuddyObjectCallback)callback
{
    [[BPClient defaultClient] login:name password:password success:^(id json) {
        BPUser *user = [[BPUser alloc] initBuddy];
        // TODO - but I don't want to share the JAGConverter :(
        callback(user);
    }];
}

+(NSString *)requestPath
{
    return @"users";
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


@end
