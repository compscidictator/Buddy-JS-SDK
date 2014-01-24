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

- (id)initBuddyWithClient:(BPClient*)client;
{
    self = [super initBuddyWithClient:client];
    if(self)
    {

    }
    return self;
}

static NSString *blobs = @"blobs";
+(NSString *) requestPath{
    return blobs;
}

+ (void)createWithData:(NSData *)data parameters:(NSDictionary *)parameters client:(BPClient*)client callback:(BuddyObjectCallback)callback

{
    NSDictionary *multipartParameters = @{@"data": data};
    
    [[client restService] MULTIPART_POST:[[self class] requestPath]
                              parameters:parameters data:multipartParameters
                                callback:^(id json, NSError *error)
    {
        
        if(error){
            callback ? callback(nil, error) : nil;
            return;
        }
        
        BuddyObject *newObject = [[[self class] alloc] initBuddyWithClient:client];
        
        newObject.id = json[@"id"];
            callback ? callback(newObject, nil) : nil;
    
        [newObject refresh:^(NSError *error){
            callback ? callback(newObject, nil) : nil;
        }];
        
    }];
}

- (void)getData:(BuddyDataResponse)callback
{
    NSString *resource = [NSString stringWithFormat:@"%@/%@/%@", [[self class] requestPath], self.id, @"file"];
    
    [[self.client restService] GET:resource parameters:nil callback:^(id json, NSError *error) {
        NSData *data = [json data];
        callback(data, error);
    }];
}

@end
