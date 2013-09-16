//
//  BuddyLocation.m
//  BuddySDK
//
//  Created by Erik Kerber on 9/15/13.
//
//

#import "BuddyLocation.h"
#import <CoreLocation/CoreLocation.h>

@interface BuddyLocation()<CLLocationManagerDelegate>

@property (strong, nonatomic) CLLocationManager *location;
@end

@implementation BuddyLocation

-(id)init
{
    self = [super init];
    if(self)
    {
        self.location = [[CLLocationManager alloc] init];
        self.location.distanceFilter = kCLDistanceFilterNone;
        self.location.desiredAccuracy = kCLLocationAccuracyKilometer;
        self.location.delegate = self;
    }
    return self;
}

#pragma mark Public interface

-(void) beginTrackingLocation:(void (^)())success andFailure:(void(^)(NSError *))failure
{
    if([CLLocationManager authorizationStatus] == kCLAuthorizationStatusDenied ||
       [CLLocationManager authorizationStatus] == kCLAuthorizationStatusRestricted)
    {
        NSError *e = [[NSError alloc] init];
        failure(e);
    }else
    {
        [self.location startUpdatingLocation];
    }
}

-(BOOL) shouldRequestLocationTracking
{
    return  [CLLocationManager authorizationStatus] == kCLAuthorizationStatusDenied ||
            [CLLocationManager authorizationStatus] == kCLAuthorizationStatusRestricted;
}


-(BOOL)authenticationStatus
{
    return [CLLocationManager authorizationStatus];
}

-(CLLocation *)currentLocation
{
    return [self.location location];
}

#pragma mark helpers


#pragma mark CLLocationManagerDelegate

- (void)locationManager:(CLLocationManager *)manager
       didFailWithError:(NSError *)error
{
    // TBD
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status
{
    // TBD
}

- (void)locationManagerDidResumeLocationUpdates:(CLLocationManager *)manager
{
    // TBD
}

- (void)locationManager:(CLLocationManager *)manager
didFinishDeferredUpdatesWithError:(NSError *)error
{
    // TBD
}

@end
