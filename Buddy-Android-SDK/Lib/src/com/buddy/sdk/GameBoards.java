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

/**
 * Represents a user that has been authenticated with the Buddy Platform. Use
 * this object to interact with the service on behalf of the user.
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
 *            {@code user.getGameScores().add(0, 0, "", 10, "My Board", "", 0, null, new OnCallback<Response<Boolean>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<Boolean> response, Object state)}
 *                <code>{</code>
 *                    {@code client.getGameBoards().getHighScores("My Board", 10, null, new OnCallback<Response<GameScore>>()}
 *                    <code>{</code>
 *                        {@code public void OnResponse(Response<GameScore> response, Object state)}
 *                        <code>{</code>
 *                            {@code GameScore = response.getresult();}
 *                        <code>}</code>
 *                    <code>});</code>
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class GameBoards {
    private BuddyClient client;
    private GamesDataModel gameDataModel = null;

    GameBoards(BuddyClient client) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");

        this.client = client;

        this.gameDataModel = new GamesDataModel(this.client);
    }

    /**
     * Gets a list of high scores for a specific game board.
     * 
     * @param boardName The board name can be a specific string or a 'LIKE'
     *            pattern using %.
     * @param recordLimit The maximum number of scores to return.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call when this method completes.
     */
    public void getHighScores(String boardName, Integer recordLimit, Object state,
            final OnCallback<ListResponse<GameScore>> callback) {
        if (this.gameDataModel != null) {
            this.gameDataModel.getHighScores(boardName, recordLimit, state, callback);
        }
    }

    /**
     * Gets a list of high scores for a specific game board.
     * 
     * @param boardName The board name can be a specific string or a 'LIKE'
     *            pattern using %.
     * @param callback The callback to call when this method completes.
     */
    public void getHighScores(String boardName, final OnCallback<ListResponse<GameScore>> callback) {
        getHighScores(boardName, 100, null, callback);
    }
    
    /**
     * Gets a list of low scores for a specific game board.
     * 
     * @param boardName The board name can be a specific string or a 'LIKE'
     *            pattern using %.
     * @param recordLimit The maximum number of scores to return.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call when this method completes.
     */
    public void getLowScores(String boardName, Integer recordLimit, Object state,
            final OnCallback<ListResponse<GameScore>> callback) {
        if (this.gameDataModel != null) {
            this.gameDataModel.getLowScores(boardName, recordLimit, state, callback);
        }
    }

    /**
     * Gets a list of low scores for a specific game board.
     * 
     * @param boardName The board name can be a specific string or a 'LIKE'
     *            pattern using %.
     * @param callback The callback to call when this method completes.
     */
    public void getLowScores(String boardName, final OnCallback<ListResponse<GameScore>> callback) {
        getLowScores(boardName, 100, null, callback);
    }
    
    /**
     * Search for game scores based on a number of different parameters.
     * @param user Optionally limit the search to a specific user. null if not limiting the search.
     * @param distanceInMeters Optionally specify a distance from a lat/long to search on. By default this is ignored.
     * @param latitude Optional latitude where we can start the search.
     * @param longitude Optional longitude where we can start the search.
     * @param recordLimit Optionally limit the number of records returned by this search.
     * @param boardName Optionally filter on a specific board name.
     * @param daysOld Optionally only return scores that are X number of days old.
     * @param minimumScore Optionally only return scores that are above a certain minimum score.
     * @param appTag Optionally return only scores that have a certain app tag.
     * @param state An optional user defined object that will be passed to the callback.
     * @param callback The callback to call when this method completes.
     */
    public void findScores(User user, Integer distanceInMeters, double latitude, double longitude, Integer recordLimit, 
            String boardName, Integer daysOld, Integer minimumScore, String appTag, Object state, final OnCallback<ListResponse<GameScore>> callback)
    {
        if(this.gameDataModel != null)
        {
            this.gameDataModel.findScores(user, distanceInMeters, latitude, longitude, recordLimit, boardName, daysOld, 
                    minimumScore, appTag, state, callback);
        }
    }
}
