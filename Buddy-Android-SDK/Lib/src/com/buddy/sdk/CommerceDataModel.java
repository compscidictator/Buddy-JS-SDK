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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.ReceiptDataResponse;
import com.buddy.sdk.json.responses.ReceiptDataResponse.ReceiptData;
import com.buddy.sdk.json.responses.StoreItemDataResponse;
import com.buddy.sdk.json.responses.StoreItemDataResponse.StoreItemData;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.web.BuddyWebWrapper;

public class CommerceDataModel extends BaseDataModel {
    private AuthenticatedUser user = null;

    CommerceDataModel(BuddyClient client, AuthenticatedUser user) {
        this.client = client;
        this.user = user;
    }

    public void getReceiptsForUser(Date fromDateTime, Object state,
            final OnCallback<ListResponse<Receipt>> callback) {
        
        String fromDate = Utils.convertStringDate(fromDateTime, "MM/dd/yyyy hh:mm:ss aa");
        BuddyWebWrapper.Commerce_Receipt_GetForUser(client.getAppName(), client.getAppPassword(), 
                user.getToken(), fromDate, RESERVED, state, new OnResponseCallback() {
            @Override
            public void OnResponse(BuddyCallbackParams response, Object state) {
                ListResponse<Receipt> receiptResponse = createReceiptResponse(response);
                callback.OnResponse(receiptResponse, state);
            }
        });
    }

    public void getReceiptForUserAndTransactionID(String customTransactionID, Object state,
            final OnCallback<ListResponse<Receipt>> callback) {
        BuddyWebWrapper.Commerce_Receipt_GetForUserAndTransactionID(client.getAppName(), client.getAppPassword(), 
                user.getToken(), customTransactionID, RESERVED, state, new OnResponseCallback() {
            @Override
            public void OnResponse(BuddyCallbackParams response, Object state) {
                ListResponse<Receipt> receiptResponse = createReceiptResponse(response);
                callback.OnResponse(receiptResponse, state);
            }
        });
    }

    public void saveReceipt(String totalCost, int totalQuantity, int storeItemID, String storeName,
            String receiptData, String customTransactionID, String appData, Object state,
            final OnCallback<Response<Boolean>> callback) {
        BuddyWebWrapper.Commerce_Receipt_Save(client.getAppName(), client.getAppPassword(), user.getToken(), 
                totalCost, totalQuantity, storeItemID, storeName, receiptData, customTransactionID, appData,
                RESERVED, state, new OnResponseCallback() {
            @Override
            public void OnResponse(BuddyCallbackParams response, Object state) {
                Response<Boolean> booleanResponse = getBooleanResponse(response);
                callback.OnResponse(booleanResponse, state);
            }
        });
    }

    public void getAllStoreItems(Object state, final OnCallback<ListResponse<StoreItem>> callback) {

        BuddyWebWrapper.Commerce_Store_GetAllItems(client.getAppName(), client.getAppPassword(),
                user.getToken(), this.RESERVED, state, new OnResponseCallback() {
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<StoreItem> storeItemResponse = createStoreItemResponse(response);
                        callback.OnResponse(storeItemResponse, state);
                    }
                });
    }

    public void getFreeStoreItems(Object state, final OnCallback<ListResponse<StoreItem>> callback) {

        BuddyWebWrapper.Commerce_Store_GetFreeItems(client.getAppName(), client.getAppPassword(),
                user.getToken(), this.RESERVED, state, new OnResponseCallback() {
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<StoreItem> storeItemResponse = createStoreItemResponse(response);
                        callback.OnResponse(storeItemResponse, state);
                    }
                });
    }

    public void getActiveStoreItems(Object state, final OnCallback<ListResponse<StoreItem>> callback) {

        BuddyWebWrapper.Commerce_Store_GetActiveItems(client.getAppName(), client.getAppPassword(),
                user.getToken(), this.RESERVED, state, new OnResponseCallback() {
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<StoreItem> storeItemResponse = createStoreItemResponse(response);
                        callback.OnResponse(storeItemResponse, state);
                    }
                });
    }

    private ListResponse<StoreItem> createStoreItemResponse(BuddyCallbackParams response) {
        ListResponse<StoreItem> storeItemResponse = new ListResponse<StoreItem>();
        if (response != null) {
            if (response.completed) {
                StoreItemDataResponse data = getJson(response.response, StoreItemDataResponse.class);
                if (data != null) {
                    List<StoreItem> storeItemList = new ArrayList<StoreItem>();
                    for (StoreItemData itemData : data.data) {
                        StoreItem storeItem = new StoreItem(client, user, itemData);
                        storeItemList.add(storeItem);
                    }
                    storeItemResponse.setList(storeItemList);
                } else {
                    storeItemResponse.setThrowable(new BuddyServiceException(response.response));
                }
            } else {
                storeItemResponse.setThrowable(response.exception);
            }
        } else {
            storeItemResponse.setThrowable(new ServiceUnknownErrorException());

        }

        return storeItemResponse;
    }
    
    private ListResponse<Receipt> createReceiptResponse(BuddyCallbackParams response) {
        ListResponse<Receipt> receiptResponse = new ListResponse<Receipt>();
        if (response != null) {
            if (response.completed) {
                ReceiptDataResponse data = getJson(response.response, ReceiptDataResponse.class);
                if (data != null) {
                    List<Receipt> receiptList = new ArrayList<Receipt>();
                    for (ReceiptData itemData : data.data) {
                        Receipt receipt = new Receipt(client, user, itemData);
                        receiptList.add(receipt);
                    }
                    receiptResponse.setList(receiptList);
                } else {
                    receiptResponse.setThrowable(new BuddyServiceException(response.response));
                }
            } else {
                receiptResponse.setThrowable(response.exception);
            }
        } else {
            receiptResponse.setThrowable(new ServiceUnknownErrorException());

        }

        return receiptResponse;
    }
}
