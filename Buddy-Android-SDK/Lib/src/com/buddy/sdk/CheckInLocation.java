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
 * Represents a single user check-in location.
 * <p>
 * 
 * <pre>
 *    {@code BuddyClient client = new BuddyClient("APPNAME", "APPPASS");}
 *    {@code client.createUser("username", "password", new OnCallback<Response<AuthenticatedUser>>()}
 *    <code>{</code>
 *        {@code public void OnResponse(Response<AuthenticatedUser> response, Object state)}
 *        <code>{</code>
 *            {@code Authenticated user = response.getResult();}
 *            {@code user.checkIn(0.0, 0.0, "test check in", "test tag", null, new OnCallback<Response<Boolean>>()}
 *            <code>{</code>
 *            <code>}</code>
 *            {@code user.getCheckIns(null, null, new OnCallback<ListResponse<CheckInLocation>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<CheckInLocation> response, Object state)}
 *                <code>{</code>
 *                    {@code List<CheckInLocation> = response.getList();}
 *                <code>}</code>
 *            <code>}</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class CheckInLocation {
    private double latitude;
    private double longitude;
    private Date checkInDate;
    private String placeName;
    private String comment;
    private String appTag;

    CheckInLocation(double latitude, double longitude, Date checkInDate, String placeName,
            String comment, String appTag) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.checkInDate = checkInDate;
        this.placeName = placeName;
        this.comment = comment;
        this.appTag = appTag;
    }

    /**
     * Gets the latitude of the check-in location.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude of the check-in location.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Gets the date of the check-in.
     */
    public Date getCheckInDate() {
        return checkInDate;
    }

    /**
     * Gets the name of the place where the check-in happened.
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * Gets the comment associated with this check-in.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Gets the application tag associated with this check-in.
     */
    public String getAppTag() {
        return appTag;
    }
}
