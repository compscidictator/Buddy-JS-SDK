/*
 * Copyright (C) 2012 Buddy Platform, Inc.
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

package com.buddy.sdk;

import java.io.InputStream;

import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.responses.Response;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

class BaseDataModel {
    protected static final String JSON_SYNTAX_ERROR = "JSON Syntax Error";

    protected final String RESERVED = "";
    protected BuddyClient client = null;

    protected Response<InputStream> getStreamResponse(BuddyCallbackParams response){
		Response<InputStream> streamResponse = new Response<InputStream>();
		
		if(response != null) {
			if(response.completed){
				streamResponse.setResult((InputStream)response.responseObj);
			} else {
				if (response.exception != null) {
	                streamResponse.setThrowable(response.exception);
	            }
			}
		} else {
	        streamResponse.setThrowable(new ServiceUnknownErrorException());
	    }
		return streamResponse;
	}    	    
    
    protected Response<String> getStringResponse(BuddyCallbackParams response) {
        Response<String> stringResponse = new Response<String>();
        if (response != null) {
            if (response.completed) {
                String result = getJson(response.response, String.class);
                if (result != null) {
                    stringResponse.setResult(result);
                } else {
                    // Probable error in parsing the JSON so raise exception
                    // with the response as error code
                    stringResponse.setThrowable(new BuddyServiceException(result));
                }
            } else {
                stringResponse.setThrowable(response.exception);
            }
        } else {
            stringResponse.setThrowable(new ServiceUnknownErrorException());
        }

        return stringResponse;
    }

    protected Response<Boolean> getBooleanResponse(BuddyCallbackParams response) {
        Response<Boolean> booleanResponse = new Response<Boolean>();
        if (response != null) {
            if (response.completed) {
                if (response.response.equals("1")) {
                    booleanResponse.setResult(true);
                } else {
                    booleanResponse.setResult(false);
                    booleanResponse.setErrorMessage(response.response);
                }
            } else {
                if (response.exception != null) {
                    booleanResponse.setThrowable(response.exception);
                }
            }
        } else {
            booleanResponse.setThrowable(new ServiceUnknownErrorException());
        }

        return booleanResponse;
    }

    public <T> T getJson(String response, Class<T> classVal) {
        T result = null;
        Gson gson = new Gson();
        try {
            result = gson.fromJson(response, classVal);
        } catch (JsonSyntaxException ex) {
        }
        return result;
    }
}
