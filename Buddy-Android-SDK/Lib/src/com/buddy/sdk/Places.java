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

import android.util.SparseArray;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;

/**
 * Represents an object that can be used to search for physical locations around
 * the user.
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
public class Places {
    private PlacesDataModel placesDataModel = null;

    protected BuddyClient client;
    protected AuthenticatedUser user;

    Places(BuddyClient client, AuthenticatedUser user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");
        if (user == null)
            throw new IllegalArgumentException("user can't be null.");
        this.client = client;
        this.user = user;

        this.placesDataModel = new PlacesDataModel(this.client, this.user);
    }

    /**
     * Find a location close to a given latitude and longitude.
     * 
     * @param searchDistanceInMeters The radius of the location search.
     * @param latitude The latitude where the search should start.
     * @param longitude The longitude where the search should start.
     * @param numberOfResults Optional number of result to return, defaults to
     *            10.
     * @param searchForName Optional search string, for example: "Star*" to
     *            search for all place that start with the string "Star"
     * @param searchCategoryId Optional search category ID to narrow down the
     *            search with.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void find(Integer searchDistanceInMeters, double latitude, double longitude,
            Integer numberOfResults, String searchForName, Integer searchCategoryId, Object state,
            final OnCallback<ListResponse<Place>> callback) {
        if (searchDistanceInMeters <= 0)
            throw new IllegalArgumentException("searchDistanceInMeters can't be smaller or equal to zero.");
        if (latitude > 90.0 || latitude < -90.0)
            throw new IllegalArgumentException(
                    "latitude can't be bigger than 90.0 or smaller than -90.0.");
        if (longitude > 180.0 || longitude < -180.0)
            throw new IllegalArgumentException(
                    "longitude can't be bigger than 180.0 or smaller than -180.0.");
        if (numberOfResults <= 0)
            throw new IllegalArgumentException("numberOfResults can't be smaller or equal to zero.");
        if (searchForName == null)
            searchForName = "";

        if (this.placesDataModel != null) {
            this.placesDataModel.find(searchDistanceInMeters, latitude, longitude, numberOfResults,
                    searchForName, searchCategoryId, state, callback);
        }
    }

    /**
     * Find a location close to a given latitude and longitude.
     * 
     * @param searchDistanceInMeters The radius of the location search.
     * @param latitude The latitude where the search should start.
     * @param longitude The longitude where the search should start.
     * @param callback The async callback to call on success or error.
     */
    public void find(Integer searchDistanceInMeters, double latitude, double longitude,
            final OnCallback<ListResponse<Place>> callback) {
        find(searchDistanceInMeters, latitude, longitude, 10, "", -1, null, callback);
    }

    /**
     * Get all geo-location categories in Buddy.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getCategories(Object state, final OnCallback<Response<SparseArray<String>>> callback) {
        if (this.placesDataModel != null) {
            this.placesDataModel.getCategories(state, callback);
        }
    }

    /**
     * Get a Place by its globally unique identifier. This method can also be
     * used to calculate a distance from a lat/long to a place.
     * 
     * @param placeId The ID of the place to retrieve.
     * @param latitude The latitude where the search should start.
     * @param longitude The longitude where the search should start.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void get(Integer placeId, double latitude, double longitude, Object state,
            final OnCallback<Response<Place>> callback) {
        if (latitude > 90.0 || latitude < -90.0)
            throw new IllegalArgumentException(
                    "Latitude can't be bigger than 90.0 or smaller than -90.0.");
        if (longitude > 180.0 || longitude < -180.0)
            throw new IllegalArgumentException(
                    "Longitude can't be bigger than 180.0 or smaller than -180.0.");

        if (this.placesDataModel != null) {
            this.placesDataModel.get(placeId, latitude, longitude, state, callback);
        }
    }

    /**
     * Get a Place by its globally unique identifier. This method can also be
     * used to calculate a distance from a lat/long to a place.
     * 
     * @param placeId The ID of the place to retreive.
     * @param callback The async callback to call on success or error.
     */
    public void get(Integer placeId, Object state, final OnCallback<Response<Place>> callback) {
        get(placeId, 0, 0, null, callback);
    }
}
