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

- (id)initBuddyWithSession:(BPSession *)session
{
    self = [super initBuddyWithSession:session];
    if(self)
    {
        [self registerProperty:@selector(caption)];
    }
    return self;
}

static NSString *photos = @"pictures";
+(NSString *) requestPath{
    return photos;
}

+ (void)createWithImage:(UIImage *)image
                    andComment:(NSString *)comment
                    session:(BPSession*)session
                    callback:(BuddyObjectCallback)callback;
{
    NSData *data = UIImagePNGRepresentation(image);

    id parameters = @{@"comment": comment};
    
    [self createWithData:data parameters:parameters session:session callback:^(id newBuddyObject, NSError *error) {
#pragma message("TODO - Error")
        callback(newBuddyObject, nil);
    }];
}

@end
