//
//  MainViewController.m
//  PhotoGallery
//
//  Created by Nick Ambrose on 1/22/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>

#import "BuddySDK/Buddy.h"
#import "BuddySDK/BPPhotoCollection.h"

#import "BuddySDK/UIImageView+BuddyImage.h"


#import "Constants.h"
#import "AppDelegate.h"

#import "PhotoList.h"

#import "MainViewController.h"
#import "AddPhotoViewController.h"
#import "EditPhotoViewController.h"

@interface MainViewController ()

// Set to TRUE in callbacks if an error occurs so that viewDidAppear can retry say after auth failure
@property (nonatomic,assign) BOOL apiError;


-(void) loadUserPhotos;
-(BuddyCollectionCallback) getLoadUserPhotosCallback;
-(BuddyImageResponse) getLoadPhotoDataCallback:(BPPhoto*)photo;
-(void)doRefreshCollection;
@end

@implementation MainViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        _apiError=FALSE;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [[CommonAppDelegate navController] setNavigationBarHidden:FALSE];
    
    
    UINib *nib =[UINib nibWithNibName:@"PhotoThumbView" bundle:nil] ;
    
    [self.galleryCollection registerNib:nib forCellWithReuseIdentifier:@"PhotoThumb"];
    
    
    UIBarButtonItem *backButton =[[UIBarButtonItem alloc]
                                  initWithTitle:@"Logout"
                                          style:UIBarButtonItemStylePlain
                                         target:self
                                         action:@selector(doLogout)];

    self.navigationItem.leftBarButtonItem = backButton;
    
    UIBarButtonItem *refreshButton =[[UIBarButtonItem alloc] initWithTitle:@"Refresh"  style:UIBarButtonItemStylePlain target:self action:@selector(doRefreshCollection)];
    
    
    self.navigationItem.rightBarButtonItem = refreshButton;

    
    self.addPhotoBut.layer.cornerRadius = DEFAULT_BUT_CORNER_RAD;
    self.addPhotoBut.layer.borderWidth = DEFAULT_BUT_BORDER_WIDTH;
    self.addPhotoBut.layer.borderColor = [UIColor blackColor].CGColor;
    self.addPhotoBut.clipsToBounds = YES;
    
    [self loadUserPhotos];
}

-(BuddyImageResponse) getLoadPhotoDataCallback:(BPPhoto*)photo
{
    MainViewController * __weak weakSelf = self;
    BPPhoto * __weak weakPhoto = photo;
    
    return ^(UIImage *image,  NSError *error)
    {
        if(weakSelf==nil)
        {
            return;
        }
        
        if(error!=nil)
        {
            // Consider setting some kind of error image in this case ? (Use case for userData hook!)
            return;
        }
        
        if(weakPhoto==nil)
        {
            return;
        }
        
        [[CommonAppDelegate userPhotos] addImage:image withPhotoID:weakPhoto.id];
        [weakSelf.galleryCollection reloadData];
    };

}

-(void) doRefreshCollection
{
    [self loadUserPhotos];
}

-(BuddyCollectionCallback) getLoadUserPhotosCallback
{
    MainViewController * __weak weakSelf = self;
    
    return ^(NSArray *buddyObjects, NSError *error)
    {
        
        /* Disabled for now as this throws a dialog on authError -- Dont really want to have to check in each CB but may have to
         */
         
        if(error!=nil)
        {
            self.apiError=TRUE;
            /*
            NSLog(@"getLoadUserPhotosCallback - error Called");
            UIAlertView *alert =
            [[UIAlertView alloc] initWithTitle: @"Error Loading Photos"
                                       message: [error localizedDescription]
                                      delegate: self
                             cancelButtonTitle: @"OK"
                             otherButtonTitles: nil];
            [alert show];
         */
            return;
        }
        self.apiError=FALSE;
        NSLog(@"getLoadUserPhotosCallback - success Called");
        [[CommonAppDelegate userPhotos] putPhotos: [buddyObjects mutableCopy]];
        [self.galleryCollection reloadData];
        
    };
}

