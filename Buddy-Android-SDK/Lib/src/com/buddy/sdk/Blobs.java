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

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.responses.ListResponse;
/**
 * Represents a class that can be used to add retrieve and manage Blobs for the current user.
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
 *            {@code user.getBlobs()}
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class Blobs {
	private BlobDataModel blobDataModel = null;
	protected BuddyClient client;
	protected AuthenticatedUser user;
	
	Blobs(BuddyClient client, AuthenticatedUser user)
	{
		if(client == null)
		{ throw new IllegalArgumentException("client can't be null or empty."); }
		if(user == null)
		{ throw new IllegalArgumentException("user can't be null or empty."); }
		
		this.client = client;
		this.user = user;
		
		this.blobDataModel = new BlobDataModel(client, user);
	}
	
	/**
	 * Add a blob for this user.
	 * 
	 * @param friendlyName A human readable name for this Blob.
	 * @param appTag Additional MetaData to be stored with this Blob.
	 * @param latitude The Latitude where the Blob was created or uploaded.
	 * @param longitude The Longitude where the Blob was created or uploaded.
	 * @param contentType The MimeType of the Blob.
	 * @param blobData A stream containing the bits of the Blob to be stored.
	 * @param callback The callback to call when this method completes.
	 */
	public void add(String friendlyName, String appTag, double latitude, double longitude, String contentType,
			InputStream blobData, OnCallback<Response<String>> callback){
		if(this.blobDataModel != null){
			this.blobDataModel.add(friendlyName, appTag, latitude, longitude, contentType, blobData, callback);
		}
	}
	
	/**
	 * Edit a blob for this user.
	 *  
	 * @param blobID The ID which identifies the Blob to be edited.
	 * @param friendlyName The new human readable name for this Blob.
	 * @param appTag The new MetaData to be stored with this Blob.
	 * @param callback The callback to call when this method completes.
	 */
	public void edit(long blobID, String friendlyName, String appTag, OnCallback<Response<Boolean>> callback){
		if(this.blobDataModel != null){
			this.blobDataModel.editInfo(blobID, friendlyName, appTag, callback);
		}
	}
	
	/**
	 * Get the Blob object for the given blobID.
	 * 
	 * @param blobID The ID which identifies the Blob to be retrieved.
	 * @param callback The callback to call when this method completes.
	 */
	public void getInfo(long blobID, OnCallback<Response<Blob>> callback){
		if(this.blobDataModel != null){
			this.blobDataModel.getInfo(blobID, callback);			
		}		
	}
	
	/**
	 * Get a stream of the given Blob
	 * 
	 * @param blobID The ID which identifies the Blob to be retrieved.
	 * @param callback The callback to call when this method completes.
	 */
	public void get(long blobID, OnCallback<Response<InputStream>> callback){
		if(this.blobDataModel != null){
			this.blobDataModel.get(blobID, callback);			
		}		
	}
	
	/**
	 * Get a list of the Blobs matching your given search criteria.
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
	public void searchBlobs(String friendlyName, String mimeType, String appTag, int searchDistance, double searchLatitude, double searchLongitude,
			int timeFilter, int recordLimit, OnCallback<ListResponse<Blob>> callback){
		if(this.blobDataModel != null){
			this.blobDataModel.searchBlobs(friendlyName, mimeType, appTag, searchDistance, 
					searchLatitude, searchLongitude, timeFilter, recordLimit, callback);
		}		
	}
	
	/**
	 * Get a list of the Blobs belonging to the current user which match your given search criteria.
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
	public void searchMyBlobs(String friendlyName, String mimeType, String appTag, int searchDistance, double searchLatitude, double searchLongitude,
			int timeFilter, int recordLimit, OnCallback<ListResponse<Blob>> callback){
		if(this.blobDataModel != null){
			this.blobDataModel.searchMyBlobs(friendlyName, mimeType, appTag, searchDistance, 
					searchLatitude, searchLongitude, timeFilter, recordLimit, callback);
		}
	}
	
	/**
	 * Get a list of Blobs, arranged in descending order by LastTouchedDate.
	 * 
	 * @param userId The userId to return Blobs belonging to. -1 to return all Blobs.
	 * @param recordLimit The maximum number of values to return.
	 * @param callback The callback to call when this method completes.
	 */
	public void getList(long userId, int recordLimit, OnCallback<ListResponse<Blob>> callback){
		if(this.blobDataModel != null){
			this.blobDataModel.getList(userId, recordLimit, callback);			
		}
	}
	
	/**
	 * Get a list of Blobs, arranged in descending order by LastTouchedDate.
	 * 
	 * @param recordLimit The maximum number of values to return.
	 * @param callback The callback to call when this method completes.
	 */
	public void getMyList(int recordLimit, OnCallback<ListResponse<Blob>> callback){
		if(this.blobDataModel != null){
			this.blobDataModel.getMyList(recordLimit, callback);
		}
	}
}
