//
//  BPAlbum.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/8/14.
//
//

#import "BPAlbum.h"
#import "BPAlbumItemCollection.h"
#import "BuddyObject+Private.h"
#import "BPRestProvider.h"
#import "BPCLient.h"
@interface BPAlbum()

@property (nonatomic, strong) BPAlbumItemCollection *items;
    
@end

@implementation BPAlbum

- (instancetype)initBuddyWithClient:(id<BPRestProvider>)client {
    self = [super initBuddyWithClient:client];
    if(self)
    {
        [self registerProperty:@selector(name)];
        [self registerProperty:@selector(comment)];
        _items = [[BPAlbumItemCollection alloc] initWithAlbum:self andClient:client];
    }
    return self;
}

static NSString *albums = @"albums";
+(NSString *) requestPath{
    return albums;
}

- (void)addItemToAlbum:(id)albumItem callback:(BuddyCompletionCallback)callback
{
    [self.items addAlbumItem:albumItem withComment:@"" callback:^(NSError *error) {
        callback(error);
    }];
}

- (void)getItems:(BuddyCollectionCallback)callback
{
    
}

@end