- (void)viewDidAppear:(BOOL)animated
{
    // Later on when iOS supports it, check if we had a changed user here?
    [[CommonAppDelegate navController] setNavigationBarHidden:FALSE];
    if(self.apiError==TRUE)
    {
        self.apiError=FALSE;
        [self loadUserPhotos];
    }
    else
    {
        
        if([[CommonAppDelegate userPhotos] count]==0)
        {
            [self loadUserPhotos];
        }
        else
        {
            [self.galleryCollection reloadData];
        }
    }
}

-(void) loadUserPhotos
{
    [[Buddy photos] getAll:[self getLoadUserPhotosCallback]];
}

-(IBAction)doAddPhoto:(id)sender
{
    AddPhotoViewController *addPhotoVC = [[AddPhotoViewController alloc]
                                          initWithNibName:@"AddPhotoViewController" bundle:nil];
    
    [ [CommonAppDelegate navController] pushViewController:addPhotoVC animated:YES];

}

-(void)doLogout
{
    [CommonAppDelegate doLogout];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - UICollectionView Datasource

- (NSInteger)collectionView:(UICollectionView *)view numberOfItemsInSection:(NSInteger)section
{
    NSInteger count= [[CommonAppDelegate userPhotos] count];
    return count;
}

- (NSInteger)numberOfSectionsInCollectionView: (UICollectionView *)collectionView
{
    return 1;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)cv cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    //PhotoThumbnailView *cell = [cv dequeueReusableCellWithReuseIdentifier:@"PhotoThumbnail" forIndexPath:indexPath];
    UICollectionViewCell *cell =[cv dequeueReusableCellWithReuseIdentifier:@"PhotoThumb" forIndexPath:indexPath];
    cell.backgroundColor = [UIColor blackColor];
    
    BPPhoto *photo = [[CommonAppDelegate userPhotos] photoAtIndex:indexPath.row];
    if(photo==nil)
    {
        // Not so great
        return cell;
    }
    
    UIImageView *image = (UIImageView*)[cell viewWithTag:1];
    image.layer.cornerRadius = DEFAULT_BUT_CORNER_RAD;
    image.layer.borderWidth = DEFAULT_BUT_BORDER_WIDTH;
    image.layer.borderColor = [UIColor blackColor].CGColor;
    image.clipsToBounds = YES;

    
    // Now try to find corresponding UIImage
    UIImage *photoImage = [[CommonAppDelegate userPhotos] getImageByPhotoID:photo.id];
    if(photoImage!=nil)
    {
        //[cell.cellImage setImage:photoImage];
    }
    else
    {
        // For now use signed URL (may want to do direct access later)
        //[photo getImage: [self getLoadPhotoDataCallback,photo]];
        NSLog(@"Signed URL: %@",photo.signedUrl);
        cell.backgroundColor = [UIColor clearColor];
        [image setImageWithBPPhoto:photo];
        //cell.cellImage.backgroundColor = [UIColor redColor];
        //cell.label.text=@"HI";
        
    }
    
    return cell;
}
// 4
/*- (UICollectionReusableView *)collectionView:
 (UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
 {
 return [[UICollectionReusableView alloc] init];
 }*/

#pragma mark - UICollectionViewDelegate
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    BPPhoto *photo = [[CommonAppDelegate userPhotos] photoAtIndex:indexPath.row];
    if(photo==nil)
    {
        // Not so great
        return ;
    }
    
    EditPhotoViewController *addPhotoVC = [[EditPhotoViewController alloc]
                                          initWithNibName:@"EditPhotoViewController" bundle:nil andPhoto:photo];
    
    [ [CommonAppDelegate navController] pushViewController:addPhotoVC animated:YES];

}
- (void)collectionView:(UICollectionView *)collectionView didDeselectItemAtIndexPath:(NSIndexPath *)indexPath {
    // TODO: Deselect item
}

#pragma mark – UICollectionViewDelegateFlowLayout

// 1
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    
    //CGSize retval = photo.thumbnail.size.width > 0 ? photo.thumbnail.size : CGSizeMake(100, 100);
    CGSize retval =CGSizeMake(110, 110);
    //retval.height += 20;
    //retval.width += 20;
    return retval;
}

// 3
- (UIEdgeInsets)collectionView:
(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section {
    return UIEdgeInsetsMake(50, 20, 50, 20);
    //return UIEdgeInsetsMake(10, 10, 10, 10);
}


@end
