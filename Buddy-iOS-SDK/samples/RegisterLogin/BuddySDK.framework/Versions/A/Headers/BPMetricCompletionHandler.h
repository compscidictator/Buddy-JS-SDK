//
//  BPMetrics.h
//  BuddySDK
//
//  Created by Erik Kerber on 2/2/14.
//
//

#import <Foundation/Foundation.h>

typedef void (^BuddyTimedMetricResult)(NSInteger elapsedTimeInMs, NSError *error);


@interface BPMetricCompletionHandler : NSObject

- (instancetype)initWithMetricId:(NSString *)metricId andClient:(id<BPRestProvider>)restProvider;
- (void)signalComplete:(BuddyTimedMetricResult)callback;

@end

typedef void (^BuddyMetricCallback)(BPMetricCompletionHandler *metricCompletionHandler, NSError *error);
