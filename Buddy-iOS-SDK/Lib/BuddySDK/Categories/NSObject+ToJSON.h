//
//  NSObject+ToJSON.h
//  BuddySDK
//
//  Created by Erik Kerber on 2/6/14.
//
//

#import <Foundation/Foundation.h>
#import "BuddyObject+Private.h"

@interface NSObject (ToJSON)

- (NSDictionary *)parametersFromProperties:(Protocol *)protocol;

@end
