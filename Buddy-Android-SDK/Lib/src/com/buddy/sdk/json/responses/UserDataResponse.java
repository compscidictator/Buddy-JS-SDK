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

public class UserDataResponse {
    @SerializedName("data")
    public List<UserData> data;

    public static class UserData {
        @SerializedName("rowNumber")
        public String RowNumber;
        @SerializedName("userID")
        public String userID;
        @SerializedName("userName")
        public String userName;
        @SerializedName("userGender")
        public String userGender;
        @SerializedName("age")
        public String age;
        @SerializedName("createdDate")
        public String createdDate;
        @SerializedName("lastLoginDate")
        public String lastLoginDate;
        @SerializedName("statusID")
        public String statusID;
        @SerializedName("profilePictureUrl")
        public String profilePictureURL;
        @SerializedName("userLatitude")
        public String userLatitude;
        @SerializedName("userLongitude")
        public String userLongitude;
        @SerializedName("locationFuzzing")
        public String locationFuzzing;
        @SerializedName("celebMode")
        public String celebMode;
        @SerializedName("userEmail")
        public String userEmail;
        @SerializedName("userApplicationTag")
        public String applicationTag;

    }
}
