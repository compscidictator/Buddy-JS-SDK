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

public class MessageGroupDataResponse {
    @SerializedName("data")
    public List<MessageGroupData> data;

    public static class MessageGroupData {
        @SerializedName("chatGroupID")
        public String chatGroupID;

        @SerializedName("chatGroupName")
        public String chatGroupName;

        @SerializedName("ownerUserID")
        public String ownerUserID;

        @SerializedName("membershipCounter")
        public String membershipCounter;

        @SerializedName("applicationTag")
        public String applicationTag;

        @SerializedName("createdDateTime")
        public String createdDateTime;

        @SerializedName("lastMessageDateTime")
        public String lastMessageDateTime;

        @SerializedName("lastMessageApplicationTag")
        public String lastMessageApplicationTag;

        @SerializedName("lastMessageLatitude")
        public String lastMessageLatitude;

        @SerializedName("lastMessageLongitude")
        public String lastMessageLongitude;

        @SerializedName("lastMessageText")
        public String lastMessageText;

        @SerializedName("lastMessagePhotoURL")
        public String lastMessagePhotoURL;

        @SerializedName("memberUserIDList")
        public String memberUserIDList;
    }
}
