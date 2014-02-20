//
//  BPSisterObject.m
//  BuddySDK
//
//  Created by Erik Kerber on 2/5/14.
//
//

#import "BPSisterObject.h"
#import <objc/runtime.h>

@interface BPSisterObject()

@property (nonatomic, strong) NSMutableDictionary *properties;

@end

@implementation BPSisterObject

- (instancetype)init
{
    self = [super init];
    if (self) {
        _properties = [NSMutableDictionary dictionary];
    }
    return self;
}

- (void)dealloc
{
    self.properties = nil;
}
- (NSMethodSignature *)methodSignatureForSelector:(SEL)aSelector
{
    return [[self class] instanceMethodSignatureForSelector:@selector(foo:)];
}

- (id)foo:(id)key
{
    return self.properties[key];
}

- (void)setFoo:(id)key value:(id)value
{
    self.properties[key] = value;
}

- (void)forwardInvocation:(NSInvocation *)anInvocation
{
    __unsafe_unretained id argument;
    [anInvocation getArgument:&argument atIndex:2];
    if (argument) {
        NSString *setterName = NSStringFromSelector(anInvocation.selector);
        NSRange range = NSMakeRange(3, [setterName length]-4);
        NSString *propertyName = [setterName substringWithRange:range];
        propertyName = [NSString stringWithFormat:@"%@%@",[[propertyName substringToIndex:1] lowercaseString],[propertyName substringFromIndex:1]];
        
#pragma message ("Holy hack batman. It is freaking hard to determine if the argument is a primitive (enum)")
        if ((NSInteger)argument < 10) {
            argument = @((NSInteger)argument);
        }
        
        [self performSelector:@selector(setFoo:value:) withObject:propertyName withObject:argument];
    } else {
        [self performSelector:@selector(foo:) withObject:argument];
    }
}

- (id)valueForKey:(NSString *)key
{
    return self.properties[key];
}

- (void)setValue:(id)value forKey:(NSString *)key
{
    self.properties[key] = value;
}

@end
