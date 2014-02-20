//
//  BPBlob.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/4/14.
//
//

#import "BPBlob.h"
#import "BuddyObject+Private.h"
#import "Buddy.h"

@implementation BPBlob

- (instancetype)initBuddyWithClient:(id<BPRestProvider>)client {
    self = [super initBuddyWithClient:client];
    if(self)
    {

    }
    return self;
}

static NSString *blobs = @"blobs";
+(NSString *) requestPath {
    return blobs;
}

+ (void)createWithData:(NSData *)data parameters:(NSDictionary *)parameters client:(id<BPRestProvider>)client callback:(BuddyObjectCallback)callback

{
    NSDictionary *multipartParameters = @{@"data": data};
    
    [client MULTIPART_POST:[[self class] requestPath]
                parameters:parameters
                      data:multipartParameters
                  callback:^(id json, NSError *error)
    {
        
        if(error){
            callback ? callback(nil, error) : nil;
            return;
        }
        
        BuddyObject *newObject = [[[self class] alloc] initBuddyWithClient:client];
        
        newObject.id = json[@"id"];
    
        [newObject refresh:^(NSError *error){
            callback ? callback(newObject, error) : nil;
        }];
        
    }];
}

- (void)getData:(BuddyDataResponse)callback
{
    NSString *resource = [NSString stringWithFormat:@"%@/%@/%@", [[self class] requestPath], self.id, @"file"];
    
    [self.client GET_FILE:resource parameters:nil callback:^(id imageByes, NSError *error) {
        callback ? callback(imageByes, error) : nil;
    }];
}

@end
