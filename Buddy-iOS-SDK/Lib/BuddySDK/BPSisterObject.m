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


- (NSMethodSignature *)methodSignatureForSelector:(SEL)aSelector {
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
    NSString *argument;
    [anInvocation getArgument:&argument atIndex:2];
    if (argument) {
        NSString *setterName = NSStringFromSelector(anInvocation.selector);
        NSRange range = NSMakeRange(3, [setterName length]-4);
        NSString *propertyName = [[setterName substringWithRange:range] lowercaseString];
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

//+ (BOOL)resolveInstanceMethod:(SEL)sel
//{
//    class_addMethod([self class], sel, (IMP)genericMethodIMP, "v@:");
//    return YES;
//}
//
//void genericMethodIMP(id self, SEL _cmd)
//{
//    NSLog(@"%@ says: I finished '%@'.",
//          NSStringFromClass([self class]),
//          NSStringFromSelector(_cmd));
//}

//+ (void)load
//{
//    [[self class] synthesizeForwarder:@"comment"];
//}
//
//- (id)_getter_:(id)key
//{
//    return self.properties[key];
//}
//
//- (void)_setter_:(id)key value:(id)value
//{
//    self.properties[key] = value;
//}
//
//+(void)synthesizeForwarder:(NSString*)getterName
//{
//    NSString*setterName=[NSString stringWithFormat:@"set%@%@:",
//                         [[getterName substringToIndex:1] uppercaseString],[getterName substringFromIndex:1]];
//    Method getter=class_getInstanceMethod(self, @selector(_getter_:));
//    class_addMethod(self, NSSelectorFromString(getterName),
//                    method_getImplementation(getter), method_getTypeEncoding(getter));
//    Method setter=class_getInstanceMethod(self, @selector(_setter_:value:));
//    class_addMethod(self, NSSelectorFromString(setterName),
//                    method_getImplementation(setter), method_getTypeEncoding(setter));
//}

@end
