//
//  BPPhoto.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPPhoto.h"

@implementation BPPhoto

-(id)init{
    self = [super init];
    if(self)
    {
        [self registerProperty:@selector(caption)];
    }
    return self;
}

static NSString *photos = @"photos";
+(NSString *) requestPath{
    return photos;
}

+ (void)createWithImage:(UIImage *)image andComment:(NSString *)comment callback:(BuddyObjectCallback)callback;
{
    NSData *data = UIImagePNGRepresentation(image);

    id parameters = @{@"comment": comment};
    
    [self createWithData:data parameters:parameters callback:^(id newBuddyObject, NSError *error) {
        
    }];
}

@end
