//
//  NSDate+JSON.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/9/14.
//
//

#import "NSDate+JSON.h"

@implementation NSDate (JSON)

- (NSString *)serializeDateToJson;
{
    return [NSString stringWithFormat:@"/Date(%lld)/",
     (long long)([self timeIntervalSince1970] * 1000)];
}

@end
