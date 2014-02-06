//
//  BPLocation.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/9/14.
//
//

#import "BPLocation.h"
#import "BuddyObject+Private.h"

@implementation BPLocation

- (void)registerProperties
{
    [super registerProperties];
    
    [self registerProperty:@selector(name)];
    [self registerProperty:@selector(description)];
    [self registerProperty:@selector(streetAddress)];
    [self registerProperty:@selector(city)];
    [self registerProperty:@selector(state)];
    [self registerProperty:@selector(country)];
    [self registerProperty:@selector(zipcode)];
    [self registerProperty:@selector(fax)];
    [self registerProperty:@selector(phone)];
    [self registerProperty:@selector(website)];
    [self registerProperty:@selector(categoryId)];
    [self registerProperty:@selector(distance)];
}

NSString *location = @"location";
+ (NSString *)requestPath
{
    return location;
}

@end
