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

public class StoreItemDataResponse {
    @SerializedName("data")
    public List<StoreItemData> data;

    public static class StoreItemData {
        @SerializedName("storeItemID")
        public String storeItemID;
        @SerializedName("itemName")
        public String itemName;
        @SerializedName("itemIconUri")
        public String itemIconUri;
        @SerializedName("itemDownloadUri")
        public String itemDownloadUri;
        @SerializedName("itemPreviewUri")
        public String itemPreviewUri;
        @SerializedName("itemFreeFlag")
        public String itemFreeFlag;
        @SerializedName("itemDescription")
        public String itemDescription;
        @SerializedName("itemDateTime")
        public String itemDateTime;
        @SerializedName("itemAvailableFlag")
        public String itemAvailableFlag;
        @SerializedName("itemCost")
        public String itemCost;
        @SerializedName("appData")
        public String appData;
        @SerializedName("customItemID")
        public String customItemID;
    }
}
