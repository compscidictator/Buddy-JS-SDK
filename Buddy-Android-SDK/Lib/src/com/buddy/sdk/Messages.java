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

import java.util.Date;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Constants;
import com.buddy.sdk.utils.Utils;

/**
 * Represents an object that can be used to send message from one user to
 * another.
 * <p>
 * 
 * <pre>
 * {@code private AuthenticatedUser user = null;}
 * 
 *    {@code BuddyClient client = new BuddyClient("APPNAME", "APPPASS");}
 *    {@code client.login("username", "password", new OnCallback<Response<AuthenticatedUser>>()}
 *    <code>{</code>
 *        {@code public void OnResponse(Response<AuthenticatedUser> response, Object state)}
 *        <code>{</code>
 *            {@code user = response.getResult();}
 *            {@code user.getMessages().send("Some Message", someOtherUser, new OnCallback<Response<Boolean>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<Boolean> response, Object state)}
 *                <code>{</code>
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class Messages {
    private MessageDataModel messageDataModel = null;

    protected BuddyClient client;
    protected AuthenticatedUser user;

    private MessageGroups groups;

    Messages(BuddyClient client, AuthenticatedUser user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");

        this.groups = new MessageGroups(client, user);
        this.client = client;
        this.user = user;

        this.messageDataModel = new MessageDataModel(this.client, this.user);
    }

    public MessageGroups getMessageGroups() {
        return this.groups;
    }

    /**
     * Send a message to a user from the current authenticated user.
     * 
     * @param toUser The user to send a message to.
     * @param message The message to send, must be less then 200 characters.
     * @param appTag The user to send a message to.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void send(User toUser, String message, String appTag, Object state,
            OnCallback<Response<Boolean>> callback) {
        if (toUser == null)
            throw new IllegalArgumentException("toUser can't be null.");
        if (message == null || message.length() > 200)
            throw new IllegalArgumentException(
                    "message can't be null or larger then 200 characters.");

        if (this.messageDataModel != null) {
            this.messageDataModel.send(message, toUser, appTag, state, callback);
        }
    }

    /**
     * Send a message to a user from the current authenticated user.
     * 
     * @param toUser The user to send a message to.
     * @param message The message to send, must be less then 200 characters.
     * @param callback The async callback to call on success or error.
     */
    public void send(User toUser, String message, OnCallback<Response<Boolean>> callback) {
        send(toUser, message, "", null, callback);
    }

    /**
     * Get all received message by the current user.
     * 
     * @param afterDate Optionally retrieve only messages after a certain Date.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getReceived(Date afterDate, Object state, OnCallback<ListResponse<Message>> callback) {
        if (this.messageDataModel != null) {
            this.messageDataModel.getReceived(Utils.convertMinDateStringTo1950(afterDate), state, callback);
        }
    }

    /**
     * Get all received message by the current user.
     * 
     * @param callback The async callback to call on success or error.
     */
    public void getReceived(OnCallback<ListResponse<Message>> callback) {
        getReceived(Constants.MinDate, null, callback);
    }

    /**
     * Get all sent message by the current user.
     * 
     * @param afterDate Optionally retrieve only messages after a certain Date.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getSent(Date afterDate, Object state, OnCallback<ListResponse<Message>> callback) {
        if (this.messageDataModel != null) {
            this.messageDataModel.getSent(Utils.convertMinDateStringTo1950(afterDate), state, callback);
        }
    }

    /**
     * Get all sent message by the current user.
     * 
     * @param callback The async callback to call on success or error.
     */
    public void getSent(OnCallback<ListResponse<Message>> callback) {
        getSent(Constants.MinDate, null, callback);
    }
}
