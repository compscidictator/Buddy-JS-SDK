/*
 * Copyright (C) 2012 Buddy Platform, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.buddy.sdk;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.utils.Utils;

/**
 * Represents an object that can be used to search for startups around the user.
 * 
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
 *            {@code user.getStartups().find(1000000, 0.0, 0.0, 10, "", null, new OnCallback<ListResponse<Startup>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(ListResponse<Startup> response, Object state)}
 *                <code>{</code>
 *                    {@code List<Startup> itemList = response.getList();}
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 * 
 */
public class Startups {
    private StartupDataModel startupDataModel = null;
    protected BuddyClient client;
    protected AuthenticatedUser user;

    Startups(BuddyClient client, AuthenticatedUser user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null or empty.");
        if (user == null)
            throw new IllegalArgumentException("user can't be null or empty.");

        this.client = client;
        this.user = user;

        this.startupDataModel = new StartupDataModel(client, user);
    }

    /**
     * Gets a list of the supported metro areas for startups including the URL
     * to an image for each area returned.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getMetroAreaList(Object state, final OnCallback<ListResponse<MetroArea>> callback) {
        if (this.startupDataModel != null) {
            this.startupDataModel.getMetroAreaList(state, callback);
        }
    }

    /**
     * Get a list of startups in the specified metro area.
     * 
     * @param metroName The name of the metro area within which to search for
     *            startups.
     * @param recordLimit The number of search results to return.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getFromMetroArea(String metroName, int recordLimit, Object state,
            final OnCallback<ListResponse<Startup>> callback) {
        if (Utils.isNullOrEmpty(metroName))
            throw new IllegalArgumentException("metroName can't be empty or null");
        if (recordLimit < 0)
            throw new IllegalArgumentException("recordLimit can't be smaller than zero.");

        if (this.startupDataModel != null) {
            this.startupDataModel.getFromMetroArea(metroName, recordLimit, state, callback);
        }
    }

    /**
     * Searches for startups by name within the distance of the specified
     * location. Note: To search for all startups within the distance from the
     * specified location, leave the SearchName parameter empty.
     * 
     * @param searchDistanceInMeters The radius of the startup search.
     * @param latitude The latitude where the search should start.
     * @param longitude The longitude where the search should start.
     * @param numberOfResults The number of search results to return.
     * @param searchForName Optional search string, for example: "Star*" to
     *            search for all startups that begin with the string "Star".
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void find(int searchDistanceInMeters, double latitude, double longitude,
            int numberOfResults, String searchForName, Object state,
            final OnCallback<ListResponse<Startup>> callback) {
        if (searchDistanceInMeters < 0)
            throw new IllegalArgumentException("searchDistanceInMeters can't be smaller than zero.");
        if (latitude > 90.0 || latitude < -90.0)
            throw new IllegalArgumentException(
                    "latitude can't be bigger than 90.0 or smaller than -90.0.");
        if (longitude > 180.0 || longitude < -180.0)
            throw new IllegalArgumentException(
                    "longitude can't be bigger than 180.0 or smaller than -180.0.");
        if (numberOfResults < 0)
            throw new IllegalArgumentException("numberOfResults can't be smaller than zero.");

        if (this.startupDataModel != null) {
            this.startupDataModel.find(searchDistanceInMeters, latitude, longitude,
                    numberOfResults, searchForName, state, callback);
        }
    }

    /**
     * Searches for startups by name within the distance of the specified
     * location. Note: To search for all startups within the distance from the
     * specified location, leave the SearchName parameter empty.
     * 
     * @param searchDistanceInMeters The radius of the startup search.
     * @param latitude The latitude where the search should start.
     * @param longitude The longitude where the search should start.
     * @param numberOfResults The number of search results to return.
     * @param callback The async callback to call on success or error.
     */
    public void find(int searchDistanceInMeters, double latitude, double longitude,
            int numberOfResults, final OnCallback<ListResponse<Startup>> callback) {
        find(searchDistanceInMeters, latitude, longitude, numberOfResults, "", null, callback);
    }
}
