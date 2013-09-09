/*
 * Copyright (C) 2012 Buddy Platform, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.buddy.com/terms-of-service/ 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.buddy.sdk;

import java.util.Date;

import com.buddy.sdk.json.responses.StoreItemDataResponse.StoreItemData;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a single, named store item in the Buddy system. 
 *
 */
public class StoreItem {
    protected BuddyClient client;
    protected AuthenticatedUser user;

    private long storeItemID;
	private String itemName;
	private String itemIconUri;
	private String itemDownloadUri;
	private String itemPreviewUri;
	private Boolean itemFreeFlag;
	private String itemDescription;
	private Date itemDateTime;
	private Boolean itemAvailableFlag;
	private String itemCost;
	private String appData;
	private String customItemID;
	
	StoreItem(BuddyClient client, AuthenticatedUser user, StoreItemData storeItem) {
        if (client == null) throw new IllegalArgumentException("client can't be null.");
        if (user == null) throw new IllegalArgumentException("user can't be null.");
        
        this.storeItemID = Utils.parseInt(storeItem.storeItemID);
        this.itemName = storeItem.itemName;
        this.itemIconUri = storeItem.itemIconUri;
        this.itemDownloadUri = storeItem.itemDownloadUri;
        this.itemPreviewUri = storeItem.itemPreviewUri;
        this.itemFreeFlag = storeItem.itemFreeFlag.toLowerCase().equals("true");
        this.itemDescription = storeItem.itemDescription;
        this.itemDateTime = Utils.convertDateString(storeItem.itemDateTime, "MM/dd/yyyy hh:mm:ss aa");
        this.itemAvailableFlag = storeItem.itemAvailableFlag.toLowerCase().equals("true");
        this.itemCost = storeItem.itemCost;
        this.appData = storeItem.appData;
        this.customItemID = storeItem.customItemID;
	}
	
	/**
	 * Gets the ID of the store item. 
	 */
	public long getStoreItemID() {
		return storeItemID;
	}

	/**
	 * Gets the name of the item. 
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * Gets the URI of the icon to display for this item. 
	 */
	public String getItemIconUri() {
		return itemIconUri;
	}

	/**
	 * Gets the URI where the item can be downloaded from. 
	 */
	public String getItemDownloadUri() {
		return itemDownloadUri;
	}

	/**
	 * Gets the URI where the item can be previewed. 
	 */
	public String getItemPreviewUri() {
		return itemPreviewUri;
	}

	/**
	 * Gets the flag indicating if the item is free. 
	 */
	public Boolean getItemFreeFlag() {
		return itemFreeFlag;
	}

	/**
	 * Gets the brief description of the item. 
	 */
	public String getItemDescription() {
		return itemDescription;
	}

	/**
	 * Gets the date and time when the item was created or last updated. 
	 */
	public Date getItemDateTime() {
		return itemDateTime;
	}

	/**
	 * Gets the flag indicating if the item is currently available for sale. 
	 */
	public Boolean getItemAvailableFlag() {
		return itemAvailableFlag;
	}

	/**
	 * Gets the cost of the item. 
	 */
	public String getItemCost() {
		return itemCost;
	}

	/**
	 * Gets the optional metadata associated with the item. 
	 */
	public String getAppData() {
		return appData;
	}

	/**
	 * Gets the ID by which external sources identify the item by. 
	 */
	public String getCustomItemID() {
		return customItemID;
	}

}
