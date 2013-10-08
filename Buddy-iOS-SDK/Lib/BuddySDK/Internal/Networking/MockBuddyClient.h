//
//  MockBuddyClient.h
//  BuddySDK
//
//  Created by Erik Kerber on 10/7/13.
//
//

#import <Foundation/Foundation.h>
#import "BuddyRequests.h"

@interface MockBuddyClient : NSObject<BuddyRequests>

-(void)getRequest;
-(void)deleteRequest;
-(void)updateRequest;

@end
