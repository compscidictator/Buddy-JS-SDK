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

#ifdef kKW_DEFAULT_PROBE_TIMEOUT
#undef kKW_DEFAULT_PROBE_TIMEOUT
#endif
#define kKW_DEFAULT_PROBE_TIMEOUT 4.0

SPEC_BEGIN(BuddyBlobSpec)

describe(@"BPBlobIntegrationSpec", ^{
    context(@"When a user is logged in", ^{
        
        beforeAll(^{
            __block BOOL fin = NO;
            
            [BuddyIntegrationHelper bootstrapLogin:^{
                fin = YES;
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
        });
        
        afterAll(^{
            
        });
        
        it(@"Should allow users to upload blobs", ^{
            NSBundle *bundle = [NSBundle bundleForClass:[self class]];
            NSString *blobPath = [bundle pathForResource:@"help" ofType:@"jpg"];
            NSData *blobData = [NSData dataWithContentsOfFile:blobPath];
            
            __block BPBlob *newBlob;
            [[Buddy blobs] addBlob:blobData callback:^(id newBuddyObject, NSError *error) {
                newBlob = newBuddyObject;
            }];
            
            [[expectFutureValue(newBlob) shouldEventually] beNonNil];
            [[expectFutureValue(theValue(newBlob.contentLength)) shouldEventually] equal:theValue(1)];
#pragma message("TODO - validate mime type.")
            //[[expectFutureValue(newBlob.contentType) shouldEventually] equal:@"image/png"];
            [[expectFutureValue(newBlob.signedUrl) shouldEventually] haveLengthOfAtLeast:1];
            
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

