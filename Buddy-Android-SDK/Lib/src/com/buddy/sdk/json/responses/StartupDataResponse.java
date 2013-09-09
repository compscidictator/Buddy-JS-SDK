/*
 * Copyright (C) 2012 Buddy Platform, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://apache.org/licenses/LICENSE-2.0 
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

public class StartupDataResponse {
    @SerializedName("data")
    public List<StartupData> data;

    public static class StartupData {
        @SerializedName("startupID")
        public String startupID;
        @SerializedName("startupName")
        public String startupName;
        @SerializedName("streetAddress")
        public String streetAddress;
        @SerializedName("city")
        public String city;
        @SerializedName("state")
        public String state;
        @SerializedName("zipPostal")
        public String zipPostal;
        @SerializedName("phoneNumber")
        public String phoneNumber;
        @SerializedName("metroLocation")
        public String metroLocation;
        @SerializedName("homePageURL")
        public String homePageURL;
        @SerializedName("employeeCount")
        public String employeeCount;
        @SerializedName("totalFundingRaised")
        public String totalFundingRaised;
        @SerializedName("fundingSource")
        public String fundingSource;
        @SerializedName("crunchBaseUrl")
        public String crunchBaseUrl;
        @SerializedName("customData")
        public String customData;
        @SerializedName("industry")
        public String industry;
        @SerializedName("logoURL")
        public String logoURL;
        @SerializedName("twitterURL")
        public String twitterURL;
        @SerializedName("facebookURL")
        public String facebookURL;
        @SerializedName("linkedinURL")
        public String linkedinURL;
        @SerializedName("centerLat")
        public String centerLat;
        @SerializedName("centerLong")
        public String centerLong;
        @SerializedName("distanceInMiles")
        public String distanceInMiles;
        @SerializedName("distanceInKilometers")
        public String distanceInKilometers;
        @SerializedName("distanceInMeters")
        public String distanceInMeters;
        @SerializedName("distanceInYards")
        public String distanceInYards;
    }
}
