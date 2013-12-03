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

-(void)addWithComment:(NSString *)comment
          description:(NSString *)description
             location:(struct BPCoordinate)coordinate
      defaultMetadata:(NSString *)defaultMetadata
      readPermissions:(BuddyPermissions)readPermissions
     writePermissions:(BuddyPermissions)writePermissions
{
    
}
@end
