//
//  BPServiceController.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <Foundation/Foundation.h>

typedef void (^AFNetworkingCallback)(id json);

@interface BPServiceController : NSObject

-(instancetype) init __attribute__((unavailable("Use initWithUrl:")));
+(instancetype) new __attribute__((unavailable("Use with initWithUrl:")));

-(instancetype)initWithBuddyUrl:(NSString *)url;

@property (nonatomic, readonly, retain) NSString *appID;
@property (nonatomic, readonly, retain) NSString *appKey;

-(void)setAppID:(NSString *)appID withKey:(NSString *)appKey complete:(void (^)())complete;

-(void)createBuddyObject:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback;

-(void)getBuddyObject:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback;

-(void)deleteBuddyObject:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback;

-(void)updateBuddyObject:(NSString *)servicePath parameters:(NSDictionary *)parameters callback:(AFNetworkingCallback)callback;

-(void)GET:(NSString *)servicePath parameters:(NSDictionary *)parameters success:(AFNetworkingCallback)callback;

@end
