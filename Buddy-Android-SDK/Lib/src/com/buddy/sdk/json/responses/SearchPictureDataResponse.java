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

public class SearchPictureDataResponse {
    @SerializedName("data")
    public List<SearchPictureData> data;

    public static class SearchPictureData {
        @SerializedName("photoID")
        public String photoId;
        @SerializedName("fullPhotoURL")
        public String fullPhotoUrl;
        @SerializedName("thumbnailPhotoURL")
        public String thumbnailUrl;
        @SerializedName("photoAdded")
        public String photoAdded;
        @SerializedName("userProfileID")
        public String userProfileId;
        @SerializedName("distanceInMeters")
        public String distanceInMeters;
        @SerializedName("distanceInYards")
        public String distanceInYards;
        @SerializedName("distanceInMiles")
        public String distanceInMiles;
        @SerializedName("distanceInKilometers")
        public String distanceInKilometers;
        @SerializedName("photoAlbumName")
        public String albumName;
        @SerializedName("photoAlbumThumbnail")
        public String albumThumbnail;
        @SerializedName("latitude")
        public String latitude;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("applicationTag")
        public String applicationTag;
    }

}
