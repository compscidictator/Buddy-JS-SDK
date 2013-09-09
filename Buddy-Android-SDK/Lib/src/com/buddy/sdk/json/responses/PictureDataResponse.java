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

public class PictureDataResponse {
    @SerializedName("data")
    public List<PictureData> data;

    public static class PictureData {
        @SerializedName("photoID")
        public String photoId;
        @SerializedName("albumID")
        public String albumId;
        @SerializedName("photoComment")
        public String photoComment;
        @SerializedName("fullPhotoURL")
        public String fullPhotoUrl;
        @SerializedName("thumbnailPhotoURL")
        public String thumbnailUrl;
        @SerializedName("addedDateTime")
        public String addedDateTime;
        @SerializedName("latitude")
        public String latitude;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("applicationTag")
        public String applicationTag;
        @SerializedName("shortId")
        public String shortId;
        @SerializedName("shortUrl")
        public String shortUrl;
    }

}
