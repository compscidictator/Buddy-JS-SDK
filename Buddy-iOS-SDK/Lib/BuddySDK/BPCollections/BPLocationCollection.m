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

@implementation BPLocationCollection

-(instancetype)initWithClient:(BPClient*)client{
    self = [super initWithClient:client];
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
    
    [[[self type] class] createFromServerWithParameters:parameters client:self.client callback:callback];
}


- (void)findLocationNamed:(NSString *)name
                 location:(BPLocation *)location
                 callback:(BuddyObjectCallback)callback
{
    callback ? callback(nil, nil) : nil;
}

-(void)getLocations:(BuddyCollectionCallback)callback
{
    [self getAll:callback];
}

@end
