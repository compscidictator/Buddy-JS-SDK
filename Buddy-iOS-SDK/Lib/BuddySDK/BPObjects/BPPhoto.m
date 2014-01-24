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

#pragma message("TODO - More syntactical sugar. Use macro for now.")
    NSMutableDictionary *parameters = [NSMutableDictionary dictionary];@{@"comment": BOXNIL(comment)};
    
    [self createWithData:data parameters:parameters client:client callback:^(id newBuddyObject, NSError *error) {
        callback ? callback(newBuddyObject, error) : nil;
    }];
}

- (void)getImage:(BuddyImageResponse)callback
{
    [self getData:^(NSData *data, NSError *error) {
        UIImage *image = [UIImage imageWithData:data];
        callback ? callback(image, self, error) : nil;

    }];
}
@end
