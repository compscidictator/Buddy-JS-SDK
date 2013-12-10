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
@property (assign, readwrite, nonatomic) BOOL isTracking;
@property (copy, nonatomic) void (^success)();
@property (copy, nonatomic) void (^fail)();

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

-(void) beginTrackingLocation:(void (^)())success andFailure:(void(^)())failure
{
    // Copy the callback.
    self.success = success;
    self.fail = failure;
    
    if([CLLocationManager authorizationStatus] == kCLAuthorizationStatusDenied ||
       [CLLocationManager authorizationStatus] == kCLAuthorizationStatusRestricted)
    {
        failure();
    }
    else
    {
        [self.location startUpdatingLocation];
    }
}

-(void) endTrackingLocation
{
    [self.location stopUpdatingLocation];
    self.isTracking = NO;
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
    if(self.fail)
        self.fail();
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status
{
    if(status != kCLAuthorizationStatusAuthorized)
    {
        self.isTracking = NO;
    }
    else
    {
        self.isTracking = YES;
    }
}

- (void)locationManagerDidResumeLocationUpdates:(CLLocationManager *)manager
{
    if(self.success){
        self.success();
        self.success = nil;
        self.fail = nil;
    }
    
}

- (void)locationManager:(CLLocationManager *)manager
didFinishDeferredUpdatesWithError:(NSError *)error
{
    // TBD
    // We likely won't be doing deferred updates, so probably not necessary.
}

@end
