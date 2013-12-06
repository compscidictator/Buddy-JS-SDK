//
//  BPPhoto.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPPhoto.h"

@implementation BPPhoto

-(id)init{
    self = [super init];
    if(self)
    {
        [self registerProperty:@selector(caption)];
    }
    return self;
}

static NSString *photos = @"photos";
+(NSString *) requestPath{
    return photos;
}

@end
