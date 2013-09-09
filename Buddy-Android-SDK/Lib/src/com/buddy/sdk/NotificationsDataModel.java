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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.NotificationsDevicesDataResponse;
import com.buddy.sdk.json.responses.NotificationsDevicesDataResponse.NotificationsDevicesData;
import com.buddy.sdk.json.responses.NotificationsGroupsDataResponse;
import com.buddy.sdk.json.responses.NotificationsGroupsDataResponse.NotificationsGroupsData;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.web.BuddyWebWrapper;

class NotificationsDataModel extends BaseDataModel {
    private AuthenticatedUser user = null;

    NotificationsDataModel(BuddyClient client, AuthenticatedUser user) {
        this.client = client;
        this.user = user;
    }

    public void registerDevice(String groupName, String registrationID, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.PushNotifications_Android_RegisterDevice(client.getAppName(),
                client.getAppPassword(), user.getToken(), groupName, registrationID, state,
                new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> stringResponse = getBooleanResponse(response);
                        callback.OnResponse(stringResponse, state);
                    }
                });
    }

    public void unregisterDevice(Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.PushNotifications_Android_RemoveDevice(client.getAppName(),
                client.getAppPassword(), user.getToken(), state, new OnResponseCallback() {

				   @Override
                   public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> stringResponse = getBooleanResponse(response);
                        callback.OnResponse(stringResponse, state);
                    }

                });
    }

    public void sendRawMessage(String rawMessage, int senderUserId, Date afterDate, String groupName,
            Object state, final OnCallback<Response<Boolean>> callback) {
        
        String dateParam = Utils.convertStringDate(afterDate, "MM/dd/yyyy");

        BuddyWebWrapper.PushNotifications_Android_SendRawMessage(client.getAppName(),
                client.getAppPassword(), String.valueOf(senderUserId), rawMessage, dateParam, groupName,
                state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> stringResponse = getBooleanResponse(response);
                        callback.OnResponse(stringResponse, state);
                    }

                });
    }

    public void getRegisteredDevices(String groupName, Integer pageSize, Integer currentPageNumber,
            Object state, final OnCallback<ListResponse<RegisteredDeviceAndroid>> callback) {
        
        BuddyWebWrapper.PushNotifications_Android_GetRegisteredDevices(client.getAppName(),
                client.getAppPassword(), user.getToken(), groupName, pageSize,
                currentPageNumber, state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<RegisteredDeviceAndroid> listResponse = new ListResponse<RegisteredDeviceAndroid>();
                        if (response != null) {
                            if (response.completed) {
                                NotificationsDevicesDataResponse data = getJson(response.response,
                                        NotificationsDevicesDataResponse.class);
                                if (data != null) {
                                    List<RegisteredDeviceAndroid> deviceList = new ArrayList<RegisteredDeviceAndroid>(
                                            data.data.size());
                                    for (NotificationsDevicesData deviceData : data.data) {
                                        RegisteredDeviceAndroid device = new RegisteredDeviceAndroid(
                                                deviceData, user);
                                        deviceList.add(device);
                                    }
                                    listResponse.setList(deviceList);
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

    public void getGroups(Object state, final OnCallback<Response<Map<String, Integer>>> callback) {
        
        BuddyWebWrapper.PushNotifications_Android_GetGroupNames(client.getAppName(),
                client.getAppPassword(), user.getToken(), state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Map<String, Integer>> groupsResponse = new Response<Map<String, Integer>>();
                        if (response != null) {
                            if (response.completed) {
                                NotificationsGroupsDataResponse data = getJson(response.response,
                                        NotificationsGroupsDataResponse.class);
                                if (data != null) {
                                    Map<String, Integer> map = new HashMap<String, Integer>(
                                            data.data.size());
                                    for (NotificationsGroupsData groupData : data.data) {
                                        Integer deviceCount = Utils.parseInt(groupData.deviceCount);
                                        map.put(groupData.groupName, deviceCount);
                                    }
                                    groupsResponse.setResult(map);
                                } else {
                                    groupsResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                groupsResponse.setThrowable(response.exception);
                            }
                        } else {
                            groupsResponse.setThrowable(new ServiceUnknownErrorException());

                        }
                        callback.OnResponse(groupsResponse, state);
                    }

                });
    }
}
