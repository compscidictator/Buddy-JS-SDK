/*
 * Copyright (C) 2012 Buddy Platform, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.buddy.com/terms-of-service/ 
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

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.json.responses.VideoDataResponse.VideoData;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Utils;

/**
 * @author RyanB
 *
 */
public class Video {
	private VideoDataModel videoDataModel = null;
	
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
	private String videoUrl;
	
	Video(BuddyClient client, AuthenticatedUser user, VideoData data){
		videoDataModel = new VideoDataModel(client, user);
		
		this.id = Long.parseLong(data.videoID);
		this.friendlyName = data.friendlyName;
		this.mimeType = data.mimeType;
		this.fileSize = Integer.parseInt(data.fileSize);
		this.appTag = data.appTag;
		this.owner = Long.parseLong(data.owner);
		this.latitude = Double.parseDouble(data.latitude);
		this.longitude = Double.parseDouble(data.longitude);
		this.uploadDate = Utils.convertDateString(data.uploadDate);
		this.lastTouchDate = Utils.convertDateString(data.lastTouchDate);
		this.videoUrl = data.videoUrl;
	}
	
	/**
	 * Deletes this Video
	 * 
	 * @param callback The callback to call when this method completes.
	 */
	public void delete(OnCallback<Response<Boolean>> callback){
		if(this.videoDataModel != null){
			this.videoDataModel.delete(this.id, callback);
		}		
	}
	
	/**
	 * Edits this Video
	 * 
	 * @param friendlyName The new human readable name of this Video.
	 * @param appTag The new metadata to store with this Video.
	 * @param callback The callback to call when this method completes.
	 */
	public void edit(String friendlyName, String appTag, OnCallback<Response<Boolean>> callback){
		if(this.videoDataModel != null){
			this.videoDataModel.editInfo(this.id, friendlyName, appTag, callback);
		}
	}
	
	/**
	 * Gets a stream of this Video.
	 * 
	 * @param callback The callback to call when this method completes.
	 */
	public void get(OnCallback<Response<InputStream>> callback){
		if(this.videoDataModel != null){
			this.videoDataModel.get(id, callback);
		}
	}
	
	/**
	 * Gets the ID of this Video.
	 */
	public long getId(){
		return this.id;
	}
	
	/**
	 * Gets the FriendlyName of this Video.
	 */
	public String getFriendlyName(){
		return this.friendlyName;
	}
	
	/**
	 * Gets the MIMEType of this Video.
	 */
	public String getMimeType(){
		return this.mimeType;
	}
	
	/**
	 * Gets the FileSize of this Video in Bytes.
	 */
	public int getFileSize(){
		return this.fileSize;
	}
	
	/**
	 * Gets the AppTage of this Video.
	 */
	public String getAppTag(){
		return this.appTag;
	}
	
	/**
	 * Gets the Owner of this Video.
	 */
	public long getOwner(){
		return this.owner;
	}
	
	/**
	 * Gets the Latitude of this Video.
	 */
	public double getLatitude(){
		return this.latitude;
	} 
	
	/**
	 * Gets the Longitude of this Video.
	 */
	public double getLongtidue(){
		return this.longitude;
	}
	
	/**
	 * Gets the UploadDate of this Video.
	 */
	public Date getUploadDate(){
		return this.uploadDate;
	}
	
	/**
	 * Gets the last time this Video was Edited or uploaded.
	 */
	public Date getLastTouchDate(){
		return this.lastTouchDate;
	}
	
	/**
	 * Gets the VideoURL of this Video. Good for passing to a MediaPlayer.
	 */
	public String getVideoUrl(){
		return this.videoUrl;
	}
}
