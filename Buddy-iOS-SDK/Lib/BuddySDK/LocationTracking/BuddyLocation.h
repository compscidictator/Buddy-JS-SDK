//
//  BuddyLocation.h
//  BuddySDK
//
//  Created by Erik Kerber on 9/15/13.
//
//

#import <Foundation/Foundation.h>

struct BPCoordinate {
    float latitude;
    float longitude;
};

@interface BuddyLocation : NSObject

///<summary>
/// Returns YES if the user has restricted location access for the current app.
///</summary>
-(BOOL) shouldRequestLocationTracking;

/// <summary>
/// TODO
/// </summary>
-(void) beginTrackingLocation:(void (^)())success andFailure:(void(^)())failure;

/// <summary>
/// TODO
/// </summary>
-(void) endTrackingLocation;

@property (readonly, assign) BOOL isTracking;

@end
