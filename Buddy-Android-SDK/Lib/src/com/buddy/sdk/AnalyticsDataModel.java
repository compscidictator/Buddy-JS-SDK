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

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.web.BuddyWebWrapper;

class AnalyticsDataModel extends BaseDataModel {
    AnalyticsDataModel(BuddyClient client) {
        this.client = client;
    }

    public void startSession(AuthenticatedUser user, String sessionName, String startAppTag,
            Object state, final OnCallback<Response<String>> callback) {
        
        BuddyWebWrapper.Analytics_Session_Start(client.getAppName(), client.getAppPassword(),
                user.getToken(), sessionName, startAppTag, state, new OnResponseCallback() {

                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<String> stringResponse = getStringResponse(response);
                        callback.OnResponse(stringResponse, state);
                    }

                });
    }

    public void endSession(AuthenticatedUser user, String sessionId, String endAppTag,
            Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Analytics_Session_End(client.getAppName(), client.getAppPassword(),
                user.getToken(), sessionId, endAppTag, state, new OnResponseCallback() {

                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void recordSessionMetric(AuthenticatedUser user, String sessionId, String metricKey,
            String metricValue, String appTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Analytics_Session_RecordMetric(client.getAppName(),
                client.getAppPassword(), user.getToken(), sessionId, metricKey, metricValue,
                appTag, state, new OnResponseCallback() {

                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });

    }

    public void recordInformation(AuthenticatedUser user, String deviceOSVersion,
            String deviceType, double latitude, double longitude, 
            String appVersion, String metaData, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Analytics_DeviceInformation_Add(client.getAppName(),
                client.getAppPassword(), user == null ? "" : user.getToken(), deviceOSVersion, deviceType,
                (float) latitude, (float) longitude, client.getAppName(), appVersion, metaData, state,
                new OnResponseCallback() {

                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        if(callback != null) {
                            Response<Boolean> booleanResponse = getBooleanResponse(response);
                            callback.OnResponse(booleanResponse, state);
                        }
                    }

                });
    }

    public void recordCrash(AuthenticatedUser user, String appVersion, String deviceOSVersion,
            String deviceType, String methodName, String stackTrace, String metaData,
            double latitude, double longitude, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Analytics_CrashRecords_Add(client.getAppName(), client.getAppPassword(),
                user == null ? "" : user.getToken(), appVersion, deviceOSVersion, deviceType, methodName,
                stackTrace, metaData, (float) latitude, (float) longitude, state,
                new OnResponseCallback() {

                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        if (callback != null) {
                            Response<Boolean> booleanResponse = getBooleanResponse(response);
                            callback.OnResponse(booleanResponse, state);
                        }
                    }

                });
    }
}
