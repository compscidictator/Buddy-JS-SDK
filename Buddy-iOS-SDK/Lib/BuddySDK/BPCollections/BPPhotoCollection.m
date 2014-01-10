//
//  BPPhotoCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/22/13.
//
//

#import "BPPhotoCollection.h"
#import "BPPhoto.h"
#import "BPSession.h"
#import "BuddyObject+Private.h"

@implementation BPPhotoCollection

-(instancetype)init{
    self = [super init];
    if(self){
        self.type = [BPPhoto class];
    }
    return self;
}

- (void)addPhoto:(UIImage *)photo
     withComment:(NSString *)comment
        callback:(BuddyObjectCallback)callback
{
    [[self type] createWithImage:photo andComment:comment callback:callback];
}

- (BPPhoto *)getPhoto:(NSInteger)photoId error:(NSError *)error
{
    return nil;
}

-(void)getPhotos:(BuddyCollectionCallback)complete
{
    [self getAll:complete];
}

@end
