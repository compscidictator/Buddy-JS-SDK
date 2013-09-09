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
import java.util.Map;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Constants;

/**
 * Represents an object that can be used to register Android devices for push
 * notifications. The class can also be used to query for all registered devices
 * and to send them notifications.
 */
public class NotificationsAndroid {
    private BuddyClient client;
    private AuthenticatedUser user;
    private NotificationsDataModel notificationDataModel;

    NotificationsAndroid(BuddyClient client, AuthenticatedUser user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");
        if (user == null)
            throw new IllegalArgumentException("user can't be null.");

        this.client = client;
        this.user = user;

        this.notificationDataModel = new NotificationsDataModel(this.client, this.user);
    }

    /**
     * Register an Android device for notifications with Buddy.
     * 
     * @param groupName Register this device as part of a group, so that you can
     *            send the whole group messages.
     * @param registrationID The registration ID for the application currently
     *            running on the device. Note: this is the registration ID
     *            returned after registering with GCM.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void registerDevice(String registrationID, String groupName, Object state,
            final OnCallback<Response<Boolean>> callback) {
        if (registrationID == null || registrationID.isEmpty())
            throw new IllegalArgumentException("registrationID can't be null or empty.");

        if (this.notificationDataModel != null) {
            this.notificationDataModel.registerDevice(groupName, registrationID, state, callback);
        }
    }

    /**
     * Register an Android device for notifications with Buddy.
     * 
     * @param registrationID The registration ID for the application currently
     *            running on the device. Note: this is the registration ID
     *            returned after registering with GCM.
     * @param callback The async callback to call on success or error.
     */
    public void registerDevice(String registrationID, final OnCallback<Response<Boolean>> callback) {
        registerDevice(registrationID, "", null, callback);
    }

    /**
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void unregisterDevice(Object state, final OnCallback<Response<Boolean>> callback) {
        if (this.notificationDataModel != null) {
            this.notificationDataModel.unregisterDevice(state, callback);
        }
    }

    /**
     * Send a raw message to a Android device. Note that this call does not
     * directly send the message but rather, adds the raw message to the queue
     * of messages to be sent.
     * 
     * @param rawMessage The message to send.
     * @param senderUserId The ID of the user that sent the notification.
     * @param deliverAfter Schedule the message to be delivered after a certain
     *            date.
     * @param groupName Send messages to an entire group of users, not just a
     *            one.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void sendRawMessage(String rawMessage, int senderUserId, Date deliverAfter, String groupName,
            Object state, final OnCallback<Response<Boolean>> callback) {
        if (rawMessage == null || rawMessage.isEmpty())
            throw new IllegalArgumentException("Raw Message can't be null or empty.");

        if (this.notificationDataModel != null) {
            this.notificationDataModel.sendRawMessage(rawMessage, senderUserId, deliverAfter, groupName, state,
                    callback);
        }
    }

    /**
     * Send a raw message to a Android device. Note that this call does not
     * directly send the message but rather, adds the raw message to the queue
     * of messages to be sent.
     * 
     * @param rawMessage The message to send.
     * @param senderUserId The ID of the user that sent the notification.
     * @param callback The async callback to call on success or error.
     */
    public void sendRawMessage(String rawMessage, int senderUserId, final OnCallback<Response<Boolean>> callback) {
        sendRawMessage(rawMessage, senderUserId, Constants.MinDate, "", null, callback);
    }

    /**
     * Get a paged list of registered Android devices for this Application. This
     * list can then be used to iterate over the devices and send each user a
     * push notification.
     * 
     * @param forGroup Optionally filter only devices in a certain group.
     * @param pageSize Set the number of devices that will be returned for each
     *            call of this method.
     * @param currentPage Set the current page.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getRegisteredDevices(String forGroup, Integer pageSize, Integer currentPage,
            Object state, final OnCallback<ListResponse<RegisteredDeviceAndroid>> callback) {
        if (pageSize <= 0)
            throw new IllegalArgumentException("Pagesize can't be smaller or equal to zero.");
        if (currentPage <= 0)
            throw new IllegalArgumentException(
                    "CurrentPage can't be smaller or equal to zero.");

        if (this.notificationDataModel != null) {
            this.notificationDataModel.getRegisteredDevices(forGroup, pageSize, currentPage, state,
                    callback);
        }
    }

    /**
     * Get a paged list of registered Android devices for this Application. This
     * list can then be used to iterate over the devices and send each user a
     * push notification.
     * 
     * @param callback The async callback to call on success or error.
     */
    public void getRegisteredDevices(final OnCallback<ListResponse<RegisteredDeviceAndroid>> callback) {
        getRegisteredDevices("", 10, 1, null, callback);
    }

    /**
     * Get a list of Android groups that have been registered with Buddy as well
     * as the number of users in each group. Groups can be used to batch-send
     * push notifications to a number of users at the same time.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getGroups(Object state, final OnCallback<Response<Map<String, Integer>>> callback) {
        if (this.notificationDataModel != null) {
            this.notificationDataModel.getGroups(state, callback);
        }
    }
}
