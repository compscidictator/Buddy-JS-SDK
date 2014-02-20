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
#define kKW_DEFAULT_PROBE_TIMEOUT 10.0

SPEC_BEGIN(BuddyPhotoSpec)

describe(@"BPPhotoIntegrationSpec", ^{
    
    context(@"When a user is NOT logged in", ^{
        
        beforeAll(^{
            [BuddyIntegrationHelper bootstrapInit];
        });
        
        it(@"Should not allow them to add and describe photos.", ^{
            __block BOOL fin = NO;
            
            NSBundle *bundle = [NSBundle bundleForClass:[self class]];
            NSString *imagePath = [bundle pathForResource:@"test" ofType:@"png"];
            UIImage *image = [UIImage imageWithContentsOfFile:imagePath];
            
            [[Buddy photos] addPhoto:image describePhoto:^(id<BPPhotoProperties>photoProperties) {
                photoProperties.caption = @"Hello, caption!";
            } callback:^(id newBuddyObject, NSError *error) {
                [[error shouldNot] beNil];
                [[newBuddyObject should] beNil];
                [[theValue([error code]) should] equal:theValue(0x107)]; // AuthUserAccessTokenRequired = 0x107
                fin = YES;
            }];
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
        });
    }),
            
    context(@"When a user is logged in", ^{
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
            NSString *imagePath = [bundle pathForResource:@"test" ofType:@"png"];
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
            [[expectFutureValue(secondPhoto.contentType) shouldEventually] equal:newPhoto.contentType];
            [[expectFutureValue([secondPhoto.signedUrl componentsSeparatedByString:@"?"][0]) shouldEventually] equal:[newPhoto.signedUrl componentsSeparatedByString:@"?"][0]];
            [[expectFutureValue(secondPhoto.caption) shouldEventually] equal:newPhoto.caption];
        });
        
        it(@"Should allow modifying photos", ^{
            __block BPPhoto *secondPhoto;
        
            newPhoto.caption = @"Some new photo caption";
            
            [newPhoto save:^(NSError *error) {
                [[error should] beNil];
                [[Buddy photos] getPhoto:newPhoto.id callback:^(id newBuddyObject, NSError *error) {
                    secondPhoto = newBuddyObject;
                }];
            }];
            

            [[expectFutureValue(secondPhoto) shouldEventually] beNonNil];
            [[expectFutureValue(secondPhoto.caption) shouldEventually] equal:@"Some new photo caption"];
        });
        
        it(@"Should allow modifying a *retrieved* photo", ^{
            __block BPPhoto *retrievedPhoto;
            [[Buddy photos] getPhoto:newPhoto.id callback:^(id newBuddyObject, NSError *error) {
                retrievedPhoto = newBuddyObject;
            }];
            
            [[expectFutureValue(retrievedPhoto) shouldEventually] beNonNil];
            
            retrievedPhoto.caption = @"Hakuna matata";
            
            [retrievedPhoto save:^(NSError *error) {
                [[error should] beNil];
                retrievedPhoto = nil;
                [[Buddy photos] getPhoto:newPhoto.id callback:^(id newBuddyObject, NSError *error) {
                    retrievedPhoto = newBuddyObject;
                }];
            }];
            
            
            [[expectFutureValue(retrievedPhoto) shouldEventually] beNonNil];
            [[expectFutureValue(retrievedPhoto.caption) shouldEventually] equal:@"Hakuna matata"];
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

            [[Buddy photos] searchPhotos:^(id<BPPhotoProperties> photoProperties) {
                photoProperties.caption = @"Hakuna matata";
            } callback:^(NSArray *buddyObjects, NSError *error) {
                NSArray *p = buddyObjects;
                
                for(BPPhoto *photo in p) {
                    [[photo.caption should] equal:@"Hakuna matata"];
                }
                retrievedPhotos = buddyObjects;
            }];
            
            [[expectFutureValue(theValue([retrievedPhotos count])) shouldEventually] beGreaterThan:theValue(0)];
        });
        
        it(@"Should allow searching for images2", ^{
            __block NSArray *retrievedPhotos;
            
            [[Buddy photos] searchPhotos:^(id<BPPhotoProperties> photoProperties) {
                photoProperties.caption = @"Hello, caption!";
            } callback:^(NSArray *buddyObjects, NSError *error) {
                retrievedPhotos = buddyObjects;
            }];
            
            [[expectFutureValue(theValue([retrievedPhotos count])) shouldEventually] equal:theValue(0)];
        });
        
        it(@"Should allow the user to delete photos", ^{
            return;
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

