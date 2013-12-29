//
//  BPRestProvider.h
//  BuddySDK
//
//  Created by Erik Kerber on 12/29/13.
//
//

#import <Foundation/Foundation.h>
#import <AFNetworking/AFNetworking.h>

typedef void (^AFNetworkingCallback)(id json);

@protocol BPRestProvider <NSObject>

@required
- (void)GET:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback;
- (void)POST:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback;
- (void)PATCH:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback;
- (void)DELETE:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback;

@end
