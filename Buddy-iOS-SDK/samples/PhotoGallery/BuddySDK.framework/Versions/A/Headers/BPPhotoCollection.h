//
//  BPPhotoCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/22/13.
//
//

#import "BuddyCollection.h"
#import "BPPhoto.h"

@interface BPPhotoCollection : BuddyCollection

- (void)addPhoto:(UIImage *)photo
   describePhoto:(DescribePhoto)describePhoto
        callback:(BuddyObjectCallback)callback;

- (void)getPhotos:(BuddyCollectionCallback)callback;

- (void)searchPhotos:(DescribePhoto)describePhoto callback:(BuddyCollectionCallback)callback;

- (void)getPhoto:(NSString *)photoId callback:(BuddyObjectCallback)callback;

@end
