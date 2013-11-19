//
//  BPClient.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPClient.h"
#import "AFNetworking.h"

@interface BPClient()
@property (nonatomic, strong) AFHTTPRequestOperationManager *service;
@end



@implementation BPClient

#pragma mark Initializer

-(instancetype) init
{
    self = [super self];
    if(self)
    {
        self.service = [[AFHTTPRequestOperationManager alloc] initWithBaseURL:[NSURL URLWithString:@"http://www.buddy.com/"]];
        self.service.responseSerializer = [AFJSONResponseSerializer serializer];
        self.service.requestSerializer = [AFJSONRequestSerializer serializer];
    }
    return self;
}

-(void)setupWithApp:(NSString *)appID password:(NSString *)appKey options:(NSDictionary *)options
{
    _appID = appID;
    _appKey = appKey;
}

# pragma mark -
# pragma mark Singleton
+(instancetype)defaultClient
{
    static BPClient *sharedClient = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedClient = [[self alloc] init];
    });
    return sharedClient;
}

#pragma mark BuddyObject

-(NSDictionary *)buildGetParameters
{
    return @{@"appKey": self.appKey,
             @"appID": self.appID};
}

-(void)createObjectWithPath:(NSString *)path parameters:(NSDictionary *)parameters withCallback:(BPBuddyObjectCallback) callback
{
    NSDictionary *fullParameters = [parameters dictionaryByMergingWith:[self buildGetParameters]];
    
    [self.service GET:path parameters:fullParameters success:^(AFHTTPRequestOperation *operation, id responseObject){
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}

-(void)refreshObjectWithPath:(NSString *)path parameters:(NSDictionary *)parameters withCallback:(BPBuddyObjectCallback) callback
{
    NSDictionary *fullParameters = [parameters dictionaryByMergingWith:[self buildGetParameters]];

    [self.service GET:path parameters:fullParameters success:^(AFHTTPRequestOperation *operation, id responseObject){
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}

-(void)updateObjectWithPath:(NSString *)path parameters:(NSDictionary *)parameters withCallback:(BPBuddyObjectCallback) callback
{
    NSDictionary *fullParameters = [parameters dictionaryByMergingWith:[self buildGetParameters]];

    [self.service GET:path parameters:fullParameters success:^(AFHTTPRequestOperation *operation, id responseObject){
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}

-(void)deleteObjectWithPath:(NSString *)path parameters:(NSDictionary *)parameters withCallback:(BPBuddyObjectCallback) callback
{
    NSDictionary *fullParameters = [parameters dictionaryByMergingWith:[self buildGetParameters]];

    [self.service DELETE:path parameters:fullParameters success:^(AFHTTPRequestOperation *operation, id responseObject){
        callback(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback(nil);
    }];
}

#pragma mark Non-BuddyObject Requests

-(void)ping:(BPPingCallback)callback
{
    [self.service GET:@"/ping" parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        callback([NSDecimalNumber decimalNumberWithString:@"2.0"]);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        callback([NSDecimalNumber decimalNumberWithString:@"2.0"]);
    }];
}


@end
