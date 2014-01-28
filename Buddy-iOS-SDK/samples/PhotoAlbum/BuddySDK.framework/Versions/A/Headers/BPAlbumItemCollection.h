//
//  BPAlbumItemCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 1/25/14.
//
//

#import "BuddyCollection.h"
#import "BPAlbum.h"

@interface BPAlbumItemCollection : BuddyCollection



- (instancetype)initWithClient:(id<BPRestProvider>)client __attribute__((unavailable("Use initWithAlbum:andClient:")));;
- (instancetype)initWithAlbum:(BPAlbum *)album andClient:(id<BPRestProvider>)client;

- (void)addAlbumItem:(NSString *)itemId
         withComment:(NSString *)comment
            callback:(BuddyCompletionCallback)callback;
    
- (void)getAlbumItems:(BuddyCollectionCallback)callback;
    
- (void)searchAlbumItems:(BuddyCollectionCallback)callback;
    
- (void)getAlbumItem:(NSString *)photoId callback:(BuddyObjectCallback)callback;

    
@end
