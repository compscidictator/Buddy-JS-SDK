
package com.buddy.sdk.unittests;

import java.util.List;
import android.util.SparseBooleanArray;

import com.buddy.sdk.GroupMessage;
import com.buddy.sdk.Message;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.MessageGroup;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Constants;
import com.buddy.sdk.web.BuddyHttpClientFactory;

public class MessagingUnitTests extends BaseUnitTest {
    @Override
    protected void setUp() throws Exception {
        createAuthenticatedUserAnd2ndUser();
    }

    public void testGetSentMessages() {
        String jsonResponse = readDataFromFile("DataResponses/MessageUnitTests-GetSent.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getMessages().getSent(Constants.MinDate, null,
                new OnCallback<ListResponse<Message>>() {
                    public void OnResponse(ListResponse<Message> response, Object state) {
                        assertNotNull(response);

                        List<Message> list = response.getList();
                        assertNotNull(list);

                        Message message = list.get(0);
                        assertEquals("Test Message", message.getText());
                    }

                });
    }

    public void testGetMessages() {
        String jsonResponse = readDataFromFile("DataResponses/MessageUnitTests-GetSent.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getMessages().getReceived(Constants.MinDate, null,
                new OnCallback<ListResponse<Message>>() {
                    public void OnResponse(ListResponse<Message> response, Object state) {
                        assertNotNull(response);

                        List<Message> list = response.getList();
                        assertNotNull(list);

                        Message message = list.get(0);
                        assertEquals("Test Message", message.getText());
                    }

                });
    }

    public void testSendMessage() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getMessages().send(testUser, "Test Message", "", null,
                new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }

    public void testGetGroupMessages() {
        String jsonResponse = readDataFromFile("DataResponses/MessageUnitTests-GetAllGroups.json");
        String jsonSuccess = readDataFromFile("DataResponses/MessageUnitTests-GetGroupMessage.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonSuccess);

        testAuthUser.getMessages().getMessageGroups()
                .getAll(null, new OnCallback<ListResponse<MessageGroup>>() {
                    public void OnResponse(ListResponse<MessageGroup> response, Object state) {
                        assertNotNull(response);

                        List<MessageGroup> list = response.getList();
                        assertNotNull(list);

                        MessageGroup group = list.get(0);
                        group.getReceived(Constants.MinDate, null,
                                new OnCallback<ListResponse<GroupMessage>>() {
                                    public void OnResponse(ListResponse<GroupMessage> response,
                                            Object state) {
                                        assertNotNull(response);

                                        List<GroupMessage> list = response.getList();
                                        assertNotNull(list);

                                        GroupMessage group = list.get(0);
                                        assertEquals("Test message", group.getText());
                                    }

                                });
                    }

                });
    }

    public void testSendGroupMessage() {
        String jsonResponse = readDataFromFile("DataResponses/MessageUnitTests-GetAllGroups.json");
        String jsonSuccess = readDataFromFile("DataResponses/MessageUnitTests-SendGroupMessage.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonSuccess);

        testAuthUser.getMessages().getMessageGroups()
                .getAll(null, new OnCallback<ListResponse<MessageGroup>>() {
                    public void OnResponse(ListResponse<MessageGroup> response, Object state) {
                        assertNotNull(response);

                        List<MessageGroup> list = response.getList();
                        assertNotNull(list);

                        MessageGroup group = list.get(0);
                        group.sendMessage("Test message", 0.0, 0.0, "", null,
                                new OnCallback<Response<SparseBooleanArray>>() {
                                    public void OnResponse(Response<SparseBooleanArray> response,
                                            Object state) {
                                        assertNotNull(response);

                                        SparseBooleanArray array = response.getResult();
                                        assertTrue(array.get(testUserIdInteger));
                                    }

                                });
                    }

                });
    }

    public void testRemoveUser() {
        String jsonResponse = readDataFromFile("DataResponses/MessageUnitTests-GetAllGroups.json");
        String jsonSuccess = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonSuccess);

        testAuthUser.getMessages().getMessageGroups()
                .getAll(null, new OnCallback<ListResponse<MessageGroup>>() {
                    public void OnResponse(ListResponse<MessageGroup> response, Object state) {
                        assertNotNull(response);

                        List<MessageGroup> list = response.getList();
                        assertNotNull(list);

                        MessageGroup group = list.get(0);
                        group.removeUser(testUser, null, new OnCallback<Response<Boolean>>() {
                            public void OnResponse(Response<Boolean> response, Object state) {
                                assertNotNull(response);
                                assertTrue(response.getResult());
                            }

                        });
                    }

                });
    }

    public void testJoinGroup() {
        String jsonResponse = readDataFromFile("DataResponses/MessageUnitTests-GetAllGroups.json");
        String jsonSuccess = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonSuccess);

        testAuthUser.getMessages().getMessageGroups()
                .getAll(null, new OnCallback<ListResponse<MessageGroup>>() {
                    public void OnResponse(ListResponse<MessageGroup> response, Object state) {
                        assertNotNull(response);

                        List<MessageGroup> list = response.getList();
                        assertNotNull(list);

                        MessageGroup group = list.get(0);
                        group.join(null, new OnCallback<Response<Boolean>>() {
                            public void OnResponse(Response<Boolean> response, Object state) {
                                assertNotNull(response);
                                assertTrue(response.getResult());
                            }

                        });
                    }

                });
    }

