//
//  AlbumList.m
//  PhotoAlbum
//
//  Created by Nick Ambrose on 1/27/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import "BuddySDK/BPAlbum.h"

#import "AlbumList.h"

@interface AlbumList()

 
@end

@implementation AlbumList

-(instancetype)init
{
    {
        self = [super init];
        if (self)
        {
            _albums = [[NSMutableArray alloc] init];
        }
        return self;
    }
}

-(NSInteger)count
{
    return [self.albums count];
}
-(void) clear
{
    [self.albums removeAllObjects];
}

-(void) addAlbum:(BPAlbum*)album
{
    [self.albums addObject:album];
}

-(BPAlbum*) getAlbumByID:(NSString*)albumID
{
    if(albumID==nil)
    {
        return nil;
    }
    
    for(BPAlbum *album in self.albums)
    {
        if([albumID compare:album.id]==0 )
        {
            return album;
        }
    }
    return nil;

}
-(BPAlbum*) albumAtIndex:(NSInteger) index
{
    if(index< [self count])
    {
        return [self.albums objectAtIndex:index];
    }
    return nil;
}



@end
