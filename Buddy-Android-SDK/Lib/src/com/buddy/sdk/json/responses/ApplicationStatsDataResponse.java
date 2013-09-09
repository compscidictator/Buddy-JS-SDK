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

public class ApplicationStatsDataResponse {
    @SerializedName("data")
    public List<ApplicationStatsData> data;

    public static class ApplicationStatsData {
        @SerializedName("totalUsers")
        public String TotalUsers;

        @SerializedName("totalPhotos")
        public String TotalPhotos;

        @SerializedName("totalUserCheckins")
        public String TotalUserCheckins;

        @SerializedName("totalUserMetadata")
        public String TotalUserMetadata;

        @SerializedName("totalAppMetadata")
        public String TotalAppMetadata;

        @SerializedName("totalFriends")
        public String TotalFriends;

        @SerializedName("totalAlbums")
        public String TotalAlbums;

        @SerializedName("totalCrashes")
        public String TotalCrashes;

        @SerializedName("totalMessages")
        public String TotalMessages;

        @SerializedName("totalPushMessages")
        public String TotalPushMessages;

        @SerializedName("totalGamePlayers")
        public String TotalGamePlayers;

        @SerializedName("totalGameScores")
        public String TotalGameScores;

        @SerializedName("totalDeviceInformation")
        public String TotalDeviceInformation;
    }

}
