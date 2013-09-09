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

import com.buddy.sdk.json.responses.GroupMessageDataResponse.GroupMessageData;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a message that was sent to a group of users through
 * AuthenticatedUser.getMessages().getGroups().sendMessage.
 */
public class GroupMessage {
    private Date dateSent;
    private int fromUserID;
    private MessageGroup group;
    private String text;
    private double latitude;
    private double longitude;

    GroupMessage(BuddyClient client, GroupMessageData msg, MessageGroup group) {
        this.dateSent = Utils.convertDateString(msg.sentDateTime, "MM/dd/yyyy hh:mm:ss aa");
        this.group = group;

        this.latitude = Utils.parseDouble(msg.latitude);
        this.longitude = Utils.parseDouble(msg.longitude);

        this.fromUserID = Utils.parseInt(msg.fromUserID);
        this.text = msg.messageText;
    }

    /**
     * Gets the Date the message was sent.
     */
    public Date getDateSent() {
        return this.dateSent;
    }

    /**
     * Gets the user ID of the user that sent the message to the group.
     */
    public int getFromUserID() {
        return this.fromUserID;
    }

    /**
     * Gets the Message group that the message was sent to.
     */
    public MessageGroup getGroup() {
        return this.group;
    }

    /**
     * Gets the text value of the message.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Gets the optional latitude from where the message was sent.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Gets the optional longitude from where the message was sent.
     */
    public double getLongitude() {
        return this.longitude;
    }
}
