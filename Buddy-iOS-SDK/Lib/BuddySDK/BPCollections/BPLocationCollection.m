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

-(instancetype)initWithSession:(BPSession*)session{
    self = [super initWithSession:session];
    if(self){
        self.type = [BPLocation class];
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
                callback:(BuddyObjectCallback)callback
{
    NSDictionary *parameters = @{};
    
    [[[self type] class] createFromServerWithParameters:parameters session:self.session callback:callback];
}


- (void)findLocationNamed:(NSString *)name
                 location:(BPLocation *)location
                 callback:(BuddyObjectCallback)callback
{
    callback(nil, nil);
}

-(void)getLocations:(BuddyCollectionCallback)callback
{
    [self getAll:callback];
}

@end
