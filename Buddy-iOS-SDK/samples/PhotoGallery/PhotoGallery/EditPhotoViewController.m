//
//  EditPhotoViewController.m
//  PhotoGallery
//
//  Created by Nick Ambrose on 1/24/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//
#import <QuartzCore/QuartzCore.h>

#import "BuddySDK/BuddyObject.h"
#import "BuddySDK/BPPhoto.h"

#import "Constants.h"
#import "AppDelegate.h"
#import "PhotoList.h"

#import "EditPhotoViewController.h"

#define TAG_META_KEY @"TAG"

@interface EditPhotoViewController ()
@property (nonatomic,strong) MBProgressHUD *HUD;
@property (nonatomic,strong) BPPhoto *photo;
@property (nonatomic,strong) NSString *tagString;
-(void) goBack;

-(void) populateUI;

-(void) resignTextFields;
-(BuddyCompletionCallback) getSavePhotoCallback;
-(BuddyCompletionCallback) getSaveTagCallback;
-(BuddyCompletionCallback) getDeletePhotoCallback;
-(BuddyObjectCallback) getFetchMetadataCallback;

-(void) loadMetaData;
@end

@implementation EditPhotoViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil andPhoto:(BPPhoto*) photo
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        _photo=photo;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.deleteButton.layer.cornerRadius = DEFAULT_BUT_CORNER_RAD;
    self.deleteButton.layer.borderWidth = DEFAULT_BUT_BORDER_WIDTH;
    self.deleteButton.layer.borderColor = [UIColor blackColor].CGColor;
    self.deleteButton.clipsToBounds = YES;
    
    self.saveButton.layer.cornerRadius = DEFAULT_BUT_CORNER_RAD;
    self.saveButton.layer.borderWidth = DEFAULT_BUT_BORDER_WIDTH;
    self.saveButton.layer.borderColor = [UIColor blackColor].CGColor;
    self.saveButton.clipsToBounds = YES;
    
    [self loadMetaData];
    [self populateUI];
}

-(BuddyCompletionCallback) getSavePhotoCallback
{
    EditPhotoViewController * __weak weakSelf = self;
    
    return ^(NSError *error)
    {
        [weakSelf.HUD hide:TRUE afterDelay:0.1];
        weakSelf.HUD=nil;
        if(error!=nil)
        {
            NSLog(@"SavePhotoCallback - error Called");
            UIAlertView *alert =
            [[UIAlertView alloc] initWithTitle: @"Error Saving Photo"
                                       message: [error localizedDescription]
                                      delegate: self
                             cancelButtonTitle: @"OK"
                             otherButtonTitles: nil];
            [alert show];
            return;
        }
        
        NSLog(@"SavePhotoCallback - success Called");
        [weakSelf.photo setMetadataWithKey:TAG_META_KEY andString:weakSelf.tagString callback:[weakSelf getSaveTagCallback]];
        
    };
    
}
-(BuddyCompletionCallback) getDeletePhotoCallback
{
    EditPhotoViewController * __weak weakSelf = self;
    
    return ^(NSError *error)
    {
        [weakSelf.HUD hide:TRUE afterDelay:0.1];
        weakSelf.HUD=nil;
        if(error!=nil)
        {
            NSLog(@"DeletePhotoCallback - error Called");
            UIAlertView *alert =
            [[UIAlertView alloc] initWithTitle: @"Error Saving Photo"
                                       message: [error localizedDescription]
                                      delegate: self
                             cancelButtonTitle: @"OK"
                             otherButtonTitles: nil];
            [alert show];
            return;
        }
        
        [[CommonAppDelegate userPhotos] removePhoto:self.photo andImage:TRUE];
        
        NSLog(@"DeletePhotoCallback - success Called");
        [self goBack];
    };

}

-(BuddyCompletionCallback) getSaveTagCallback
{
    EditPhotoViewController * __weak weakSelf = self;
    return ^(NSError *error)
    {
        [weakSelf.HUD hide:TRUE afterDelay:0.1];
        weakSelf.HUD=nil;
        if(error!=nil)
        {
            NSLog(@"SaveTagCallback - error Called");
            UIAlertView *alert =
            [[UIAlertView alloc] initWithTitle: @"Error Saving Photo"
                                       message: [error localizedDescription]
                                      delegate: self
                             cancelButtonTitle: @"OK"
                             otherButtonTitles: nil];
            [alert show];
            return;
        }
        
        NSLog(@"SaveTagCallback - success Called");
        [self goBack];
    };

}

-(BuddyObjectCallback) getFetchMetadataCallback
{
    EditPhotoViewController * __weak weakSelf = self;
    return ^(id newBuddyObject, NSError *error)
    {
        [weakSelf.HUD hide:TRUE afterDelay:0.1];
        weakSelf.HUD=nil;
        
        if(error==nil)
        {
            self.tagString = newBuddyObject;
            [self populateUI];
        }
    };
}

-(void) goBack
{
    [[CommonAppDelegate navController] popViewControllerAnimated:YES];
}

-(void) resignTextFields
{
    [self.commentText resignFirstResponder];
    [self.tagText resignFirstResponder];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textBoxName
{
	[textBoxName resignFirstResponder];
	return YES;
}

-(void) populateUI
{
    if(self.photo==nil)
    {
        return;
    }
    
    UIImage *photoImage = [[CommonAppDelegate userPhotos] getImageByPhotoID:self.photo.id];
    if(photoImage!=nil)
    {
        [self.mainImage setImage:photoImage];
    }
    else
    {
        [self.mainImage setBackgroundColor:[UIColor blackColor]];
    }
    
    self.commentText.text = self.photo.comment;
    
    self.tagText.text = self.tagString;
    
    
}

-(IBAction)doDelete:(id)sender
{
    if(self.photo==nil)
    {
        return;
    }
    [self.photo deleteMe:[self getDeletePhotoCallback]];

}

-(IBAction)doSave:(id)sender
{
    if(self.photo==nil)
    {
        return;
    }
    
    self.photo.comment = self.commentText.text;
    self.tagString = self.tagText.text;
    
    
    [self.photo save:[self getSavePhotoCallback]];
    
}

-(void) loadMetaData
{
    self.HUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    self.HUD.labelText= @"Loading Tag Info";
    self.HUD.dimBackground = YES;
    self.HUD.delegate=self;
    [self.photo getMetadataWithKey:TAG_META_KEY callback:[self getFetchMetadataCallback]];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
