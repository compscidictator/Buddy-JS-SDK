//
//  BPClientDelegate.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/22/13.
//
//

#import <Foundation/Foundation.h>
#import "BPClient.h" // TODO - Don't want to import client here.

@protocol BPClientDelegate <NSObject>

-(void)clientActivityChanged:(BOOL)isActive;

-(void)authenticationLevelChanged:(BPAuthenticationLevel)authenticationLevel;

// TODO - Probably not put this in a protocol. Rather see it in a block callback from a auth call.
-(void)authorizationFailed;

@end
