//
//  BPClientDelegate.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/22/13.
//
//

#import <Foundation/Foundation.h>
//#import "BPClient.h" // TODO - Don't want to import client here.

@protocol BPClientDelegate <NSObject>

//-(void)clientActivityChanged:(BOOL)isActive;

//-(void)authenticationLevelChanged:(BPAuthenticationLevel)authenticationLevel;

-(void)authorizationNeedsUserLogin;

@end
