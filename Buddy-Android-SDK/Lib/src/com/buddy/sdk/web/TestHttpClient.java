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


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TestHttpClient extends AsyncHttpClient {
    private String dummyWebResponse;

    public void setDummyResponse(String value) {
        this.dummyWebResponse = value;
    }

    public void post(Context context, String url, HttpEntity entity, String contentType,
            AsyncHttpResponseHandler responseHandler) {
        sendRequest(null, null, null, contentType, responseHandler, context);
    }

    public void get(Context context, String url, RequestParams params,
            AsyncHttpResponseHandler responseHandler) {
        sendRequest(null, null, null, null, responseHandler, context);
    }

    private void sendRequest(DefaultHttpClient client, HttpContext httpContext,
            HttpUriRequest uriRequest, String contentType,
            AsyncHttpResponseHandler responseHandler, Context context) {
        responseHandler.onSuccess(dummyWebResponse);
        dummyWebResponse = "";
    }
}


