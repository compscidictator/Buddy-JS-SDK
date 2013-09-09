/* Copyright (C) 2012 Buddy Platform, Inc.
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
import com.buddy.sdk.json.responses.MetadataSearchDataResponse.MetadataSearchData;

import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a single item of metadata. Metadata is used to store custom
 * key/value pairs at the application or user level.
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
 *            {@code user.getMetadata().get("some key", null, new OnCallback<Response<MetadataItem>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<MetadataItem> response, Object state)}
 *                <code>{</code>
 *                    {@code MetadataItem item = response.getResult();}
 *                    {@code item.set("some value", 0, 0, new OnCallback<Response<Boolean>>()}
 *                    <code>{</code>
 *                        {@code public void OnResponse(Response<Boolean> response, Object state)}
 *                        <code>{</code>
 *                        <code>}</code>
 *                    <code>});</code>
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class MetadataItem implements Comparable<MetadataItem> {
    private String value;
    private String key;
    private double latitude;
    private double longitude;
    private Date lastUpdate;
    private double distanceInKilometers;
    private double distanceInMeters;
    private double distanceInMiles;
    private double distanceInYards;
    private String applicationTag;
    private UserMetadata owner;
    private AppMetadata ownerApp;

    MetadataItem(BuddyClient client, UserMetadata owner, AppMetadata ownerApp, String token,
            MetadataSearchData data, double originLatitude, double originLongitude) {
        this(client, owner, ownerApp, token, data.metaKey, data.metaValue, Utils
                .parseDouble(data.metaLatitude), Utils.parseDouble(data.metaLongitude), Utils
                .convertDateString(data.lastUpdateDate), null);
        this.distanceInKilometers = Utils.parseDouble(data.distanceInKilometers);
        this.distanceInMeters = Utils.parseDouble(data.distanceInMeters);
        this.distanceInMiles = Utils.parseDouble(data.distanceInMiles);
        this.distanceInYards = Utils.parseDouble(data.distanceInYards);
    }

    MetadataItem(BuddyClient client, UserMetadata owner, AppMetadata ownerApp, String token,
            String key, String value, double latitude, double longitude, Date lastUpdateOn,
            String appTag) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null or empty.");
        if (Utils.isNullOrEmpty(key))
            throw new IllegalArgumentException("key can't be null or empty.");

        this.key = key;
        this.value = value;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastUpdate = lastUpdateOn;
        this.applicationTag = appTag;
        this.owner = owner;
        this.ownerApp = ownerApp;
    }

    /**
     * Gets the value for this item.
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the key for this item.
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the latitude of this item.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude of this item.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Gets the last date this item was updated.
     */
    public Date getLastUpdateOn() {
        return lastUpdate;
    }

    /**
     * Gets the distance in kilometers from the given origin in the Metadata
     * Search method.
     */
    public double getDistanceInKilometers() {
        return distanceInKilometers;
    }

    /**
     * Gets the distance in meters from the given origin in the Metadata Search
     * method.
     */
    public double getDistanceInMeters() {
        return distanceInMeters;
    }

    /**
     * Gets the distance in miles from the given origin in the Metadata Search
     * method.
     */
    public double getDistanceInMiles() {
        return distanceInMiles;
    }

    /**
     * Gets the distance in yards from the given origin in the Metadata Search
     * method.
     */
    public double getDistanceInYards() {
        return distanceInYards;
    }

    /**
     * Gets a custom application Tag for this item.
     */
    public String getApplicationTag() {
        return applicationTag;
    }

    /**
     * Updates the value of this metadata item.
     * 
     * @param value The value of the metadata item, can't be null.
     * @param latitude The optional latitude of the metadata item.
     * @param longitude The optional longitude of the metadata item.
     * @param appTag Additional data to associate with the metadata.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void set(String value, double latitude, double longitude, String appTag, Object state,
            OnCallback<Response<Boolean>> callback) {
        if (this.owner != null) {
            this.owner.set(this.key, value, latitude, longitude, appTag, state, callback);
        } else {
            ownerApp.set(this.key, value, latitude, longitude, appTag, state, callback);
        }
    }

    /**
     * Updates the value of this metadata item.
     * 
     * @param value The value of the metadata item, can't be null.
     * @param callback The async callback to call on success or error.
     */
    public void set(String value, OnCallback<Response<Boolean>> callback) {
        if (this.owner != null) {
            this.owner.set(this.key, value, 0, 0, "", null, callback);
        } else {
            ownerApp.set(this.key, value, 0, 0, "", null, callback);
        }
    }

    /**
     * Deletes this metadata item.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void delete(Object state, OnCallback<Response<Boolean>> callback) {
        if (this.owner != null) {
            this.owner.delete(this.key, state, callback);
        } else {
            ownerApp.delete(this.key, state, callback);
        }
    }

    @Override
    public int compareTo(MetadataItem other) {
        if (other.key == this.key && other.value == this.value)
            return 0;
        else
            return this.key.compareTo(other.key);
    }
}