    public void testGetMyList() {
        String jsonResponse = readDataFromFile("DataResponses/MessageUnitTests-GetAllGroups.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getMessages().getMessageGroups()
                .getMy(null, new OnCallback<ListResponse<MessageGroup>>() {
                    public void OnResponse(ListResponse<MessageGroup> response, Object state) {
                        assertNotNull(response);

                        List<MessageGroup> list = response.getList();
                        assertNotNull(list);

                        MessageGroup group = list.get(0);
                        assertEquals(142020, group.getId());
                    }

                });
    }

    public void testGetAllGroups() {
        String jsonResponse = readDataFromFile("DataResponses/MessageUnitTests-GetAllGroups.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getMessages().getMessageGroups()
                .getAll(null, new OnCallback<ListResponse<MessageGroup>>() {
                    public void OnResponse(ListResponse<MessageGroup> response, Object state) {
                        assertNotNull(response);

                        List<MessageGroup> list = response.getList();
                        assertNotNull(list);

                        MessageGroup group = list.get(0);
                        assertEquals(142020, group.getId());
                    }

                });
    }

    public void testDepartGroup() {
        String jsonResponse = readDataFromFile("DataResponses/MessageUnitTests-GetAllGroups.json");
        String jsonSuccess = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonSuccess);

        testAuthUser.getMessages().getMessageGroups()
                .getAll(null, new OnCallback<ListResponse<MessageGroup>>() {
                    public void OnResponse(ListResponse<MessageGroup> response, Object state) {
                        assertNotNull(response);

                        List<MessageGroup> list = response.getList();
                        assertNotNull(list);

                        MessageGroup group = list.get(0);
                        group.leave(null, new OnCallback<Response<Boolean>>() {
                            public void OnResponse(Response<Boolean> response, Object state) {
                                assertNotNull(response);
                                assertTrue(response.getResult());
                            }

                        });
                    }

                });
    }

    public void testAddMember() {
        String jsonResponse = readDataFromFile("DataResponses/MessageUnitTests-GetAllGroups.json");
        String jsonSuccess = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonSuccess);

        testAuthUser.getMessages().getMessageGroups()
                .getAll(null, new OnCallback<ListResponse<MessageGroup>>() {
                    public void OnResponse(ListResponse<MessageGroup> response, Object state) {
                        assertNotNull(response);

                        List<MessageGroup> list = response.getList();
                        assertNotNull(list);

                        MessageGroup group = list.get(0);
                        group.addUser(testUser, null, new OnCallback<Response<Boolean>>() {
                            public void OnResponse(Response<Boolean> response, Object state) {
                                assertNotNull(response);
                                assertTrue(response.getResult());
                            }

                        });
                    }

                });
    }

    public void testDeleteGroup() {
        String jsonResponse = readDataFromFile("DataResponses/MessageUnitTests-GetAllGroups.json");
        String jsonSuccess = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonSuccess);

        testAuthUser.getMessages().getMessageGroups()
                .getAll(null, new OnCallback<ListResponse<MessageGroup>>() {
                    public void OnResponse(ListResponse<MessageGroup> response, Object state) {
                        assertNotNull(response);

                        List<MessageGroup> list = response.getList();
                        assertNotNull(list);

                        MessageGroup group = list.get(0);
                        group.delete(null, new OnCallback<Response<Boolean>>() {
                            public void OnResponse(Response<Boolean> response, Object state) {
                                assertNotNull(response);
                                assertTrue(response.getResult());
                            }

                        });
                    }

                });
    }

    public void testCreateGroup() {
        String jsonResponse = readDataFromFile("DataResponses/MessageUnitTests-CreateGroup.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getMessages().getMessageGroups()
                .create("Test Group", true, "", null, new OnCallback<Response<MessageGroup>>() {
                    public void OnResponse(Response<MessageGroup> response, Object state) {
                        assertNotNull(response);
                        MessageGroup group = response.getResult();

                        assertEquals(142009, group.getId());
                    }

                });
    }

    public void testCheckForGroup() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getMessages().getMessageGroups()
                .checkIfExists("Test Group", null, new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }
}
