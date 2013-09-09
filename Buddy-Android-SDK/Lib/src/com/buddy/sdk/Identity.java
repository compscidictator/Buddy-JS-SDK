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

package com.buddy.sdk;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a class that can access identity values for a user or search for
 * values across the entire app. Identity values can be used to share public
 * information between users, for example hashes of email address that can be
 * used to check whether a certain user is in the system.
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
 *            {@code user.getIdentity().getAll(null, new OnCallback<Response<IdentityItem>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<IdentityItem> response, Object state)}
 *                <code>{</code>
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class Identity {
    private BuddyClient client;

    private IdentityDataModel identityDataModel = null;

    Identity(BuddyClient client, String token) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null");
        if (Utils.isNullOrEmpty(token))
            throw new IllegalArgumentException("token can't be null or empty");

        this.client = client;

        this.identityDataModel = new IdentityDataModel(this.client, token);
    }

    /**
     * Check for the existence of an identity value in the system. The search is
     * perform for the entire app.
     * 
     * @param values The value to search for. This can either be a single value
     *            or a semi-colon separated list of values.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void checkForValues(String values, Object state,
            OnCallback<ListResponse<IdentityItemSearchResult>> callback) {
        if (Utils.isNullOrEmpty(values))
            throw new IllegalArgumentException("values can not be null or empty.");
        this.identityDataModel.checkForValues(values, state, callback);
    }

    /**
     * Add an identity value for this user.
     * 
     * @param value The value to add.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void add(String value, Object state, OnCallback<Response<Boolean>> callback) {
        if (Utils.isNullOrEmpty(value))
            throw new IllegalArgumentException("value can not be null or empty.");
        this.identityDataModel.add(value, state, callback);
    };

    /**
     * Returns all the identity values for this user.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getAll(Object state, OnCallback<ListResponse<IdentityItem>> callback) {
        this.identityDataModel.getAll(state, callback);
    }

    /**
     * Remove an identity value for this user.
     * 
     * @param value The value to remove.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void remove(String value, Object state, OnCallback<Response<Boolean>> callback) {
        if (Utils.isNullOrEmpty(value))
            throw new IllegalArgumentException("value can not be null or empty.");
        this.identityDataModel.remove(value, state, callback);
    }
}
