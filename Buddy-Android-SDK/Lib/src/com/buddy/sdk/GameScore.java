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

import com.buddy.sdk.json.responses.GameScoreDataResponse.GameScoreData;
import com.buddy.sdk.utils.Utils;

/**
 * Represents an object that describes a single game score entry.
 */
public class GameScore {
    private String boardName;
    private Date addedOn;
    private double latitude;
    private double longitude;
    private String rank;
    private double score;
    private int userID;
    private String userName;
    private String applicationTag;

    GameScore(BuddyClient client, GameScoreData score) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");
        if (score == null)
            throw new IllegalArgumentException("score can't be null.");

        this.boardName = score.scoreBoardName;
        this.addedOn = Utils.convertDateString(score.scoreDate, "MM/dd/yyyy hh:mm:ss aa");
        this.latitude = Utils.parseDouble(score.scoreLatitude);
        this.longitude = Utils.parseDouble(score.scoreLongitude);
        this.rank = score.scoreRank;
        this.score = Utils.parseDouble(score.scoreValue);
        this.userID = Utils.parseInt(score.userId);
        this.userName = score.userName;
        this.applicationTag = score.applicationTag;
    }

    /**
     * Gets the name of the board this score is related to.
     */
    public String getBoardName() {
        return this.boardName;
    }

    /**
     * Gets the date this score was added.
     */
    public Date getAddedOn() {
        return this.addedOn;
    }

    /**
     * Gets the optional latitude for this score.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Gets the optional longitude for this score.
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Gets the optional rank value for this score.
     */
    public String getRank() {
        return this.rank;
    }

    /**
     * Gets the numeric value of the score entry.
     */
    public double getScore() {
        return this.score;
    }

    /**
     * Gets the user ID that owns this score.
     */
    public int getUserID() {
        return this.userID;
    }

    /**
     * Gets the user name of the user who owns this score.
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Gets the optional application tag for this score.
     */
    public String getAppTag() {
        return this.applicationTag;
    }
}
