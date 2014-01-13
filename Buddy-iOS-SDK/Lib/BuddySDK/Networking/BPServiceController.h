//
//  BPServiceController.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import <Foundation/Foundation.h>
#import "BPRestProvider.h"

@interface BPServiceController : NSObject <BPRestProvider>

-(instancetype) init __attribute__((unavailable("Use initWithUrl:")));
+(instancetype) new __attribute__((unavailable("Use with initWithUrl:")));

-(instancetype)initWithBuddyUrl:(NSString *)url;

@property (nonatomic, readonly, retain) NSString *appID;
@property (nonatomic, readonly, retain) NSString *appKey;

-(void)setAppID:(NSString *)appID withKey:(NSString *)appKey callback:(RESTCallback)callback;

@end
