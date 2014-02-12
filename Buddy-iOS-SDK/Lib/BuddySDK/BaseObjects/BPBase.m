//
//  BPMetadataBase.m
//  BuddySDK
//
//  Created by Erik Kerber on 2/11/14.
//
//

#import "BPBase.h"
#import "BPEnumMapping.h"

@interface BPBase()<BPEnumMapping>

@end

@implementation BPBase

+ (id)convertValue:(NSString *)value forKey:(NSString *)key
{
    return nil;
}

+ (id)convertValueToJSON:(NSString *)value forKey:(NSString *)key
{
    return nil;
}

+ (NSDictionary *)mapForProperty:(NSString *)key
{
    return [self enumMap][key];
}

+ (NSDictionary *)enumMap
{
    return [self baseEnumMap];
}

+ (NSDictionary *)baseEnumMap
{
    // Return any enum->string mappings used in responses subclass.
    return @{NSStringFromSelector(@selector(readPermissions)) : @{
                     @(BuddyPermissionsApp) : @"App",
                     @(BuddyPermissionsUser) : @"User",
                     },
             NSStringFromSelector(@selector(writePermissions)) : @{
                     @(BuddyPermissionsApp) : @"App",
                     @(BuddyPermissionsUser) : @"User",
                     }};
}

- (NSString *)metadataPath:(NSString *)key
{
    return @"";
}

- (void)setMetadataWithKey:(NSString *)key andString:(NSString *)value permissions:(BuddyPermissions)permissions callback:(BuddyCompletionCallback)callback
{
    NSDictionary *parameters = @{@"value": BOXNIL(value),
                                 @"permissions": [[self class] enumMap][@"readPermissions"][@(permissions)]};
    
    [self.client PUT:[self metadataPath:key] parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(error) : nil;
    }];
}

- (void)setMetadataWithKey:(NSString *)key andInteger:(NSInteger)value permissions:(BuddyPermissions)permissions callback:(BuddyCompletionCallback)callback
{
#pragma message("Convert to 'convertValue' method from enum map")
    
    NSDictionary *parameters = @{@"value": [NSString stringWithFormat:@"%ld", (long)value],
                                 @"permissions": [[self class] enumMap][@"readPermissions"][@(permissions)]};
    
    [self.client PUT:[self metadataPath:key] parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(error) : nil;
    }];
}

- (void)setMetadataWithKey:(NSString *)key andKeyValues:(NSDictionary *)keyValuePaths permissions:(BuddyPermissions)permissions callback:(BuddyCompletionCallback)callback
{
    NSDictionary *parameters = @{@"keyValuePairs": keyValuePaths,
                                 @"permissions": [[self class] enumMap][@"readPermissions"][@(permissions)]};
    
    [self.client PUT:[self metadataPath:key] parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(error) : nil;
    }];
}

- (void)getMetadataWithKey:(NSString *)key callback:(BuddyObjectCallback)callback
{
    [self.client GET:[self metadataPath:key] parameters:nil callback:^(id metadata, NSError *error) {
        id md = nil;
        if ([NSJSONSerialization isValidJSONObject:metadata]) {
            md = metadata[@"value"];
        }
        callback ? callback(md, error) : nil;
    }];
}

- (void)deleteMetadataWithKey:(NSString *)key callback:(BuddyCompletionCallback)callback
{
    [self.client DELETE:[self metadataPath:key] parameters:nil callback:^(id metadata, NSError *error) {
        callback ? callback(error) : nil;
    }];
}

@end
