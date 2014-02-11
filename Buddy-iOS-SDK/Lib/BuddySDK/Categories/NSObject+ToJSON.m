//
//  NSObject+ToJSON.m
//  BuddySDK
//
//  Created by Erik Kerber on 2/6/14.
//
//

#import "NSObject+ToJSON.h"
#import "BPEnumMapping.h"
#import <objc/runtime.h>

@implementation NSObject (ToJSON)

- (NSDictionary *)parametersFromProperties:(Protocol *)protocol
{
    NSMutableDictionary *parameters = [NSMutableDictionary dictionary];
    
    @autoreleasepool {
        unsigned int numberOfProperties = 0;
        objc_property_t *propertyArray = protocol_copyPropertyList(protocol, &numberOfProperties);
        for (NSUInteger i = 0; i < numberOfProperties; i++)
        {
            objc_property_t property = propertyArray[i];
            NSString *name = [[NSString alloc] initWithUTF8String:property_getName(property)];
            NSString *attributesString = [[NSString alloc] initWithUTF8String:property_getAttributes(property)];
            NSLog(@"Property %@ attributes: %@", name, attributesString);
            id val = [self valueForKey:name];
            
            if ([val respondsToSelector:@selector(stringValue)]) {
                val = [val stringValue];
            }
            
            if (val) {
                parameters[name] = val;
            }
        }
        free(propertyArray);
    }
    return parameters;
}

@end
