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

public class MetadataItemDataResponse {
    @SerializedName("data")
    public List<MetadataValueData> data;

    public static class MetadataValueData {
        @SerializedName("metaValue")
        public String metaValue;

        @SerializedName("metaKey")
        public String metaKey;

        @SerializedName("metaLatitude")
        public String metaLatitude;

        @SerializedName("metaLongitude")
        public String metaLongitude;

        @SerializedName("lastUpdateDate")
        public String lastUpdateDate;
    }
}
