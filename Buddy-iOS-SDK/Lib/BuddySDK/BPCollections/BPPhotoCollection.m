//
//  BPPhotoCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/22/13.
//
//

#import "BPPhotoCollection.h"
#import "BPPhoto.h"
#import "BPClient.h"
#import "BuddyObject+Private.h"
#import "Buddy.h"

@implementation BPPhotoCollection

- (instancetype)initWithClient:(id<BPRestProvider>)client {
    self = [super initWithClient:client];
    if(self){
        self.type = [BPPhoto class];
    }
    return self;
}

- (void)addPhoto:(UIImage *)photo
     withComment:(NSString *)comment
        callback:(BuddyObjectCallback)callback
{
    [[self type] createWithImage:photo andComment:comment client:self.client callback:callback];
}


-(void)getPhotos:(BuddyCollectionCallback)callback
{
    [self getAll:callback];
}

-(void)searchPhotos:(BuddyCollectionCallback)callback
{
    NSDictionary *parameters = @{
                                 @"ownerID": BOXNIL([Buddy user].id)
                                 };
    
    [self search:parameters callback:callback];
}

- (void)getPhoto:(NSString *)photoId callback:(BuddyObjectCallback)callback
{
    [self getItem:photoId callback:callback];
}

@end
