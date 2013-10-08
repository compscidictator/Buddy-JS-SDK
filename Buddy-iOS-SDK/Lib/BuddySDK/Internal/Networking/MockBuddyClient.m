//
//  MockBuddyClient.m
//  BuddySDK
//
//  Created by Erik Kerber on 10/7/13.
//
//

#import "MockBuddyClient.h"

@implementation MockBuddyClient

-(void)getRequest:(NSString *)resource withId:(NSInteger)identifier callback:(cb)callback
{
    callback(nil, nil);
}

-(void)deleteRequest:(NSString *)resource withId:(NSInteger)identifier callback:(cb)callback
{
    callback(nil, nil);
}

-(void)updateRequest:(NSString *)resource withId:(NSInteger)identifier payload:(NSDictionary *)payload callback:(cb)callback
{
    callback(nil, nil);
}

-(void)createRequest:(NSString *)resource withId:(NSInteger)identifier payload:(NSDictionary *)payload callback:(cb)callback
{
    callback(nil, nil);
}

@end
