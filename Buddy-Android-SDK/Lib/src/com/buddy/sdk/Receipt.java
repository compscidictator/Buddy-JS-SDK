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

import java.util.Date;

import com.buddy.sdk.json.responses.ReceiptDataResponse.ReceiptData;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a receipt in the Buddy system.
 *
 */
public class Receipt
{
    protected BuddyClient client;
    protected AuthenticatedUser user;

    private long receiptHistoryID;
    private String storeName;
    private long userID;
    private Date historyDateTime;
    private String receiptData;
    private String totalCost;
    private int itemQuantity;
    private String appData;
    private String historyCustomTransactionID;
    private String verificationResultData;
    private long storeItemID;

    Receipt(BuddyClient client, AuthenticatedUser user, ReceiptData receipt)
    {
        if (client == null) throw new IllegalArgumentException("client can't be null.");
        if (user == null) throw new IllegalArgumentException("user can't be null.");
        this.client = client;
        this.user = user;

        this.appData = receipt.appData;
        this.historyCustomTransactionID = receipt.historyCustomTransactionID;
        this.historyDateTime = Utils.convertDateString(receipt.historyDateTime, "MM/dd/yyyy hh:mm:ss aa");
        this.itemQuantity = Utils.parseInt(receipt.itemQuantity);
        this.receiptData = receipt.receiptData;
        this.receiptHistoryID = Utils.parseInt(receipt.receiptHistoryID);
        this.storeItemID = Utils.parseInt(receipt.storeItemID);
        this.storeName = receipt.storeName;
        this.totalCost = receipt.totalCost;
        this.userID = Utils.parseInt(receipt.userID);
        this.verificationResultData = !Utils.isNullOrEmpty(receipt.verificationResult) && Boolean.parseBoolean(receipt.verificationResult) ? this.verificationResultData : "";
    }
    
    /**
     * Gets the ID of the retrieved receipt history item.
     */
    public long getReceiptHistoryID() { 
        return this.receiptHistoryID;
    }

    /**
     * Gets the name of the store in which this receipt was saved.
     */
    public String getStoreName() { 
        return this.storeName;
    }

    /**
     * Gets the ID of the user this receipt was saved for.
     */
    public long getUserID() {
        return this.userID;
    }

    /**
     * Gets the DateTime this receipt was saved or modified.
     */
    public Date getHistoryDateTime() { 
        return this.historyDateTime;
    }

    /**
     * Gets the receipt data that was stored with this receipt.
     */
    public String getReceiptData() { 
        return this.receiptData;
    }

    /**
     * Gets the total cost of the transaction associated with this receipt.
     */
    public String getTotalCost() { 
        return this.totalCost;
    }

    /**
     * Gets the number of items which were purchased during the transaction associated with this receipt.
     */
    public int getItemQuantity() { 
        return this.itemQuantity;
    }

    /**
     * Gets the (optional) metadata that was stored with this receipt.
     */
    public String getAppData() { 
        return this.appData;
    }
    
    /**
     * Gets the customTransactionID that was saved for this receipt.
     */
    public String getHistoryCustomTransactionID() { 
        return this.historyCustomTransactionID;
    }

    /**
     * Gets the raw verification data associated with the receipt as returned from the underlying Facebook or Apple servers.
     */
    public String getVerificationResultData() { 
        return this.verificationResultData;
    }

    /**
     * Gets the Buddy StoreItemID of the item purchased in this transaction.
     */
    public long getStoreItemID() {
        return this.storeItemID;
    }

}