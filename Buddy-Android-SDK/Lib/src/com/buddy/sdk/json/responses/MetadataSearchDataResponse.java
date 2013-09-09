/* Copyright (C) 2012 Buddy Platform, Inc.
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

public class MetadataSearchDataResponse {
    @SerializedName("data")
    public List<MetadataSearchData> data;

    public static class MetadataSearchData {
        @SerializedName("metaKey")
        public String metaKey;

        @SerializedName("metaValue")
        public String metaValue;

        @SerializedName("lastUpdateDate")
        public String lastUpdateDate;

        @SerializedName("metaLatitude")
        public String metaLatitude;

        @SerializedName("metaLongitude")
        public String metaLongitude;

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
