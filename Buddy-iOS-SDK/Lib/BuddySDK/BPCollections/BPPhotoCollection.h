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

- (BPPhoto *)getPhoto:(NSInteger)photoId error:(NSError *)error;

- (void)getPhotos:(BuddyCollectionCallback)complete;

@end
