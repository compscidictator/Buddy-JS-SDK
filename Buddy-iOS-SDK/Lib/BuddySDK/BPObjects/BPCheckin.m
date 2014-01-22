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

-(instancetype)initBuddyWithClient:(BPClient *)client{
    self = [super initBuddyWithClient:client];
    if(self)
    {
        [self registerProperty:@selector(comment)];
        [self registerProperty:@selector(description)];
    }
    return self;
}

static NSString *checkins = @"checkins";
+(NSString *) requestPath{
    return checkins;
}

@end
