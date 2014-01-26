//
//  EditPhotoViewController.h
//  PhotoGallery
//
//  Created by Nick Ambrose on 1/24/14.
//  Copyright (c) 2014 Buddy Platform. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "MBProgressHUD.h"

@interface EditPhotoViewController : UIViewController <MBProgressHUDDelegate>

@property (nonatomic,weak) IBOutlet UIImageView *mainImage;
@property (nonatomic,weak) IBOutlet UITextField *commentText;
@property (nonatomic,weak) IBOutlet UITextField *tagText;
@property (nonatomic,weak) IBOutlet UIButton *deleteButton;
@property (nonatomic,weak) IBOutlet UIButton *saveButton;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil andPhoto:(BPPhoto*) photo;

-(IBAction)doDelete:(id)sender;
-(IBAction)doSave:(id)sender;


@end
