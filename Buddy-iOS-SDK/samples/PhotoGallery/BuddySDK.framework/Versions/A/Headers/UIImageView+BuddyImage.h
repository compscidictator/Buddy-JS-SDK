//
//  UIImageView+BuddyImage.h
//  BuddySDK
//
//  Created by Erik Kerber on 1/9/14.
//
//

#import <UIKit/UIKit.h>
@class BPPhoto;

@interface UIImageView (BuddyImage)

- (void)setImageWithBPPhoto:(BPPhoto *)photo;
@end
