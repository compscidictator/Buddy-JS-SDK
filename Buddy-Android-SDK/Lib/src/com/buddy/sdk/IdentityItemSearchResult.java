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

import java.util.Date;

/**
 * Represents a single identity search result. Use the
 * AuthenticatedUser.IdentityValues.CheckForValues() method to search for items.
 * A search item can belong to any user in the system.
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
 *            {@code user.getIdentity().checkForValues("somevalue", null, new OnCallback<ListResponse<IdentityItemSearchResult>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(ListResponse<IdentityItemSearchResult> response, Object state)}
 *                <code>{</code>
 *                    {@code List<IdentityItemSearchResult> items = response.getList();}
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class IdentityItemSearchResult extends IdentityItem {
    private boolean found;
    private int belongsToUserId;

    IdentityItemSearchResult(String value, Date created, boolean found, int userId) {
        super(value, created);

        this.found = found;
        this.belongsToUserId = userId;
    }

    /**
     * Gets whether the specific item was found.
     */
    public boolean isFound() {
        return found;
    }

    /**
     * Gets the ID of the user the item was found on.
     */
    public int getBelongsToUserId() {
        return belongsToUserId;
    }
}
