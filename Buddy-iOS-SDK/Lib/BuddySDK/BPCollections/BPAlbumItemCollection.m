//
//  BPAlbumItemCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/25/14.
//
//

#import "BPAlbumItemCollection.h"

@interface BPAlbumItemCollection()

@property (nonatomic, weak) BPAlbum *album;

@end

@implementation BPAlbumItemCollection

- (instancetype)initWithAlbum:(BPAlbum *)album andClient:(id<BPRestProvider>)client
{
    self = [super initWithClient:client];
    if (self) {
        _album = album;
    }
    return self;
}

- (NSString *)buildItemPath
{
    return [NSString stringWithFormat:@"albums/%@/items", self.album.id];
}

- (void)addAlbumItem:(NSString *)itemId
         withComment:(NSString *)comment
            callback:(BuddyCompletionCallback)callback
{
    NSDictionary *params = @{
                             @"ItemId": itemId,
                             @"Comment": comment
                             };
    
    [self.client POST:[self buildItemPath] parameters:params callback:^(id json, NSError *error) {
        callback(error);
    }];
}

@end
