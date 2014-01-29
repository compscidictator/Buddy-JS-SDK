//
//  BPAlbumItem.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/24/14.
//
//

#import "BPAlbumItem.h"
#import "BuddyObject+Private.h"

@implementation BPAlbumItem

- (instancetype)initBuddyWithClient:(id<BPRestProvider>)client {
    self = [super initBuddyWithClient:client];
    if(self)
    {
        [self registerProperty:@selector(albumID)];
        [self registerProperty:@selector(itemID)];
    }
    return self;
}

+ (NSString *)requestPath
{
    return @"items";
}
@end
