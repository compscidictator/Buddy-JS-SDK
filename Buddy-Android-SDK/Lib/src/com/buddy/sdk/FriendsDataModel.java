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
import java.util.Date;
import java.util.List;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.BlockedDataResponse;
import com.buddy.sdk.json.responses.BlockedDataResponse.BlockedData;
import com.buddy.sdk.json.responses.FriendDataResponse;
import com.buddy.sdk.json.responses.FriendDataResponse.FriendData;
import com.buddy.sdk.json.responses.SearchFriendsDataResponse;
import com.buddy.sdk.json.responses.SearchFriendsDataResponse.SearchFriendsData;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.web.BuddyWebWrapper;

class FriendsDataModel extends BaseDataModel {
    private AuthenticatedUser authUser = null;

    FriendsDataModel(BuddyClient client, AuthenticatedUser authUser) {
        this.client = client;
        this.authUser = authUser;
    }

    public void getAll(Date date, Object state, final OnCallback<ListResponse<User>> callback) {
        
        String fromDate = Utils.convertStringDate(date, "MM/dd/yyyy hh:mm:ss aa");
        BuddyWebWrapper.Friends_Friends_GetList(client.getAppName(), client.getAppPassword(),
                authUser.getToken(), fromDate, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<User> listResponse = new ListResponse<User>();

                        if (response != null) {
                            if (response.completed) {
                                FriendDataResponse result = getJson(response.response,
                                        FriendDataResponse.class);
                                if (result != null) {
                                    List<User> userList = new ArrayList<User>(result.data.size());
                                    for (FriendData data : result.data) {
                                        User user = new User(client, data, authUser.getId());
                                        userList.add(user);
                                    }
                                    listResponse.setList(userList);
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

    public void remove(Integer friendProfileID, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Friends_Friends_Remove(client.getAppName(), client.getAppPassword(),
                authUser.getToken(), friendProfileID, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void findFriends(Integer timeFilter, Integer searchDistance, float latitude,
            float longitude, Integer recordLimit, Integer pageSize, Integer pageNumber,
            Object state, final OnCallback<ListResponse<User>> callback) {
        
        BuddyWebWrapper.Friends_Friends_Search(client.getAppName(), client.getAppPassword(),
                authUser.getToken(), timeFilter, searchDistance, latitude, longitude,
                recordLimit, pageSize, pageNumber, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<User> listResponse = new ListResponse<User>();

                        if (response != null) {
                            if (response.completed) {
                                SearchFriendsDataResponse result = getJson(response.response,
                                        SearchFriendsDataResponse.class);
                                if (result != null) {
                                    List<User> userList = new ArrayList<User>(result.data.size());
                                    for (SearchFriendsData data : result.data) {
                                        User user = new User(client, data);
                                        userList.add(user);
                                    }
                                    listResponse.setList(userList);
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

    public void add(Integer friendProfileID, String applicationTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Friends_FriendRequest_Add(client.getAppName(), client.getAppPassword(),
                authUser.getToken(), friendProfileID, applicationTag, this.RESERVED, state,
                new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void getAllRequests(Date date, Object state,
            final OnCallback<ListResponse<User>> callback) {
        
        String fromDate = Utils.convertStringDate(date, "MM/dd/yyyy hh:mm:ss aa");
        BuddyWebWrapper.Friends_FriendRequest_Get(client.getAppName(), client.getAppPassword(),
                authUser.getToken(), fromDate, state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<User> listResponse = new ListResponse<User>();

                        if (response != null) {
                            if (response.completed) {
                                FriendDataResponse result = getJson(response.response,
                                        FriendDataResponse.class);
                                if (result != null) {
                                    List<User> userList = new ArrayList<User>(result.data.size());
                                    for (FriendData data : result.data) {
                                        User user = new User(client, data, authUser.getId());
                                        userList.add(user);
                                    }
                                    listResponse.setList(userList);
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

    public void deny(Integer friendProfileID, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Friends_FriendRequest_Deny(client.getAppName(), client.getAppPassword(),
                authUser.getToken(), friendProfileID, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void accept(Integer friendProfileID, String applicationTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Friends_FriendRequest_Accept(client.getAppName(), client.getAppPassword(),
                authUser.getToken(), friendProfileID, applicationTag, this.RESERVED, state,
                new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void addBlock(Integer blockProfileID, String applicationTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Friends_Block_Add(client.getAppName(), client.getAppPassword(),
                authUser.getToken(), blockProfileID, applicationTag, this.RESERVED, state,
                new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void removeBlock(Integer blockProfileID, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Friends_Block_Remove(client.getAppName(), client.getAppPassword(),
                authUser.getToken(), blockProfileID, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void getBlockList(Object state, final OnCallback<ListResponse<User>> callback) {
        
        BuddyWebWrapper.Friends_Block_GetList(client.getAppName(), client.getAppPassword(),
                authUser.getToken(), state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<User> listResponse = new ListResponse<User>();

                        if (response != null) {
                            if (response.completed) {
                                BlockedDataResponse result = getJson(response.response,
                                        BlockedDataResponse.class);
                                if (result != null) {
                                    List<User> userList = new ArrayList<User>(result.data.size());
                                    for (BlockedData data : result.data) {
                                        User user = new User(client, data);
                                        userList.add(user);
                                    }
                                    listResponse.setList(userList);
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
