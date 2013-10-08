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
