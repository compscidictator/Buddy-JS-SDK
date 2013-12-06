//
//  TestHelper.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/5/13.
//
//

#import "TestHelper.h"
@interface TestHelper()
@property (nonatomic, strong) dispatch_semaphore_t semaphore;
@end

@implementation TestHelper

-(instancetype)init
{
    self = [super init];
    if(self)
    {
        _semaphore = dispatch_semaphore_create(0);
    }
    return self;
}

-(void)signalDone
{
    dispatch_semaphore_signal(_semaphore);
}

-(void)wait
{
    // Run loop
    while (dispatch_semaphore_wait(_semaphore, DISPATCH_TIME_NOW))
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode
                                 beforeDate:[NSDate dateWithTimeIntervalSinceNow:10]];
}

@end
