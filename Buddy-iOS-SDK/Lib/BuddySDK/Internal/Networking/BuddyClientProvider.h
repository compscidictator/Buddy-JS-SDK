//
//  BuddyClientProvider.h
//  BuddySDK
//
//  Created by Erik Kerber on 10/7/13.
//
//

#import <Foundation/Foundation.h>
#import "BuddyRequests.h"

@interface BuddyClientProvider : NSObject

+(id<BuddyRequests>)sharedClient;

@end
