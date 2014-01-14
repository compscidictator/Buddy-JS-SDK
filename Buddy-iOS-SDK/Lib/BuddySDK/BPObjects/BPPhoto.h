//
//  BPPhoto.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <UIKit/UIKit.h>
#import "BPBlob.h"

@interface BPPhoto : BPBlob

@property (nonatomic, copy) NSString *comment;
//@property (nonatomic, assign) CGSize size;

+ (void)createWithImage:(UIImage *)image andComment:(NSString *)comment callback:(BuddyObjectCallback)callback;

@end
