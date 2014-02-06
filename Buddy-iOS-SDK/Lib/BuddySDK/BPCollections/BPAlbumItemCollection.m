//
//  BPAlbumItemCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/25/14.
//
//

#import "BPAlbumItemCollection.h"
#import "BuddyCollection+Private.h"
#import "BPAlbumItemContainer.h"

@interface BPAlbumItemCollection()

@property (nonatomic, weak) BPAlbum *album;

@end

@implementation BPAlbumItemCollection

- (instancetype)initWithAlbum:(BPAlbum *)album andClient:(id<BPRestProvider>)client
{
    self = [super initWithClient:client];
    if (self) {
        self.type = [BPAlbumItemContainer class];
        _album = album;
    }
    return self;
}

- (NSString *)requestPrefix
{
    return [NSString stringWithFormat:@"albums/%@/", self.album.id];
}

- (void)addAlbumItem:(NSString *)itemId
         withComment:(NSString *)comment
            callback:(BuddyObjectCallback)callback
{
    NSDictionary *params = @{
                             @"ItemId": itemId,
                             @"Comment": comment
                             };
    NSString *requestPath = [self.requestPrefix stringByAppendingString:[[self type] requestPath]];
    
    [self.client POST:requestPath parameters:params callback:^(id json, NSError *error) {
        callback(json, error);
    }];
}

- (void)getAlbumItem:(NSString *)itemId
            callback:(BuddyObjectCallback)callback
{
    [self getItem:itemId callback:callback];
}
@end
