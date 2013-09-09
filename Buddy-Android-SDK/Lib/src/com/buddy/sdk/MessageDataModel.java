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

import android.util.SparseBooleanArray;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.GroupMessageDataResponse;
import com.buddy.sdk.json.responses.GroupMessageDataResponse.GroupMessageData;
import com.buddy.sdk.json.responses.GroupMessageSendDataResponse;
import com.buddy.sdk.json.responses.GroupMessageSendDataResponse.GroupMessageSendData;
import com.buddy.sdk.json.responses.MessageDataResponse;
import com.buddy.sdk.json.responses.MessageDataResponse.MessageData;
import com.buddy.sdk.json.responses.MessageGroupDataResponse;
import com.buddy.sdk.json.responses.MessageGroupDataResponse.MessageGroupData;
import com.buddy.sdk.json.responses.MessageSentDataResponse;
import com.buddy.sdk.json.responses.MessageSentDataResponse.MessageSentData;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.web.BuddyWebWrapper;

class MessageDataModel extends BaseDataModel {
    private AuthenticatedUser authUser = null;

    MessageDataModel(BuddyClient client, AuthenticatedUser user) {
        this.client = client;
        this.authUser = user;
    }

    // Messages - Messages
    public void send(String message, User toUser, String applicationTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Messages_Message_Send(client.getAppName(), client.getAppPassword(),
                this.authUser.getToken(), message, toUser.getId(), applicationTag,
                this.RESERVED, state, new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void getReceived(Date afterDate, Object state,
            final OnCallback<ListResponse<Message>> callback) {
        
        String dateParam = Utils.convertStringDate(afterDate, "MM/dd/yyyy");

        BuddyWebWrapper.Messages_Messages_Get(client.getAppName(), client.getAppPassword(),
                this.authUser.getToken(), dateParam, state, new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<Message> listResponse = new ListResponse<Message>();
                        if (response != null) {
                            if (response.completed) {
                                MessageDataResponse data = getJson(response.response,
                                        MessageDataResponse.class);
                                if (data != null) {
                                    List<Message> msgList = new ArrayList<Message>(data.data.size());
                                    for (MessageData msgData : data.data) {
                                        Message message = new Message(msgData, authUser.getId());
                                        msgList.add(message);
                                    }
                                    listResponse.setList(msgList);
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

    public void getSent(Date afterDate, Object state,
            final OnCallback<ListResponse<Message>> callback) {
        
        String dateParam = Utils.convertStringDate(afterDate, "MM/dd/yyyy");

        BuddyWebWrapper.Messages_SentMessages_Get(client.getAppName(), client.getAppPassword(),
                this.authUser.getToken(), dateParam, state, new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<Message> listResponse = new ListResponse<Message>();
                        if (response != null) {
                            if (response.completed) {
                                MessageSentDataResponse data = getJson(response.response,
                                        MessageSentDataResponse.class);
                                if (data != null) {
                                    List<Message> msgList = new ArrayList<Message>(data.data.size());
                                    for (MessageSentData msgData : data.data) {
                                        Message message = new Message(msgData, authUser.getId());
                                        msgList.add(message);
                                    }
                                    listResponse.setList(msgList);
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

    // GroupMessage - Messages
    public void getReceivedGroupMessages(Date afterDate, final MessageGroup group, Object state,
            final OnCallback<ListResponse<GroupMessage>> callback) {
        
        String dateParam = Utils.convertStringDate(afterDate, "MM/dd/yyyy");

        BuddyWebWrapper.GroupMessages_Message_Get(client.getAppName(), client.getAppPassword(),
                this.authUser.getToken(), group.getId(), dateParam, this.RESERVED, state,
                new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<GroupMessage> listResponse = new ListResponse<GroupMessage>();
                        if (response != null) {
                            if (response.completed) {
                                GroupMessageDataResponse data = getJson(response.response,
                                        GroupMessageDataResponse.class);
                                if (data != null) {
                                    List<GroupMessage> msgList = new ArrayList<GroupMessage>(
                                            data.data.size());
                                    for (GroupMessageData msgData : data.data) {
                                        GroupMessage groupMessage = new GroupMessage(client,
                                                msgData, group);
                                        msgList.add(groupMessage);
                                    }
                                    listResponse.setList(msgList);
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

    public void sendMessage(Integer groupChatId, String messageContent, double latitude,
            double longitude, String applicationTag, Object state,
            final OnCallback<Response<SparseBooleanArray>> callback) {
        
        BuddyWebWrapper.GroupMessages_Message_Send(client.getAppName(), client.getAppPassword(),
                this.authUser.getToken(), groupChatId, messageContent, (float) latitude,
                (float) longitude, applicationTag, this.RESERVED, state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<SparseBooleanArray> sendResponse = new Response<SparseBooleanArray>();
                        if (response != null) {
                            if (response.completed) {
                                GroupMessageSendDataResponse data = getJson(response.response,
                                        GroupMessageSendDataResponse.class);
                                if (data != null) {
                                    SparseBooleanArray map = new SparseBooleanArray(data.data
                                            .size());
                                    for (GroupMessageSendData msgData : data.data) {
                                        Integer memberId = Utils.parseInt(msgData.memberUserIDList);
                                        map.put(memberId, msgData.sendResult.equals("1") ? true
                                                : false);
                                    }
                                    sendResponse.setResult(map);
                                } else {
                                    sendResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                sendResponse.setThrowable(response.exception);
                            }
                        } else {
                            sendResponse.setThrowable(new ServiceUnknownErrorException());

                        }
                        callback.OnResponse(sendResponse, state);
                    }

                });
    }

    // GroupMessages - Manage
    public void checkIfExists(String groupName, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.GroupMessages_Manage_CheckForGroup(client.getAppName(),
                client.getAppPassword(), this.authUser.getToken(), groupName, this.RESERVED,
                state, new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void create(final String groupName, Boolean openGroup, String applicationTag,
            Object state, final OnCallback<Response<MessageGroup>> callback) {
        
        BuddyWebWrapper.GroupMessages_Manage_CreateGroup(client.getAppName(), client
                .getAppPassword(), this.authUser.getToken(), groupName, openGroup ? "1" : "0",
                applicationTag, this.RESERVED, state, new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<MessageGroup> groupMessageResponse = new Response<MessageGroup>();
                        if (response != null) {
                            if (response.completed) {
                                MessageGroup messageGroup = null;
                                Response<String> stringResponse = getStringResponse(response);
                                if (stringResponse.isCompleted()) {
                                	try {
                                        messageGroup = new MessageGroup(client, authUser, Utils
                                                .parseInt(stringResponse.getResult()), groupName);
                                        groupMessageResponse.setResult(messageGroup);
                                	} 
                                	catch (NumberFormatException ex) {
                                		groupMessageResponse.setThrowable(ex);
                                	}
                                } else {
                                    groupMessageResponse.setThrowable(stringResponse.getThrowable());
                                }
                            } else {
                                groupMessageResponse.setThrowable(response.exception);
                            }
                        } else {
                            groupMessageResponse.setThrowable(new ServiceUnknownErrorException());

                        }
                        callback.OnResponse(groupMessageResponse, state);
                    }

                });
    }

    public void delete(Integer groupId, Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.GroupMessages_Manage_DeleteGroup(client.getAppName(),
                client.getAppPassword(), this.authUser.getToken(), groupId, this.RESERVED,
                state, new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void join(Integer groupId, Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.GroupMessages_Membership_JoinGroup(client.getAppName(),
                client.getAppPassword(), this.authUser.getToken(), groupId, this.RESERVED,
                state, new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void leave(Integer groupId, Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.GroupMessages_Membership_DepartGroup(client.getAppName(),
                client.getAppPassword(), this.authUser.getToken(), groupId, this.RESERVED,
                state, new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void addUser(Integer groupId, User user, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.GroupMessages_Membership_AddNewMember(client.getAppName(),
                client.getAppPassword(), this.authUser.getToken(), groupId, user.getId(),
                RESERVED, state, new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void removeUser(Integer groupId, User user, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.GroupMessages_Membership_RemoveUser(client.getAppName(),
                client.getAppPassword(), this.authUser.getToken(), user.getId(), groupId,
                RESERVED, state, new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void getAll(Object state, final OnCallback<ListResponse<MessageGroup>> callback) {
        
        BuddyWebWrapper.GroupMessages_Membership_GetAllGroups(client.getAppName(),
                client.getAppPassword(), this.authUser.getToken(), this.RESERVED, state,
                new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<MessageGroup> listResponse = new ListResponse<MessageGroup>();
                        if (response != null) {
                            if (response.completed) {
                                MessageGroupDataResponse data = getJson(response.response,
                                        MessageGroupDataResponse.class);
                                if (data != null) {
                                    List<MessageGroup> msgList = new ArrayList<MessageGroup>(
                                            data.data.size());
                                    for (MessageGroupData msgData : data.data) {
                                        MessageGroup messageGroup = new MessageGroup(client,
                                                authUser, msgData);
                                        msgList.add(messageGroup);
                                    }
                                    listResponse.setList(msgList);
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

    public void getMy(Object state, final OnCallback<ListResponse<MessageGroup>> callback) {
        
        BuddyWebWrapper.GroupMessages_Membership_GetMyList(client.getAppName(),
                client.getAppPassword(), this.authUser.getToken(), this.RESERVED, state,
                new OnResponseCallback() {

        		   @Override
                   public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<MessageGroup> listResponse = new ListResponse<MessageGroup>();
                        if (response != null) {
                            if (response.completed) {
                                MessageGroupDataResponse data = getJson(response.response,
                                        MessageGroupDataResponse.class);
                                if (data != null) {
                                    List<MessageGroup> msgList = new ArrayList<MessageGroup>(
                                            data.data.size());
                                    for (MessageGroupData msgData : data.data) {
                                        MessageGroup messageGroup = new MessageGroup(client,
                                                authUser, msgData);
                                        msgList.add(messageGroup);
                                    }
                                    listResponse.setList(msgList);
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
