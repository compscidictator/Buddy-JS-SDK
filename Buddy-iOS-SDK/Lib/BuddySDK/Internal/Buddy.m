//
//  Buddy.m
//  BuddySDK
//
//  Created by Erik Kerber on 9/11/13.
//
//

#import "Buddy.h"
#import "BuddyUtility.h"
#import "BuddyDevice.h"

/* TODO - appInfo container class? */
static NSString *applicationName;
static NSString *applicationPassword;
static NSString *applicationVersion;

@implementation Buddy


+ (void)setApplicationName:(NSString *)name withPassword:(NSString *)password
{
    
	[Buddy setApplicationName:name withPassword:password options:nil];
    
	//recordDeviceInfo = true;
}

+ (void)setApplicationName:(NSString *)name withPassword:(NSString *)password options:(NSDictionary *)options
{
    if (name == nil || name.length == 0)
	{
		[BuddyUtility throwNilArgException:@"BuddyClient" reason:@"appName"];
	}
    
	if ([password length] == 0)
	{
		[BuddyUtility throwNilArgException:@"BuddyClient" reason:@"appPassword"];
	}

    applicationVersion = [options[@"appVersion"] count] > 0 ? options[@"appVersion"] : @"1.0";
    
	applicationName = name;
	applicationPassword = password;
    
    /* TODO - I don't believe we should couple features in the setup. */
	//_device = [[BuddyDevice alloc] initWithClient:self];
	//_gameBoards = [[BuddyGameBoards alloc] initWithClient:self];
	//_metadata = [[BuddyAppMetadata alloc] initWithClient:self];
    //_sounds = [[BuddySounds alloc] initSounds:self];
}

@end
