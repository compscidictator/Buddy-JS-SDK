//
//  AFHTTPClient+Buddy.m
//  BuddySDK
//
//  Created by Erik Kerber on 10/2/13.
//
//

#import "AFHTTPClient+Buddy.h"
#import "BuddyFile.h"

@implementation AFHTTPClient (Buddy)

//added by Buddy
- (void)getPath:(NSString *)path
        timeout:(int) timeout
        success:(void (^)(AFHTTPRequestOperation *operation, id responseObject))success
        failure:(void (^)(AFHTTPRequestOperation *operation, NSError *error))failure
{
	NSMutableURLRequest *request = [self requestWithMethod:@"GET" path:path parameters:nil];
    [request setTimeoutInterval: timeout];
    
    AFHTTPRequestOperation *operation = [self HTTPRequestOperationWithRequest:request success:success failure:failure];
    [self enqueueHTTPRequestOperation:operation];
}

//added by Buddy



-(void) postPath:(NSString *)path
         timeout:(int) timeout
      parameters:(NSDictionary *)parameters
         success:(void (^)(AFHTTPRequestOperation *operation, id responseObject))success
         failure:(void (^)(AFHTTPRequestOperation *operation, NSError *error))failure
{
    
    // look for any NSData parameters.
    //
    BOOL multipart = false;
    for (NSString* key in parameters) {
        NSObject* value = [parameters objectForKey:key];
        
        if ([value isKindOfClass:[BuddyFile class]]){
            multipart = true;
            break;
        }
    }
    
	NSMutableURLRequest *request = nil;
    
    if (!multipart){
        request = [self requestWithMethod:@"POST" path:path parameters:parameters];
    }
    else {
        request = [self multipartFormRequestWithMethod:@"POST" path:path parameters:parameters constructingBodyWithBlock:nil];
    }
    [request  setTimeoutInterval: timeout];
    AFHTTPRequestOperation *operation = [self HTTPRequestOperationWithRequest:request success:success failure:failure];
    [self enqueueHTTPRequestOperation:operation];
}

//added by Buddy


@end
