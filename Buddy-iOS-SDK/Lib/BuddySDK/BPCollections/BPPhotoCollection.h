//
//  BPPhotoCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/22/13.
//
//

#import "BuddyCollection.h"

@interface BPPhotoCollection : BuddyCollection

- (void)addPhoto:(UIImage *)photo
     withComment:(NSString *)comment;

@end
