//
//  BPPhoto.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPPhoto.h"
#import "BuddyObject+Private.h"
#import "BPSisterObject.h"
#import <objc/runtime.h>

@implementation BPPhoto

@synthesize caption;

- (void)registerProperties
{
    [super registerProperties];
    
    [self registerProperty:@selector(caption)];
}


static NSString *photos = @"pictures";
+(NSString *) requestPath{
    return photos;
}

+ (void)createWithImage:(UIImage *)image
          describePhoto:(DescribePhoto)describePhoto
                 client:(id<BPRestProvider>)client
               callback:(BuddyObjectCallback)callback
{
    //NSData *data = UIImageJPEGRepresentation(image, 1);
    NSData *data = UIImagePNGRepresentation(image);
    
    id photoProperties= [BPSisterObject new];
    describePhoto ? describePhoto(photoProperties) : nil;

    id parameters = [photoProperties parametersFromProperties:@protocol(BPPhotoProperties)];
    
    [self createWithData:data parameters:parameters client:client callback:^(id newBuddyObject, NSError *error) {
        callback ? callback(newBuddyObject, error) : nil;
    }];
}

- (void)getImage:(BuddyImageResponse)callback
{
    [self getData:^(NSData *data, NSError *error) {
        UIImage *image = [UIImage imageWithData:data];
        callback ? callback(image, error) : nil;
    }];
}

@end
