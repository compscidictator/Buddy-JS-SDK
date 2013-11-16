//
//  BPServiceController.m
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BPServiceController.h"
#import "AFNetworking.h"

@interface BPServiceController()
@property (nonatomic, strong) AFHTTPRequestOperationManager *manager;
@end

@implementation BPServiceController

-(instancetype)initWithBuddyUrl:(NSString *)url
{
    self = [super init];
    if(self)
    {
        self.manager = [[AFHTTPRequestOperationManager alloc] initWithBaseURL:[NSURL URLWithString:url]];
        
        self.manager.responseSerializer = [AFJSONResponseSerializer serializer];
    }
    return self;
    
    
}

@end
