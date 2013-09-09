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

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Utils;

/**
 * Represents an object that can be used to handle commerce for the user.
 * 
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
 *            {@code user.getCommerce().getAllStoreItems(null, new OnCallback<ListResponse<StoreItem>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(ListResponse<StoreItem> response, Object state)}
 *                <code>{</code>
 *                    {@code List<StoreItem> itemList = response.getList();}
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 * 
 */
public class Commerce {
    private CommerceDataModel commerceDataModel = null;
    protected BuddyClient client;
    protected AuthenticatedUser user;

    Commerce(BuddyClient client, AuthenticatedUser user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null or empty.");
        if (user == null)
            throw new IllegalArgumentException("user can't be null or empty.");

        this.client = client;
        this.user = user;

        this.commerceDataModel = new CommerceDataModel(client, user);
    }

    /**
     * Finds the receipt list based on the fromDateTime parameter for the currently logged in user.
     * 
     * @param fromDateTime The starting date and time to get receipts from, leave this blank to get
     *            all the receipts.
     * @param state An optional user defined object that will be passed to the callback.
     * @param callback The async callback to call on success or error.
     */
    public void getReceiptsForUser(Date fromDateTime, Object state,
            final OnCallback<ListResponse<Receipt>> callback) {
        if (this.commerceDataModel != null) {
            this.commerceDataModel.getReceiptsForUser(fromDateTime, state, callback);
        }
    }

    /**
     * Finds the receipt list based on the FromDateTime parameter for the currently logged in user.
     * 
     * @param callback The async callback to call on success or error.
     */
    public void getReceiptsForUser(final OnCallback<ListResponse<Receipt>> callback) {
        getReceiptsForUser(null, null, callback);
    }

    /**
     * Finds the receipt associated with the specified CustomTransactionID for the currently logged
     * in user.
     * 
     * @param customTransactionID The CustomTransactionID of the transaction. For Facebook payments
     *            this is the OrderID of the transaction.
     * @param state An optional user defined object that will be passed to the callback.
     * @param callback The async callback to call on success or error.
     */
    public void getReceiptForUserAndTransactionID(String customTransactionID, Object state,
            final OnCallback<ListResponse<Receipt>> callback) {
        if (this.commerceDataModel != null) {
            this.commerceDataModel.getReceiptForUserAndTransactionID(customTransactionID, state,
                    callback);
        }
    }

    /**
     * Saves a receipt for the purchase of an item made to the application's store.
     * 
     * @param totalCost The total cost for the items purchased in the transaction.
     * @param totalQuantity The total number of items purchased.
     * @param storeItemID The store ID of the item of the item being purchased.
     * @param storeName The name of the application's store to be saved with the transaction. This
     *            field is used by the commerce analytics to track purchases.
     * @param receiptData Optional information to store with the receipt such as notes about the
     *            transaction.
     * @param customTransactionID An optional app-specific ID to associate with the purchase.
     * @param appData Optional metadata to associate with the transaction.
     * @param state An optional user defined object that will be passed to the callback.
     * @param callback The async callback to call on success or error.
     */
    public void saveReceipt(String totalCost, int totalQuantity, int storeItemID, String storeName,
            String receiptData, String customTransactionID, String appData, Object state,
            OnCallback<Response<Boolean>> callback) {
        if (Utils.isNullOrEmpty(totalCost))
            throw new IllegalArgumentException("totalCost can't be null or empty.");
        if (Utils.isNullOrEmpty(storeName))
            throw new IllegalArgumentException("storeName can't be null or empty.");

        if (this.commerceDataModel != null) {
            this.commerceDataModel.saveReceipt(totalCost, totalQuantity, storeItemID, storeName,
                    receiptData, customTransactionID, appData, state, callback);
        }
    }

    /**
     * Saves a receipt for the purchase of an item made to the application's store.
     * 
     * @param totalCost The total cost for the items purchased in the transaction.
     * @param totalQuantity The total number of items purchased.
     * @param storeItemID The store ID of the item of the item being purchased.
     * @param storeName The name of the application's store to be saved with the transaction. This
     *            field is used by the commerce analytics to track purchases.
     * @param callback The async callback to call on success or error.
     */
    public void saveReceipt(String totalCost, int totalQuantity, int storeItemID, String storeName,
            OnCallback<Response<Boolean>> callback) {
        saveReceipt(totalCost, totalQuantity, storeItemID, storeName, "", "", "", null, callback);
    }

    /**
     * Returns information about all items in the store for the current application.
     * 
     * @param state An optional user defined object that will be passed to the callback.
     * @param callback The async callback to call on success or error.
     */
    public void getAllStoreItems(Object state, final OnCallback<ListResponse<StoreItem>> callback) {
        if (this.commerceDataModel != null) {
            this.commerceDataModel.getAllStoreItems(state, callback);
        }
    }

    /**
     * Returns information about all items in the store for the current application which are marked
     * as free.
     * 
     * @param state An optional user defined object that will be passed to the callback.
     * @param callback The async callback to call on success or error.
     */
    public void getFreeStoreItems(Object state, final OnCallback<ListResponse<StoreItem>> callback) {
        if (this.commerceDataModel != null) {
            this.commerceDataModel.getFreeStoreItems(state, callback);
        }
    }

    /**
     * Returns information about all store items for an application which are currently active
     * (available for sale).
     * 
     * @param state An optional user defined object that will be passed to the callback.
     * @param callback The async callback to call on success or error.
     */
    public void getActiveStoreItems(Object state, final OnCallback<ListResponse<StoreItem>> callback) {
        if (this.commerceDataModel != null) {
            this.commerceDataModel.getActiveStoreItems(state, callback);
        }
    }
}
