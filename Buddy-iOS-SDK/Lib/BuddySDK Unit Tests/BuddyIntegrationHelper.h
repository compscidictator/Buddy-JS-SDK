//
//  BuddyIntegrationHelper.h
//  BuddySDK
//
//  Created by Erik Kerber on 12/29/13.
//
//

#import <Foundation/Foundation.h>

@interface BuddyIntegrationHelper : NSObject

+ (void) bootstrapLogin:(void(^)())complete;

+ (NSDate *)randomDate;

+ (NSString *)randomString:(int)len;

@end
