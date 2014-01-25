//
//  BPAlbum.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/8/14.
//
//

#import "BPAlbum.h"
#import "BuddyObject+Private.h"

@implementation BPAlbum

- (id)initBuddyWithClient:(BPClient *)client
{
    self = [super initBuddyWithClient:client];
    if(self)
    {
        [self registerProperty:@selector(name)];
        [self registerProperty:@selector(comment)];
    }
    return self;
}

@end
