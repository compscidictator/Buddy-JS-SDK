//
//  BPCheckinCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BuddyCollection.h"
#import "BPCheckin.h"
#import "BPCoordinate.h"

@interface BPCheckinCollection : BuddyCollection

- (void)checkin:(DescribeCheckin)describeCheckin
       callback:(BuddyObjectCallback)callback;

- (void)searchCheckins:(DescribeCheckin)describeCheckin callback:(BuddyCollectionCallback)callback;

- (void)getCheckin:(NSString *)checkinId callback:(BuddyObjectCallback)callback;

@end
