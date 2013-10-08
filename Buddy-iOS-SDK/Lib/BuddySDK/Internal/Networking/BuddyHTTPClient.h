//
//  BuddyHTTPClient.h
//  BuddySDK
//
//  Created by Erik Kerber on 10/2/13.
//
//

#import "AFHTTPClient.h"
#import "BuddyRequest.h"

@interface BuddyHTTPClient : AFHTTPClient<BuddyRequests>

// User
-(void)getRequest;
-(void)deleteRequest;
-(void)updateRequest;
@end
