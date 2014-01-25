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
#import "BuddyObject+Private.h"

@implementation BPCheckinCollection

-(instancetype)initWithClient:(BPClient*)client{
    self = [super initWithClient:client];
    if(self){
        self.type = [BPCheckin class];
    }
    return self;
}

-(void)checkinWithComment:(NSString *)comment
              description:(NSString *)description
                 callback:(BuddyObjectCallback)callback
{
    NSDictionary *parameters = @{@"comment": comment,
                                 @"description": description,
                                 @"location": @"1.2, 3.4"};
    
    [self.type createFromServerWithParameters:parameters client:self.client callback:callback];
}

-(void)checkinWithComment:(NSString *)comment
          description:(NSString *)description
      defaultMetadata:(NSString *)defaultMetadata
      readPermissions:(BuddyPermissions)readPermissions
     writePermissions:(BuddyPermissions)writePermissions
             callback:(BuddyObjectCallback)callback
{
    [NSException raise:@"NotImplementedException" format:@"Not Implemented."];
}

-(void)getCheckins:(BuddyCollectionCallback)callback
{
    [self getAll:callback];
}

- (void)getCheckin:(NSString *)checkinId callback:(BuddyObjectCallback)callback
{
    [self getItem:checkinId callback:callback];
}

@end
