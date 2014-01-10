//
//  BPCheckinCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPCheckinCollection.h"
#import "BPCheckin.h"
#import "BPSession.h"
#import "BuddyObject+Private.h"

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
    
    [BPCheckin createFromServerWithParameters:parameters complete:complete];
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


// TODO - probaly move the code up to base class, then make name specific methods that the user can call.
-(void)getCheckins:(BuddyCollectionCallback)complete
{
    [self getAll:[[BPCheckin class] requestPath] complete:^(NSArray *buddyObjects) {
        NSArray *f = [buddyObjects map:^id(id object) {
            id newO = [[self.type alloc] initBuddyWithResponse:object];
            return newO;
        }];
        complete(f);
    }];
}

@end
