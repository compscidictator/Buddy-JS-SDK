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

-(instancetype)initWithSession:(BPSession*)session{
    self = [super initWithSession:session];
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
    
    [BPCheckin createFromServerWithParameters:parameters session:self.session callback:callback];
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

-(void)getCheckin:(BuddyObjectCallback)callback
{
    callback(nil, nil);
}

@end
