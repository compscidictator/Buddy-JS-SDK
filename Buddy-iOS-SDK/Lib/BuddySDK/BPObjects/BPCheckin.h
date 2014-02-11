//
//  BPCheckin.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/14/13.
//
//

#import <UIKit/UIKit.h>
#import "BPCoordinate.h"

@protocol BPCheckinProperties <BuddyObjectProperties>

@property (nonatomic, copy) NSString *comment;
@property (nonatomic, copy) NSString *description;
@property (nonatomic, strong) BPCoordinate *location;

@end

typedef void(^DescribeCheckin)(id<BPCheckinProperties> checkinProperties);

@interface BPCheckin : BuddyObject<BPCheckinProperties>

@end
