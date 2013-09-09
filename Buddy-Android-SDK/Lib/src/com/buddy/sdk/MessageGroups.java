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

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;

/**
 * Represents an object that can be used to create or query message groups for
 * the app.
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
 *            {@code user.getMessages().getMessageGroups().create("My Group", true, new OnCallback<Response<MessageGroup>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<MessageGroup> response, Object state)}
 *                <code>{</code>
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class MessageGroups {
    private MessageDataModel messageDataModel = null;

    protected BuddyClient client;
    protected AuthenticatedUser user;

    MessageGroups(BuddyClient client, AuthenticatedUser user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");
        this.client = client;
        this.user = user;

        this.messageDataModel = new MessageDataModel(this.client, this.user);
    }

    /**
     * Create a new message group.
     * 
     * @param name The name of the new group, must be unique for the app.
     * @param openGroup Optionally whether to make to group open for all user
     *            (anyone can join), or closed (only the owner can add users to
     *            it).
     * @param appTag An optional application tag for this message group.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void create(String name, Boolean openGroup, String appTag, Object state,
            OnCallback<Response<MessageGroup>> callback) {
        if (this.messageDataModel != null) {
            this.messageDataModel.create(name, openGroup, appTag, state, callback);
        }
    }

    /**
     * Create a new message group.
     * 
     * @param name The name of the new group, must be unique for the app.
     * @param openGroup Optionally whether to make to group open for all user
     *            (anyone can join), or closed (only the owner can add users to
     *            it).
     * @param callback The async callback to call on success or error.
     */
    public void create(String name, Boolean openGroup, OnCallback<Response<MessageGroup>> callback) {
        create(name, openGroup, "", null, callback);
    }

    /**
     * Check if a group with this name already exists.
     * 
     * @param name The name of the group to check for.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void checkIfExists(String name, Object state, OnCallback<Response<Boolean>> callback) {
        if (this.messageDataModel != null) {
            this.messageDataModel.checkIfExists(name, state, callback);
        }
    }

    /**
     * Get all message groups for this app.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getAll(Object state, OnCallback<ListResponse<MessageGroup>> callback) {
        if (this.messageDataModel != null) {
            this.messageDataModel.getAll(state, callback);
        }
    }

    /**
     * Get all message groups that this user is part of.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getMy(Object state, OnCallback<ListResponse<MessageGroup>> callback) {
        if (this.messageDataModel != null) {
            this.messageDataModel.getMy(state, callback);
        }
    }
}
