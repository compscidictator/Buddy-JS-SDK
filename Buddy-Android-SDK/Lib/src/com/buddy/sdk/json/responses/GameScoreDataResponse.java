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

public class GameScoreDataResponse {
    @SerializedName("data")
    public List<GameScoreData> data;

    public static class GameScoreData {
        @SerializedName("scoreID")
        public String scoreId;

        @SerializedName("userID")
        public String userId;

        @SerializedName("userName")
        public String userName;

        @SerializedName("scoreValue")
        public String scoreValue;

        @SerializedName("scoreLatitude")
        public String scoreLatitude;

        @SerializedName("scoreLongitude")
        public String scoreLongitude;

        @SerializedName("scoreBoardName")
        public String scoreBoardName;

        @SerializedName("scoreDate")
        public String scoreDate;

        @SerializedName("scoreRank")
        public String scoreRank;

        @SerializedName("applicationTag")
        public String applicationTag;
    }

}
