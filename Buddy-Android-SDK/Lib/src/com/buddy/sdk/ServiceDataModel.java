/* Copyright (C) 2012 Buddy Platform, Inc.
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

import java.util.Date;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.web.BuddyWebWrapper;

class ServiceDataModel extends BaseDataModel {
    private BuddyClient client = null;

    ServiceDataModel(BuddyClient client) {
        this.client = client;
    }

    public void getServiceTime(Object state, final OnCallback<Response<Date>> callback) {
        
        BuddyWebWrapper.Service_DateTime_Get(this.client.getAppName(),
                this.client.getAppPassword(), state, new OnResponseCallback() {
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Date> dateTimeResponse = new Response<Date>();
                        if (response != null) {
                            if (response.completed) {
                            	try {
                                    // Is this a valid date time
                                    Date date = Utils.convertDateString(response.response,
                                            "MM/dd/yyyy hh:mm:ss aa", true);
                                    dateTimeResponse.setResult(date);
                           		
                            	} catch (Exception ex) {
                                    dateTimeResponse.setThrowable(ex);                            		
                            	}
                            } else {
                                dateTimeResponse.setThrowable(response.exception);
                            }
                        } else {
                            dateTimeResponse.setThrowable(new ServiceUnknownErrorException());

                        }
                        callback.OnResponse(dateTimeResponse, state);
                    }
                });
    }

    public void ping(Object state, final OnCallback<Response<String>> callback) {
        
        BuddyWebWrapper.Service_Ping_Get(this.client.getAppName(), this.client.getAppPassword(),
                state, new OnResponseCallback() {
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<String> dateTimeResponse = getStringResponse(response);
                        callback.OnResponse(dateTimeResponse, state);
                    }
                });
    }

    public void getServiceVersion(Object state, final OnCallback<Response<String>> callback) {
        
        BuddyWebWrapper.Service_Version_Get(this.client.getAppName(), this.client.getAppPassword(),
                state, new OnResponseCallback() {
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<String> dateTimeResponse = getStringResponse(response);
                        callback.OnResponse(dateTimeResponse, state);
                    }
                });
    }
}
