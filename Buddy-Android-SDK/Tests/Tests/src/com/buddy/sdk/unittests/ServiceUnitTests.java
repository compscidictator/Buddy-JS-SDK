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

import java.util.Date;

import com.buddy.sdk.BuddyClient;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.web.BuddyHttpClientFactory;

public class ServiceUnitTests extends BaseUnitTest {
    public void testServiceGetDateTime() {
        String jsonValue = readDataFromFile("DataResponses/ServiceUnitTests-GetDate.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValue);

        BuddyHttpClientFactory.setUnitTestMode(true);

        BuddyClient client = new BuddyClient(applicationName, applicationPassword, this.getInstrumentation().getContext(), "0.1", true);

        client.getServiceTime(null, new OnCallback<Response<Date>>() {
            public void OnResponse(Response<Date> response, Object state) {
                assertNotNull(response);
                assertEquals("12/24/2012 05:25:22 AM", Utils.convertStringDate(response.getResult(), "MM/dd/yyyy hh:mm:ss aa"));
            }
        });
    }

    public void testServiceGetPing() {
        String jsonValue = readDataFromFile("DataResponses/ServiceUnitTests-GetPing.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValue);

        BuddyHttpClientFactory.setUnitTestMode(true);

        BuddyClient client = new BuddyClient(applicationName, applicationPassword, this.getInstrumentation().getContext(), "0.1", true);

        client.ping(null, new OnCallback<Response<String>>() {
            public void OnResponse(Response<String> response, Object state) {
                assertNotNull(response);
                assertEquals("Pong", response.getResult());
            }
        });
    }

    public void testServiceGetVersion() {
        String jsonValue = readDataFromFile("DataResponses/ServiceUnitTests-GetVersion.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValue);

        BuddyHttpClientFactory.setUnitTestMode(true);

        BuddyClient client = new BuddyClient(applicationName, applicationPassword, this.getInstrumentation().getContext(), "0.1", true);

        client.getServiceVersion(null, new OnCallback<Response<String>>() {
            public void OnResponse(Response<String> response, Object state) {
                assertNotNull(response);
                assertEquals("V1.24", response.getResult());
            }
        });
    }
}
