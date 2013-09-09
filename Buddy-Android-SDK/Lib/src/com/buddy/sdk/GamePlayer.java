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

import com.buddy.sdk.json.responses.GamePlayerDataResponse.GamePlayerData;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a game player object.
 */
public class GamePlayer {
    private String name;
    private Date createdOn;
    private String boardName;
    private String applicationTag;
    private double latitude;
    private double longitude;
    private int userID;
    private double distanceInKilometers = 0;
    private double distanceInMeters = 0;
    private double distanceInMiles = 0;
    private double distanceInYards = 0;
    private String rank;

    GamePlayer(BuddyClient client, AuthenticatedUser user, GamePlayerData info) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");
        if (info == null)
            throw new IllegalArgumentException("info can't be null.");
        if (user == null)
            throw new IllegalArgumentException("user can't be null.");

        this.userID = user.getId();

        this.applicationTag = info.applicationTag;
        this.boardName = info.playerBoardName;
        this.createdOn = Utils.convertDateString(info.playerDate, "MM/dd/yyyy hh:mm:ss aa");
        this.latitude = Utils.parseDouble(info.playerLatitude);
        this.longitude = Utils.parseDouble(info.playerLongitude);
        this.name = info.playerName;
        this.rank = info.playerRank;

        if (!Utils.isNullOrEmpty(info.distanceInKilometers)) {
            this.distanceInKilometers = Utils.parseDouble(info.distanceInKilometers);
        }
        if (!Utils.isNullOrEmpty(info.distanceInMeters)) {
            this.distanceInMeters = Utils.parseDouble(info.distanceInMeters);
        }
        if (!Utils.isNullOrEmpty(info.distanceInMiles)) {
            this.distanceInMiles = Utils.parseDouble(info.distanceInMiles);
        }
        if (!Utils.isNullOrEmpty(info.distanceInYards)) {
            this.distanceInYards = Utils.parseDouble(info.distanceInYards);
        }
    }

    /**
     * Gets the name of the player.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the date the player was created.
     */
    public Date getCreatedOn() {
        return this.createdOn;
    }

    /**
     * Gets the name of the board the player belongs to.
     */
    public String getBoardName() {
        return this.boardName;
    }

    /**
     * Gets the optional application tag for the player.
     */
    public String getApplicationTag() {
        return this.applicationTag;
    }

    /**
     * Gets the latitude where the player was created.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Gets the longitude where the player was created.
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Gets the UserID of the user this player is tied to.
     */
    public int getUserID() {
        return this.userID;
    }

    /**
     * Gets the distance in kilometers from the given origin in the Metadata
     * Search method.
     */
    public double getDistanceInKilometers() {
        return this.distanceInKilometers;
    }

    /**
     * Gets the distance in meters from the given origin in the Metadata Search
     * method.
     */
    public double getDistanceInMeters() {
        return this.distanceInMeters;
    }

    /**
     * Gets the distance in miles from the given origin in the Metadata Search
     * method.
     */
    public double getDistanceInMiles() {
        return this.distanceInMiles;
    }

    /**
     * Gets the distance in yards from the given origin in the Metadata Search
     * method.
     */
    public double getDistanceInYards() {
        return this.distanceInYards;
    }

    /**
     * Gets the rank of the player.
     */
    public String getRank() {
        return this.rank;
    }
}
