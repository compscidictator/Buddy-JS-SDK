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
 * Represents a collection of friend requests. Use the Add method to request a
 * friend connection from another user.
 * <p>
 * 
 * <pre>
 * {@code private AuthenticatedUser user = null;}
 * {@code private AuthenticatedUser user2 = null;}
 * 
 *    {@code BuddyClient client = new BuddyClient("APPNAME", "APPPASS");}
 *    {@code client.createUser("username", "password", new OnCallback<Response<AuthenticatedUser>>()}
 *    <code>{</code>
 *        {@code public void OnResponse(Response<AuthenticatedUser> response, Object state)}
 *        <code>{</code>
 *            {@code user = response.getResult();}
 *            {@code client.createUser("username2", "password2", new OnCallback<Response<AuthenticatedUser>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<AuthenticatedUser> response, Object state)}
 *                <code>{</code>
 *                    {@code user2 = response.getResult();}
 *                    {@code user.getFriends().getRequests().add(user2, null, new OnCallback<Response<Boolean>>()}
 *                    <code>{</code>
 *                        {@code public void OnResponse(Response<Boolean> response, Object state)}
 *                        <code>{</code>
 *                            {@code user2.getFriends().getRequests().accept(user, null, new OnCallback<Response<Boolean>>()}
 *                            <code>{</code>
 *                                {@code public void OnResponse(Response<Boolean> response, Object state)}
 *                                <code>{</code>
 *                                <code>}</code>
 *                            <code>});</code>
 *                        <code>}</code>
 *                    <code>});</code>
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * 
 * </pre>
 */
public class FriendRequests {
    private FriendsDataModel friendsDataModel = null;
    protected BuddyClient client;
    protected AuthenticatedUser user;

    FriendRequests(BuddyClient client, AuthenticatedUser user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null or empty.");
        if (user == null)
            throw new IllegalArgumentException("user can't be null or empty.");

        this.client = client;
        this.user = user;

        this.friendsDataModel = new FriendsDataModel(client, user);
    }

    /**
     * Add a friend request to a user.
     * 
     * @param user The user to send the request to, can't be null.
     * @param appTag Mark this request with an tag, can be used on the user's
     *            side to make a decision on whether to accept the request.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void add(User user, String appTag, Object state, OnCallback<Response<Boolean>> callback) {
        if (this.friendsDataModel != null) {
            this.friendsDataModel.add(user.getId(), appTag, state, callback);
        }
    }

    /**
     * Add a friend request to a user.
     * 
     * @param friend The user to send the request to, can't be null.
     * @param callback The async callback to call on success or error.
     */
    public void add(User friend, OnCallback<Response<Boolean>> callback) {
        add(friend, "", null, callback);
    }

    /**
     * A list of all users that have request to be friends with this user.
     * 
     * @param afterDate Filter the list by returning only the friend requests
     *            after a certain date.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getAll(Date afterDate, Object state, OnCallback<ListResponse<User>> callback) {
        if (this.friendsDataModel != null) {
            this.friendsDataModel.getAllRequests(Utils.convertMinDateStringTo1950(afterDate), state, callback);
        }
    }

    /**
     * A list of all users that have request to be friends with this user.
     * 
     * @param callback The async callback to call on success or error.
     */
    public void getAll(OnCallback<ListResponse<User>> callback) {
        getAll(Constants.MinDate, null, callback);
    }

    /**
     * Accept a friend request from a user.
     * 
     * @param user The user to accept as friend. Can't be null and must be on
     *            the friend requests list.
     * @param appTag Tag this friend accept with a string.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void accept(User user, String appTag, Object state,
            OnCallback<Response<Boolean>> callback) {
        if (this.friendsDataModel != null) {
            this.friendsDataModel.accept(user.getId(), appTag, state, callback);
        }
    }

    /**
     * Accept a friend request from a user.
     * 
     * @param user The user to accept as friend. Can't be null and must be on
     *            the friend requests list.
     * @param callback The async callback to call on success or error.
     */
    public void accept(User user, OnCallback<Response<Boolean>> callback) {
        accept(user, "", null, callback);
    }

    /**
     * Deny the friend request from a user.
     * 
     * @param user The user to deny the friend request from. User can't be null
     *            and must be on the friend request list.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void deny(User user, Object state, OnCallback<Response<Boolean>> callback) {
        if (this.friendsDataModel != null) {
            this.friendsDataModel.deny(user.getId(), state, callback);
        }
    }
}
