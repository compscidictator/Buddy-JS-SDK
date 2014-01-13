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
#import "Buddy.h"

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


-(void)getPhotos:(BuddyCollectionCallback)callback
{
    [self getAll:callback];
}

-(void)searchPhotos:(BuddyCollectionCallback)callback
{
    NSDictionary *parameters = @{
                                 @"ownerID": [Buddy user].id
                                 };
    
    [self search:parameters callback:callback];
}

- (void)getPhoto:(NSString *)photoId callback:(BuddyObjectCallback)callback
{
    [self getItem:photoId callback:callback];
}

@end
