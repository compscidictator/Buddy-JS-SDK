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

import com.buddy.sdk.json.responses.GameStateDataResponse.GameStateData;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a single game state object.
 */
public class GameState {
    private String applicationTag;
    private Date addedOn;
    private int id;
    private String key;
    private String value;

    GameState(GameStateData gameState) {
        this.applicationTag = gameState.applicationTag;
        this.key = gameState.stateKey;
        this.id = Utils.parseInt(gameState.stateId);
        this.value = gameState.stateValue;
        this.addedOn = Utils.convertDateString(gameState.stateDateTime, "MM/dd/yyyy hh:mm:ss aa");
    }

    /**
     * Gets the optional application tag for this GameState.
     */
    public String getAppTag() {
        return this.applicationTag;
    }

    /**
     * Gets the date this GameState was created.
     */
    public Date getAddedOn() {
        return this.addedOn;
    }

    /**
     * ID of the GameState record.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get the key for this GameState object.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Gets the value for this GameState object.
     */
    public String getValue() {
        return this.value;
    }
}
