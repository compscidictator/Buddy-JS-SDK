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
#import "BPSisterObject.h"
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
   describePhoto:(DescribePhoto)describePhoto
        callback:(BuddyObjectCallback)callback
{
    [[self type] createWithImage:photo describePhoto:describePhoto client:self.client callback:callback];
}


-(void)getPhotos:(BuddyCollectionCallback)callback
{
    [self getAll:callback];
}

-(void)searchPhotos:(DescribePhoto)describePhoto callback:(BuddyCollectionCallback)callback
{
    id photoProperties= [BPSisterObject new];
    describePhoto ? describePhoto(photoProperties) : nil;
    
    id parameters = [photoProperties parametersFromProperties:@protocol(BPPhotoProperties)];
    
    [self search:parameters callback:callback];
}

- (void)getPhoto:(NSString *)photoId callback:(BuddyObjectCallback)callback
{
    [self getItem:photoId callback:callback];
}

@end
