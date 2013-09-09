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

package com.buddy.sdk.json.responses;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GamePlayerDataResponse {
    @SerializedName("data")
    public List<GamePlayerData> data;

    public static class GamePlayerData {
        @SerializedName("playerID")
        public String playerID;

        @SerializedName("userID")
        public String userID;

        @SerializedName("playerLatitude")
        public String playerLatitude;

        @SerializedName("playerLongitude")
        public String playerLongitude;

        @SerializedName("playerBoardName")
        public String playerBoardName;

        @SerializedName("playerDate")
        public String playerDate;

        @SerializedName("applicationTag")
        public String applicationTag;

        @SerializedName("playerName")
        public String playerName;

        @SerializedName("userName")
        public String userName;

        @SerializedName("distanceInMiles")
        public String distanceInMiles;

        @SerializedName("distanceInKilometers")
        public String distanceInKilometers;

        @SerializedName("distanceInMeters")
        public String distanceInMeters;

        @SerializedName("distanceInYards")
        public String distanceInYards;

        @SerializedName("playerRank")
        public String playerRank;
   }
}
