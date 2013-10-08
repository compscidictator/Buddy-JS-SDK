//
//  BuddyHTTPClient.m
//  BuddySDK
//
//  Created by Erik Kerber on 10/2/13.
//
//

#import "BuddyHTTPClient.h"
#import "AFJSONRequestOperation.h"

@implementation BuddyHTTPClient


- (id)initWithBaseURL:(NSURL *)url
{
	self = [super initWithBaseURL:url];
	if (!self)
	{
		return nil;
	}
    
	[self registerHTTPOperationClass:[AFHTTPRequestOperation class]];
	[self setDefaultHeader:@"Accept" value:@"application/json"];
	return self;
}

-(void)oneCall
{
    
    NSMutableURLRequest *request = [self requestWithMethod:@"GET" path:path parameters:nil];
    [request setTimeoutInterval: timeout];
    
    AFJSONRequestOperation *operation = [self AFJSONRequestOperation:request success:success failure:failure];
    [self enqueueHTTPRequestOperation:operation];
}
@end
