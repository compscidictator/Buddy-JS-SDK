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

-(instancetype)initBuddyWithSession:(BPSession *)session{
    self = [super initBuddyWithSession:session];
    if(self)
    {
        [self registerProperty:@selector(comment)];
        [self registerProperty:@selector(description)];
    }
    return self;
}

/*
+(instancetype)checkin
{
    return nil;
    // TODO return [[[self class] alloc] initBuddyWithSession: self.session];
}
*/

static NSString *checkins = @"checkins";
+(NSString *) requestPath{
    return checkins;
}

@end
