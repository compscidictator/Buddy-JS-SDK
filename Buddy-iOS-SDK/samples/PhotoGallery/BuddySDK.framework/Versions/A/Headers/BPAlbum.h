//
//  BPAlbum.h
//  BuddySDK
//
//  Created by Erik Kerber on 1/8/14.
//
//

#import "Buddy.h"

@interface BPAlbum : BuddyObject

@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *comment;

- (void)addItemToAlbum:(id)albumItem callback:(BuddyObjectCallback)callback;

- (void)getAlbumItem:(NSString *)itemId callback:(BuddyObjectCallback)callback;

- (void)addItemIdToAlbum:(NSString *)itemId callback:(BuddyObjectCallback)callback;

@end
