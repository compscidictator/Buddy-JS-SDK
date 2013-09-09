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

import com.buddy.sdk.json.responses.MessageDataResponse.MessageData;
import com.buddy.sdk.json.responses.MessageSentDataResponse.MessageSentData;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a single message that one user sent to another.
 */
public class Message {
    private Date dateSent;
    private int fromUserID;
    private int toUserID;
    private String text;

    Message(MessageData msg, int toId) {
        this.dateSent = Utils.convertDateString(msg.dateSent);
        this.fromUserID = Utils.parseInt(msg.fromUserID);
        this.toUserID = toId;
        this.text = msg.messageString;
    }

    Message(MessageSentData msg, int fromId) {
        this.dateSent = Utils.convertDateString(msg.dateSent);
        this.fromUserID = fromId;
        this.toUserID = Utils.parseInt(msg.toUserID);
        this.text = msg.messageString;
    }

    /**
     * Gets the Date the message was sent.
     */
    public Date getDateSent() {
        return this.dateSent;
    }

    /**
     * Gets the ID of the user who sent the message.
     */
    public int getFromUserID() {
        return this.fromUserID;
    }

    /**
     * Gets the ID of the user who received the message.
     */
    public int getToUserID() {
        return this.toUserID;
    }

    /**
     * Gets the text value of the message.
     */
    public String getText() {
        return this.text;
    }
}
