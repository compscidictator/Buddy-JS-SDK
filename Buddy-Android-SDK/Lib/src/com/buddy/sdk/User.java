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
import java.util.Date;

import android.net.Uri;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.json.responses.BlockedDataResponse.BlockedData;
import com.buddy.sdk.json.responses.FriendDataResponse.FriendData;
import com.buddy.sdk.json.responses.SearchFriendsDataResponse.SearchFriendsData;
import com.buddy.sdk.json.responses.UserDataResponse.UserData;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.utils.Constants.UserGender;
import com.buddy.sdk.utils.Constants.UserStatus;

/**
 * Represents a public user profile. Public user profiles are usually returned
 * when looking at an AuthenticatedUser's friends or making a search with
 * FindUser.
 * <p>
 * 
 * <pre>
 * {@code private AuthenticatedUser user = null;}
 * 
 *    {@code BuddyClient client = new BuddyClient("APPNAME", "APPPASS");}
 *    {@code client.login("username", "password", new OnCallback<Response<AuthenticatedUser>>()}
 *    <code>{</code>
 *        {@code public void OnResponse(Response<AuthenticatedUser> response, Object state)}
 *        <code>{</code>
 *            {@code user = response.getResult();}
 *            {@code user.findUser(userId, null, new OnCallback<Response<User>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<User> response, Object state)}
 *                <code>{</code>
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class User {
    protected BaseUserDataModel baseUserDataModel = null;

    protected BuddyClient client;
    protected String name;
    protected int id;

    private GameScores gameScores;
    private GameStates gameStates;

    protected UserGender gender;
    protected String applicationTag;
    protected double latitude;
    protected double longitude;
    protected Date lastLoginOn;
    protected Uri profilePicture;
    protected String profilePictureId;
    protected int age;
    protected UserStatus status;
    protected Date createdOn;
    protected double distanceInKilometers;
    protected double distanceInMeters;
    protected double distanceInMiles;
    protected double distanceInYards;
    protected Boolean friendRequestPending;

    User(BuddyClient client, int id) {
        this.client = client;
        this.id = id;

        this.gameScores = new GameScores(this.client, null, this);
        this.gameStates = new GameStates(this.client, this);
    }

    User(BuddyClient client, UserData profile) {
        this(client, Integer.valueOf(profile.userID));

        this.name = profile.userName;
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
    }

    User(BuddyClient client, FriendData profile, int userId) {
        this(client, (Integer.valueOf(profile.userID) == userId ? Integer.valueOf(profile.userID)
                : Integer.valueOf(profile.friendID)));

        this.name = profile.userName;
        if(Utils.isNullOrEmpty(profile.userGender)) {
        	this.gender = UserGender.any;
        } else {
        	this.gender = UserGender.valueOf(profile.userGender);
        }
        this.applicationTag = "";
        this.latitude = Utils.parseDouble(profile.userLatitude);
        this.longitude = Utils.parseDouble(profile.userLongitude);
        this.lastLoginOn = Utils.convertDateString(profile.lastLoginDate);
        this.profilePicture = Uri.parse(profile.profilePictureURL);
        this.createdOn = Utils.convertDateString(profile.createdDate);
        this.status = Utils.getUserStatus(Integer.valueOf(profile.statusID));
        this.age = Integer.valueOf(profile.age);
    }

    User(BuddyClient client, BlockedData profile) {
        this(client, Integer.valueOf(profile.userID));

        this.name = profile.userName;
        if(Utils.isNullOrEmpty(profile.userGender)) {
        	this.gender = UserGender.any;
        } else {
        	this.gender = UserGender.valueOf(profile.userGender);
        }
        this.applicationTag = "";
        this.latitude = Utils.parseDouble(profile.userLatitude);
        this.longitude = Utils.parseDouble(profile.userLongitude);
        this.lastLoginOn = Utils.convertDateString(profile.lastLoginDate);
        this.profilePicture = Uri.parse(profile.profilePictureURL);
        this.createdOn = Utils.convertDateString(profile.createdDate);
        this.status = Utils.getUserStatus(Integer.valueOf(profile.statusID));
        this.age = Integer.valueOf(profile.age);
    }

    User(BuddyClient client, SearchFriendsData profile) {
        this(client, Integer.valueOf(profile.userID));

        this.name = profile.userName;
        if(Utils.isNullOrEmpty(profile.userGender)) {
        	this.gender = UserGender.any;
        } else {
        	this.gender = UserGender.valueOf(profile.userGender);
        }
        this.applicationTag = "";
        this.latitude = Utils.parseDouble(profile.userLatitude);
        this.longitude = Utils.parseDouble(profile.userLongitude);
        this.profilePicture = Uri.parse(profile.profilePictureURL);
        this.status = Utils.getUserStatus(Integer.valueOf(profile.statusID));
        this.age = Integer.valueOf(profile.age);

        this.distanceInMiles = Utils.parseDouble(profile.distanceInMiles);
        this.distanceInKilometers = Utils.parseDouble(profile.distanceInKilometers);
        this.distanceInMeters = Utils.parseDouble(profile.distanceInMeters);
        this.distanceInYards = Utils.parseDouble(profile.distanceInYards);
    }

    /**
     * Add and remove GameScores for this user.
     */
    public GameScores getGameScores() {
        return gameScores;
    }

    /**
     * Add and remove GameStates for this user.
     */
    public GameStates getGameStates() {
        return gameStates;
    }

    protected String TokenOrId() {
        return String.valueOf(this.id);
    }

    /**
     * Gets the system-wide unique ID of the user.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the name of the user.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the gender of the user.
     */
    public UserGender getGender() {
        return this.gender;
    }

    /**
     * Gets the optional application tag for the user.
     */
    public String getApplicationTag() {
        return this.applicationTag;
    }

    /**
     * Gets the latitude of the last check-in for this user.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Gets the longitude of the last check-in for this user.
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Gets the last time this user logged on to the platform.
     */
    public Date getLastLoginOn() {
        return this.lastLoginOn;
    }

    /**
     * Gets the profile picture for this user.
     */
    public Uri getProfilePicture() {
        return this.profilePicture;
    }

    /**
     * Gets the profile picture for this user.
     */
    public String getProfilePictureId() {
        return this.profilePictureId;
    }

    /**
     * Gets the age of the user.
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Gets the status of the user.
     */
    public UserStatus getStatus() {
        return this.status;
    }

    /**
     * Gets the date this user was created.
     */
    public Date getCreatedOn() {
        return this.createdOn;
    }

    /**
     * If this user profile was returned from a search, gets the distance in
     * kilometers from the search origin.
     */
    public double getDistanceInKilometers() {
        return this.distanceInKilometers;
    }

    /**
     * If this user profile was returned from a search, gets the distance in
     * meters from the search origin.
     */
    public double getDistanceInMeters() {
        return this.distanceInMeters;
    }

    /**
     * If this user profile was returned from a search, gets the distance in
     * miles from the search origin.
     */
    public double getDistanceInMiles() {
        return this.distanceInMiles;
    }

    /**
     * If this user profile was returned from a search, gets the distance in
     * yards from the search origin.
     */
    public double getDistanceInYards() {
        return this.distanceInYards;
    }

    /**
     * Does this user have a friends request pending.
     */
    public Boolean getFriendRequestPending() {
        return this.friendRequestPending;
    }

    /**
     * Gets a list of profile photos for this user.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getProfilePhotos(Object state,
            final OnCallback<ListResponse<PicturePublic>> callback) {
        if (this.baseUserDataModel != null) {
            this.baseUserDataModel.getProfilePhotos(this.id, state, callback);
        }
    }
}
