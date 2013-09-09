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

import java.util.Map;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.utils.Utils;

/**
 * Represents a collection of application level metadata items. You can access
 * this class through the BuddyClient object.
 * <p>
 * 
 * <pre>
 * 
 *    {@code BuddyClient client = new BuddyClient("APPNAME", "APPPASS");}
 *    {@code client.getMetadata().set("test key", "test value", 0.0, 0.0, "", null, new OnCallback<Response<Boolean>>()}
 *    <code>{</code>
 *        {@code public void OnResponse(Response<Boolean> response, Object state)}
 *        <code>{</code>
 *        <code>}</code>
 * 		
 *    <code>});</code>
 *    
 *    {@code // Search for items within a thousand meters of a lat:0.0 and long:0.0.}
 *    {@code client.getMetadata().find(40075000, 0.0, 0.0, 10, "", "", -1, 0, 100, false, true, false, null, new OnCallback<Response<Map<String, MetadataItem>>>()}
 *    <code>{</code>
 *        {@code public void OnResponse(Response<Map<String, MetadataItem>> response, Object state)}
 *        <code>{</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class AppMetadata {
    private AppMetadataModel appMetadataModel = null;

    protected BuddyClient client;

    public AppMetadata(BuddyClient client) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");
        this.client = client;

        this.appMetadataModel = new AppMetadataModel(this.client, this);
    }

    /**
     * Returns the sum of a set of metadata items that correspond to a certain
     * key wildcard. Note that the values of these items need to be integers or
     * floats, otherwise this method will fail.
     * 
     * @param forKeys The key to use to filter the items that need to be summed.
     *            Is always treated as a wildcard.
     * @param withinDistance Optionally sum only items within a certain number
     *            of meters from lat/long.
     * @param latitude Optionally provide a latitude where the search can be
     *            started from.
     * @param longitude Optionally provide a longitude where the search can be
     *            started from.
     * @param updatedMinutesAgo Optionally sum only on items that have been
     *            updated a number of minutes ago.
     * @param withAppTag Optionally sum only items that have a certain
     *            application tag.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void batchSum(String forKeys, String withinDistance, double latitude, double longitude,
            int updatedMinutesAgo, String withAppTag, Object state,
            OnCallback<ListResponse<MetadataSum>> callback) {
        if (this.appMetadataModel != null) {
            this.appMetadataModel.batchSum(forKeys, withinDistance, latitude, longitude,
                    updatedMinutesAgo, withAppTag, state, callback);
        }
    }

    /**
     * Returns the sum of a set of metadata items that correspond to a certain
     * key wildcard. Note that the values of these items need to be integers or
     * floats, otherwise this method will fail.
     * 
     * @param forKeys The key to use to filter the items that need to be summed.
     *            Is always treated as a wildcard.
     * @param callback The async callback to call on success or error.
     */
    public void batchSum(String forKeys, OnCallback<ListResponse<MetadataSum>> callback) {
        batchSum(forKeys, "-1", -1.0, -1.0, -1, "", null, callback);
    }

    /**
     * Returns the sum of a set of metadata items that correspond to a certain
     * key wildcard. Note that the values of these items need to be integers or
     * floats, otherwise this method will fail.
     * 
     * @deprecated Please use either of the 2 other batchSum methods.
     * 
     * @param forKeys The key to use to filter the items that need to be summed.
     *            Is always treated as a wildcard.
     * @param withinDistance Optionally sum only items within a certain number
     *            of meters from lat/long.
     * @param latitude Optionally provide a latitude where the search can be
     *            started from.
     * @param longitude Optionally provide a longitude where the search can be
     *            started from.
     * @param updatedMinutesAgo Optionally sum only on items that have been
     *            updated a number of minutes ago.
     * @param callback The async callback to call on success or error.
     */
    @Deprecated
    public void batchSum(String forKeys, String withinDistance, double latitude, double longitude,
            int updatedMinutesAgo, OnCallback<ListResponse<MetadataSum>> callback) {
        batchSum(forKeys, withinDistance, latitude, longitude, updatedMinutesAgo, "", null,
                callback);
    }

    /**
     * Get a metadata item with a key. The key can't be null or an empty string.
     * 
     * @param key The key to use to reference the metadata item.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void get(String key, Object state, OnCallback<Response<MetadataItem>> callback) {
        if (Utils.isNullOrEmpty(key))
            throw new IllegalArgumentException("key can't be null or empty.");

        if (this.appMetadataModel != null) {
            this.appMetadataModel.get(key, state, callback);
        }
    }

    /**
     * Get all the metadata items for this application. Note that this can be a
     * very time-consuming method, try to retrieve specific items if possible or
     * do a search.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getAll(Object state, OnCallback<Response<Map<String, MetadataItem>>> callback) {
        if (this.appMetadataModel != null) {
            this.appMetadataModel.getAll(state, callback);
        }
    }

    /**
     * Delete a metadata item referenced by key.
     * 
     * @param key A valid key of a metadata item.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void delete(String key, Object state, OnCallback<Response<Boolean>> callback) {
        if (Utils.isNullOrEmpty(key))
            throw new IllegalArgumentException("key can't be null or empty.");

        if (this.appMetadataModel != null) {
            this.appMetadataModel.delete(key, state, callback);
        }
    }

    /**
     * Delete all application metadata. There is no way to recover from this
     * operation, be careful when you call it.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void deleteAll(Object state, OnCallback<Response<Boolean>> callback) {
        if (this.appMetadataModel != null) {
            this.appMetadataModel.deleteAll(state, callback);
        }
    }

    /**
     * Search for metadata items in this application. Note that this method will
     * only find app-level metadata items.
     * 
     * @param searchDistanceMeters The distance in meters from the latitude and
     *            longitude to search in. To ignore this distance pass in
     *            40075000 (the circumference of the earth).
     * @param latitude The latitude from where the search will start.
     * @param longitude The longitude from where the search will start.
     * @param numberOfResults Optionally limit the number of returned metadata
     *            items.
     * @param withKey Optionally search for items with a specific key. The value
     *            of this parameter is treated as a wildcard.
     * @param withValue Optionally search for items with a specific value. The
     *            value of this parameter is treated as a wildcard.
     * @param updatedMinutesAgo Optionally return only items that were updated
     *            some minutes ago.
     * @param valueMin Optionally search for metadata item values that are
     *            bigger than this number.
     * @param valueMax Optionally search for metadata item values that are
     *            smaller than this number.
     * @param searchAsFloat Optionally treat all metadata values as floats.
     *            Useful for min/max searches.
     * @param sortAscending Optionally sort the results ascending.
     * @param disableCache Optionally disable cache searches.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void find(int searchDistanceMeters, double latitude, double longitude,
            int numberOfResults, String withKey, String withValue, int updatedMinutesAgo,
            double valueMin, double valueMax, boolean searchAsFloat, boolean sortAscending,
            boolean disableCache, Object state,
            OnCallback<Response<Map<String, MetadataItem>>> callback) {
        if (this.appMetadataModel != null) {
            this.appMetadataModel.findData(searchDistanceMeters, latitude, longitude,
                    numberOfResults, withKey, withValue, updatedMinutesAgo, (int) valueMin,
                    (int) valueMax, searchAsFloat, sortAscending, disableCache, state, callback);
        }
    }

    /**
     * Search for metadata items in this application. Note that this method will
     * only find app-level metadata items.
     * 
     * @param searchDistanceMeters The distance in meters from the latitude and
     *            longitude to search in. To ignore this distance pass in
     *            40075000 (the circumference of the earth).
     * @param latitude The latitude from where the search will start.
     * @param longitude The longitude from where the search will start.
     * @param callback The async callback to call on success or error.
     */
    public void find(int searchDistanceMeters, double latitude, double longitude,
            OnCallback<Response<Map<String, MetadataItem>>> callback) {
        find(searchDistanceMeters, latitude, longitude, 10, "", "", -1, 0, 100, false, false,
                false, null, callback);
    }

    /**
     * Set a metadata item value for a key. You can add additional latitude and
     * longitude coordinates to record the location from where this item was
     * set, or tag the item with a custom tag. The item doesn't have to exist to
     * be set, this method acts as an Add method in cases where the item doesn't
     * exist.
     * 
     * @param key The key of the metadata item, can't be null or empty.
     * @param value The value of the metadata item, can't be null.
     * @param latitude The optional latitude of the metadata item.
     * @param longitude The optional longitude of the metadata item.
     * @param appTag Additional data to associate with the metadata.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void set(String key, String value, double latitude, double longitude, String appTag,
            Object state, OnCallback<Response<Boolean>> callback) {
        if (Utils.isNullOrEmpty(key))
            throw new IllegalArgumentException("key can't be null or empty.");
        if (value == null)
            throw new IllegalArgumentException("value can't be null.");
        if (appTag == null)
            appTag = "";

        if (this.appMetadataModel != null) {
            this.appMetadataModel.set(key, value, latitude, longitude, appTag, state, callback);
        }
    }

    /**
     * Set a metadata item value for a key. You can add additional latitude and
     * longitude coordinates to record the location from where this item was
     * set, or tag the item with a custom tag. The item doesn't have to exist to
     * be set, this method acts as an Add method in cases where the item doesn't
     * exist.
     * 
     * @param key The key of the metadata item, can't be null or empty.
     * @param value The value of the metadata item, can't be null.
     * @param callback The async callback to call on success or error.
     */
    public void set(String key, String value, OnCallback<Response<Boolean>> callback) {
        set(key, value, 0, 0, "", null, callback);
    }

    /**
     * This method returns the sum of a set of metadata items that correspond to
     * a certain key wildcard. Note that the values of these items need to be
     * numbers or floats, otherwise this method will fail.
     * 
     * @param forKeys The key to use to filter the items that need to be summed.
     *            Is always treated as a wildcard.
     * @param withinDistance Optionally sum only items within a certain number
     *            of meters from lat/long.
     * @param latitude Optionally provide a latitude where the search can be
     *            started from.
     * @param longitude Optionally provide a longitude where the search can be
     *            started from.
     * @param updatedMinutesAgo Optionally sum only on items that have been
     *            updated a number of minutes ago.
     * @param withAppTag Optionally sum only items that have a certain
     *            application tag.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void sum(String forKeys, int withinDistance, double latitude, double longitude,
            int updatedMinutesAgo, String withAppTag, Object state,
            OnCallback<Response<MetadataSum>> callback) {
        if (this.appMetadataModel != null) {
            this.appMetadataModel.sum(forKeys, withinDistance, latitude, longitude,
                    updatedMinutesAgo, withAppTag, state, callback);
        }
    }

    /**
     * This method returns the sum of a set of metadata items that correspond to
     * a certain key wildcard. Note that the values of these items need to be
     * numbers or floats, otherwise this method will fail.
     * 
     * @param forKeys The key to use to filter the items that need to be summed.
     *            Is always treated as a wildcard.
     * @param callback The async callback to call on success or error.
     */
    public void sum(String forKeys, OnCallback<Response<MetadataSum>> callback) {
        sum(forKeys, -1, 0, 0, -1, "", null, callback);
    }
}
