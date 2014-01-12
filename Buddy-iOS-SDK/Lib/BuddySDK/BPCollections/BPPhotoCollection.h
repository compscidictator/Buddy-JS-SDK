//
//  BPPhotoCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/22/13.
//
//

#import "BuddyCollection.h"
@class BPPhoto;

@interface BPPhotoCollection : BuddyCollection

- (void)addPhoto:(UIImage *)photo
     withComment:(NSString *)comment
        callback:(BuddyObjectCallback)callback;

- (void)getPhotos:(BuddyCollectionCallback)complete;

- (void)getPhoto:(NSString *)photoId callback:(BuddyObjectCallback)callback;

@end
