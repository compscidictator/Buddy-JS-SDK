//
//  BuddyAppDelegateDecorator.h
//  BuddySDK
//
//  Created by Tyler Vann-Campbell on 2/5/14.
//
//

#import <Foundation/Foundation.h>
#import <BPClient.h>
#import "BPAppSettings.h"

@interface BuddyAppDelegateDecorator : NSProxy <UIApplicationDelegate>
@property (strong, nonatomic) BPClient* client;
@property (strong, nonatomic) NSObject<UIApplicationDelegate> *wrapped;
@property (strong, nonatomic) BPAppSettings* settings;


+(BuddyAppDelegateDecorator*) appDelegateDecoratorWithAppDelegate:(id<UIApplicationDelegate>)wrapped client:(BPClient*) client
                                                      andSettings:(BPAppSettings*)appSettings;
@end

