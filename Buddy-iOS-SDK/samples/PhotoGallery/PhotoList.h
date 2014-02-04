//
//  PhotoList.h
//  PhotoGallery
//
//  Created by Nick Ambrose on 1/23/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import <Foundation/Foundation.h>

@class BPPhoto;


/* We could consider a higher-level class that automates fetching an Image for a photo if it does not exist and uses callbacks. For now, keeping it basic
 */

@interface PhotoList : NSObject

// For large numbers of photos this would be better as a dictionary but then need a mapping in the LayoutView.
@property (nonatomic,strong) NSMutableArray *photoList;

-(void)clearAndImages:(BOOL)andImages;
-(NSInteger)count;

-(void)putPhotos:(NSMutableArray *)photos;
-(BPPhoto*)getPhotoByID:(NSString*)photoID;
-(BPPhoto*)photoAtIndex:(NSInteger)index;
-(void) addPhoto:(BPPhoto*)photo;

// Uses Photo.id to remove
-(void) removePhoto:(BPPhoto*)photo andImage:(BOOL) andImage;

-(void) removePhotoByID:(NSString*)photoID andImage:(BOOL) andImage;


-(void)clearImagesOnly;
-(NSInteger)countImages;
-(UIImage*)getImageByPhotoID:(NSString*)photoID;

// Replaces whatever is in already with same ID
-(void) addImage:(UIImage*)image withPhotoID:(NSString*)photoID;

// Could return BOOL to indicate if image was actually present to remove
-(void) removeImageByID:(NSString*)photoID;


@end
