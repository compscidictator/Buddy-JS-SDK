//
//  MainViewController.h
//  PhotoGallery
//
//  Created by Nick Ambrose on 1/22/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "MBProgressHUD.h"

@interface MainViewController : UIViewController <MBProgressHUDDelegate,UICollectionViewDataSource,
                                                  UICollectionViewDelegateFlowLayout>

 
@property (nonatomic,weak) IBOutlet UICollectionView *galleryCollection;
@property (nonatomic,weak) IBOutlet UIButton *addPhotoBut;

-(void)doLogout;
-(IBAction)doAddPhoto:(id)sender;
@end
