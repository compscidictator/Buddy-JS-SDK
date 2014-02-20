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

@implementation BPCoordinateRange

- (NSString *)stringValue
{
    return [NSString stringWithFormat:@"%.4f, %.4f, %li", self.latitude, self.longitude, (long)self.range];
}

@end