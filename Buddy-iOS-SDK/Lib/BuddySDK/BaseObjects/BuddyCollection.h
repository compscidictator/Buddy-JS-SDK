//
//  BuddyCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 10/7/13.
//
//

#import <Foundation/Foundation.h>

@class BPSession;

typedef void (^BuddyCollectionCallback)(NSArray *buddyObjects, NSError *error);

@interface BuddyCollection : NSObject

@property (nonatomic) Class type;
@property (nonatomic,readonly,strong) BPSession *session;

- (instancetype) init __attribute__((unavailable("init not available")));
+ (instancetype) new __attribute__((unavailable("new not available")));

- (instancetype)initWithSession:(BPSession*)session;

- (void)getAll:(BuddyCollectionCallback)callback;

- (void)getItem:(NSString *)identifier callback:(BuddyObjectCallback)callback;

- (void)search:(NSDictionary *)searchParmeters callback:(BuddyCollectionCallback)callback;

@end
