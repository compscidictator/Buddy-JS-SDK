//
//  BPEnumMapping.h
//  BuddySDK
//
//  Created by Erik Kerber on 1/12/14.
//
//

#import <Foundation/Foundation.h>

@protocol BPEnumMapping <NSObject>

+ (NSDictionary *)mapForProperty:(NSString *)key;
+ (id)convertValue:(NSString *)value forKey:(NSString *)key;

@end
