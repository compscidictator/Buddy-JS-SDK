//
//  BuddyBase.h
//  BuddySDK
//
//  Created by Erik Kerber on 9/11/13.
//
//

#import <Foundation/Foundation.h>

@interface BuddyBase : NSObject

-(id)initWithExternalRepresentation:(NSDictionary *)json;

@property (nonatomic, readonly, assign) BOOL isDirty;

@end
