//
//  BPBlob.h
//  BuddySDK
//
//  Created by Erik Kerber on 1/4/14.
//
//


@interface BPBlob : BuddyObject

+ (void)createWithData:(NSData *)data parameters:(NSDictionary *)parameters callback:(BuddyObjectCallback)callback;

@end
