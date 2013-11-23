//
//  BPCheckin.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/14/13.
//
//

#import "BPCheckin.h"

@implementation BPCheckin

-(id)init{
    self = [super init];
    if(self)
    {
        [self registerProperty:@selector(comment)];
        [self registerProperty:@selector(description)];
    }
    return self;
}

static NSString *checkins = @"/checkins";
+(NSString *) requestPath{
    return checkins;
}

@end
