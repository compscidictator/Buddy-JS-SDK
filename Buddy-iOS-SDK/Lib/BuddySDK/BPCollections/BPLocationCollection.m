//
//  BPCheckinCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPLocationCollection.h"
#import "BPSession.h"
#import "BPLocation.h"

@implementation BPLocationCollection

-(instancetype)init{
    self = [super init];
    if(self){
        self.type = [BPCheckin class];
    }
    return self;
}


- (void)addLocationNamed:(NSString *)name
             description:(NSString *)description
                location:(BPLocation *)location
                 address:(NSString *)address
                    city:(NSString *)city
                   state:(NSString *)state
              postalCode:(NSString *)postalCode
                 website:(NSString *)website
              categoryId:(NSString *)categoryId
         defaultMetadata:(NSString *)defaultMetadata
         readPermissions:(BuddyPermissions)readPermissions
        writePermissions:(BuddyPermissions)writePermissions
                complete:(BuddyObjectCallback)complete
{
    NSDictionary *parameters = @{};
    
    [[[self type] class] createFromServerWithParameters:parameters complete:complete];
}


- (void)findLocationNamed:(NSString *)name
                 location:(BPLocation *)location
                 complete:(BuddyObjectCallback)complete
{
    complete(nil, nil);
}

@end
