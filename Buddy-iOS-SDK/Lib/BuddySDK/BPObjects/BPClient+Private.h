//
//  BPClient+Private.h
//  BuddySDK
//
//  Created by Nick Ambrose on 1/21/14.
//
//

#ifndef BuddySDK_BPClient_Private_h
#define BuddySDK_BPClient_Private_h

#import "BPClient.h"
#import "BPBase.h"

@interface BPClient (Private)<BPRestProvider>

- (void) raiseAuthError;

@end

#endif
