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
 * Represents a collection of friends. Use the AuthenticatedUser.getFriends()
 * method to access this object.
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
 * </pre>
 */
public class Friends {
    private FriendRequests requests = null;
    private FriendsDataModel friendsDataModel = null;
    protected BuddyClient client;
    protected AuthenticatedUser user;

    /**
     * Gets a list of friend requests that are still pending for this user.
     */
    public FriendRequests getRequests() {
        return requests;
    }

    Friends(BuddyClient client, AuthenticatedUser user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null or empty.");
        if (user == null)
            throw new IllegalArgumentException("user can't be null or empty.");

        this.client = client;
        this.user = user;
        this.requests = new FriendRequests(client, user);

        this.friendsDataModel = new FriendsDataModel(client, user);
    }

    /**
     * Returns the list of friends for the user.
     * 
     * @param afterDate Filter the list by friends added 'afterDate'.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getAll(Date afterDate, Object state, OnCallback<ListResponse<User>> callback) {
        if (this.friendsDataModel != null) {
            this.friendsDataModel.getAll(Utils.convertMinDateStringTo1950(afterDate), state, callback);
        }
    }

    /**
     * Returns the list of friends for the user.
     * 
     * @param callback The async callback to call on success or error.
     */
    public void getAll(OnCallback<ListResponse<User>> callback) {
        getAll(Constants.MinDate, null, callback);
    }

    /**
     * Remove a user from the current list of friends.
     * 
     * @param user The user to remove from the friends list. Must be already on
     *            the list and can't be null.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void remove(User user, Object state, OnCallback<Response<Boolean>> callback) {
        if (this.friendsDataModel != null) {
            this.friendsDataModel.remove(user.getId(), state, callback);
        }
    }
}
