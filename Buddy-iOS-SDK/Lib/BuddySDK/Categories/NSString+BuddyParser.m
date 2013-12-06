//
//  NSString+BuddyParser.m
//  BuddySDK
//
//  Created by Erik Kerber on 12/5/13.
//
//

#import "NSString+BuddyParser.h"

@implementation NSString (BuddyParser)
-(NSString *)stripBuddyId
{
    return [[self componentsSeparatedByString:@"/"] lastObject];
}
@end
