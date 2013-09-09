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

public class ReceiptDataResponse {
    @SerializedName("data")
    public List<ReceiptData> data;

    public class ReceiptData {
        @SerializedName("receiptHistoryID")
        public String receiptHistoryID;
        @SerializedName("storeName")
        public String storeName;
        @SerializedName("userID")
        public String userID;
        @SerializedName("historyDateTime")
        public String historyDateTime;
        @SerializedName("receiptData")
        public String receiptData;
        @SerializedName("totalCost")
        public String totalCost;
        @SerializedName("itemQuantity")
        public String itemQuantity;
        @SerializedName("appData")
        public String appData;
        @SerializedName("historyCustomTransactionID")
        public String historyCustomTransactionID;
        @SerializedName("verificationResult")
        public String verificationResult;
        @SerializedName("verificationResultData")
        public String verificationResultData;
        @SerializedName("storeItemID")
        public String storeItemID;
    }
}
