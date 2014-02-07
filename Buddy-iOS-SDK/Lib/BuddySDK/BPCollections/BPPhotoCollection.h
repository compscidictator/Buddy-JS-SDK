//
//  BPPhotoCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/22/13.
//
//

#import "BuddyCollection.h"
#import "BPPhoto.h"

@class BPPhoto;

@interface BPPhotoCollection : BuddyCollection

- (void)addPhoto:(UIImage *)photo
   describePhoto:(DescribePhoto)describePhoto
        callback:(BuddyObjectCallback)callback;

- (void)getPhotos:(BuddyCollectionCallback)callback;

- (void)searchPhotos:(BuddyCollectionCallback)callback;

- (void)getPhoto:(NSString *)photoId callback:(BuddyObjectCallback)callback;

@end
