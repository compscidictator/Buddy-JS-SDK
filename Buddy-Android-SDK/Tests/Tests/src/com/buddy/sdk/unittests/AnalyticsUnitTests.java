
package com.buddy.sdk.unittests;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.web.BuddyHttpClientFactory;

public class AnalyticsUnitTests extends BaseUnitTest {
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createAuthenticatedUser();
    }

    public void testStartSession() {
        String jsonValue = readDataFromFile("DataResponses/AnalyticsUnitTests-StartSession.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValue);

        testClient.startSession(testAuthUser, "Test Session", "", null,
                new OnCallback<Response<String>>() {
                    public void OnResponse(Response<String> response, Object state) {
                        assertNotNull(response);

                        String id = response.getResult();

                        assertEquals("617564", id);
                    }
                });
    }

    public void testStartSessionOverload() {
        String jsonValue = readDataFromFile("DataResponses/AnalyticsUnitTests-StartSession.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValue);

        testClient.startSession(testAuthUser, "Test Session", new OnCallback<Response<String>>() {
            public void OnResponse(Response<String> response, Object state) {
                assertNotNull(response);

                String id = response.getResult();

                assertEquals("617564", id);
            }
        });
    }

    public void testEndSession() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testClient.endSession(testAuthUser, "617564", "", null,
                new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }

    public void testEndSessionOverload() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testClient.endSession(testAuthUser, "617564", new OnCallback<Response<Boolean>>() {
            public void OnResponse(Response<Boolean> response, Object state) {
                assertNotNull(response);
                assertTrue(response.getResult());
            }

        });
    }

    public void testSessionRecordMetric() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testClient.recordSessionMetric(testAuthUser, "617564", "Test metric", "Test value", "",
                null, new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }

    public void testSessionRecordMetricOverload() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testClient.recordSessionMetric(testAuthUser, "617564", "Test metric", "Test value",
                new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }

    public void testCrashRecordsAdd() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testClient.getDevice().recordCrash("TestMethod", "4.0.3", "Android", testAuthUser, "1.0", "", 0,
                0, "", null, new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }

    public void testDeviceInformationAdd() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testClient.getDevice().recordInformation("4.0.3", "Android", testAuthUser, "1.0", 0.0, 0.0, "", null,
                new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }
}
