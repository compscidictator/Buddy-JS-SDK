/*
 * Copyright (C) 2012 Buddy Platform, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

#import "BuddyApplicationStatistics.h"
#import "BuddyAppMetadata.h"
#import "BuddyAuthenticatedUser.h"
#import "BuddyBoolResponse.h"
#import "BuddyBlob.h"
#import "BuddyBlobs.h"
#import "BuddyVideo.h"
#import "BuddyVideos.h"
#import "BuddySounds.h"
#import "BuddyCallbackParams.h"
#import "BuddyCheckInLocation.h"
#import "BuddyClient.h"
#import "BuddyCommerce.h"
#import "BuddyDataResponses.h"
#import "BuddyDevice.h"
#import "BuddyEnums.h"
#import "BuddyFriendRequests.h"
#import "BuddyFriends.h"
#import "BuddyGameBoards.h"
#import "BuddyGamePlayer.h"
#import "BuddyGamePlayers.h"
#import "BuddyGameScore.h"
#import "BuddyGameScores.h"
#import "BuddyGameState.h"
#import "BuddyGameStates.h"
#import "BuddyGroupMessage.h"
#import "BuddyIdentity.h"
#import "BuddyIdentityItem.h"
#import "BuddyIdentityItemSearchResult.h"
#import "BuddyMessage.h"
#import "BuddyMessageGroup.h"
#import "BuddyMessageGroups.h"
#import "BuddyMessages.h"
#import "BuddyMetadataItem.h"
#import "BuddyMetadataSum.h"
#import "BuddyMetroArea.h"
#import "BuddyNotificationsApple.h"
#import "BuddyPhotoAlbum.h"
#import "BuddyPhotoAlbumPublic.h"
#import "BuddyPhotoAlbums.h"
#import "BuddyPicture.h"
#import "BuddyPicturePublic.h"
#import "BuddyPlace.h"
#import "BuddyPlaces.h"
#import "BuddyRegisteredDeviceApple.h"
#import "BuddyPhotoAlbum.h"
#import "BuddyPhotoAlbumPublic.h"
#import "BuddyPhotoAlbums.h"
#import "BuddyPicture.h"
#import "BuddyPicturePublic.h"
#import "BuddyPlace.h"
#import "BuddyPlaces.h"
#import "BuddyRegisteredDeviceApple.h"
#import "BuddyStartup.h"
#import "BuddyStartups.h"
#import "BuddyUser.h"
#import "BuddyUserMetadata.h"
#import "BuddyVirtualAlbum.h"
#import "BuddyVirtualAlbums.h"

@interface Buddy : NSObject

/// <summary>
/// Initializes a new instance of the BuddyClient class. To get an application username and password, go to http://buddy.com, create a new
/// developer account and create a new application.
/// </summary>
/// <param name="appName">The name of the application to use with this client. Can't be null or empty.</param>
/// <param name="appPassword">The password of the application to use with this client. Can't be null or empty.</param>
+ (void)initClient:(NSString *)name appPassword:(NSString *)password;

/// <summary>
/// Initializes a new instance of the BuddyClient class. To get an application username and password, go to http://buddy.com, create a new
/// developer account and create a new application.
/// </summary>
/// <param name="appName">The name of the application to use with this client. Can't be null or empty.</param>
/// <param name="appPassword">The password of the application to use with this client. Can't be null or empty.</param>
/// <param name="options">Optional dictionary of application options
///     appVersion: String containing the version of the app (i.e. 1.0.3)
///     autoRecordDeviceInfo: Boolean value indicating if the applciation should automatically track device information.
/// </param>
+ (void)    initClient:(NSString *)name
           appPassword:(NSString *)password
           withOptions:(NSDictionary *)options;

///<summary>
/// TODO
///</summary>
+ (void)    initClient:(NSString *)name
           appPassword:(NSString *)password
  autoRecordDeviceInfo:(BOOL)autoRecordDeviceInfo
    autoRecordLocation:(BOOL)autoRecordLocation
           withOptions:(NSDictionary *)options;

/// <summary>
/// TODO 
/// <summary>
+ (BuddyAuthenticatedUser *)user;



@end