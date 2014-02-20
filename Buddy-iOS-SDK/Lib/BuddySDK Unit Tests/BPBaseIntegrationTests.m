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
#define kKW_DEFAULT_PROBE_TIMEOUT 3.0

SPEC_BEGIN(BuddyObjectSpec)

describe(@"BuddyObjectSpec", ^{
    context(@"With a valid buddy object", ^{
        __block BPPhoto *newPhoto;
        
        beforeAll(^{
            __block BOOL fin = NO;
            
            [BuddyIntegrationHelper bootstrapLogin:^{
                fin = YES;
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
        });
        
        afterAll(^{
            
        });
        
        it(@"Should allow users to post photos", ^{
            NSBundle *bundle = [NSBundle bundleForClass:[self class]];
            NSString *imagePath = [bundle pathForResource:@"1" ofType:@"jpg"];
            UIImage *image = [UIImage imageWithContentsOfFile:imagePath];
            
            [[Buddy photos] addPhoto:image describePhoto:^(id<BPPhotoProperties> photoProperties) {
                photoProperties.caption = @"Hello, caption!";
            } callback:^(id buddyObject, NSError *error) {
                newPhoto = buddyObject;
            }];
            
            [[expectFutureValue(newPhoto) shouldEventually] beNonNil];
            [[expectFutureValue(theValue(newPhoto.contentLength)) shouldEventually] beGreaterThan:theValue(1)];
            [[expectFutureValue(newPhoto.contentType) shouldEventually] equal:@"image/png"];
            [[expectFutureValue(newPhoto.signedUrl) shouldEventually] haveLengthOfAtLeast:1];
            [[expectFutureValue(newPhoto.caption) shouldEventually] equal:@"Hello, caption!"];

        });
        
        it(@"Should allow retrieving photos", ^{
            __block BPPhoto *secondPhoto;
            [[Buddy photos] getPhoto:newPhoto.id callback:^(id newBuddyObject, NSError *error) {
                secondPhoto = newBuddyObject;
            }];
            
            [[expectFutureValue(secondPhoto) shouldEventually] beNonNil];
            [[expectFutureValue(theValue(secondPhoto.contentLength)) shouldEventually] equal:theValue(newPhoto.contentLength)];
            [[expectFutureValue(secondPhoto.contentType) shouldEventually] equal:@"image/png"];
            [[expectFutureValue([secondPhoto.signedUrl componentsSeparatedByString:@"?"][0]) shouldEventually] equal:[newPhoto.signedUrl componentsSeparatedByString:@"?"][0]];
            [[expectFutureValue(secondPhoto.caption) shouldEventually] equal:newPhoto.caption];
        });
           
        it(@"Should allow directly retrieving the image file", ^{
            __block UIImage *theImage;
            [newPhoto getImage:^(UIImage *image, NSError *error) {
                theImage = image;
            }];
            
            [[expectFutureValue(theImage) shouldEventually] beNonNil];
        });
        
        it(@"Should allow searching for images", ^{
            __block NSArray *retrievedPhotos;
            [[Buddy photos] searchPhotos:nil callback:^(NSArray *buddyObjects, NSError *error) {
                retrievedPhotos = buddyObjects;
            }];
            
            [[expectFutureValue(theValue([retrievedPhotos count])) shouldEventually] beGreaterThan:theValue(0)];
        });
        
        it(@"Should allow the user to delete photos", ^{
            [newPhoto deleteMe:^(NSError *error){
                [[Buddy photos] getPhoto:newPhoto.id callback:^(id newBuddyObject, NSError *error) {
                    newPhoto = newBuddyObject;
                }];
            }];
            
            [[expectFutureValue(newPhoto) shouldEventually] beNil];
        });
        
        
    });
});

SPEC_END

