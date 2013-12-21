//
//  NSDate+JSON.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/20/13.
//
//

#import "NSString+JSON.h"

@implementation NSString (JSON)
- (NSDate *)deserializeJsonDateString
{
    NSInteger offset = [[NSTimeZone defaultTimeZone] secondsFromGMT]; //get number of seconds to add or subtract according to the client default time zone
    
    NSInteger startPosition = [self rangeOfString:@"("].location + 1; //start of the date value
    
    NSTimeInterval unixTime = [[self substringWithRange:NSMakeRange(startPosition, 13)] doubleValue] / 1000; //WCF will send 13 digit-long value for the time interval since 1970 (millisecond precision) whereas iOS works with 10 digit-long values (second precision), hence the divide by 1000
    
    NSDate *date = [[NSDate dateWithTimeIntervalSince1970:unixTime] dateByAddingTimeInterval:offset];
    
    return date;
}

- (BOOL) isDate
{
    return [self rangeOfString:@"/Date"].location != NSNotFound;
}
@end
