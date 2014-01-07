//
//  BPPhotoIntegrationTests.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/3/13.
//
//

#import "Buddy.h"
#import "BuddyIntegrationHelper.h"
#import <Kiwi/Kiwi.h>

SPEC_BEGIN(BuddyPhotoSpec)

describe(@"BPPhotoIntegrationSpec", ^{
    context(@"When a user is logged in", ^{
        
        beforeAll(^{
            __block BOOL fin = NO;
            
            [BuddyIntegrationHelper bootstrapLogin:^{
                fin = YES;
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventuallyBeforeTimingOutAfter(4.0)] beTrue];
        });
        
        afterAll(^{
            
        });
        
        it(@"Should allow users to post photos", ^{
#pragma message("Unit test helper method")
            NSBundle *bundle = [NSBundle bundleForClass:[self class]];
            NSString *imagePath = [bundle pathForResource:@"1" ofType:@"jpg"];
            UIImage *image = [UIImage imageWithContentsOfFile:imagePath];
            
            [[Buddy photos] addPhoto:image withComment:@"Hello, comment!" callback:^(NSArray *buddyObjects) {
                NSLog(@"%@", buddyObjects);
            }];
        });
        
        pending_(@"Should allow retrieving photos", ^{
            
        });
        
        pending_(@"Should allow searching for images", ^{
            
        });
        
        it (@"Should allow the user to delete photos", ^{
            
        });
        
        
    });
});

SPEC_END

