//
//  BPAlbumCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 1/24/14.
//
//

#import "BuddyCollection.h"
#import "BPAlbum.h"

@interface BPAlbumCollection : BuddyCollection

- (void)addAlbum:(NSString *)name
     withCaption:(NSString *)caption
        callback:(BuddyObjectCallback)callback;

- (void)searchAlbums:(DescribeAlbum)describeAlbum callback:(BuddyCollectionCallback)callback;

- (void)getAlbum:(NSString *)albumId callback:(BuddyObjectCallback)callback;
    
@end
