//
//  MainViewController.h
//  PhotoAlbum
//
//  Created by Nick Ambrose on 1/27/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MBProgressHUD.h"

@interface MainViewController : UIViewController <MBProgressHUDDelegate,UICollectionViewDataSource,
                                    UICollectionViewDelegateFlowLayout>


@property (nonatomic,weak) IBOutlet UICollectionView *albumCollection;
@property (nonatomic,weak) IBOutlet UIButton *addAlbumBut;

-(void)doLogout;
-(IBAction)doAddAlbum:(id)sender;


@end
