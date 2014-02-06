//
//  PhotoList.m
//  PhotoGallery
//
//  Created by Nick Ambrose on 1/23/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import "BuddySDK/BuddyObject.h"
#import "BuddySDK/BPPhoto.h"

#import "PhotoList.h"
#import "ImageCache.h"

@interface PhotoList ()

@property (nonatomic,strong) ImageCache *cache;

-(NSInteger) findIndexOfPhotoByID:(NSString*)photoID;

@end

@implementation PhotoList

-(instancetype)init
{
    {
        self = [super init];
        if (self)
        {
            _photoList = [[NSMutableArray alloc] init];
            _cache = [[ImageCache alloc] init];
        }
        return self;
    }
}

-(void)clearAndImages:(BOOL)andImages
{
    [self.photoList removeAllObjects];
    if(andImages)
    {
        [self clearImagesOnly];
    }
}

-(NSInteger)count
{
    return [self.photoList count];
}

-(void)putPhotos:(NSMutableArray *)photos
{
    self.photoList = photos;
}

-(BPPhoto*)getPhotoByID:(NSString*)photoID
{
    if(photoID==nil)
    {
        return nil;
    }
    
    for(BPPhoto *photo in self.photoList)
    {
        if([photoID compare:photo.id]==0 )
        {
            return photo;
        }
    }
    return nil;
}

-(void) addPhoto:(BPPhoto*)photo
{
    if(photo==nil)
    {
        return;
    }
    
    if( [self getPhotoByID:photo.id]!=nil)
    {
        // Already present by ID, dont add.
        return;
    }
    [self.photoList addObject:photo];
}

-(BPPhoto*)photoAtIndex:(NSInteger)index
{
    if(index< [self count])
    {
        return [self.photoList objectAtIndex:index];
    }
    return nil;
}

-(NSInteger) findIndexOfPhotoByID:(NSString*)photoID
{
    if(photoID==nil)
    {
        return NSNotFound;
    }
    
    NSInteger index=0;
    for(BPPhoto *photo in self.photoList)
    {
        if([photoID isEqualToString:photo.id])
        {
            return index;
        }
        index++;
    }
    return NSNotFound;
}

-(void) removePhoto:(BPPhoto*)photo andImage:(BOOL) andImage
{
    NSInteger photoIndex = [self findIndexOfPhotoByID:photo.id];
    if(photoIndex!=NSNotFound)
    {
        [self.photoList removeObjectAtIndex:photoIndex];
    }
    if(andImage)
    {
        [self.cache removeImageByID:photo.id];
    }
}

-(void) removePhotoByID:(NSString*)photoID andImage:(BOOL) andImage
{
    NSInteger photoIndex = [self findIndexOfPhotoByID:photoID];
    if(photoIndex!=NSNotFound)
    {
        [self.photoList removeObjectAtIndex:photoIndex];
    }
    
    if(andImage)
    {
        [self.cache removeImageByID:photoID];
    }
}

-(void)clearImagesOnly
{
    [self.cache clear];
}
-(NSInteger)countImages
{
    return [self.cache count];
}
-(UIImage*)getImageByPhotoID:(NSString*)photoID
{
    return [self.cache getImageByPhotoID:photoID];
}

-(void) addImage:(UIImage*)image withPhotoID:(NSString*)photoID
{
    [self.cache addImage:image withPhotoID:photoID];
}

-(void) removeImageByID:(NSString*)photoID
{
    [self.cache removeImageByID:photoID];
}

@end
