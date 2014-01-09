//
//  BuddyCollection.m
//  BuddySDK
//
//  Created by Erik Kerber on 10/7/13.
//
//

#import "BuddyCollection.h"
#import "BPClient.h"

@implementation BuddyCollection

-(void)getAll:(NSString *)resource complete:(BuddyCollectionCallback)complete
{
    [[[BPClient defaultClient] restService] GET:resource parameters:nil callback:^(id json, NSError *error) {
        complete(json[@"pageResults"]);
    }];
}

@end
