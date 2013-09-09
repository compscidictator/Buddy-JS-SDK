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

public class VirtualAlbumDataResponse {
    @SerializedName("data")
    public List<VirtualAlbumData> data;

    public static class VirtualAlbumData {
        @SerializedName("virtualAlbumID")
        public String VirtualAlbumID;

        @SerializedName("photoAlbumName")
        public String PhotoAlbumName;

        @SerializedName("userID")
        public String UserID;

        @SerializedName("photoCount")
        public String PhotoCount;

        @SerializedName("photoAlbumThumbnail")
        public String PhotoAlbumThumbnail;

        @SerializedName("createdDateTime")
        public String CreatedDateTime;

        @SerializedName("lastUpdatedDateTime")
        public String LastUpdatedDateTime;

        @SerializedName("applicationTag")
        public String ApplicationTag;
    }

}
