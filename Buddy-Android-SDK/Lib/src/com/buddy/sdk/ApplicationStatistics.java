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

import com.buddy.sdk.json.responses.ApplicationStatsDataResponse.ApplicationStatsData;

public class ApplicationStatistics {
    private String totalUsers;
    private String totalPhotos;
    private String totalUserCheckins;
    private String totalUserMetadata;
    private String totalAppMetadata;
    private String totalFriends;
    private String totalAlbums;
    private String totalCrashes;
    private String totalMessages;
    private String totalPushMessages;
    private String totalGamePlayers;
    private String totalGameScores;
    private String totalDeviceInformation;

    ApplicationStatistics(ApplicationStatsData appStats, BuddyClient client) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");

        this.totalUsers = appStats.TotalUsers;
        this.totalDeviceInformation = appStats.TotalDeviceInformation;
        this.totalCrashes = appStats.TotalCrashes;
        this.totalAppMetadata = appStats.TotalAppMetadata;
        this.totalAlbums = appStats.TotalAlbums;
        this.totalFriends = appStats.TotalFriends;
        this.totalGamePlayers = appStats.TotalGamePlayers;
        this.totalGameScores = appStats.TotalGameScores;
        this.totalMessages = appStats.TotalMessages;
        this.totalPhotos = appStats.TotalPhotos;
        this.totalPushMessages = appStats.TotalPushMessages;
        this.totalUserCheckins = appStats.TotalUserCheckins;
        this.totalUserMetadata = appStats.TotalUserMetadata;
    }

    /**
	 * 
	 */
    public String getTotalUsers() {
        return this.totalUsers;
    }

    /**
     * This is the combined total of all profile photos and photo album photos
     * for the application.
     */
    public String getTotalPhotos() {
        return this.totalPhotos;
    }

    /**
	 * 
	 */
    public String getTotalUserCheckins() {
        return this.totalUserCheckins;
    }

    /**
	 * 
	 */
    public String getTotalUserMetadata() {
        return this.totalUserMetadata;
    }

    /**
	 * 
	 */
    public String getTotalAppMetadata() {
        return this.totalAppMetadata;
    }

    /**
	 * 
	 */
    public String getTotalFriends() {
        return this.totalFriends;
    }

    /**
	 * 
	 */
    public String getTotalAlbums() {
        return this.totalAlbums;
    }

    /**
	 * 
	 */
    public String getTotalCrashes() {
        return this.totalCrashes;
    }

    /**
	 * 
	 */
    public String getTotalMessages() {
        return this.totalMessages;
    }

    /**
     * This is the combined total of all push notifications sent for all
     * platforms supported.
     */
    public String getTotalPushMessages() {
        return this.totalPushMessages;
    }

    /**
	 * 
	 */
    public String getTotalGamePlayers() {
        return this.totalGamePlayers;
    }

    /**
	 * 
	 */
    public String getTotalGameScores() {
        return this.totalGameScores;
    }

    /**
	 * 
	 */
    public String getTotalDeviceInformation() {
        return this.totalDeviceInformation;
    }
}
