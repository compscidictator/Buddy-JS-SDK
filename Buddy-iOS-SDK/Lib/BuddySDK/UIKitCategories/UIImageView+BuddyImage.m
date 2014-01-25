//
//  UIImageView+BuddyImage.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/9/14.
//
//

#import "UIImageView+BuddyImage.h"
#import "BPPhoto.h"
#import "UIImageView+AFNetworking.h"

@implementation UIImageView (BuddyImage)

- (void)setImageWithBPPhoto:(BPPhoto *)photo
{
    [self setImageWithURL:[NSURL URLWithString:photo.signedUrl]];
}

@end
