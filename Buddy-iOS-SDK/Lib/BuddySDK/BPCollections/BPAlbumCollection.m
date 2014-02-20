//
//  BPAlbumCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/24/14.
//
//

#import "BPAlbumCollection.h"
#import "BuddyCollection+Private.h"
#import "BuddyObject+Private.h"
#import "BPSisterObject.h"
#import "BPAlbum.h"

@implementation BPAlbumCollection

- (instancetype)initWithClient:(id<BPRestProvider>)client{
    self = [super initWithClient:client];
    if(self){
        self.type = [BPAlbum class];
    }
    return self;
}
    
- (void)addAlbum:(NSString *)name
     withCaption:(NSString *)caption
        callback:(BuddyObjectCallback)callback
{
    NSDictionary *parameters = @{
                                 @"name": BOXNIL(name),
                                 @"caption": BOXNIL(caption)
                                 };
    
    [self.type createFromServerWithParameters:parameters client:self.client callback:callback];
}
    
    
-(void)getAlbums:(BuddyCollectionCallback)callback
{
    [self getAll:callback];
}
    
//-(void)searchAlbums:(BuddyCollectionCallback)callback
//{
//    NSDictionary *parameters = @{
//                                 @"ownerID": BOXNIL([Buddy user].id)
//                                 };
//    
//    [self search:parameters callback:callback];
//}

-(void)searchAlbums:(DescribeAlbum)describeAlbum callback:(BuddyCollectionCallback)callback
{
    id albumProperties = [BPSisterObject new];
    describeAlbum ? describeAlbum(albumProperties) : nil;
    
    id parameters = [albumProperties parametersFromProperties:@protocol(BPAlbumProperties)];
    
    [self search:parameters callback:callback];
}
    
- (void)getAlbum:(NSString *)albumId callback:(BuddyObjectCallback)callback
{
    [self getItem:albumId callback:callback];
}

@end
