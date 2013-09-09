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

package com.buddy.sdk;

import java.io.InputStream;
import java.util.Date;

import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.json.responses.BlobDataResponse.BlobData;
import com.buddy.sdk.Callbacks.OnCallback;

/**
 * @author RyanB
 *
 */
public class Blob {
	private BlobDataModel blobDataModel = null;
	
	private long id;
	private String friendlyName;
	private String mimeType;
	private int fileSize;
	private String appTag;
	private long owner;
	private double latitude;
	private double longitude;
	private Date uploadDate;
	private Date lastTouchDate;
	
	Blob(BuddyClient client, AuthenticatedUser user, BlobData data){
		blobDataModel = new BlobDataModel(client, user);
		
		this.id = Long.parseLong(data.blobID);
		this.friendlyName = data.friendlyName;
		this.mimeType = data.mimeType;
		this.fileSize = Integer.parseInt(data.fileSize);
		this.appTag = data.appTag;
		this.owner = Long.parseLong(data.owner);
		this.latitude = Double.parseDouble(data.latitude);
		this.longitude = Double.parseDouble(data.longitude);
		this.uploadDate = Utils.convertDateString(data.uploadDate);
		this.lastTouchDate = Utils.convertDateString(data.lastTouchDate);
	}
	/**
	 * Deletes this Blob
	 * 
	 * @param callback The callback to call when this method completes.
	 */
	public void delete(OnCallback<Response<Boolean>> callback){
		if(this.blobDataModel != null){
			this.blobDataModel.delete(this.id, callback);
		}		
	}
	
	/**
	 * Edits this Blob
	 * 
	 * @param friendlyName The new human readable name of this Blob.
	 * @param appTag The new metadata to store with this Blob.
	 * @param callback The callback to call when this method completes.
	 */
	public void edit(String friendlyName, String appTag, OnCallback<Response<Boolean>> callback){
		if(this.blobDataModel != null){
			this.blobDataModel.editInfo(this.id, friendlyName, appTag, callback);
		}
	}
	
	/**
	 * Gets a stream of this Blob.
	 * 
	 * @param callback The callback to call when this method completes.
	 */
	public void get(OnCallback<Response<InputStream>> callback){
		if(this.blobDataModel != null){
			this.blobDataModel.get(id, callback);
		}
	}
	
	/**
	 * Gets the ID of this Blob.
	 */
	public long getId(){
		return this.id;
	}
	
	/**
	 * Gets the FriendlyName of this Blob.
	 */
	public String getFriendlyName(){
		return this.friendlyName;
	}
	
	/**
	 * Gets the MIMEType of this Blob.
	 */
	public String getMimeType(){
		return this.mimeType;
	}
	
	/**
	 * Gets the FileSize of this Blob in Bytes.
	 */
	public int getFileSize(){
		return this.fileSize;
	}

	/**
	 * Gets the AppTage of this Blob.
	 */
	public String getAppTag(){
		return this.appTag;
	}
	
	/**
	 * Gets the Owner of this Blob.
	 */
	public long getOwner(){
		return this.owner;
	}
	
	/**
	 * Gets the Latitude of this Blob.
	 */
	public double getLatitude(){
		return this.latitude;
	} 
	
	/**
	 * Gets the Longitude of this Blob.
	 */
	public double getLongtidue(){
		return this.longitude;
	}
	
	/**
	 * Gets the UploadDate of this Blob.
	 */
	public Date getUploadDate(){
		return this.uploadDate;
	}
	
	/**
	 * Gets the last time this Blob was Edited or uploaded.
	 */
	public Date getLastTouchDate(){
		return this.lastTouchDate;
	}
}