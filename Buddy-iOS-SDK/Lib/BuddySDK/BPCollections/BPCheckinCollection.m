//
//  BPCheckinCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPCheckinCollection.h"
#import "BPCheckin.h"
#import "BPClient.h"
#import "BuddyCollection+Private.h"
#import "BuddyObject+Private.h"
#import "BPSisterObject.h"

@implementation BPCheckinCollection

- (instancetype)initWithClient:(id<BPRestProvider>)client {
    self = [super initWithClient:client];
    if(self){
        self.type = [BPCheckin class];
    }
    return self;
}

-(void)checkin:(DescribeCheckin)describeCheckin
      callback:(BuddyObjectCallback)callback
{
    id checkinCollection= [BPSisterObject new];
    describeCheckin ? describeCheckin(checkinCollection) : nil;

    id parameters = [checkinCollection parametersFromProperties:@protocol(BPCheckinProperties)];
    
    [self.type createFromServerWithParameters:parameters client:self.client callback:callback];
}

- (void)searchCheckins:(DescribeCheckin)describeCheckin callback:(BuddyCollectionCallback)callback;
{
    id checkinCollection= [BPSisterObject new];
    describeCheckin ? describeCheckin(checkinCollection) : nil;
    
    id parameters = [checkinCollection parametersFromProperties:@protocol(BPCheckinProperties)];
    
    [self search:parameters callback:callback];
}

- (void)getCheckin:(NSString *)checkinId callback:(BuddyObjectCallback)callback
{
    [self getItem:checkinId callback:callback];
}

@end
