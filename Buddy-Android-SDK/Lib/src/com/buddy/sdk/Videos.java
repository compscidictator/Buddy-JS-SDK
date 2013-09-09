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

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;

/**
 * Represents a class that can be used to add retrieve and manage Videos for the current user.
 * <p>
 * 
 * <pre>
 * {@code private AuthenticatedUser user = null;}
 * 
 *    {@code BuddyClient client = new BuddyClient("APPNAME", "APPPASS");}
 *    {@code client.login("username", "password", new OnCallback<Response<AuthenticatedUser>>()}
 *    <code>{</code>
 *        {@code public void OnResponse(Response<AuthenticatedUser> response, Object state)}
 *        <code>{</code>
 *            {@code user = response.getResult();}
 *            {@code user.getVideos()}
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class Videos {
	private VideoDataModel videoDataModel = null;
	protected BuddyClient client;
	protected AuthenticatedUser user;
	
	Videos(BuddyClient client, AuthenticatedUser user)
	{
		if(client == null)
		{ throw new IllegalArgumentException("client can't be null or empty."); }
		if(user == null)
		{ throw new IllegalArgumentException("user can't be null or empty."); }
		
		this.client = client;
		this.user = user;
		
		this.videoDataModel = new VideoDataModel(client, user);
	}
	
	/**
	 * Add a video for this user.
	 * 
	 * @param friendlyName A human readable name for this Video.
	 * @param appTag Additional MetaData to be stored with this Video.
	 * @param latitude The Latitude where the Video was created or uploaded.
	 * @param longitude The Longitude where the Video was created or uploaded.
	 * @param contentType The MimeType of the Video.
	 * @param videoData A stream containing the bits of the Video to be stored.
	 * @param callback The callback to call when this method completes.
	 */	
	public void add(String friendlyName, String appTag, double latitude, double longitude, String contentType,
			InputStream videoData, OnCallback<Response<String>> callback){
		if(this.videoDataModel != null){
			this.videoDataModel.add(friendlyName, appTag, latitude, longitude, contentType, videoData, callback);
		}
	}
	
	/**
	 * Edit a video for this user.
	 *  
	 * @param videoID The ID which identifies the Video to be edited.
	 * @param friendlyName The new human readable name for this Video.
	 * @param appTag The new MetaData to be stored with this Video.
	 * @param callback The callback to call when this method completes.
	 */
	public void edit(long videoID, String friendlyName, String appTag, OnCallback<Response<Boolean>> callback){
		if(this.videoDataModel != null){
			this.videoDataModel.editInfo(videoID, friendlyName, appTag, callback);
		}
	}
	
	/**
	 * Get the Video object for the given videoID.
	 * 
	 * @param videoID The ID which identifies the Video to be retrieved.
	 * @param callback The callback to call when this method completes.
	 */
	public void getInfo(long videoID, OnCallback<Response<Video>> callback){
		if(this.videoDataModel != null){
			this.videoDataModel.getInfo(videoID, callback);			
		}		
	}
	
	/**
	 * Get a list of the Videos matching your given search criteria.
	 * 
	 * @param videoID The ID of the video to retrieve
	 * @param callback The callback to call when this method completes.
	 */
	public void get(long videoID, OnCallback<Response<InputStream>> callback){
		if(this.videoDataModel != null){
			this.videoDataModel.get(videoID, callback);			
		}		
	}
	
	/**
	 * Get a list of the Videos matching your given search criteria.
	 * 
	 * @param friendlyName A string to search the friendlyName field by.
	 * @param mimeType A string to search the mimeType field by.
	 * @param appTag A string to search the appTag field by.
	 * @param searchDistance The distance around the given point to search.
	 * @param searchLatitude The latitude around which to search.
	 * @param searchLongitude The longitude around which to search.
	 * @param timeFilter The number of days in the past to search.
	 * @param recordLimit The maximum amount of values to return.
	 * @param callback The callback to call when this method completes.
	 */
	public void searchVideos(String friendlyName, String mimeType, String appTag, int searchDistance, double searchLatitude, double searchLongitude,
			int timeFilter, int recordLimit, OnCallback<ListResponse<Video>> callback){
		if(this.videoDataModel != null){
			this.videoDataModel.searchVideos(friendlyName, mimeType, appTag, searchDistance, 
					searchLatitude, searchLongitude, timeFilter, recordLimit, callback);
		}		
	}
	
	/**
	 * Get a list of the Videos belonging to the current user which match your given search criteria.
	 * 
	 * @param friendlyName A string to search the friendlyName field by.
	 * @param mimeType A string to search the mimeType field by.
	 * @param appTag A string to search the appTag field by.
	 * @param searchDistance The distance around the given point to search.
	 * @param searchLatitude The latitude around which to search.
	 * @param searchLongitude The longitude around which to search.
	 * @param timeFilter The number of days in the past to search.
	 * @param recordLimit The maximum amount of values to return.
	 * @param callback The callback to call when this method completes.
	 */
	public void searchMyVideos(String friendlyName, String mimeType, String appTag, int searchDistance, double searchLatitude, double searchLongitude,
			int timeFilter, int recordLimit, OnCallback<ListResponse<Video>> callback){
		if(this.videoDataModel != null){
			this.videoDataModel.searchMyVideos(friendlyName, mimeType, appTag, searchDistance, 
					searchLatitude, searchLongitude, timeFilter, recordLimit, callback);
		}
	}
	
	/**
	 * Get a list of Videos, arranged in descending order by LastTouchedDate.
	 * 
	 * @param userId The userId to return Videos belonging to. -1 to return all Videos.
	 * @param recordLimit The maximum number of values to return.
	 * @param callback The callback to call when this method completes.
	 */
	public void getList(long userId, int recordLimit, OnCallback<ListResponse<Video>> callback){
		if(this.videoDataModel != null){
			this.videoDataModel.getList(userId, recordLimit, callback);			
		}
	}
	
	/**
	 * Get a list of Videos, arranged in descending order by LastTouchedDate.
	 * 
	 * @param recordLimit The maximum number of values to return.
	 * @param callback The callback to call when this method completes.
	 */
	public void getMyList(int recordLimit, OnCallback<ListResponse<Video>> callback){
		if(this.videoDataModel != null){
			this.videoDataModel.getMyList(recordLimit, callback);
		}
	}
}
