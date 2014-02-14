//
//  BPCoordinate.h
//  BuddySDK
//
//  Created by Erik Kerber on 12/9/13.
//
//

#import <Foundation/Foundation.h>

@interface BPCoordinate : NSObject
@property (nonatomic, assign) float latitude;
@property (nonatomic, assign) float longitude;
@end

static inline BPCoordinate *BPCoordinateMake(float lat, float lon)
{
    BPCoordinate *coord = [[BPCoordinate alloc] init];
    coord.latitude = lat;
    coord.longitude = lon;
    return coord;
};