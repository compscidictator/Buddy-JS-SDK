//
//  BuddyCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 10/7/13.
//
//

#import <Foundation/Foundation.h>

typedef void (^BuddyCollectionCallback)(NSArray *buddyObjects, NSError *error);

@interface BuddyCollection : NSObject

@property (nonatomic) Class type;

- (void)getAll:(BuddyCollectionCallback)callback;

- (void)getItem:(NSString *)identifier callback:(BuddyObjectCallback)callback;

- (void)search:(NSDictionary *)searchParmeters callback:(BuddyCollectionCallback)callback;

@end
