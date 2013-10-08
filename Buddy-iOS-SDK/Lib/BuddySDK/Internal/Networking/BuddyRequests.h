//
//  BuddyRequest.h
//  BuddySDK
//
//  Created by Erik Kerber on 10/7/13.
//
//

#import <Foundation/Foundation.h>

@protocol BuddyRequests <NSObject>

-(void)getRequest;
-(void)deleteRequest;
-(void)updateRequest;

@end
