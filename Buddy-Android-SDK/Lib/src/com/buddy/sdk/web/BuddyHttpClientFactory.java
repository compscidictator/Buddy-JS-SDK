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

package com.buddy.sdk.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.loopj.android.http.AsyncHttpClient;

public class BuddyHttpClientFactory {
    private static String BuddyPlatformSDKHeaderValue = "Android,v0.1.2";
    
    private static AsyncHttpClient client = null;

    private static Boolean isUnitTestMode = false;

    private static ArrayList<String> dummyOrderedResponses = new ArrayList<String>();

    private static InputStream dummyWebResponse;
	
	public static void setDummyReponse(InputStream value){
		dummyWebResponse = value;
	}
    
    public static void setUnitTestMode(Boolean value) {
        isUnitTestMode = value;
    }

    public static void addDummyResponse(String value) {
        dummyOrderedResponses.add(value);
    }

    public static AsyncHttpClient setupHttpClient() {
        client = null;
        return createHttpClient();
    }

    public static AsyncHttpClient setupHttpClient(int socketTimeoutValue, int maxConnections,
            int socketBufferSize) {
        client = null;
        return createHttpClient(socketTimeoutValue, maxConnections, socketBufferSize);
    }

    /**
     * Get the AsynHttpClient created with default parameters. If unit test mode
     * is set then dummy responses are returned when calls to the
     * AsyncHttpClient are made
     * 
     * @return AsyncHttpClient
     */
    private static AsyncHttpClient createHttpClient() {
        if (isUnitTestMode) {
            client = new TestHttpClient();
        } else {
            client = new AsyncHttpClient();
            
            client.addHeader("x-buddy-sdk", BuddyPlatformSDKHeaderValue);
        }

        return client;
    }
    
    public static HttpURLConnection createHttpURLConnection(URL url){
    	if(isUnitTestMode){
    		TestHttpURLConnection conn = new TestHttpURLConnection(url);
    		if(dummyWebResponse != null){
    			conn.setDummyResponse(dummyWebResponse);
    		}
    		else{
    			conn.setDummyResponse(dummyOrderedResponses.get(0));
    			dummyOrderedResponses.remove(0);
    		}
    		return conn;
    	} else {
    		try {
				return (HttpURLConnection) url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return null;
    }

    /**
     * Get the AsynHttpClient using specified parameters. If unit test mode is
     * set then dummy responses are returned when calls to the AsyncHttpClient
     * are made
     * 
     * @param socketTimeoutValue Default socket timeout (SO_TIMEOUT) in
     *            milliseconds which is the timeout for waiting for data. A
     *            timeout value of zero is interpreted as an infinite timeout.
     *            Value of -1 sets the default value of 10 seconds
     * @param maxConnections Maximum number of connections allowed. Value of -1
     *            sets the default value of 10
     * @param socketBufferSize Socket buffer size. Value of -1 sets the default
     *            value of 8192
     * @return
     */
    private static AsyncHttpClient createHttpClient(int socketTimeoutValue, int maxConnections,
            int socketBufferSize) {
        if (isUnitTestMode) {
            if (dummyOrderedResponses.size() > 0) {
                client = createHttpClient();
            } else {
                client = new AsyncHttpClient(socketTimeoutValue, maxConnections, socketBufferSize);
            }
        } else {
            client = new AsyncHttpClient(socketTimeoutValue, maxConnections, socketBufferSize);
        }

        return client;
    }

    public static AsyncHttpClient getHttpClient() {
        if (client == null || (isUnitTestMode && client instanceof AsyncHttpClient)) {
            client = null;
            client = createHttpClient();
        }

        if (isUnitTestMode && dummyOrderedResponses.size() > 0) {
            String nextResponse = "";
            if (dummyOrderedResponses.size() > 0) {
                nextResponse = dummyOrderedResponses.get(0);
            }

            ((TestHttpClient) client).setDummyResponse(nextResponse);

            dummyOrderedResponses.remove(0);
        }
        return client;
    }
}
