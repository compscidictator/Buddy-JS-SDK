//
//  BuddyHTTPClient.m
//  BuddySDK
//
//  Created by Erik Kerber on 10/2/13.
//
//

#import "BuddyHTTPClient.h"

@implementation BuddyHTTPClient

-(void)oneCall
{
    NSMutableURLRequest *request = [self requestWithMethod:@"GET" path:path parameters:nil];
    [request setTimeoutInterval: timeout];
    
    AFHTTPRequestOperation *operation = [self HTTPRequestOperationWithRequest:request success:success failure:failure];
    [self enqueueHTTPRequestOperation:operation];
}
@end
