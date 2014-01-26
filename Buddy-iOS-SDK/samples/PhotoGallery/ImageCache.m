//
//  ImageCache.m
//  PhotoGallery
//
//  Created by Nick Ambrose on 1/23/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import "ImageCache.h"
@interface ImageCache ()
@property (nonatomic,strong) NSMutableDictionary *imageCache;
@end

@implementation ImageCache

-(instancetype)init
{
    {
        self = [super init];
        if (self)
        {
            _imageCache = [[NSMutableDictionary alloc] init];
        }
        return self;
    }
}

-(void)clear
{
    [self.imageCache removeAllObjects];
}

-(NSInteger)count
{
    return [self.imageCache count];
}

-(UIImage*)getImageByPhotoID:(NSString*)photoID
{
    return [self.imageCache objectForKey:photoID];
}

-(void) addImage:(UIImage*)image withPhotoID:(NSString*)photoID
{
    [self.imageCache setObject:image forKey:photoID];
}

-(void) removeImageByID:(NSString*)photoID
{
    [self.imageCache removeObjectForKey:photoID];
}


@end
