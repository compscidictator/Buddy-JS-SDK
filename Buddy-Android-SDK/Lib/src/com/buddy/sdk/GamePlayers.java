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

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;

/**
 * Represents a player in a game. The Player object tracks game specific items
 * such as board, ranks, and other data specific to building game leader boards
 * and other game related constructs.
 */
public class GamePlayers {
    private AuthenticatedUser user;
    private BuddyClient client;
    private GamesDataModel gameDataModel = null;

    GamePlayers(BuddyClient client, AuthenticatedUser user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");

        this.client = client;
        this.user = user;

        this.gameDataModel = new GamesDataModel(this.client, this.user, null);
    }

    /**
     * Creates a new game Player object for an existing user in Buddy.
     * 
     * @param name The name of the new player.
     * @param board An optional name of a "Board" for the game. Used for
     *            grouping scores together either by user group, levels, or some
     *            other method relevant to the game. Although optional, a value
     *            is recommended such as "Default" for use in later searches of
     *            scores. If no board is to be stored, then pass in null or
     *            leave empty.
     * @param rank An optional ranking to associate with the score. Can be any
     *            string ie: descriptions of achievements, ranking strings like
     *            "excellent", etc. Pass in null or an empty string if you do
     *            not wish to store a rank
     * @param latitude The latitude of the location where the Player object is
     *            being created.
     * @param longitude The longitude of the location where the Player object is
     *            being created.
     * @param appTag Optional metadata to store with the Player object. ie: a
     *            list of players, game state, etc. Leave empty or set to null
     *            if there is no data to store with the score.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void add(String name, String board, String rank, double latitude, double longitude,
            String appTag, Object state, OnCallback<Response<Boolean>> callback) {
        if (latitude > 90.0 || latitude < -90.0)
            throw new IllegalArgumentException(
                    "latitude can't be bigger than 90.0 or smaller than -90.0.");
        if (longitude > 180.0 || longitude < -180.0)
            throw new IllegalArgumentException(
                    "longitude can't be bigger than 180.0 or smaller than -180.0.");

        if (this.gameDataModel != null) {
            this.gameDataModel.add(name, latitude, longitude, rank, board, appTag, state, callback);
        }
    }

    /**
     * Creates a new game Player object for an existing user in Buddy.
     * 
     * @param name The name of the new player.
     * @param callback The async callback to call on success or error.
     */
    public void add(String name, OnCallback<Response<Boolean>> callback) {
        add(name, "", "", 0, 0, "", null, callback);
    }

    /**
     * Updates one or more fields of an existing Player object which was
     * previously created.
     * 
     * @param name The name of the new player.
     * @param board An optional name of a "Board" for the game. Used for
     *            grouping scores together either by user group, levels, or some
     *            other method relevant to the game. Although optional, a value
     *            is recommended such as "Default" for use in later searches of
     *            scores. If no board is to be stored, then pass in null or
     *            leave empty.
     * @param rank An optional ranking to associate with the score. Can be any
     *            string ie: descriptions of achievements, ranking strings like
     *            "excellent", etc. Pass in null or an empty string if you do
     *            not wish to store a rank
     * @param latitude The latitude of the location where the Player object is
     *            being updated.
     * @param longitude The longitude of the location where the Player object is
     *            being updated.
     * @param appTag Optional metadata to store with the Player object. ie: a
     *            list of players, game state, etc. Leave empty or set to null
     *            if there is no data to store with the score
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void update(String name, String board, String rank, double latitude, double longitude,
            String appTag, Object state, OnCallback<Response<Boolean>> callback) {
        if (latitude > 90.0 || latitude < -90.0)
            throw new IllegalArgumentException(
                    "latitude can't be bigger than 90.0 or smaller than -90.0.");
        if (longitude > 180.0 || longitude < -180.0)
            throw new IllegalArgumentException(
                    "longitude can't be bigger than 180.0 or smaller than -180.0.");

        if (this.gameDataModel != null) {
            this.gameDataModel.update(name, latitude, longitude, rank, board, appTag, state,
                    callback);
        }
    }

    /**
     * Updates one or more fields of an existing Player object which was
     * previously created.
     * 
     * @param name The name of the new player.
     * @param callback The async callback to call on success or error.
     */
    public void update(String name, OnCallback<Response<Boolean>> callback) {
        update(name, "", "", 0, 0, "", null, callback);
    }

    /**
     * Delete the player object for this user.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void delete(Object state, OnCallback<Response<Boolean>> callback) {
        if (this.gameDataModel != null) {
            this.gameDataModel.delete(state, callback);
        }
    }

    /**
     * Get all the player info for this user.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getInfo(Object state, OnCallback<Response<GamePlayer>> callback) {
        if (this.gameDataModel != null) {
            this.gameDataModel.getInfo(state, callback);
        }
    }

    /**
     * Searches for Player objects stored in the Buddy system. Searches can
     * optionally be performed based on location.
     * 
     * @param searchDistanceInMeters The radius (in meters) around the specified
     *            location in which to look for locations. Pass in -1 to ignore
     *            this field.
     * @param latitude The latitude of the location around which to search for
     *            locations within the specified SearchDistance.
     * @param longitude The longitude of the location around which to search for
     *            locations within the specified SearchDistance.
     * @param recordLimit The maximum number of search results to return or -1
     *            to return all search results.
     * @param boardName Searches for scores which contain the specified board.
     *            Leave empty or pass in null if no board filter is to be used.
     * @param onlyForLastNumberOfDays The number of days into the past for which
     *            to look for scores. ie: passing in 5 will filter scores to
     *            include those which were added/updated on or after 5 days ago.
     *            Pass in -1 to ignore this filter.
     * @param minimumScore The minimum score value to search for. Pass in -1 to
     *            ignore this filter.
     * @param appTag Searches for scores with the specified ApplicationTag
     *            stored with them. Leave empty or pass in null to ignore this
     *            filter.
     * @param rank Optionally search for a player rank.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void find(Integer searchDistanceInMeters, double latitude, double longitude,
            Integer recordLimit, String boardName, Integer onlyForLastNumberOfDays,
            Integer minimumScore, String appTag, String rank, Object state,
            OnCallback<ListResponse<GamePlayer>> callback) {
        if (latitude > 90.0 || latitude < -90.0)
            throw new IllegalArgumentException(
                    "Latitude can't be bigger than 90.0 or smaller than -90.0.");
        if (longitude > 180.0 || longitude < -180.0)
            throw new IllegalArgumentException(
                    "Longitude can't be bigger than 180.0 or smaller than -180.0.");

        if (this.gameDataModel != null) {
            this.gameDataModel.find(searchDistanceInMeters, latitude, longitude, recordLimit,
                    boardName, onlyForLastNumberOfDays, minimumScore, appTag, rank, state, callback);
        }
    }
}
