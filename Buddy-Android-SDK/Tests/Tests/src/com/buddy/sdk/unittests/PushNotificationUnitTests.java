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

package com.buddy.sdk.unittests;

import java.util.List;
import java.util.Map;

import com.buddy.sdk.RegisteredDeviceAndroid;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Constants;
import com.buddy.sdk.web.BuddyHttpClientFactory;

public class PushNotificationUnitTests extends BaseUnitTest {
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createAuthenticatedUser();
    }

    public void testPushNotificationRegisterDevice() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getPushNotifications().registerDevice(testRegistrationId, testGroupName, null,
                new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }

    public void testPushNotificationRemoveDevice() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getPushNotifications().unregisterDevice(null, new OnCallback<Response<Boolean>>() {
            public void OnResponse(Response<Boolean> response, Object state) {
                assertNotNull(response);
                assertTrue(response.getResult());
            }

        });
    }

    public void testPushNotificationSendRawMessage() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getPushNotifications().sendRawMessage("Test Message", testAuthUser.getId(), Constants.MinDate, "", null,
                new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }

    public void testPushNotificationGetGroupNames() {
        String jsonResponse = readDataFromFile("DataResponses/PushNotificationUnitTests-GetGroups.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getPushNotifications().getGroups(null,
                new OnCallback<Response<Map<String, Integer>>>() {
                    public void OnResponse(Response<Map<String, Integer>> response, Object state) {
                        assertNotNull(response);

                        Map<String, Integer> map = response.getResult();
                        assertNotNull(map);

                        Integer count = map.get("Sports");
                        assertTrue(count == 1);
                    }

                });
    }

    public void testPushNotificationGetRegisteredDevices() {
        String jsonResponse = readDataFromFile("DataResponses/PushNotificationUnitTests-GetDevices.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getPushNotifications().getRegisteredDevices("Test Group", 1, 1, null,
                new OnCallback<ListResponse<RegisteredDeviceAndroid>>() {
                    public void OnResponse(ListResponse<RegisteredDeviceAndroid> response,
                            Object state) {
                        assertNotNull(response);

                        List<RegisteredDeviceAndroid> list = response.getList();
                        assertNotNull(list);

                        RegisteredDeviceAndroid device = list.get(0);
                        assertEquals("Sports", device.getGroupName());
                    }

                });
    }
}
