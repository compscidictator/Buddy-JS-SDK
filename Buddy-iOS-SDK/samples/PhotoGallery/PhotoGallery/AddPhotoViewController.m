//
//  AddPhotoViewController.m
//  PhotoGallery
//
//  Created by Nick Ambrose on 1/22/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import <MobileCoreServices/UTCoreTypes.h>

#import <QuartzCore/QuartzCore.h>

#import "MBProgressHUD.h"

#import "BuddySDK/Buddy.h"

#import "Constants.h"
#import "AppDelegate.h"

#import "PhotoList.h"

#import "AddPhotoViewController.h"

@interface AddPhotoViewController ()

@property (nonatomic,strong) MBProgressHUD *HUD;

-(void) goBack;

-(void) resignTextFields;
-(BuddyObjectCallback) getAddPhotoCallback;
@end

@implementation AddPhotoViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [UIButton buttonWithType:UIButtonTypeSystem];
    
    self.choosePhotoBut.layer.cornerRadius = DEFAULT_BUT_CORNER_RAD;
    self.choosePhotoBut.layer.borderWidth = DEFAULT_BUT_BORDER_WIDTH;
    self.choosePhotoBut.layer.borderColor = [UIColor blackColor].CGColor;
    self.choosePhotoBut.clipsToBounds = YES;
    
    self.cancelBut.layer.cornerRadius = DEFAULT_BUT_CORNER_RAD;
    self.cancelBut.layer.borderWidth = DEFAULT_BUT_BORDER_WIDTH;
    self.cancelBut.layer.borderColor = [UIColor blackColor].CGColor;
    self.cancelBut.clipsToBounds = YES;

    
    self.addBut.layer.cornerRadius = DEFAULT_BUT_CORNER_RAD;
    self.addBut.layer.borderWidth = DEFAULT_BUT_BORDER_WIDTH;
    self.addBut.layer.borderColor = [UIColor blackColor].CGColor;
    self.addBut.clipsToBounds = YES;


}

-(BuddyObjectCallback) getAddPhotoCallback
{
    AddPhotoViewController * __weak weakSelf = self;
    
    return ^(id newBuddyObject, NSError *error)
    {
        [weakSelf.HUD hide:TRUE afterDelay:0.1];
        weakSelf.HUD=nil;
        
        if(error!=nil)
        {
            NSLog(@"AddPhotoCallback - error Called");
            UIAlertView *alert =
            [[UIAlertView alloc] initWithTitle: @"Error Adding Photo"
                                       message: [error localizedDescription]
                                      delegate: self
                             cancelButtonTitle: @"OK"
                             otherButtonTitles: nil];
            [alert show];
            return;
        }
        
        NSLog(@"LoadUserPhotosCallback - success Called");
        [[CommonAppDelegate userPhotos] addPhoto: newBuddyObject];
        [self goBack];
        
    };

}
-(void) goBack
{
    [[CommonAppDelegate navController] popViewControllerAnimated:YES];
}

-(IBAction)doCancel:(id)sender
{
    [self goBack];
}

-(IBAction)doAdd:(id)sender
{
    self.HUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    self.HUD.labelText= @"Adding Photo";
    self.HUD.dimBackground = YES;
    self.HUD.delegate=self;
    [[Buddy photos] addPhoto:self.selectedImage describePhoto:^(id<BPPhotoProperties> photoProperties)
     {
         photoProperties.caption =self.captionField.text;
     } callback: [self getAddPhotoCallback]];
}

-(IBAction)showCamera:(id)sender
{
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera])
    {
        UIImagePickerController *imagePickerCamera =[[UIImagePickerController alloc] init];
        imagePickerCamera.delegate = self;
        imagePickerCamera.mediaTypes = [NSArray arrayWithObjects:(NSString *) kUTTypeImage,nil];
        imagePickerCamera.allowsEditing = YES;
        imagePickerCamera.sourceType = UIImagePickerControllerSourceTypeCamera;
        
        [self presentViewController:imagePickerCamera  animated:YES completion:nil];
    }
    
    else if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeSavedPhotosAlbum])
    {
        UIImagePickerController *imagePickerAlbum =[[UIImagePickerController alloc] init];
        imagePickerAlbum.delegate = self;
        imagePickerAlbum.mediaTypes = [NSArray arrayWithObjects:(NSString *) kUTTypeImage,nil];
        imagePickerAlbum.allowsEditing = YES;
        imagePickerAlbum.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
        
        [self presentViewController:imagePickerAlbum animated:YES completion:nil];
    }
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    
    UIImage *chosenImage = info[UIImagePickerControllerEditedImage];
    self.selectedImage = chosenImage;
    [self.choosePhotoBut setTitle:@"" forState:UIControlStateNormal];
    [self.choosePhotoBut setImage:self.selectedImage forState: UIControlStateNormal];
    [self.choosePhotoBut setImage:self.selectedImage forState:UIControlStateSelected];
    [self.choosePhotoBut setContentMode:UIViewContentModeScaleToFill];

    [picker dismissViewControllerAnimated:YES completion:NULL];
    
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    
    [picker dismissViewControllerAnimated:YES completion:NULL];
    [self.choosePhotoBut setImage:nil forState: UIControlStateNormal];
    [self.choosePhotoBut setTitle:@"Choose Photo" forState:UIControlStateNormal];
    
}

-(void) resignTextFields
{
    [self.captionField resignFirstResponder];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textBoxName
{
	[textBoxName resignFirstResponder];
	return YES;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
