//
//  BPPhoto.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPPhoto.h"
#import "BuddyObject+Private.h"

@implementation BPPhoto

- (instancetype)initBuddyWithClient:(id<BPRestProvider>)client {
    self = [super initBuddyWithClient:client];
    if(self)
    {
        [self registerProperty:@selector(comment)];
    }
    return self;
}

static NSString *photos = @"pictures";
+(NSString *) requestPath{
    return photos;
}

+ (void)createWithImage:(UIImage *)image
             andComment:(NSString *)comment
                 client:(id<BPRestProvider>)client
               callback:(BuddyObjectCallback)callback;
{
    NSData *data = UIImagePNGRepresentation(image);

#pragma message("TODO - More syntactical sugar. Use macro for now.")
    id parameters = @{@"comment": BOXNIL(comment)};
    
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
