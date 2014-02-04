//
//  BPAlbumCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 1/24/14.
//
//

#import "BuddyCollection.h"

@interface BPAlbumCollection : BuddyCollection

- (void)addAlbum:(NSString *)name
     withComment:(NSString *)comment
        callback:(BuddyObjectCallback)callback;
    
- (void)getAlbums:(BuddyCollectionCallback)callback;
    
- (void)searchAlbums:(BuddyCollectionCallback)callback;
    
- (void)getAlbum:(NSString *)albumId callback:(BuddyObjectCallback)callback;
    
@end
