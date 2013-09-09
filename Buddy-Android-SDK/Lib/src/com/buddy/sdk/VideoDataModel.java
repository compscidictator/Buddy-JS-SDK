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
import java.util.ArrayList;
import java.util.List;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.VideoDataResponse;
import com.buddy.sdk.json.responses.VideoDataResponse.VideoData;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.web.BuddyWebWrapper;

/**
 * @author RyanB
 *
 */
class VideoDataModel extends BaseDataModel {
	private AuthenticatedUser authUser = null;
	
	VideoDataModel(BuddyClient client, AuthenticatedUser authUser){
		this.client = client;
		this.authUser = authUser;
	}
	
	public void add(String friendlyName, String appTag, double latitude, double longitude, 
			String contentType, InputStream videoData, final OnCallback<Response<String>> callback)
	{
		BuddyFile videoFile = new BuddyFile();
		videoFile.contentType = contentType;
		videoFile.data = videoData;
		
		BuddyWebWrapper.Videos_Video_AddVideo(client, authUser, friendlyName, appTag, latitude, longitude, videoFile,
				new OnResponseCallback(){

			@Override
			public void OnResponse(BuddyCallbackParams response, Object state) {
				Response<String> booleanResponse = getStringResponse(response);
				callback.OnResponse(booleanResponse, state);
			}
		});
	}
	
	public void delete(long videoID, final OnCallback<Response<Boolean>> callback){
		BuddyWebWrapper.Videos_Video_DeleteVideo(client, authUser, videoID, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state) {
				Response<Boolean> booleanResponse = getBooleanResponse(response);
				callback.OnResponse(booleanResponse, state);
			}
			
		});
	}
	
	public void editInfo(long videoID, String friendlyName, String appTag, final OnCallback<Response<Boolean>> callback)
	{
		BuddyWebWrapper.Videos_Video_EditInfo(client, authUser, videoID, friendlyName, appTag, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				Response<Boolean> booleanResponse = getBooleanResponse(response);
				callback.OnResponse(booleanResponse, state);
			}
		});
	}
	
	public void getInfo(long videoID, final OnCallback<Response<Video>> callback){
		BuddyWebWrapper.Videos_Video_GetVideoInfo(client, authUser, videoID, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				Response<Video> resp = new Response<Video>();
				
				if(response != null){
					if(response.completed){
						VideoDataResponse result = getJson(response.response, VideoDataResponse.class);
						
						if(result != null && result.data != null && result.data.size() > 0){
							VideoDataResponse.VideoData resultData = result.data.get(0);
							Video video = new Video(client, authUser, resultData);
							resp.setResult(video);
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
	
	public void get(long videoID, final OnCallback<Response<InputStream>> callback){
		BuddyWebWrapper.Videos_Video_GetVideo(client, authUser, videoID, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				Response<InputStream> streamResponse = getStreamResponse(response);
				callback.OnResponse(streamResponse, state);
			}
		});
	}
	
	public void searchVideos(String friendlyName, String mimeType, String appTag, int searchDistance,
			double searchLatitude, double searchLongitude, int timeFilter, int recordLimit, final OnCallback<ListResponse<Video>> callback){
		BuddyWebWrapper.Videos_Video_SearchVideos(client, authUser, friendlyName, mimeType, appTag, searchDistance, searchLatitude, searchLongitude,
				timeFilter, recordLimit, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				ListResponse<Video> resp = new ListResponse<Video>();
				
				if(response != null){
					if(response.completed){
						VideoDataResponse data = getJson(response.response,
								VideoDataResponse.class);
						if(data != null){
							List<Video> videos = new ArrayList<Video>(
									data.data.size());
							
							for(VideoData videoData : data.data){
								Video video = new Video(client, authUser, videoData);
								videos.add(video);
							}
							
							resp.setList(videos);
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
	
	public void searchMyVideos(String friendlyName, String mimeType, String appTag, int searchDistance,
			double searchLatitude, double searchLongitude, int timeFilter, int recordLimit, final OnCallback<ListResponse<Video>> callback){
		BuddyWebWrapper.Videos_Video_SearchMyVideos(client, authUser, friendlyName, mimeType, appTag, searchDistance, searchLatitude, searchLongitude,
				timeFilter, recordLimit, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				ListResponse<Video> resp = new ListResponse<Video>();
				
				if(response != null){
					if(response.completed){
						VideoDataResponse data = getJson(response.response,
								VideoDataResponse.class);
						if(data != null){
							List<Video> videos = new ArrayList<Video>(
									data.data.size());
							
							for(VideoData videoData : data.data){
								Video video = new Video(client, authUser, videoData);
								videos.add(video);
							}
							
							resp.setList(videos);
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
	
	public void getList(long userId, int recordLimit, final OnCallback<ListResponse<Video>> callback){
		BuddyWebWrapper.Videos_Video_GetVideoList(client, authUser, userId, recordLimit, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				ListResponse<Video> resp = new ListResponse<Video>();
				
				if(response != null){
					if(response.completed){
						VideoDataResponse data = getJson(response.response,
								VideoDataResponse.class);
						if(data != null){
							List<Video> videos = new ArrayList<Video>(
									data.data.size());
							
							for(VideoData videoData : data.data){
								Video video = new Video(client, authUser, videoData);
								videos.add(video);
							}
							
							resp.setList(videos);
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
	
	public void getMyList(int recordLimit, final OnCallback<ListResponse<Video>> callback){
		BuddyWebWrapper.Videos_Video_GetMyVideoList(client, authUser, recordLimit, new OnResponseCallback(){

			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				ListResponse<Video> resp = new ListResponse<Video>();
				
				if(response != null){
					if(response.completed){
						VideoDataResponse data = getJson(response.response,
								VideoDataResponse.class);
						if(data != null){
							List<Video> videos = new ArrayList<Video>(
									data.data.size());
							
							for(VideoData videoData : data.data){
								Video video = new Video(client, authUser, videoData);
								videos.add(video);
							}
							
							resp.setList(videos);
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
