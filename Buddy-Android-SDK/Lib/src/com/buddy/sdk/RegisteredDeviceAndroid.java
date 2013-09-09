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

import com.buddy.sdk.json.responses.NotificationsDevicesDataResponse.NotificationsDevicesData;
import com.buddy.sdk.utils.Utils;

public class RegisteredDeviceAndroid {
    protected AuthenticatedUser user;

    private String registrationID;
    private String groupName;
    private Date lastUpdateDate;
    private Date registrationDate;
    private int userID;

    RegisteredDeviceAndroid(NotificationsDevicesData device, AuthenticatedUser user) {
        if (device == null)
            throw new IllegalArgumentException("device can't be null or empty.");
        if (user == null)
            throw new IllegalArgumentException("user can't be null or empty.");

        this.userID = Utils.parseInt(device.userID);
        this.registrationID = device.registrationID;
        this.groupName = device.groupName;
        this.lastUpdateDate = Utils.convertDateString(device.deviceModified);
        this.registrationDate = Utils.convertDateString(device.deviceRegistered);
        this.user = user;
    }

    public String getRegistrationID() {
        return this.registrationID;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public Date getRegistrationDate() {
        return this.registrationDate;
    }

    public int getUserID() {
        return this.userID;
    }

}
