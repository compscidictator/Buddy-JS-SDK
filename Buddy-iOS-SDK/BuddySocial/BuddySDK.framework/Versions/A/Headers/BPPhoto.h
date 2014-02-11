//
//  BPPhoto.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <UIKit/UIKit.h>
#import "BPBlob.h"

@protocol BPPhotoProperties <BuddyObjectProperties>

@property (nonatomic, copy) NSString *comment;

@end

@class BPPhoto;

typedef void(^BuddyImageResponse)(UIImage *image, NSError *error);
typedef void(^DescribePhoto)(id<BPPhotoProperties>photoProperties);

@interface BPPhoto : BPBlob<BPPhotoProperties>

//@property (nonatomic, assign) CGSize size;

+ (void)createWithImage:(UIImage *)image
          describePhoto:(DescribePhoto)describePhoto
                 client:(id<BPRestProvider>)client
               callback:(BuddyObjectCallback)callback;

- (void)getImage:(BuddyImageResponse)callback;

@end
