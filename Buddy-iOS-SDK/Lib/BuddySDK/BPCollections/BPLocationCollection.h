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
                complete:(BuddyObjectCallback)complete;

- (void)findLocationNamed:(NSString *)name
                 location:(BPLocation *)location
                 complete:(BuddyObjectCallback)complete;
//               maxResults?

- (void)getLocations:(BuddyCollectionCallback)complete;

@end
