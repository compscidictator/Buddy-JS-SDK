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

import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Sounds.SoundQuality;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.web.BuddyWebWrapper;

/**
 * @author RyanB
 *
 */
class SoundDataModel extends BaseDataModel {
	
	SoundDataModel(BuddyClient client){
		this.client = client;
	}
	
	public void get(String soundName, SoundQuality quality, final OnCallback<Response<InputStream>> callback){
		BuddyWebWrapper.Sound_Sounds_GetSound(client, soundName, quality, new OnResponseCallback(){
			
			@Override
			public void OnResponse(BuddyCallbackParams response, Object state){
				Response<InputStream> streamResponse = getStreamResponse(response);
				callback.OnResponse(streamResponse, state);
			}
		});
	}
}
