//
//  NSDate+JSON.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/20/13.
//
//

#import "NSString+JSON.h"

#define DATE_FORMAT_REGEX @"^\\/Date\\((-?[+-]?\\d+)\\)\\/$"

@implementation NSString (JSON)
- (NSDate *)deserializeJsonDateString
{
    static NSRegularExpression *dateRegEx = nil;
    static dispatch_once_t onceToken;
    
    // NOTE: If you change this code here, make sure to also change isDate below
    dispatch_once(&onceToken, ^{
        dateRegEx = [[NSRegularExpression alloc] initWithPattern:DATE_FORMAT_REGEX
                        options:NSRegularExpressionCaseInsensitive error:nil];
    });
    
    NSTextCheckingResult *regexResult = [dateRegEx firstMatchInString:self options:0 range:NSMakeRange(0, [self length])];
    
    // Server timestamp is in msec but iOS works with seconds and fractional msec, hence the divide by 1000

    NSString *TimePortion =[self substringWithRange:[regexResult rangeAtIndex:1]];
                            
    NSTimeInterval seconds = [ TimePortion doubleValue] / 1000.0;
    return [NSDate dateWithTimeIntervalSince1970:seconds];
}

- (BOOL) isDate
{
    // NOTE: If you change this code here, make sure to also change deserializeJsonDateString above
    static NSRegularExpression *dateRegEx = nil;
    static dispatch_once_t onceToken;
    
    dispatch_once(&onceToken, ^{
        dateRegEx = [[NSRegularExpression alloc] initWithPattern:DATE_FORMAT_REGEX
                                                         options:NSRegularExpressionCaseInsensitive error:nil];
    });
    
    NSTextCheckingResult *regexResult = [dateRegEx firstMatchInString:self options:0 range:NSMakeRange(0, [self length])];
    

    return (regexResult != nil);
}
@end
