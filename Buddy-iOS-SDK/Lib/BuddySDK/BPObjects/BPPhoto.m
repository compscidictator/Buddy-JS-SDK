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

- (id)initBuddyWithClient:(BPClient *)client
{
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
                    client:(BPClient*)client
                    callback:(BuddyObjectCallback)callback;
{
    NSData *data = UIImagePNGRepresentation(image);

    id parameters = @{@"comment": comment};
    
    [self createWithData:data parameters:parameters client:client callback:^(id newBuddyObject, NSError *error) {
        callback(newBuddyObject, error);
    }];
}

- (void)getImage:(BuddyImageResponse)callback
{
    [self getData:^(NSData *data, NSError *error) {
        UIImage *image = [UIImage imageWithData:data];
        callback(image, error);
    }];
}
@end
