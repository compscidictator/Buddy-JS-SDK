//
//  BPBlobCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 1/7/14.
//
//

#import "BPBlobCollection.h"
#import "BPBlob.h"

@implementation BPBlobCollection

-(instancetype)initWithSession:(BPSession*)session{
    self = [super initWithSession:session];
    if(self){
        self.type = [BPBlob class];
    }
    return self;
}

- (void)addBlob:(NSData *)data
        callback:(BuddyObjectCallback)callback
{
    [BPBlob createWithData:data parameters:nil session:self.session callback:callback ];
}

-(void)getBlobs:(BuddyCollectionCallback)callback
{
    [self getAll:callback];
}

- (BPBlob *)getBlob:(NSInteger)blobId error:(NSError *)error
{
    return nil;
}

@end
