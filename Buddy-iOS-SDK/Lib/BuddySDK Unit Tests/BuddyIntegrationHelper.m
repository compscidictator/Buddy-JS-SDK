//
//  BuddyIntegrationHelper.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/29/13.
//
//

#import "BuddyIntegrationHelper.h"
#import "Buddy.h"

@implementation BuddyIntegrationHelper

+ (void) bootstrapLogin:(void(^)())complete
{
    [Buddy initClient:APP_NAME appKey:APP_KEY complete:^{
        NSDictionary *options = @{@"name": @"Erik Kerber",
                                  @"gender": @(BPUserGender_Male),
                                  @"email": @"erik.kerber@gmail.com",
                                  @"dateOfBirth": [NSNull null],
                                  @"relationshipStatus": @(BPUserRelationshipStatusOnTheProwl),
                                  @"celebrityMode": @(YES),
                                  @"fuzzLocation": @(NO)
                                  };
        
        [Buddy login:TEST_USERNAME password:TEST_PASSWORD completed:^(BPUser *loggedInsUser, NSError *error) {
            //if(loggedInsUser)
            //    complete();
            //else {
                [Buddy createUser:TEST_USERNAME password:TEST_PASSWORD options:options completed:^(BPUser *newBuddyObject, NSError *error) {
                    [Buddy login:TEST_USERNAME password:TEST_PASSWORD completed:^(BPUser *loggedInsUser, NSError *error) {
                        complete();
                    }];
                }];
            //}
        }];
        

    }];
}

+ (NSDate *)randomDate
{
    NSDate *today = [[NSDate alloc] init];
    NSLog(@"%@", today);
    NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
    NSDateComponents *offsetComponents = [[NSDateComponents alloc] init];
    [offsetComponents setYear:-100]; // note that I'm setting it to -1
    NSDate *endOfWorldWar3 = [gregorian dateByAddingComponents:offsetComponents toDate:today options:0];
    NSLog(@"%@", endOfWorldWar3);
    
    
    NSCalendar *currentCalendar = [NSCalendar currentCalendar];
    NSDateComponents *comps = [currentCalendar components:NSYearCalendarUnit | NSMonthCalendarUnit | NSDayCalendarUnit fromDate:endOfWorldWar3];
    
    [comps setMonth:arc4random_uniform(12)];
    
    NSRange range = [[NSCalendar currentCalendar] rangeOfUnit:NSDayCalendarUnit inUnit:NSMonthCalendarUnit forDate:[currentCalendar dateFromComponents:comps]];
    
    [comps setDay:arc4random_uniform(range.length)];
    
    // Normalise the time portion
    [comps setHour:0];
    [comps setMinute:0];
    [comps setSecond:0];
    [comps setTimeZone:[NSTimeZone timeZoneWithName:@"GMT"]];
    
    NSDate *randomDate = [currentCalendar dateFromComponents:comps];
    
    return randomDate;
}

NSString *letters = @"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

+ (NSString *)randomString:(int)len
{
    NSMutableString *randomString = [NSMutableString stringWithCapacity: len];
    
    for (int i=0; i<len; i++) {
        [randomString appendFormat: @"%C", [letters characterAtIndex: arc4random() % [letters length]]];
    }
    
    return randomString;
}

@end
