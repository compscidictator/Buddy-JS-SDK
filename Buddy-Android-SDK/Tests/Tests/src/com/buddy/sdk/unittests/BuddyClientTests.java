
package com.buddy.sdk.unittests;

import com.buddy.sdk.AuthenticatedUser;
import com.buddy.sdk.BuddyClient;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.web.BuddyHttpClientFactory;

public class BuddyClientTests extends BaseUnitTest {
    public void testLogin() {
        String jsonValue = testToken;
        String jsonValueUser = readDataFromFile("DataResponses/validUserResponse.json");
        String jsonDeviceReportingResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValue);
        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);
        BuddyHttpClientFactory.addDummyResponse(jsonDeviceReportingResponse);

        BuddyHttpClientFactory.setUnitTestMode(true);

        BuddyClient testClient = new BuddyClient(applicationName, applicationPassword, this.getInstrumentation().getContext(), "0.1", true);

        testClient.login(testUserName, testUserPassword, null,
                new OnCallback<Response<AuthenticatedUser>>() {
                    public void OnResponse(Response<AuthenticatedUser> response, Object state) {
                        AuthenticatedUser user = response.getResult();
                        assertNotNull(response.getResult());
                        assertEquals(testUserName, user.getName());
                        String userId = String.valueOf(user.getId());
                        assertEquals(testUserId, userId);
                    }
                });
    }
}
