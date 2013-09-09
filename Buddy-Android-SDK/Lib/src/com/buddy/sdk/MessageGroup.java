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
import com.buddy.sdk.json.responses.MessageGroupDataResponse.MessageGroupData;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Constants;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a single message group. Message groups are groups of users that
 * can message each other. Groups can either be public, with anyone being able
 * to join them, or private - where only the user that create the group can add
 * other users to it.
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
public class MessageGroup {
    protected BuddyClient client;
    protected AuthenticatedUser user;

    private MessageDataModel messageDataModel = null;
    private List<Integer> memberUserIDs;

    private int id;
    private String name;
    private Date createdOn;
    private String appTag;
    private int ownerUserID;

    /**
     * Gets the App unique ID of the message group.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the name of the message group.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the Date the message group was created.
     */
    public Date getCreatedOn() {
        return this.createdOn;
    }

    /**
     * Gets the app tag that was associated with this message group.
     */
    public String getAppTag() {
        return this.appTag;
    }

    /**
     * Gets the ID of the user that created this message group.
     */
    public int getOwnerUserID() {
        return this.ownerUserID;
    }

    /**
     * Gets a list of IDs of users that belong to this message group.
     */
    public List<Integer> getMemberUserIDs() {
        return this.memberUserIDs;
    }

    MessageGroup(BuddyClient client, AuthenticatedUser user, int groupId, String name) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");
        this.client = client;
        this.user = user;
        this.id = groupId;
        this.name = name;

        this.messageDataModel = new MessageDataModel(this.client, this.user);
    }

    MessageGroup(BuddyClient client, AuthenticatedUser user, MessageGroupData group) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");

        this.id = Utils.parseInt(group.chatGroupID);
        this.name = group.chatGroupName;
        this.createdOn = Utils.convertDateString(group.createdDateTime, "MM/dd/yyyy hh:mm:ss aa");
        this.appTag = group.applicationTag;
        this.ownerUserID = Utils.parseInt(group.ownerUserID);
        this.memberUserIDs = new ArrayList<Integer>();
        if (group.memberUserIDList != null) {
            for (String id : group.memberUserIDList.split(";"))
                this.memberUserIDs.add(Utils.parseInt(id));
        }

        this.client = client;
        this.user = user;
        this.messageDataModel = new MessageDataModel(this.client, this.user);
    }

    /**
     * This method has the current user join this message group.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback
     * @param callback The async callback to call on success or error.
     */
    public void join(Object state, final OnCallback<Response<Boolean>> callback) {
        if (this.messageDataModel != null) {
            this.messageDataModel.join(this.id, state, callback);
        }
    }

    /**
     * This method has the current user leave this message group.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback
     * @param callback The async callback to call on success or error.
     */
    public void leave(Object state, final OnCallback<Response<Boolean>> callback) {
        if (this.messageDataModel != null) {
            this.messageDataModel.leave(this.id, state, callback);
        }
    }

    /**
     * Add a user to this message group.
     * 
     * @param userToAdd The User to add to the message group.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void addUser(User userToAdd, Object state, final OnCallback<Response<Boolean>> callback) {
        if (this.messageDataModel != null) {
            this.messageDataModel.addUser(this.id, userToAdd, state, callback);
        }
    }

    /**
     * Remove a user from this message group.
     * 
     * @param userToRemove The user to remove from the group.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void removeUser(User userToRemove, Object state,
            final OnCallback<Response<Boolean>> callback) {
        if (this.messageDataModel != null) {
            this.messageDataModel.removeUser(this.id, userToRemove, state, callback);
        }
    }

    /**
     * Send a message to the entire message group.
     * 
     * @param message The message to send to this group. Must be less then 1000
     *            characters.
     * @param latitude The optional latitude from where this message was sent.
     * @param longitude The optional longitude from where this message was sent.
     * @param appTag An optional application tag for this message.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void sendMessage(String message, double latitude, double longitude, String appTag,
            Object state, final OnCallback<Response<SparseBooleanArray>> callback) {
        if (message == null || message.length() > 1000)
            throw new IllegalArgumentException(
                    "message can't be null or larger then 1000 characters.");
        if (latitude > 90.0 || latitude < -90.0)
            throw new IllegalArgumentException(
                    "latitude can't be bigger than 90.0 or smaller than -90.0.");
        if (longitude > 180.0 || longitude < -180.0)
            throw new IllegalArgumentException(
                    "longitude can't be bigger than 180.0 or smaller than -180.0.");

        if (this.messageDataModel != null) {
            this.messageDataModel.sendMessage(this.id, message, latitude, longitude, appTag, state,
                    callback);
        }
    }

    /**
     * Send a message to the entire message group.
     * 
     * @param message The message to send to this group. Must be less then 1000
     *            characters.
     * @param callback The async callback to call on success or error.
     */
    public void sendMessage(String message, final OnCallback<Response<SparseBooleanArray>> callback) {
        sendMessage(message, 0.0, 0.0, "", null, callback);
    }

    /**
     * Get all messages this group has received.
     * 
     * @param afterDate Optionally return only messages sent after this date.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getReceived(Date afterDate, Object state,
            final OnCallback<ListResponse<GroupMessage>> callback) {
        if (this.messageDataModel != null) {
            this.messageDataModel.getReceivedGroupMessages(Utils.convertMinDateStringTo1950(afterDate), this, state, callback);
        }
    }

    /**
     * Get all messages this group has received.
     * 
     * @param callback The async callback to call on success or error.
     */
    public void getReceived(final OnCallback<ListResponse<GroupMessage>> callback) {
        getReceived(Constants.MinDate, null, callback);
    }

    /**
     * Delete this message group.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void delete(Object state, final OnCallback<Response<Boolean>> callback) {
        if (this.messageDataModel != null) {
            this.messageDataModel.delete(this.id, state, callback);
        }
    }
}
