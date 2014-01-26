//
//  BPCheckinCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BuddyCollection.h"
#import "BPLocation.h"
#import "BPCoordinate.h"

@interface BPLocationCollection : BuddyCollection

- (void)addLocationNamed:(NSString *)name
             description:(NSString *)description
                location:(BPLocation *)location
                 address:(NSString *)address
                    city:(NSString *)city
                   state:(NSString *)state
              postalCode:(NSString *)postalCode
                 website:(NSString *)website
              categoryId:(NSString *)categoryId
         defaultMetadata:(NSString *)defaultMetadata
         readPermissions:(BuddyPermissions)readPermissions
        writePermissions:(BuddyPermissions)writePermissions
                callback:(BuddyObjectCallback)callback;

- (void)findLocationNamed:(NSString *)name
                 location:(BPLocation *)location
                 callback:(BuddyObjectCallback)callback;
//               maxResults?

- (void)getLocations:(BuddyCollectionCallback)callback;

@end
