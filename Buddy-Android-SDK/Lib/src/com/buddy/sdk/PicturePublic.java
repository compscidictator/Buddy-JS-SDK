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

import java.util.Date;

import com.buddy.sdk.json.responses.SearchPictureDataResponse.SearchPictureData;
import com.buddy.sdk.json.responses.VirtualPhotoDataResponse.VirtualPhotoData;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a single picture on the Buddy Platform. This is a public view of a
 * picture, can be retrieve either by getting a User's profile pictures or by
 * searching for albums.
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
 *            {@code user.getProfilePhotos(null, new OnCallback<Response<ListResponse<PicturePublic>>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<ListResponse<PicturePublic>> response, Object state)}
 *                <code>{</code>
 *                    {@code List<PicturePublic> pictures = reponse.getList();}
 *                    {@code PicturePublic pic = pictures.get(0);}
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class PicturePublic {
    protected BuddyClient client;
    protected User user;
    protected int userId;
    protected AuthenticatedUser owner;
    protected int photoId;
    protected String fullUrl;
    protected String thumbnailUrl;
    protected double latitude;
    protected double longitude;
    protected String photoComment;
    protected String applicationTag;
    protected Date addedDate;
    protected double distanceInKilometers;
    protected double distanceInMeters;
    protected double distanceInMiles;
    protected double distanceInYards;

    PicturePublic(BuddyClient client, String fullUrl, String thumbnailUrl, double latitude,
            double longitude, String comment, String appTag, Date addedOn, int photoId, User user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");
        if (user == null)
            throw new IllegalArgumentException("user can't be null.");

        this.fullUrl = fullUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoComment = comment;
        this.applicationTag = appTag;
        this.addedDate = addedOn;
        this.photoId = photoId;
        this.client = client;
        this.user = user;
    }

    PicturePublic(BuddyClient client, User user, SearchPictureData photo, int userId,
            AuthenticatedUser searchOwner) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");
        if (photo == null)
            throw new IllegalArgumentException("photo can't be null.");

        this.client = client;
        this.fullUrl = photo.fullPhotoUrl;
        this.thumbnailUrl = photo.thumbnailUrl;
        this.latitude = Utils.parseDouble(photo.latitude);
        this.longitude = Utils.parseDouble(photo.longitude);
        this.addedDate = Utils.convertDateString(photo.photoAdded, "MM/dd/yyyy hh:mm:ss aa");
        this.applicationTag = photo.applicationTag;
        this.photoId = Utils.parseInt(photo.photoId);
        this.user = user;
        this.userId = userId;
        this.owner = searchOwner;

        this.distanceInKilometers = Utils.parseDouble(photo.distanceInKilometers);
        this.distanceInMeters = Utils.parseDouble(photo.distanceInMeters);
        this.distanceInMiles = Utils.parseDouble(photo.distanceInMiles);
        this.distanceInYards = Utils.parseDouble(photo.distanceInYards);
    }

    PicturePublic(BuddyClient client, VirtualPhotoData photo) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");
        if (photo == null)
            throw new IllegalArgumentException("photo can't be null.");

        this.client = client;
        this.fullUrl = photo.FullPhotoURL;
        this.thumbnailUrl = photo.ThumbnailPhotoURL;
        this.latitude = Utils.parseDouble(photo.Latitude);
        this.longitude = Utils.parseDouble(photo.Longitude);
        this.addedDate = Utils.convertDateString(photo.AddedDateTime);
        this.applicationTag = photo.ApplicationTag;
        this.photoId = Utils.parseInt(photo.PhotoID);
        this.userId = Utils.parseInt(photo.UserID);
    }

    /**
     * Gets the url of the full picture.
     */
    public String getFullUrl() {
        return this.fullUrl;
    }

    /**
     * Gets the url of the thumbnail of the picture.
     */
    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    /**
     * Gets the latitude of the picture location.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Gets the longitude of the picture location.
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Gets the optional comment of the picture.
     */
    public String getComment() {
        return this.photoComment;
    }

    /**
     * Gets the optional application tag of the picture.
     */
    public String getAppTag() {
        return this.applicationTag;
    }

    /**
     * Gets the date when this picture was added.
     */
    public Date getAddedOn() {
        return this.addedDate;
    }

    /**
     * Gets the system-wide ID of the picture.
     */
    public int getPhotoId() {
        return this.photoId;
    }

    /**
     * If this picture was returned as part of an album search, gets the
     * distance in kilometers from the location that was used as the origin of
     * the search.
     */
    public double getDistanceInKilometers() {
        return this.distanceInKilometers;
    }

    /**
     * If this picture was returned as part of an album search, gets the
     * distance in meters from the location that was used as the origin of the
     * search.
     */
    public double getDistanceInMeters() {
        return this.distanceInMeters;
    }

    /**
     * If this picture was returned as part of an album search, gets the
     * distance in miles from the location that was used as the origin of the
     * search.
     */
    public double getDistanceInMiles() {
        return this.distanceInMiles;
    }

    /**
     * If this picture was returned as part of an album search, gets the
     * distance in yards from the location that was used as the origin of the
     * search.
     */
    public double getDistanceInYards() {
        return this.distanceInYards;
    }
}
