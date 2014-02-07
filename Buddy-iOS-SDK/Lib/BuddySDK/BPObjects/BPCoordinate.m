//
//  BPCoordinate.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/9/13.
//
//

#import "BPCoordinate.h"

@implementation BPCoordinate

- (NSString *)stringValue
{
    return [NSString stringWithFormat:@"%.4f, %.4f", self.latitude, self.longitude];
}

@end
