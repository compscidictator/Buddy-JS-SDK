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

import java.util.ArrayList;
import java.util.List;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.ApplicationEmailDataResponse;
import com.buddy.sdk.json.responses.UserDataResponse;
import com.buddy.sdk.json.responses.ApplicationEmailDataResponse.ApplicationEmailData;
import com.buddy.sdk.json.responses.ApplicationStatsDataResponse;
import com.buddy.sdk.json.responses.ApplicationStatsDataResponse.ApplicationStatsData;
import com.buddy.sdk.json.responses.UserDataResponse.UserData;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.web.BuddyWebWrapper;

class ApplicationDataModel extends BaseDataModel {
    ApplicationDataModel(BuddyClient client) {
        this.client = client;
    }

    public void getUserEmails(int firstRow, int lastRow, Object state,
            final OnCallback<ListResponse<String>> callback) {
        
        BuddyWebWrapper.Application_Users_GetEmailList(client.getAppName(),
                client.getAppPassword(), firstRow, lastRow, this.RESERVED, state,
                new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<String> listResponse = new ListResponse<String>();
                        if (response != null) {
                            if (response.completed) {
                                ApplicationEmailDataResponse dataResponse = getJson(
                                        response.response, ApplicationEmailDataResponse.class);
                                if (dataResponse != null) {
                                    List<String> list = new ArrayList<String>(dataResponse.data
                                            .size());
                                    for (ApplicationEmailData emailData : dataResponse.data) {
                                        String userEmail = emailData.UserEmail;
                                        list.add(userEmail);
                                    }
                                    listResponse.setList(list);
                                } else {
                                    listResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                listResponse.setThrowable(response.exception);
                            }
                        } else {
                            listResponse.setThrowable(new ServiceUnknownErrorException());
                        }

                        callback.OnResponse(listResponse, state);
                    }
                });
    }

    public void getUserProfiles(int firstRow, int lastRow, Object state,
            final OnCallback<ListResponse<User>> callback) {
        
        BuddyWebWrapper.Application_Users_GetProfileList(client.getAppName(),
                client.getAppPassword(), firstRow, lastRow, this.RESERVED, state,
                new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<User> listResponse = new ListResponse<User>();
                        if (response != null) {
                            if (response.completed) {
                                UserDataResponse data = getJson(response.response,
                                        UserDataResponse.class);
                                if (data != null) {
                                    List<User> list = new ArrayList<User>(data.data.size());
                                    for (UserData userData : data.data) {
                                        User user = new User(client, userData);
                                        list.add(user);
                                    }
                                    listResponse.setList(list);
                                } else {
                                    listResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                listResponse.setThrowable(response.exception);
                            }
                        } else {
                            listResponse.setThrowable(new ServiceUnknownErrorException());
                        }
                        callback.OnResponse(listResponse, state);
                    }
                });
    }

    public void getApplicationStatistics(Object state,
            final OnCallback<ListResponse<ApplicationStatistics>> callback) {
        
        BuddyWebWrapper.Application_Metrics_GetStats(client.getAppName(), client.getAppPassword(),
                this.RESERVED, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<ApplicationStatistics> listResponse = new ListResponse<ApplicationStatistics>();
                        if (response != null) {
                            if (response.completed) {
                                ApplicationStatsDataResponse dataResponse = getJson(
                                        response.response, ApplicationStatsDataResponse.class);
                                if (dataResponse != null) {
                                    List<ApplicationStatistics> list = new ArrayList<ApplicationStatistics>(
                                            dataResponse.data.size());
                                    for (ApplicationStatsData statsData : dataResponse.data) {
                                        ApplicationStatistics appStats = new ApplicationStatistics(
                                                statsData, client);
                                        list.add(appStats);
                                    }
                                    listResponse.setList(list);
                                } else {
                                    listResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                listResponse.setThrowable(response.exception);
                            }
                        } else {
                            listResponse.setThrowable(new ServiceUnknownErrorException());
                        }

                        callback.OnResponse(listResponse, state);
                    }
                });
    }
}
