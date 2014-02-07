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
}

static NSString *checkins = @"checkins";
+(NSString *) requestPath{
    return checkins;
}

@end
