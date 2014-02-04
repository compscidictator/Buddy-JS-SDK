//
//  BPMetrics.m
//  BuddySDK
//
//  Created by Erik Kerber on 2/2/14.
//
//

#import "BPMetricCompletionHandler.h"

@interface BPMetricCompletionHandler()

@property (nonatomic, strong)NSString *metricId;
@property (nonatomic, strong)id<BPRestProvider> client;

@end

@implementation BPMetricCompletionHandler

- (instancetype)initWithMetricId:(NSString *)metricId andClient:(id<BPRestProvider>)client {
    self = [super init];
    if (self) {
        _metricId = metricId;
        _client = client;
    }
    return self;
}

- (void)signalComplete
{
    NSString *resource = [NSString stringWithFormat:@"/metrics/events/%@", self.metricId];
    [self.client DELETE:resource parameters:nil callback:nil];
}

@end
