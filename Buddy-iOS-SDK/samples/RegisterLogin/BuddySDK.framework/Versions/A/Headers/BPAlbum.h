//
//  BPAlbum.h
//  BuddySDK
//
//  Created by Erik Kerber on 1/8/14.
//
//

@protocol BPAlbumProperties <BuddyObjectProperties>

@property (nonatomic, copy) NSString *caption;

@end

typedef void(^DescribeAlbum)(id<BPAlbumProperties>photoProperties);

@interface BPAlbum : BuddyObject

@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *caption;

- (void)addItemToAlbum:(id)albumItem callback:(BuddyObjectCallback)callback;

- (void)getAlbumItem:(NSString *)itemId callback:(BuddyObjectCallback)callback;

- (void)addItemIdToAlbum:(NSString *)itemId callback:(BuddyObjectCallback)callback;

@end
