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

-(instancetype)initBuddy{
    self = [super initBuddy];
    if(self)
    {
        [self registerProperty:@selector(comment)];
        [self registerProperty:@selector(description)];
    }
    return self;
}

+(instancetype)checkin
{
    return [[[self class] alloc] initBuddy];
}

static NSString *checkins = @"checkins";
+(NSString *) requestPath{
    return checkins;
}

@end
