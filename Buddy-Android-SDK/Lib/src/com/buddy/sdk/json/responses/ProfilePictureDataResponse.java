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

public class ProfilePictureDataResponse {
    @SerializedName("data")
    public List<ProfilePictureData> data;

    public static class ProfilePictureData {
        @SerializedName("photoID")
        public String photoID;

        @SerializedName("photoComment")
        public String photoComment;

        @SerializedName("fullPhotoURL")
        public String fullPhotoURL;

        @SerializedName("thumbnailPhotoURL")
        public String thumbnailPhotoURL;

        @SerializedName("addedDateTime")
        public String addedDateTime;

        @SerializedName("latitude")
        public String latitude;

        @SerializedName("longitude")
        public String longitude;

        @SerializedName("userProfileID")
        public String userProfileID;
    }
}
