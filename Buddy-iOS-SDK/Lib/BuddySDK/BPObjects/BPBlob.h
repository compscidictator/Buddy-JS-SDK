//
//  BPBlob.h
//  BuddySDK
//
//  Created by Erik Kerber on 1/4/14.
//
//


@interface BPBlob : BuddyObject

typedef void(^BuddyDataResponse)(NSData *data, NSError *error);

@property (nonatomic, assign) NSInteger contentLength;
@property (nonatomic, copy) NSString *contentType;
@property (nonatomic, copy) NSString *signedUrl;
//@property (nonatomic, copy) NSString *size;

+ (void)createWithData:(NSData *)data parameters:(NSDictionary *)parameters client:(BPClient*)client callback:(BuddyObjectCallback)callback;

- (void)getData:(BuddyDataResponse)callback;

@end
