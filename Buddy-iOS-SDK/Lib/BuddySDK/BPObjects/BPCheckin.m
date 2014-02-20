//
//  BPCheckin.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/14/13.
//
//

#import "BPCheckin.h"
#import "BuddyObject+Private.h"

@implementation BPCheckin

@synthesize comment, description, location;

- (void)registerProperties
{
    [super registerProperties];
    
    [self registerProperty:@selector(comment)];
    [self registerProperty:@selector(description)];
    [self registerProperty:@selector(location)];
}

static NSString *checkins = @"checkins";
+(NSString *) requestPath{
    return checkins;
}

+ (id)convertValue:(NSString *)value forKey:(NSString *)key
{
    return nil;
}

+ (id)convertValueToJSON:(NSString *)value forKey:(NSString *)key
{
    if ([key isEqualToString:@"location"]) {
        BPCoordinate *coord = (BPCoordinate *)value;
        return [NSString stringWithFormat:@"%f,%f", coord.latitude, coord.longitude];
    }
    return nil;
}

@end
