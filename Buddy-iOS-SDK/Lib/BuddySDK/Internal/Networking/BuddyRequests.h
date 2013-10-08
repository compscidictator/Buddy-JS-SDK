//
//  BuddyRequest.h
//  BuddySDK
//
//  Created by Erik Kerber on 10/7/13.
//
//

#import <Foundation/Foundation.h>
#import "BuddyCallbackParams.h"

typedef void (^cb) (BuddyCallbackParams *callbackParams, id jsonString);

@protocol BuddyRequests <NSObject>


-(void)createRequest:(NSString *)resource
              withId:(NSInteger)identifier
             payload:(NSDictionary *)payload
            callback:(cb)callback;

-(void)getRequest:(NSString *)resource
           withId:(NSInteger)identifier
         callback:(cb)callback;

-(void)deleteRequest:(NSString *)resource
              withId:(NSInteger)identifier
            callback:(cb)callback;

-(void)updateRequest:(NSString *)resource
              withId:(NSInteger)identifier
             payload:(NSDictionary *)payload
            callback:(cb)callback;

@end
