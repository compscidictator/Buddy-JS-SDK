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

- (void)addLocation:(DescribeLocation)describe
           callback:(BuddyObjectCallback)callback;

-(void)findLocation:(BPCoordinateRange *)range callback:(BuddyCollectionCallback)callback;

@end
