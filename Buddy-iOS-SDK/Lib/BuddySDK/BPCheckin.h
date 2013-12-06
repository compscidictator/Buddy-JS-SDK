//
//  BPCheckin.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/14/13.
//
//

#import <UIKit/UIKit.h>

@interface BPCheckin : BuddyObject

+(instancetype)checkin;

@property (nonatomic, copy) NSString *comment;
@property (nonatomic, copy) NSString *description;

@end
