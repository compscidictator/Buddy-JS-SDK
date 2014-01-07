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
        callback:(BuddyCollectionCallback)callback;

- (BPPhoto *)getPhoto:(NSInteger)photoId error:(NSError *)error;

@end
