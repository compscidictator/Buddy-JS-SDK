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

package com.buddy.sdk;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;

import android.net.Uri;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.json.responses.UserDataResponse;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.utils.Constants;
import com.buddy.sdk.utils.Constants.UserGender;
import com.buddy.sdk.utils.Constants.UserStatus;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a user that has been authenticated with the Buddy Platform. Use
 * this object to interact with the service on behalf of the user.
 * <p>
 * 
 * <pre>
 * {@code private AuthenticatedUser user = null;}
 * {@code private AuthenticatedUser user2 = null;}
 * 
 *    {@code BuddyClient client = new BuddyClient("APPNAME", "APPPASS");}
 *    {@code client.createUser("username", "password", new OnCallback<Response<AuthenticatedUser>>()}
 *    <code>{</code>
 *        {@code public void OnResponse(Response<AuthenticatedUser> response, Object state)}
 *        <code>{</code>
 *            {@code user = response.getResult();}
 *        <code>}</code>
 *    <code>});</code>
 *    
 *    {@code client.login("username", "password", new OnCallback<Response<AuthenticatedUser>>()}
 *    <code>{</code>
 *        {@code public void OnResponse(Response<AuthenticatedUser> response, Object state)}
 *        <code>{</code>
 *            {@code user = response.getResult();}
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class AuthenticatedUser extends User {
    private UserDataModel userDataModel = null;
    private PicturesDataModel pictureDataModel = null;
    private GamePlayers gamePlayers;

    private String email;
    private Boolean locationFuzzing;
    private Boolean celebrityMode;

    private String userToken;
    private NotificationsAndroid pushNotifications;
    private PhotoAlbums photoAlbums;
    private Identity identity;
    private VirtualAlbums virtualAlbums;
    private Messages messages;
    private Places places;
    private UserMetadata metaData;
    private Friends friends;
    private Commerce commerce;
    private Startups startups;
    private Blobs blobs;
    private Videos videos;

    AuthenticatedUser(BuddyClient client, String userToken, UserDataResponse.UserData profile) {
        super(client, Integer.valueOf(profile.userID));

        if (Utils.isNullOrEmpty(userToken))
            throw new IllegalArgumentException("userToken can't be null or empty.");
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");

        this.userToken = userToken;
        this.pushNotifications = new NotificationsAndroid(this.client, this);

        this.userDataModel = new UserDataModel(this.client, this);
        this.baseUserDataModel = new BaseUserDataModel(this.client, this);
        this.pictureDataModel = new PicturesDataModel(this.client, this);

        this.photoAlbums = new PhotoAlbums(this.client, this);

        this.identity = new Identity(this.client, this.userToken);
        this.virtualAlbums = new VirtualAlbums(this.client, this);

        this.messages = new Messages(this.client, this);
        this.places = new Places(this.client, this);

        this.metaData = new UserMetadata(this.client, this.userToken);
        this.gamePlayers = new GamePlayers(this.client, this);
        this.friends = new Friends(this.client, this);
        this.commerce = new Commerce(this.client, this);
        this.startups = new Startups(this.client, this);

        this.blobs = new Blobs(this.client, this);
        this.videos = new Videos(this.client, this);
        
        this.updateProfile(profile);
    }

    protected String TokenOrId() {
        return this.userToken;
    }

    /**
     * Gets the collection of identity values for the user.
     */
    public Identity getIdentityValues() {
        return identity;
    }

    /**
     * Gets the unique user token that is the secret used to login this user.
     * Each user has a unique ID, a secret user token and a user/pass
     * combination.
     */
    public String getToken() {
        return this.userToken;
    }

    /**
     * Gets an object that can be used to register a device for push
     * notifications, send notifications or query the state of devices and
     * groups.
     */
    public NotificationsAndroid getPushNotifications() {
        return this.pushNotifications;
    }

    /**
     * Gets the collection of photo albums for this user. Note that the actual
     * album information is loaded on demand when you call the All or Get
     * method.
     */
    public PhotoAlbums getPhotoAlbums() {
        return this.photoAlbums;
    }

    /**
     * Gets the collection of virtual albums for this users. All virtual albums
     * are owned by a single user, however any user may add existing photos to
     * the album. Only the owner of the virtual album can delete the album.
     */
    public VirtualAlbums getVirtualAlbums() {
        return this.virtualAlbums;
    }

    /**
     * Gets an object that can be used to send or receive messages, create
     * message groups, etc.
     */
    public Messages getMessages() {
        return this.messages;
    }

    /**
     * Gets an object that can be user for search for locations around the user
     * (places, not other users).
     */
    public Places getPlaces() {
        return this.places;
    }

    /**
     * Gets the collection of user metadata. Note that the actual metadata is
     * loaded on demand when you call the All or Get method.
     */
    public UserMetadata getMetadata() {
        return this.metaData;
    }

    /**
     * Add and remove GamePlayers for this user.
     */
    public GamePlayers getGamePlayers() {
        return gamePlayers;
    }

    /**
     * Gets the collection of friends for this user. Note that the actual
     * friends information is loaded on demand when you call the All or Get
     * method.
     */
    public Friends getFriends() {
        return friends;
    }

    /**
     * Gets an object that can be used for commerce for the user.
     */
    public Commerce getCommerce() {
    	return commerce;
    }
    
    /**
     * Gets an object that can be used for search for startups around the user (startups, not other users).
     */
    public Startups getStartups() {
    	return startups;
    }

        
    /**
     * Gets an object that can be used for upload search and management of Blobs for the user.
	 */
    public Blobs getBlobs(){
    	return blobs;
    }
        
    /**
     * Gets an object that can be used for upload search and management of Videos for the user.
     */
    public Videos getVideos(){
    	return videos;
    }
    
    /**
     * Gets the email of the user. Can be an empty string or null.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets whether location fuzzing is enabled. When enabled any reported
     * locations for this user will be randomized for a few miles. This is a
     * security feature that makes it difficult for users to track each other.
     */
    public Boolean getLocationFuzzing() {
        return this.locationFuzzing;
    }

    /**
     * Gets whether celebrity mode is enabled for this user. When enabled the
     * user will be hidden from all searches in the system.
     */
    public Boolean getCelebrityMode() {
        return this.celebrityMode;
    }

    /**
     * Delete this user.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void delete(Object state, final OnCallback<Response<Boolean>> callback) {
        if (this.userDataModel != null) {
            this.userDataModel.delete(this.id, state, callback);
        }
    }

    /**
     * Add a profile photo for this user.
     * 
     * @param blob raw byte data of the picture.
     * @param applicationTag An optional tag for the photo.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void addProfilePhoto(byte[] blob, String applicationTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        if (this.userDataModel != null) {
            this.userDataModel.addProfilePhoto(blob, applicationTag, state, callback);
        }
    }

    /**
     * Add a profile photo for this user.
     * 
     * @param blob raw byte data of the picture.
     * @param callback The async callback to call on success or error.
     */
    public void addProfilePhoto(byte[] blob, final OnCallback<Response<Boolean>> callback) {
        addProfilePhoto(blob, "", null, callback);
    }

    /**
     * Delete a profile photo for this user. You can use the GetProfilePhotos
     * method to retrieve all the profile photos.
     * 
     * @param picture The photo to delete.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void deleteProfilePhoto(PicturePublic picture, Object state,
            final OnCallback<Response<Boolean>> callback) {
    	if (picture == null)
    		throw new IllegalArgumentException("picture can't be null.");
    	
        if (this.userDataModel != null) {
            this.userDataModel.deleteProfilePhoto(picture, state, callback);
        }
    }

    /**
     * Set a new "active" profile photo from the list of profile photos that the
     * user has uploaded. The photo needs to be already uploaded.
     * 
     * @param picture The photo to set as the "active" profile photo.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void setProfilePhoto(PicturePublic picture, Object state,
            OnCallback<Response<Boolean>> callback) {
    	if (picture == null)
    		throw new IllegalArgumentException("picture can't be null.");

    	if (this.userDataModel != null) {
            this.userDataModel.setProfilePhoto(picture, state, callback);
        }
    }

    /**
     * Update the profile of this user.
     * 
     * @param name Optional new name for the user, can't be null or empty.
     * @param password Optional new password for the user, can't be null.
     * @param gender Optional new gender for the user.
     * @param age An optional age for the user.
     * @param email An optional new email for the user.
     * @param status An optional new status for the user.
     * @param fuzzLocation Optional change in location fuzzing for this user. If
     *            location fuzzing is enable, user location will be randomized
     *            in all searches by other users.
     * @param celebrityMode >Optional change in celebrity mode for this user. If
     *            celebrity mode is enabled the user will be hidden from all
     *            searches in the system.
     * @param appTag Optional update to the custom application tag for this
     *            user.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void update(String name, String password, UserGender gender, int age, String email,
            UserStatus status, Boolean fuzzLocation, Boolean celebrityMode, String appTag,
            Object state, final OnCallback<Response<Boolean>> callback) {
        if (this.userDataModel != null) {
            this.userDataModel.update(name, password, gender, age, email, status, fuzzLocation,
                    celebrityMode, appTag, state, callback);
        }
    }

    private void updateProfile(UserDataResponse.UserData profile) {
        this.name = profile.userName;
        this.id = Integer.valueOf(profile.userID);
        if(Utils.isNullOrEmpty(profile.userGender)) {
        	this.gender = UserGender.any;
        } else {
        	this.gender = UserGender.valueOf(profile.userGender);
        }
        this.applicationTag = profile.applicationTag;
        this.latitude = Utils.parseDouble(profile.userLatitude);
        this.longitude = Utils.parseDouble(profile.userLongitude);
        this.lastLoginOn = Utils.convertDateString(profile.lastLoginDate);
        try {
            URL profileUrl = new URL(profile.profilePictureURL);
            this.profilePicture = Uri.parse(profileUrl.toURI().toString());
        } catch (MalformedURLException e) {
            this.profilePictureId = profile.profilePictureURL;
        } catch (URISyntaxException e) {
            this.profilePictureId = profile.profilePictureURL;
        }
        this.createdOn = Utils.convertDateString(profile.createdDate);
        this.status = Utils.getUserStatus(Integer.valueOf(profile.statusID));
        this.age = Integer.valueOf(profile.age);
        this.email = profile.userEmail;
        this.locationFuzzing = Boolean.valueOf(profile.locationFuzzing);
        this.celebrityMode = Boolean.valueOf(profile.celebMode);
    }

    /**
     * Check-in the user at a location.
     * 
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param comment An optional comment for the check-in.
     * @param appTag An optional application specific tag for the location.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void checkIn(double latitude, double longitude, String comment, String appTag,
            Object state, final OnCallback<Response<Boolean>> callback) {
        if (this.userDataModel != null) {
            this.userDataModel.checkIn(latitude, longitude, comment, appTag, state, callback);
        }
    }

    /**
     * Check-in the user at a location.
     * 
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param callback The async callback to call on success or error.
     */
    public void checkIn(double latitude, double longitude, Object state, 
            final OnCallback<Response<Boolean>> callback) {
        checkIn(latitude, longitude, "", "", null, callback);
    }

    /**
     * Get a list of user check-in locations.
     * 
     * @param afterDate Filter the list to return only check-in after a date.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getCheckIns(Date afterDate, Object state,
            final OnCallback<ListResponse<CheckInLocation>> callback) {
        if (this.userDataModel != null) {
            this.userDataModel.getCheckIns(Utils.convertMinDateStringTo1950(afterDate), state, callback);
        }
    }

    /**
     * Get a list of user check-in locations.
     * 
     * @param callback The async callback to call on success or error.
     */
    public void getCheckIns(final OnCallback<ListResponse<CheckInLocation>> callback) {
        getCheckIns(Constants.MinDate, null, callback);
    }

    /**
     * Retrieve a picture by its unique ID. Any picture that the user owns or is
     * publicly available can be retrieved.
     * 
     * @param pictureId The id of the picture to retrieve.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getPicture(int pictureId, Object state, OnCallback<Response<Picture>> callback) {
    	if (pictureId < 0)
    		throw new IllegalArgumentException("pictureId can't be smaller than 0");
    	
    	if (this.userDataModel != null) {
            this.userDataModel.getPicture(pictureId, state, callback);
        }
    }

    /**
     * Returns the user account associated with the specified UserName.
     * 
     * @param the UserName of the user to return, can't be null or empty.
     * @param callback The async callback to call on success or error.
     */
    public void findUser(String userNameToFetch, final OnCallback<Response<User>> callback) {
    	if(userNameToFetch == null || userNameToFetch.isEmpty())
    		throw new IllegalArgumentException("UserNameToFetch can't be null or empty.");
    	
    	if (this.userDataModel != null){
    		this.userDataModel.findUser(userNameToFetch, callback);
    	}
    }
    
    /**
     * Returns the user account associated with the specified user id.
     * 
     * @param userId The ID of the user, must be bigger than 0.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void findUser(int userId, Object state, final OnCallback<Response<User>> callback) {
        if (userId <= 0)
            throw new IllegalArgumentException("Id can't be smaller or equal to zero.");

        if (this.userDataModel != null) {
            this.userDataModel.findUser(userId, state, callback);
        }

    }

    /**
     * Find the public profiles of all users that match the search parameters.
     * @param latitude The latitude of the position to search from. Must be a value between -90.0 and 90.0.
     * @param longitude The Longitude of the position to search from. Must be a value between -180.0 and 180.0.
     * @param searchDistance The distance in meters from the specified latitude/longitude to search for results. To ignore this distance pass in 40075000 (the circumference of the earth).
     * @param recordLimit The maximum number of users to return with this search.
     * @param gender The gender of the users, use UserGender.Any to search for both.
     * @param ageStart Specifies the starting age for the range of ages to search in. The value must be >= 0.
     * @param ageStop Specifies the ending age for the range of ages to search in. The value must be > ageStart.
     * @param status The status of the users to search for. Use UserStatus.Any to ignore this parameter.
     * @param checkinsWithinMinutes Filter for users who have checked-in in the past 'checkinsWithinMinutes' number of minutes.
     * @param appTag Search for the custom appTag that was stored with the user.
     * @param state An optional user defined object that will be passed to the callback.
     * @param callback The async callback to call on success or error.
     */
    public void findUser(double latitude, double longitude, Integer searchDistance, Integer recordLimit, UserGender gender, Integer ageStart, 
            Integer ageStop, UserStatus status, String checkinsWithinMinutes, String appTag, Object state, final OnCallback<ListResponse<User>> callback)
    {
        if (latitude > 90.0 || latitude < -90.0) throw new IllegalArgumentException("latitude can't be bigger than 90.0 or smaller than -90.0.");
        if (longitude > 180.0 || longitude < -180.0) throw new IllegalArgumentException("longitude can't be bigger than 180.0 or smaller than -180.0.");

        if(this.userDataModel != null)
        {
            this.userDataModel.findUser(searchDistance, latitude, longitude, recordLimit, gender, ageStart, ageStop, 
                    status, checkinsWithinMinutes, appTag, state, callback);
        }
    }

    /**
     * Search for public albums from other users.
     * 
     * @param searchDistanceInMeters The distance in meters from the specified
     *            latitude/longitude to search for results. To ignore this
     *            distance pass in 40075000 (the circumference of the earth).
     * @param latitude Optionally search for photos added near a latitude.
     * @param longitude Optionally search for photos added near a longitude.
     * @param limitResults Optionally limit the number of returned photos. Note
     *            that this parameter limits the photos returned, not albums.
     *            It's possible that a partial album is returned.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void searchForAlbums(Integer searchDistanceInMeters, float latitude, float longitude,
            Integer limitResults, Object state,
            final OnCallback<Response<Collection<PhotoAlbumPublic>>> callback) {
        if (this.pictureDataModel != null) {
            this.pictureDataModel.searchForAlbums(searchDistanceInMeters, latitude, longitude,
                    limitResults, state, callback);
        }
    }
    
    /**
     * Search for public albums from other users.
     * 
     * @param callback The async callback to call on success or error.
     */
    public void searchForAlbums(final OnCallback<Response<Collection<PhotoAlbumPublic>>> callback) {
        searchForAlbums(99999999, 0, 0, 50, null, callback);
    }
}
