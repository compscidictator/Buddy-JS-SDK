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

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.json.responses.PlacesDataResponse.PlacesData;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a single, named location in the Buddy system that's not a user.
 * Locations are related to stores, hotels, parks, etc.
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
 *            {@code user.getPlaces().find(100, 0, 0, 10, new OnCallback<Response<ListResponse<Place>>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<ListResponse<Place>> response, Object state)}
 *                <code>{</code>
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class Place {
    private PlacesDataModel placesDataModel = null;

    protected BuddyClient client;
    protected AuthenticatedUser user;

    private int id;
    private String address;
    private String appTagData;
    private int categoryID;
    private String categoryName;
    private String city;
    private Date createdDate;
    private double distanceInKilometers;
    private double distanceInMeters;
    private double distanceInMiles;
    private double distanceInYards;
    private String fax;
    private double latitude;
    private double longitude;
    private String name;
    private String postalState;
    private String postalZip;
    private String region;
    private String shortID;
    private String telephone;
    private Date touchedDate;
    private String userTagData;
    private String website;

    Place(BuddyClient client, AuthenticatedUser user, PlacesData place) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null");
        if (user == null)
            throw new IllegalArgumentException("User can't be null");
        this.client = client;
        this.user = user;

        this.address = place.address;
        this.appTagData = place.appTagData;
        this.categoryID = Utils.parseInt(place.categoryID);
        this.categoryName = place.categoryName;
        this.city = place.city;
        this.createdDate = Utils.convertDateString(place.createdDate, "MM/dd/yyyy hh:mm:ss aa");
        this.distanceInKilometers = Utils.parseDouble(place.distanceInKilometers);
        this.distanceInMeters = Utils.parseDouble(place.distanceInMeters);
        this.distanceInMiles = Utils.parseDouble(place.distanceInMiles);
        this.distanceInYards = Utils.parseDouble(place.distanceInYards);
        this.fax = place.fax;
        this.id = Utils.parseInt(place.geoID);
        this.latitude = Utils.parseDouble(place.latitude);
        this.longitude = Utils.parseDouble(place.longitude);
        this.name = place.name;
        this.postalState = place.postalState;
        this.postalZip = place.postalZip;
        this.region = place.region;
        this.shortID = place.shortID;
        this.telephone = place.telephone;
        this.touchedDate = Utils.convertDateString(place.touchedDate, "MM/dd/yyyy hh:mm:ss aa");
        this.userTagData = place.userTagData;
        this.website = place.webSite;
        
        this.placesDataModel = new PlacesDataModel(this.client, this.user);
    }

    /**
     * Gets the address of the location.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Gets the custom application tag data for the location.
     */
    public String getAppTagData() {
        return this.appTagData;
    }

    /**
     * Gets the category ID of the location (i.e. Hotels).
     */
    public int getCategoryID() {
        return this.categoryID;
    }

    /**
     * Gets the category name for the location.
     */
    public String getCategoryName() {
        return this.categoryName;
    }

    /**
     * Gets the city for the location.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Gets the date the location was created in the system.
     */
    public Date getCreatedDate() {
        return this.createdDate;
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
     * Gets the city for the location.
     */
    public String getFax() {
        return this.fax;
    }

    /**
     * Gets the globally unique ID of the location.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the latitude of the location.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Gets the longitude of the location.
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Gets the name of the location.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the postal state of the location.
     */
    public String getPostalState() {
        return this.postalState;
    }

    /**
     * Gets the postal ZIP of the location.
     */
    public String getPostalZip() {
        return this.postalZip;
    }

    /**
     * Gets the region of the location.
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * Gets the ShortID of the location.
     */
    public String getShortID() {
        return this.shortID;
    }

    /**
     * Gets the telephone number of the location.
     */
    public String getTelephone() {
        return this.telephone;
    }

    /**
     * Gets the last update date of the location.
     */
    public Date getTouchedDate() {
        return this.touchedDate;
    }

    /**
     * Gets the user tag data of the location.
     */
    public String getUserTagData() {
        return this.userTagData;
    }

    /**
     * Gets the website of the location.
     */
    public String getWebsite() {
        return this.website;
    }
    
    /**
     * Set an application specific tag or a user tag for a place.
     * 
     * @param appTag The application level tag to set.
     * @param userTag The user-level tag to set for this Place.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void setTag(String appTag, String userTag, Object state,
            OnCallback<Response<Boolean>> callback) {
        if (this.placesDataModel != null) {
            this.placesDataModel.setTag(this.id, appTag, userTag, state, callback);
        }
    }
}
