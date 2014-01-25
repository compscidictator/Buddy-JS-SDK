//
//  BPAlbumCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/24/14.
//
//

#import "BPAlbumCollection.h"
#import "BuddyObject+Private.h"

@implementation BPAlbumCollection

- (void)addAlbum:(UIImage *)photo
     withComment:(NSString *)comment
        callback:(BuddyObjectCallback)callback
{
    id parameters;
    [self.type createFromServerWithParameters:parameters client:self.client callback:callback];
}

@end
