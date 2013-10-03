//
//  AFHTTPClient+Buddy.h
//  BuddySDK
//
//  Created by Erik Kerber on 10/2/13.
//
//

#import "AFHTTPClient.h"

@interface AFHTTPClient (Buddy)

- (void)getPath:(NSString *)path
        timeout:(int) timeout
        success:(void (^)(AFHTTPRequestOperation *operation, id responseObject))success
        failure:(void (^)(AFHTTPRequestOperation *operation, NSError *error))failure;

-(void) postPath:(NSString *)path
         timeout:(int) timeout
      parameters:(NSDictionary *)parameters
         success:(void (^)(AFHTTPRequestOperation *operation, id responseObject))success
         failure:(void (^)(AFHTTPRequestOperation *operation, NSError *error))failure;

@end
