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
import java.util.ArrayList;
import java.util.List;

import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.BlobDataResponse;
import com.buddy.sdk.json.responses.BlobDataResponse.BlobData;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;

import com.buddy.sdk.web.BuddyWebWrapper;

/**
 * @author RyanB
 *
 */
class BlobDataModel extends BaseDataModel {
	private AuthenticatedUser authUser = null;
	
	BlobDataModel(BuddyClient client, AuthenticatedUser authUser){
		this.client = client;
		this.authUser = authUser;
	}
	
	public void add(String friendlyName, String appTag, double latitude, double longitude, 
			String contentType, InputStream blobData, final OnCallback<Response<String>> callback)
	{
		BuddyFile blobFile = new BuddyFile();
		blobFile.contentType = contentType;
		blobFile.data = blobData;
		
		BuddyWebWrapper.Blobs_Blob_AddBlob(client, authUser, friendlyName, appTag, latitude, longitude, blobFile,
				new OnResponseCallback(){

			@Override
			public void OnResponse(BuddyCallbackParams response, Object state) {
				Response<String> booleanResponse = getStringResponse(response);
				callback.OnResponse(booleanResponse, state);
			}
		});
	}
	
	public void delete(long blobID, final OnCallback<Response<Boolean>> callback){
		BuddyWebWrapper.Blobs_Blob_DeleteBlob(client, authUser, blobID, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state) {
				Response<Boolean> booleanResponse = getBooleanResponse(response);
				callback.OnResponse(booleanResponse, state);
			}
			
		});
	}
	
	public void editInfo(long blobID, String friendlyName, String appTag, final OnCallback<Response<Boolean>> callback)
	{
		BuddyWebWrapper.Blobs_Blob_EditInfo(client, authUser, blobID, friendlyName, appTag, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				Response<Boolean> booleanResponse = getBooleanResponse(response);
				callback.OnResponse(booleanResponse, state);
			}
		});
	}
	
	public void getInfo(long blobID, final OnCallback<Response<Blob>> callback){
		BuddyWebWrapper.Blobs_Blob_GetBlobInfo(client, authUser, blobID, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				Response<Blob> resp = new Response<Blob>();
				
				if(response != null){
					if(response.completed){
						BlobDataResponse result = getJson(response.response, BlobDataResponse.class);
						
						if(result != null && result.data != null && result.data.size() > 0){
							BlobDataResponse.BlobData resultData = result.data.get(0);
							Blob blob = new Blob(client, authUser, resultData);
							resp.setResult(blob);
						}else{
							resp.setThrowable(new BuddyServiceException(response.response));
						}
					}else {
						resp.setThrowable(response.exception);
					}
				}else{
					resp.setThrowable(new ServiceUnknownErrorException());
				}
				callback.OnResponse(resp, state);
			}
		});
	}
	
	public void get(long blobID, final OnCallback<Response<InputStream>> callback){
		BuddyWebWrapper.Blobs_Blob_GetBlob(client, authUser, blobID, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				Response<InputStream> streamResponse = getStreamResponse(response);
				callback.OnResponse(streamResponse, state);
			}
		});
	}
	
	public void searchBlobs(String friendlyName, String mimeType, String appTag, int searchDistance,
			double searchLatitude, double searchLongitude, int timeFilter, int recordLimit, final OnCallback<ListResponse<Blob>> callback){
		BuddyWebWrapper.Blobs_Blob_SearchBlobs(client, authUser, friendlyName, mimeType, appTag, searchDistance, searchLatitude, searchLongitude,
				timeFilter, recordLimit, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				ListResponse<Blob> resp = new ListResponse<Blob>();
				
				if(response != null){
					if(response.completed){
						BlobDataResponse data = getJson(response.response,
								BlobDataResponse.class);
						if(data != null){
							List<Blob> blobs = new ArrayList<Blob>(
									data.data.size());
							
							for(BlobData blobData : data.data){
								Blob blob = new Blob(client, authUser, blobData);
								blobs.add(blob);
							}
							
							resp.setList(blobs);
						}else{
							resp.setThrowable(new BuddyServiceException(response.response));
						}
					}else{
						if(response.exception != null){
							resp.setThrowable(response.exception);
						}
					}
				}else{
					resp.setThrowable(new ServiceUnknownErrorException());
				}
				
				callback.OnResponse(resp, state);
			}
		});
	}
	
	public void searchMyBlobs(String friendlyName, String mimeType, String appTag, int searchDistance,
			double searchLatitude, double searchLongitude, int timeFilter, int recordLimit, final OnCallback<ListResponse<Blob>> callback){
		BuddyWebWrapper.Blobs_Blob_SearchMyBlobs(client, authUser, friendlyName, mimeType, appTag, searchDistance, searchLatitude, searchLongitude,
				timeFilter, recordLimit, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				ListResponse<Blob> resp = new ListResponse<Blob>();
				
				if(response != null){
					if(response.completed){
						BlobDataResponse data = getJson(response.response,
								BlobDataResponse.class);
						if(data != null){
							List<Blob> blobs = new ArrayList<Blob>(
									data.data.size());
							
							for(BlobData blobData : data.data){
								Blob blob = new Blob(client, authUser, blobData);
								blobs.add(blob);
							}
							
							resp.setList(blobs);
						}else{
							resp.setThrowable(new BuddyServiceException(response.response));
						}
					}else{
						if(response.exception != null){
							resp.setThrowable(response.exception);
						}
					}
				}else{
					resp.setThrowable(new ServiceUnknownErrorException());
				}
				
				callback.OnResponse(resp, state);
			}
		});
	}
	
	public void getList(long userId, int recordLimit, final OnCallback<ListResponse<Blob>> callback){
		BuddyWebWrapper.Blobs_Blob_GetBlobList(client, authUser, userId, recordLimit, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				ListResponse<Blob> resp = new ListResponse<Blob>();
				
				if(response != null){
					if(response.completed){
						BlobDataResponse data = getJson(response.response,
								BlobDataResponse.class);
						if(data != null){
							List<Blob> blobs = new ArrayList<Blob>(
									data.data.size());
							
							for(BlobData blobData : data.data){
								Blob blob = new Blob(client, authUser, blobData);
								blobs.add(blob);
							}
							
							resp.setList(blobs);
						}else{
							resp.setThrowable(new BuddyServiceException(response.response));
						}
					}else{
						if(response.exception != null){
							resp.setThrowable(response.exception);
						}
					}
				}else{
					resp.setThrowable(new ServiceUnknownErrorException());
				}
				
				callback.OnResponse(resp, state);
			}
		});
	}
	
	public void getMyList(int recordLimit, final OnCallback<ListResponse<Blob>> callback){
		BuddyWebWrapper.Blobs_Blob_GetMyBlobList(client, authUser, recordLimit, new OnResponseCallback(){

			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				ListResponse<Blob> resp = new ListResponse<Blob>();
				
				if(response != null){
					if(response.completed){
						BlobDataResponse data = getJson(response.response,
								BlobDataResponse.class);
						if(data != null){
							List<Blob> blobs = new ArrayList<Blob>(
									data.data.size());
							
							for(BlobData blobData : data.data){
								Blob blob = new Blob(client, authUser, blobData);
								blobs.add(blob);
							}
							
							resp.setList(blobs);
						}else{
							resp.setThrowable(new BuddyServiceException(response.response));
						}
					}else{
						if(response.exception != null){
							resp.setThrowable(response.exception);
						}
					}
				}else{
					resp.setThrowable(new ServiceUnknownErrorException());
				}
				
				callback.OnResponse(resp, state);
			}
		});
	}
}
