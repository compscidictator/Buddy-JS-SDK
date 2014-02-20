//
//  CheckinIntegrationTests.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/2/13.
//
//

#import "Buddy.h"
#import "BuddyIntegrationHelper.h"
#import <Kiwi/Kiwi.h>

#ifdef kKW_DEFAULT_PROBE_TIMEOUT
#undef kKW_DEFAULT_PROBE_TIMEOUT
#endif
#define kKW_DEFAULT_PROBE_TIMEOUT 4.0

SPEC_BEGIN(MetricsSpec)

describe(@"Metrics", ^{
    context(@"When an app has a valid device token", ^{
        
        beforeAll(^{
            [Buddy initClient:APP_NAME appKey:APP_KEY];
        });
        
        afterAll(^{
        });
        
        it(@"Should allow recording untimed metrics", ^{
            __block BOOL fin = NO;

            NSDictionary *myVals = @{@"Foo": @"Bar"};
            
            [Buddy recordMetric:@"MetricKey" andValue:myVals callback:^(NSError *error) {
                [[error should] beNil];
                fin = YES;
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
        });
        
        it(@"Should allow recording timed metrics", ^{
            __block BOOL fin = NO;
            NSDictionary *myVals = @{@"Foo": @"Bar"};

            [Buddy recordTimedMetric:@"MetricKey" andValue:myVals timeout:10 callback:^(BPMetricCompletionHandler *completionHandler, NSError *error) {
                [[error should] beNil];
                [completionHandler signalComplete:^(NSInteger elapsedTimeInMs, NSError *error) {
                    [[theValue(elapsedTimeInMs) should] beGreaterThan:theValue(0)];
                    [[error should] beNil];
                    fin = YES;
                }];
            }];
            
            [[expectFutureValue(theValue(fin)) shouldEventually] beTrue];
        });
    });
});

SPEC_END
