//
//  BPAlbumItemCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 1/25/14.
//
//

#import "BuddyCollection.h"

@interface BPAlbumItemCollection : BuddyCollection

- (void)addAlbumItem:(UIImage *)photo
         withComment:(NSString *)comment
            callback:(BuddyObjectCallback)callback;
    
- (void)getAlbumItems:(BuddyCollectionCallback)callback;
    
- (void)searchAlbumItems:(BuddyCollectionCallback)callback;
    
- (void)getAlbumItem:(NSString *)photoId callback:(BuddyObjectCallback)callback;

    
@end
