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

package com.buddy.sdk.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author RyanB
 *
 */
public class TestHttpURLConnection extends HttpURLConnection {
	/**
	 * @param url
	 */
	
	private InputStream dummyWebResponse;
	private String dummyResponse;
	
	public void setDummyResponse(InputStream value){
		this.dummyWebResponse = value;
	}
	
	public void setDummyResponse(String value){
		this.dummyResponse = value;
	}
	
	protected TestHttpURLConnection(URL url) {
		super(url);
		// TODO Auto-generated constructor stub
	}	

	/* (non-Javadoc)
	 * @see java.net.HttpURLConnection#disconnect()
	 */
	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputStream getInputStream(){
		if(dummyWebResponse != null){
			return dummyWebResponse;
		}
		else{
			return new ByteArrayInputStream(dummyResponse.getBytes());
		}
	}
	
	@Override
	public int getResponseCode(){
		return HttpURLConnection.HTTP_OK;
	}
	
	/* (non-Javadoc)
	 * @see java.net.HttpURLConnection#usingProxy()
	 */
	@Override
	public boolean usingProxy() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.net.URLConnection#connect()
	 */
	@Override
	public void connect() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	
}