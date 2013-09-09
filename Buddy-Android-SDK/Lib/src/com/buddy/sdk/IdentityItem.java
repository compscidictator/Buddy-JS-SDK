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
 * Represents an identity item that belongs to a user.
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
public class IdentityItem {
    private String value;
    private Date createdOn;

    IdentityItem(String value, Date created) {
        this.value = value;
        this.createdOn = created;
    }

    /**
     * Gets the value of the identity item.
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the date the identity value was added.
     */
    public Date getCreatedOn() {
        return createdOn;
    }
}
