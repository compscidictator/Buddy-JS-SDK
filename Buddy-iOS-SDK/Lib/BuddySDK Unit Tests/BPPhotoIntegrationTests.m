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

SPEC_BEGIN(BuddyPhotoSpec)

describe(@"BPPhotoIntegrationSpec", ^{
    
    context(@"When a user is NOT logged in", ^{
        
        beforeAll(^{
            [BuddyIntegrationHelper bootstrapInit];
        });
        
        it(@"Should throw an auth error if they try to access photos.", ^{
            id mock = [KWMock mockForProtocol:@protocol(BPClientDelegate)];
            [Buddy setClientDelegate:mock];
#pragma message("Why the heck doesn't this always work?")
            [[[mock shouldEventually] receive] authorizationNeedsUserLogin];
            [[Buddy photos] searchPhotos:nil];
        });
        
        it(@"Should not allow them to add photos.", ^{
            __block BOOL fin = NO;

            NSBundle *bundle = [NSBundle bundleForClass:[self class]];
            NSString *imagePath = [bundle pathForResource:@"test" ofType:@"png"];
            UIImage *image = [UIImage imageWithContentsOfFile:imagePath];
            
            [[Buddy photos] addPhoto:image describePhoto:^(id<BPPhotoProperties> photoProperties) {
                photoProperties.comment = @"Hello, comment!";
            } callback:^(id buddyObject, NSError *error) {
                [[error shouldNot] beNil];
                [[buddyObject should] beNil];
                [[theValue([error code]) should] equal:theValue(0x107)]; // AuthUserAccessTokenRequired = 0x107
                fin = YES;
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
        });
        
        it(@"Should not allow them to add and describe photos.", ^{
            __block BOOL fin = NO;
            
            NSBundle *bundle = [NSBundle bundleForClass:[self class]];
            NSString *imagePath = [bundle pathForResource:@"test" ofType:@"png"];
            UIImage *image = [UIImage imageWithContentsOfFile:imagePath];
            
            [[Buddy photos] addPhoto:image describePhoto:^(id<BPPhotoProperties>photoProperties) {
                photoProperties.comment = @"Hello, comment!";
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
                photoProperties.comment = @"Hello, comment!";
            } callback:^(id buddyObject, NSError *error) {
                newPhoto = buddyObject;
            }];
            
            [[expectFutureValue(newPhoto) shouldEventually] beNonNil];
            [[expectFutureValue(theValue(newPhoto.contentLength)) shouldEventually] beGreaterThan:theValue(1)];
            [[expectFutureValue(newPhoto.contentType) shouldEventually] equal:@"image/png"];
            [[expectFutureValue(newPhoto.signedUrl) shouldEventually] haveLengthOfAtLeast:1];
            [[expectFutureValue(newPhoto.comment) shouldEventually] equal:@"Hello, comment!"];

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
            [[expectFutureValue(secondPhoto.comment) shouldEventually] equal:newPhoto.comment];
        });
        
        it(@"Should allow modifying photos", ^{
            __block BPPhoto *secondPhoto;
        
            newPhoto.comment = @"Some new photo comment";
            
            [newPhoto save:^(NSError *error) {
                [[error should] beNil];
                [[Buddy photos] getPhoto:newPhoto.id callback:^(id newBuddyObject, NSError *error) {
                    secondPhoto = newBuddyObject;
                }];
            }];
            

            [[expectFutureValue(secondPhoto) shouldEventually] beNonNil];
            [[expectFutureValue(secondPhoto.comment) shouldEventually] equal:@"Some new photo comment"];
        });
        
        it(@"Should allow modifying a *retrieved* photo", ^{
            __block BPPhoto *retrievedPhoto;
            [[Buddy photos] getPhoto:newPhoto.id callback:^(id newBuddyObject, NSError *error) {
                retrievedPhoto = newBuddyObject;
            }];
            
            [[expectFutureValue(retrievedPhoto) shouldEventually] beNonNil];
            
            retrievedPhoto.comment = @"Hakuna matata";
            
            [retrievedPhoto save:^(NSError *error) {
                [[error should] beNil];
                retrievedPhoto = nil;
                [[Buddy photos] getPhoto:newPhoto.id callback:^(id newBuddyObject, NSError *error) {
                    retrievedPhoto = newBuddyObject;
                }];
            }];
            
            
            [[expectFutureValue(retrievedPhoto) shouldEventually] beNonNil];
            [[expectFutureValue(retrievedPhoto.comment) shouldEventually] equal:@"Hakuna matata"];
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
            return;
            [[Buddy photos] searchPhotos:^(NSArray *buddyObjects, NSError *error) {
                retrievedPhotos = buddyObjects;
            }];
            
            [[expectFutureValue(theValue([retrievedPhotos count])) shouldEventually] beGreaterThan:theValue(0)];
        });
        
        it(@"Should allow searching for images2", ^{
            __block NSArray *retrievedPhotos;
            [[Buddy photos] search:@{@"comment": @"Hello, comment!"} callback:^(NSArray *buddyObjects, NSError *error) {
                retrievedPhotos = buddyObjects;
            }];
            
            [[expectFutureValue(theValue([retrievedPhotos count])) shouldEventually] beGreaterThan:theValue(0)];
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

