//
//  BuddyBase.m
//  BuddySDK
//
//  Created by Erik Kerber on 9/11/13.
//
//

#import "BuddyBase.h"

@interface BuddyBase()

@property (nonatomic, readwrite, assign) BOOL isDirty;

@end

@implementation BuddyBase

-(id)init
{
    self = [super init];
    if(self)
    {
        // Anything common that is dependent on the application/user whatnot?
    }
    return self;
}



-(void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
    if(change)
        self.isDirty = YES;
}

@end
