//
//  AlbumList.h
//  PhotoAlbum
//
//  Created by Nick Ambrose on 1/27/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import <Foundation/Foundation.h>

@class BPAlbum;

@interface AlbumList : NSObject

@property (nonatomic,strong) NSMutableArray *albums;

-(NSInteger)count;
-(void) clear;

-(void) addAlbum:(BPAlbum*)album;

-(BPAlbum*) getAlbumByID:(NSString*)albumID;
-(BPAlbum*) albumAtIndex:(NSInteger) index;


@end
