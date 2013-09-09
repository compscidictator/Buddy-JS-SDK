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
 * Represents a class that can be used to add, retrieve or delete game scores
 * for any user in the system.
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
 *                            {@code GameScore score = response.getresult();}
 *                        <code>}</code>
 *                    <code>});</code>
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class GameScores {
    private AuthenticatedUser authUser;
    private BuddyClient client;
    private User user;
    private GamesDataModel gameDataModel = null;

    GameScores(BuddyClient client, AuthenticatedUser authUser, User user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");

        this.client = client;
        this.authUser = authUser;
        this.user = user;

        this.gameDataModel = new GamesDataModel(this.client, this.authUser, this.user);
    }

    /**
     * Return all score entries for this user.
     * 
     * @param recordLimit Limit the number of entries returned.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call when this method completes.
     */
    public void getAll(Integer recordLimit, Object state,
            final OnCallback<ListResponse<GameScore>> callback) {
        if (this.gameDataModel != null) {
            this.gameDataModel.getAll(recordLimit, state, callback);
        }
    }

    /**
     * Return all score entries for this user.
     * 
     * @param callback The callback to call when this method completes.
     */
    public void getAll(final OnCallback<ListResponse<GameScore>> callback) {
        getAll(100, null, callback);
    }

    /**
     * Add a new score for this user.
     * 
     * @param score The numeric value of the score.
     * @param board The optional name of the game board.
     * @param rank The optional rank for this score. This can be used for adding
     *            badges, achievements, etc.
     * @param latitude The optional latitude for this score.
     * @param longitude The optional longitude for this score.
     * @param oneScorePerPlayer The optional one-score-per-player parameter.
     *            Setting this to true will always update the score for this
     *            user, instead of creating a new one.
     * @param appTag An optional application tag for this score.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call when this method completes.
     */
    public void add(double score, String board, String rank, double latitude, double longitude,
            boolean oneScorePerPlayer, String appTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        if (this.gameDataModel != null) {
            this.gameDataModel.add(latitude, longitude, rank, score, board, appTag,
                    oneScorePerPlayer, state, callback);
        }
    }
    
	/**
	 * Add a new score for this user.
	 * 
	 * @param score The numeric value of the score.
	 * @param callback The callback to call when this method completes.
	 */
	public void add(double score, final OnCallback<Response<Boolean>> callback) {
	    add(score, "", "", 0.0, 0.0, false, "", null, callback);
	}

    /**
     * Delete all scores for this user.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call when this method completes.
     */
    public void deleteAll(Object state, final OnCallback<Response<Boolean>> callback) {
        if (this.gameDataModel != null) {
            this.gameDataModel.deleteAll(state, callback);
        }
    }
}
