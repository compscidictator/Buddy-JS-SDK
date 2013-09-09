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

/**
 * @author RyanB
 *
 */
public class BlobDataResponse {
	@SerializedName("data")
	public List<BlobData> data;
	
	public static class BlobData {
		@SerializedName("blobID")
		public String blobID;
		@SerializedName("friendlyName")
		public String friendlyName;
		@SerializedName("mimeType")
		public String mimeType;
		@SerializedName("fileSize")
		public String fileSize;
		@SerializedName("appTag")
		public String appTag;
		@SerializedName("owner")
		public String owner;
		@SerializedName("latitude")
		public String latitude;
		@SerializedName("longitude")
		public String longitude;
		@SerializedName("uploadDate")
		public String uploadDate;
		@SerializedName("lastTouchDate")
		public String lastTouchDate;
	}
}
