//
//  BuddyCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 10/7/13.
//
//

#import "BuddyCollection.h"
#import "BPSession.h"

@implementation BuddyCollection

-(void)getAll:(NSString *)resource complete:(BuddyCollectionCallback)complete
{
    [[[BPSession currentSession] restService] GET:resource parameters:nil callback:^(id json, NSError *error) {
        complete(json[@"pageResults"]);
    }];
}

@end
