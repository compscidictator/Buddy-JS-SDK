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

package com.buddy.sdk.unittests;

import java.util.List;
import com.buddy.sdk.Identity;
import com.buddy.sdk.IdentityItem;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.IdentityItemSearchResult;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.web.BuddyHttpClientFactory;

public class IdentityUnitTests extends BaseUnitTest {
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createAuthenticatedUser();
    }

    public void testGetIdentityValues() {
        String jsonResponse = readDataFromFile("DataResponses/IdentityUnitTests-GetAll.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        Identity identity = testAuthUser.getIdentityValues();

        identity.getAll(null, new OnCallback<ListResponse<IdentityItem>>() {

            @Override
            public void OnResponse(ListResponse<IdentityItem> response, Object state) {
                assertNotNull(response);

                List<IdentityItem> list = response.getList();
                assertNotNull(list);

                IdentityItem item = list.get(0);
                assertEquals("Test Value", item.getValue());
            }

        });

    }

    public void testSearchIdentityValue() {
        String jsonResponse = readDataFromFile("DataResponses/IdentityUnitTests-CheckValues.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        Identity identity = testAuthUser.getIdentityValues();

        identity.checkForValues("Test Value", null,
                new OnCallback<ListResponse<IdentityItemSearchResult>>() {

                    @Override
                    public void OnResponse(ListResponse<IdentityItemSearchResult> response,
                            Object state) {
                        assertNotNull(response);

                        List<IdentityItemSearchResult> list = response.getList();
                        assertNotNull(list);

                        IdentityItemSearchResult item = list.get(0);
                        assertEquals("Test Value", item.getValue());
                    }

                });
    }

    public void testAddIdentityValue() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        Identity identity = testAuthUser.getIdentityValues();

        identity.add("Test Value", null, new OnCallback<Response<Boolean>>() {

            @Override
            public void OnResponse(Response<Boolean> response, Object state) {
                assertNotNull(response);
                assertTrue(response.getResult());
            }

        });
    }

    public void testRemoveIdentityValue() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        Identity identity = testAuthUser.getIdentityValues();

        identity.remove("Test Value", null, new OnCallback<Response<Boolean>>() {

            @Override
            public void OnResponse(Response<Boolean> response, Object state) {
                assertNotNull(response);
                assertTrue(response.getResult());
            }

        });
    }
}
