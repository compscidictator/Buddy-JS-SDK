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

public class PlacesDataResponse {
    @SerializedName("data")
    public List<PlacesData> data;

    public static class PlacesData {
        @SerializedName("geoID")
        public String geoID;
        @SerializedName("latitude")
        public String latitude;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("name")
        public String name;
        @SerializedName("address")
        public String address;
        @SerializedName("city")
        public String city;
        @SerializedName("postalState")
        public String postalState;
        @SerializedName("postalZip")
        public String postalZip;
        @SerializedName("region")
        public String region;
        @SerializedName("telephone")
        public String telephone;
        @SerializedName("fax")
        public String fax;
        @SerializedName("webSite")
        public String webSite;
        @SerializedName("touchedDate")
        public String touchedDate;
        @SerializedName("createdDate")
        public String createdDate;
        @SerializedName("categoryID")
        public String categoryID;
        @SerializedName("categoryName")
        public String categoryName;
        @SerializedName("distanceInMiles")
        public String distanceInMiles;
        @SerializedName("distanceInKilometers")
        public String distanceInKilometers;
        @SerializedName("distanceInMeters")
        public String distanceInMeters;
        @SerializedName("distanceInYards")
        public String distanceInYards;
        @SerializedName("userTagData")
        public String userTagData;
        @SerializedName("appTagData")
        public String appTagData;
        @SerializedName("shortID")
        public String shortID;
    }

}
