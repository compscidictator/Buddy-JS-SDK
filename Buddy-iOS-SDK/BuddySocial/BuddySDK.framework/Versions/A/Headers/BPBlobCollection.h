//
//  BPBlobCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 1/7/14.
//
//

@class BPBlob;

@interface BPBlobCollection : BuddyCollection

- (void)addBlob:(NSData *)data
       callback:(BuddyObjectCallback)callback;

- (void)getBlobs:(BuddyCollectionCallback)callback;

- (void)searchBlobs:(NSDictionary *)parameters callback:(BuddyCollectionCallback)callback;

@end
