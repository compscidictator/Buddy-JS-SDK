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

- (id)initBuddyWithSession:(BPSession*)session;
{
    self = [super initBuddyWithSession:session];
    if(self)
    {

    }
    return self;
}

static NSString *blobs = @"blobs";
+(NSString *) requestPath{
    return blobs;
}

+ (void)createWithData:(NSData *)data parameters:(NSDictionary *)parameters session:(BPSession*)session callback:(BuddyObjectCallback)callback

{
    NSDictionary *multipartParameters = @{@"data": data};
    
    [[session restService] MULTIPART_POST:[[self class] requestPath]
                                                parameters:parameters data:multipartParameters
                                                  callback:^(id json, NSError *error) {

        BuddyObject *newObject = [[[self class] alloc] initBuddyWithSession:session];

#pragma messsage("TODO - Short term hack until response is always an object.")
        if([json isKindOfClass:[NSDictionary class]]){
            newObject.id = json[@"id"];
        }else{
            newObject.id = json;
        }
        
        if(!newObject.id){
#pragma messsage("TODO - Error")
            callback(newObject, nil);
            return;
        }
        
        [newObject refresh:^(NSError *error){
#pragma messsage("TODO - Error")
            callback(newObject, nil);
        }];
        
    }];
}

@end
