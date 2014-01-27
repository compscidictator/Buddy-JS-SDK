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

static NSString *albums = @"albums";
+(NSString *) requestPath{
    return albums;
}

- (void)addItemToAlbum:(id)albumItem
{
    [[self.client restService] POST:[[self class] requestPath] parameters:nil callback:^(id json, NSError *error) {
    }];
}

- (void)getItems:(BuddyCollectionCallback)callback
{
    
}

@end
