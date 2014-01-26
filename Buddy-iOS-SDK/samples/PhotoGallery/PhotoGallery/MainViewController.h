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


/* Probably should pass in the userPhotos collection to be accessed here instead of using AppDelegate. More flexible.
 * Thats for later though
 */
 
@property (nonatomic,weak) IBOutlet UICollectionView *galleryCollection;
@property (nonatomic,weak) IBOutlet UIButton *addPhotoBut;

-(void)doLogout;
-(IBAction)doAddPhoto:(id)sender;
@end
