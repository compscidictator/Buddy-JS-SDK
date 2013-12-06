//
//  BPCheckinCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPCheckinCollection.h"


@implementation BPCheckinCollection

-(instancetype)init{
    self = [super init];
    if(self){
        self.type = [BPCheckin class];
    }
    return self;
}

-(void)checkinWithComment:(NSString *)comment
              description:(NSString *)description
                 complete:(BuddyObjectCallback)complete
{
    NSDictionary *parameters = @{@"comment": comment,
                                 @"description": description,
                                 @"location": @"1.2, 3.4"};
    
    [BPCheckin createFromServerWithParameters:parameters complete:^(BPCheckin *newBuddyObject) {
        [newBuddyObject refresh:^{
            complete(newBuddyObject);
        }];
    }];
}

-(void)checkinWithComment:(NSString *)comment
          description:(NSString *)description
      defaultMetadata:(NSString *)defaultMetadata
      readPermissions:(BuddyPermissions)readPermissions
     writePermissions:(BuddyPermissions)writePermissions
             complete:(BuddyObjectCallback)complete
{
    [NSException raise:@"NotImplementedException" format:@"Not Implemented."];
}

-(void)addCheckin:(BPCheckin *)checkin
{
    
}
@end
