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

import java.util.ArrayList;
import java.util.List;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.IdentityCheckDataResponse;
import com.buddy.sdk.json.responses.IdentityValueListDataResponse;
import com.buddy.sdk.json.responses.IdentityCheckDataResponse.IdentityCheckData;
import com.buddy.sdk.json.responses.IdentityValueListDataResponse.IdentityValueListData;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Constants;
import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.web.BuddyWebWrapper;

class IdentityDataModel extends BaseDataModel {
    private String token;

    IdentityDataModel(BuddyClient client, String token) {
        this.client = client;
        this.token = token;
    }

    public void add(String value, Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.UserAccount_Identity_AddNewValue(this.client.getAppName(),
                this.client.getAppPassword(), this.token, value, this.RESERVED, state,
                new OnResponseCallback() {
        	
        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> addResponse = getBooleanResponse(response);
                        callback.OnResponse(addResponse, state);
                    }
                });
    }

    public void checkForValues(String value, Object state,
            final OnCallback<ListResponse<IdentityItemSearchResult>> callback) {
        
        BuddyWebWrapper.UserAccount_Identity_CheckForValues(this.client.getAppName(),
                this.client.getAppPassword(), this.token, value, this.RESERVED, state,
                new OnResponseCallback() {
        	
					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<IdentityItemSearchResult> listResponse = new ListResponse<IdentityItemSearchResult>();

                        if (response != null) {
                            if (response.completed) {
                                IdentityCheckDataResponse data = getJson(response.response,
                                        IdentityCheckDataResponse.class);
                                if (data != null) {
                                    List<IdentityItemSearchResult> list = new ArrayList<IdentityItemSearchResult>(
                                            data.data.size());

                                    for (IdentityCheckData identityCheck : data.data) {
                                        int id = -1;

                                        boolean found = identityCheck.ValueFound.equals("1") ? true
                                                : false;

                                        if (found) {
                                            id = Utils.parseInt(identityCheck.UserProfileID);
                                        }

                                        IdentityItemSearchResult item = new IdentityItemSearchResult(
                                                identityCheck.IdentityValue, Constants.MinDate,
                                                found, id);

                                        list.add(item);
                                    }

                                    listResponse.setList(list);
                                } else {
                                    listResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                if (response.exception != null) {
                                    listResponse.setThrowable(response.exception);
                                }
                            }
                        } else {
                            listResponse.setThrowable(new ServiceUnknownErrorException());

                        }

                        callback.OnResponse(listResponse, state);
                    };
                });
    }

    public void getAll(Object state, final OnCallback<ListResponse<IdentityItem>> callback) {
        
        BuddyWebWrapper.UserAccount_Identity_GetMyList(this.client.getAppName(),
                this.client.getAppPassword(), this.token, this.RESERVED, state,
                new OnResponseCallback() {
        	
					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<IdentityItem> listResponse = new ListResponse<IdentityItem>();

                        if (response != null) {
                            // Now need to get data from the token
                            if (response.completed) {
                                IdentityValueListDataResponse data = getJson(response.response,
                                        IdentityValueListDataResponse.class);
                                if (data != null) {
                                    List<IdentityItem> items = new ArrayList<IdentityItem>(
                                            data.data.size());

                                    for (IdentityValueListData identityValue : data.data) {
                                        IdentityItem item = new IdentityItem(
                                                identityValue.identityValue, Utils
                                                        .convertDateString(
                                                                identityValue.createdDateTime,
                                                                "MM/dd/yyyy hh:mm:ss aa"));

                                        items.add(item);
                                    }

                                    listResponse.setList(items);
                                } else {
                                    listResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                if (response.exception != null) {
                                    listResponse.setThrowable(response.exception);
                                }
                            }
                        } else {
                            listResponse.setThrowable(new ServiceUnknownErrorException());

                        }

                        callback.OnResponse(listResponse, state);
                    }
                });
    }

    public void remove(String value, Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.UserAccount_Identity_RemoveValue(this.client.getAppName(),
                this.client.getAppPassword(), this.token, value, this.RESERVED, state,
                new OnResponseCallback() {
        	
					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> addResponse = getBooleanResponse(response);
                        callback.OnResponse(addResponse, state);
                    }
                });
    }
}
