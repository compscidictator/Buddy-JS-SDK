//
//  BuddyLocation.m
//  BuddySDK
//
//  Created by Erik Kerber on 9/15/13.
//
//

#import "BuddyLocation.h"
#import "BPCoordinate.h"
#import <CoreLocation/CoreLocation.h>

@interface BuddyLocation()<CLLocationManagerDelegate>

@property (strong, nonatomic) CLLocationManager *location;
@property (assign, readwrite, nonatomic) BOOL isTracking;
@property (copy, nonatomic) BuddyCompletionCallback callback;

@property (nonatomic, strong) BPCoordinate *currentCoordinate;
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

-(void) beginTrackingLocation:(BuddyCompletionCallback)callback;
{
    self.callback = callback;
    
    if([CLLocationManager authorizationStatus] == kCLAuthorizationStatusDenied ||
       [CLLocationManager authorizationStatus] == kCLAuthorizationStatusRestricted)
    {
        _callback([NSError errorWithDomain:@"BuddyLocation" code:0 userInfo:@{@"message": @"Location denied."}]);
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
    if(_callback) {
        _callback([NSError errorWithDomain:@"BuddyLocation" code:0 userInfo:@{@"message": @"Location initialization failed"}]);
    }
    
    _callback = nil;
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
    if (_callback){
        _callback(nil);
    }
    _callback = nil;
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    if (!self.currentCoordinate) {
        self.currentCoordinate = [BPCoordinate new];
    }
    
    CLLocation *newLocation = [locations lastObject];
    CLLocationCoordinate2D coord = newLocation.coordinate;
    
    self.currentCoordinate.latitude = coord.latitude;
    self.currentCoordinate.longitude = coord.longitude;
    
    if (self.delegate) {
        [self.delegate didUpdateBuddyLocation:self.currentCoordinate];
    }
}

- (void)locationManager:(CLLocationManager *)manager
didFinishDeferredUpdatesWithError:(NSError *)error
{
    // TBD
    // We likely won't be doing deferred updates, so probably not necessary.
}

@end
