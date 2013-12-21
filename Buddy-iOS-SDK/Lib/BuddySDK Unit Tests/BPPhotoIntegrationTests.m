//
//  BPPhotoIntegrationTests.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/3/13.
//
//

#import "BPPhotoIntegrationTests.h"
#import "BUddy.h"

@implementation BPPhotoIntegrationTests

-(void)setUp{
    dispatch_semaphore_t semaphore = dispatch_semaphore_create(0);
    
    [Buddy initClient:APP_NAME appKey:APP_KEY complete:^{
        dispatch_semaphore_signal(semaphore);
    }];
    
    while (dispatch_semaphore_wait(semaphore, DISPATCH_TIME_NOW))
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode
                                 beforeDate:[NSDate dateWithTimeIntervalSinceNow:10]];
}

-(void)tearDown
{
    
}

-(void)testCreatePhoto
{
    //[BPPhoto]
}

@end
