//
//  BuddyAppDelegateDecorator.m
//  BuddySDK
//
//  Created by Tyler Vann-Campbell on 2/5/14.
//
//

#import "BuddyAppDelegateDecorator.h"
#import "BuddyDevice.h"

@implementation BuddyAppDelegateDecorator

+ (BuddyAppDelegateDecorator*) appDelegateDecoratorWithAppDelegate:(id<UIApplicationDelegate>)wrapped client:(BPClient *)client andSettings:(BPAppSettings *)appSettings{
    BuddyAppDelegateDecorator* decorator = [BuddyAppDelegateDecorator alloc];
    decorator.wrapped = wrapped;
    decorator.client = client;
    decorator.settings = appSettings;
    return decorator;
}

- (void) forwardInvocation:(NSInvocation *)invocation {
    [invocation setTarget:self.wrapped];
    [invocation invoke];
}

- (NSMethodSignature*) methodSignatureForSelector:(SEL)sel{
    return [self.wrapped methodSignatureForSelector:sel];
}

- (BOOL) respondsToSelector:(SEL)aSelector{
    const char* selectorName;
    const char* didRegisterForRemote = "application:didRegisterForRemoteNotificationsWithDeviceToken:";
    const char* didReceiveRemoteNote = "application:didReceiveRemoteNotification:";
    selectorName = sel_getName(aSelector);
    if(strcmp(selectorName, didRegisterForRemote) == 0 || strcmp(selectorName, didReceiveRemoteNote) == 0){
        return YES;
    }
    return [self.wrapped respondsToSelector:aSelector];
}

- (void) application:(UIApplication*)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
    //hook this method and send the results to buddy
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    NSString* rawDeviceTokenHex = [[deviceToken description] stringByTrimmingCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@"<>"]];

    [BuddyDevice pushToken:[rawDeviceTokenHex stringByReplacingOccurrencesOfString:@" " withString:@""]];
    
    if(![self.wrapped respondsToSelector:@selector(application:didRegisterForRemoteNotificationsWithDeviceToken:)]){
        return;
    }
    return [self.wrapped application:application didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
}


@end
