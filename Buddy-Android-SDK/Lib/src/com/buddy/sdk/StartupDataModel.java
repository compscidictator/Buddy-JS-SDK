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

import java.util.ArrayList;
import java.util.List;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.MetroAreaDataResponse;
import com.buddy.sdk.json.responses.MetroAreaDataResponse.MetroAreaData;
import com.buddy.sdk.json.responses.StartupDataResponse;
import com.buddy.sdk.json.responses.StartupDataResponse.StartupData;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.web.BuddyWebWrapper;

class StartupDataModel extends BaseDataModel {
    private AuthenticatedUser authUser = null;

    StartupDataModel(BuddyClient client, AuthenticatedUser authUser) {
        this.client = client;
        this.authUser = authUser;
    }

    public void getMetroAreaList(Object state, final OnCallback<ListResponse<MetroArea>> callback) {
        BuddyWebWrapper.StartupData_Location_GetMetroList(client.getAppName(),
                client.getAppPassword(), state, new OnResponseCallback() {
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<MetroArea> listResponse = new ListResponse<MetroArea>();

                        if (response != null) {
                            if (response.completed) {
                                MetroAreaDataResponse result = getJson(response.response,
                                        MetroAreaDataResponse.class);
                                if (result != null) {
                                    List<MetroArea> metroAreaList = new ArrayList<MetroArea>(
                                            result.data.size());
                                    for (MetroAreaData data : result.data) {
                                        MetroArea metroArea = new MetroArea(client, authUser, data);
                                        metroAreaList.add(metroArea);
                                    }
                                    listResponse.setList(metroAreaList);
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

    public void getFromMetroArea(String metroName, int recordLimit, Object state,
            final OnCallback<ListResponse<Startup>> callback) {
        BuddyWebWrapper.StartupData_Location_GetFromMetroArea(client.getAppName(),
                client.getAppPassword(), this.authUser.getToken(), metroName, recordLimit, state,
                new OnResponseCallback() {
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<Startup> listResponse = createStartupListResponse(response);
                        callback.OnResponse(listResponse, state);
                    }
                });
    }

    public void find(int searchDistanceInMeters, double latitude, double longitude,
            int numberOfResults, String searchForName, Object state,
            final OnCallback<ListResponse<Startup>> callback) {
        BuddyWebWrapper.StartupData_Location_Search(client.getAppName(), client.getAppPassword(),
                this.authUser.getToken(), searchDistanceInMeters, latitude, longitude,
                numberOfResults, searchForName, state, new OnResponseCallback() {
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<Startup> listResponse = createStartupListResponse(response);
                        callback.OnResponse(listResponse, state);
                    }
                });
    }

    private ListResponse<Startup> createStartupListResponse(BuddyCallbackParams response) {
        ListResponse<Startup> listResponse = new ListResponse<Startup>();

        if (response != null) {
            if (response.completed) {
                StartupDataResponse result = getJson(response.response, StartupDataResponse.class);
                if (result != null) {
                    List<Startup> startupList = new ArrayList<Startup>(result.data.size());
                    for (StartupData data : result.data) {
                        Startup startup = new Startup(client, authUser, data);
                        startupList.add(startup);
                    }
                    listResponse.setList(startupList);
                } else {
                    listResponse.setThrowable(new BuddyServiceException(response.response));
                }
            } else {
                listResponse.setThrowable(response.exception);
            }
        } else {
            listResponse.setThrowable(new ServiceUnknownErrorException());
        }

        return listResponse;
    }
}
