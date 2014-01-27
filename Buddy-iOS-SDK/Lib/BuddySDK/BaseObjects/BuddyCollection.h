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
@property (nonatomic, readonly, strong) id<BPRestProvider> client;

- (instancetype) init __attribute__((unavailable("init not available")));
+ (instancetype) new __attribute__((unavailable("new not available")));

- (instancetype)initWithClient:(id<BPRestProvider>)client;

- (void)getAll:(BuddyCollectionCallback)callback;

- (void)getItem:(NSString *)identifier callback:(BuddyObjectCallback)callback;

- (void)search:(NSDictionary *)searchParmeters callback:(BuddyCollectionCallback)callback;

@end
