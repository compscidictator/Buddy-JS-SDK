/*
 * Copyright (C) 2012 Buddy Platform, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.buddy.sdk;

import com.buddy.sdk.json.responses.MetroAreaDataResponse.MetroAreaData;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a single, named metropolitan area in the Buddy system.
 * 
 */
public class MetroArea {
    protected BuddyClient client;
    protected AuthenticatedUser user;

    private String iconURL;
    private String imageURL;
    private String metroName;
    private int startupCount;

    MetroArea(BuddyClient client, AuthenticatedUser user, MetroAreaData data) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");
        if (user == null)
            throw new IllegalArgumentException("user can't be null.");
        this.client = client;
        this.user = user;

        this.iconURL = data.iconURL;
        this.imageURL = data.imageURL;
        this.metroName = data.metroName;
        this.startupCount = Utils.parseInt(data.startupCount);
    }

    /**
     * Gets the icon URL an image for the area.
     */
    public String getIconURL() {
        return this.iconURL;
    }
    
    /**
     * Gets the image URL an image for the area.
     */
    public String getImageURL() {
        return this.imageURL;
    }

    /**
     * Gets the name of the supported metro area.
     */
    public String getMetroName() {
        return this.metroName;
    }

    /**
     * Gets the number of startups in the supported metro area.
     */
    public int getStartupCount() {
        return this.startupCount;
    }
}
