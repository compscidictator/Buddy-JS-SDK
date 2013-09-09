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

import java.util.Map;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Utils;

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
 *            {@code user.getGameStates().add("MyGameState", "MyGameStateValue", "", null, new OnCallback<Response<Boolean>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<Boolean> response, Object state)}
 *                <code>{</code>
 *                    {@code user.getGameStates().get("MyGameState", null, new OnCallback<Response<GameState>>()}
 *                    <code>{</code>
 *                        {@code public void OnResponse(Response<GameState> response, Object state)}
 *                        <code>{</code>
 *                            {@code GameState state = response.getresult();}
 *                        <code>}</code>
 *                    <code>});</code>
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class GameStates {
    private BuddyClient client;
    private User user;
    private GamesDataModel gameDataModel = null;

    GameStates(BuddyClient client, User user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null");
        if (user == null)
            throw new IllegalArgumentException("user can't be null");

        this.client = client;
        this.user = user;

        this.gameDataModel = new GamesDataModel(this.client, null, this.user);
    }

    /**
     * Adds a key/value pair to the User GameState.
     * 
     * @param gameStateKey The game state key.
     * @param gameStateValue The value to persist.
     * @param appTag An optional application tag for this score.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call when this method completes.
     */
    public void add(String gameStateKey, String gameStateValue, String appTag, Object state,
            OnCallback<Response<Boolean>> callback) {
        if (Utils.isNullOrEmpty(gameStateKey))
            throw new IllegalArgumentException("gameStateKey can not be null or empty.");
        if (gameStateValue == null)
            throw new IllegalArgumentException("gameStateValue can not be null.");

        if (this.gameDataModel != null) {
            this.gameDataModel.add(gameStateKey, gameStateValue, appTag, state, callback);
        }
    }

    /**
     * Adds a key/value pair to the user's GameState.
     * 
     * @param gameStateKey The game state key.
     * @param gameStateValue The value to persist.
     * @param callback The callback to call when this method completes.
     */
    public void add(String gameStateKey, String gameStateValue,
            OnCallback<Response<Boolean>> callback) {
        add(gameStateKey, gameStateValue, "", "", callback);
    }

    /**
     * Get a GameState item with a key. The key can't be null or an empty
     * string.
     * 
     * @param gameStateKey The gameStateKey to use to reference the GameState
     *            item.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call when this method completes.
     */
    public void get(String gameStateKey, Object state, OnCallback<Response<GameState>> callback) {
        if (Utils.isNullOrEmpty(gameStateKey))
            throw new IllegalArgumentException("gameStateKey can not be null or empty.");

        if (this.gameDataModel != null) {
            this.gameDataModel.get(gameStateKey, state, callback);
        }
    }

    /**
     * Get all GameState keys and values.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call when this method completes.
     */
    public void getAll(Object state, OnCallback<Response<Map<String, GameState>>> callback) {
        if (this.gameDataModel != null) {
            this.gameDataModel.getAll(state, callback);
        }
    }

    /**
     * Remove a GameState key.
     * 
     * @param gameStateKey The key to remove from the GameState.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call when this method completes.
     */
    public void remove(String gameStateKey, Object state, OnCallback<Response<Boolean>> callback) {
        if (Utils.isNullOrEmpty(gameStateKey))
            throw new IllegalArgumentException("gameStateKey can not be null or empty.");

        if (this.gameDataModel != null) {
            this.gameDataModel.remove(gameStateKey, state, callback);
        }
    }

    /**
     * Update a GameState value.
     * 
     * @param gameStateKey The key to update.
     * @param gameStateValue The value to update.
     * @param newAppTag An optional new application tag for the value.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call when this method completes.
     */
    public void update(String gameStateKey, String gameStateValue, String newAppTag, Object state,
            OnCallback<Response<Boolean>> callback) {
        if (Utils.isNullOrEmpty(gameStateKey))
            throw new IllegalArgumentException("gameStateKey can not be null or empty.");
        if (gameStateValue == null)
            throw new IllegalArgumentException("gameStateValue can not be null or empty.");

        if (this.gameDataModel != null) {
            this.gameDataModel.update(gameStateKey, gameStateValue, newAppTag, state, callback);
        }
    }

    /**
     * Update a GameState value.
     * 
     * @param gameStateKey The key to update.
     * @param gameStateValue The value to update.
     * @param callback The callback to call when this method completes.
     */
    public void update(String gameStateKey, String gameStateValue,
            OnCallback<Response<Boolean>> callback) {
        update(gameStateKey, gameStateValue, "", null, callback);
    }
}
