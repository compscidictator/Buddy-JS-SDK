//
//  NSDate+JSON.h
//  BuddySDK
//
//  Created by Erik Kerber on 12/20/13.
//
//

#import <Foundation/Foundation.h>

@interface NSString (JSON)
- (NSDate *)deserializeJsonDateString;
- (BOOL) isDate;
@end
