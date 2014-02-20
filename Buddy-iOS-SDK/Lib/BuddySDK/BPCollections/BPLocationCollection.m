//
//  BPCheckinCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPLocationCollection.h"
#import "BPClient.h"
#import "BPLocation.h"
#import "BPSisterObject.h"

@implementation BPLocationCollection

- (instancetype)initWithClient:(id<BPRestProvider>)client{
    self = [super initWithClient:client];
    if(self){
        self.type = [BPLocation class];
    }
    return self;
}


- (void)addLocation:(DescribeLocation)describe
           callback:(BuddyObjectCallback)callback
{
    // TODO
}


-(void)findLocation:(BPCoordinateRange *)range callback:(BuddyCollectionCallback)callback
{
    // TODO
}

@end
