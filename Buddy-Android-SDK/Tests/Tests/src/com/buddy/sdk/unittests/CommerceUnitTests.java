package com.buddy.sdk.unittests;

import java.util.List;

import com.buddy.sdk.Receipt;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.StoreItem;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.web.BuddyHttpClientFactory;

public class CommerceUnitTests extends BaseUnitTest {
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createAuthenticatedUser();
    }

    public void testReceiptSave() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getCommerce().saveReceipt("1.23", 4, 5, "TestStoreName",
                new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }
                });
    }
    
    public void testReceiptGetReceiptsForUser() {
        String jsonResponse = readDataFromFile("DataResponses/CommerceUnitTests-GetReceipt.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getCommerce().getReceiptsForUser(null, null, new OnCallback<ListResponse<Receipt>>() {
                    public void OnResponse(ListResponse<Receipt> response, Object state) {
                        assertNotNull(response);
                        List<Receipt> receipts = response.getList();
                        assertNotNull(receipts);
                        
                        Receipt receipt = receipts.get(0);
                        assertEquals("TestStoreName", receipt.getStoreName());
                    }
                });
    }

    public void testReceiptGetReceiptsForUserAndTransactionId() {
        String jsonResponse = readDataFromFile("DataResponses/CommerceUnitTests-GetReceiptWithTransactionID.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        String customTransactionID = "42";
        testAuthUser.getCommerce().getReceiptForUserAndTransactionID(customTransactionID, null, 
                new OnCallback<ListResponse<Receipt>>() {
                    public void OnResponse(ListResponse<Receipt> response, Object state) {
                        assertNotNull(response);
                        List<Receipt> receipts = response.getList();
                        assertNotNull(receipts);
                        
                        Receipt receipt = receipts.get(0);
                        assertEquals("TestStoreName", receipt.getStoreName());
                    }
                });
    }

    public void testStoreGetAllItems() {
        String jsonResponse = readDataFromFile("DataResponses/CommerceUnitTests-GetAllItems.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getCommerce().getAllStoreItems(null, new OnCallback<ListResponse<StoreItem>>() {
                    public void OnResponse(ListResponse<StoreItem> response, Object state) {
                        assertNotNull(response);
                        List<StoreItem> storeItems = response.getList();
                        assertNotNull(storeItems);
                        assertTrue(storeItems.size() == 3);
                        
                        StoreItem storeItem = storeItems.get(2);
                        assertEquals(98, storeItem.getStoreItemID());
                    }
                });
    }

    public void testStoreGetActiveItems() {
        String jsonResponse = readDataFromFile("DataResponses/CommerceUnitTests-GetActiveItems.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getCommerce().getActiveStoreItems(null, new OnCallback<ListResponse<StoreItem>>() {
                    public void OnResponse(ListResponse<StoreItem> response, Object state) {
                        assertNotNull(response);
                        List<StoreItem> storeItems = response.getList();
                        assertNotNull(storeItems);
                        assertTrue(storeItems.size() == 2);
                        
                        StoreItem storeItem = storeItems.get(1);
                        assertEquals(97, storeItem.getStoreItemID());
                    }
                });
    }

    public void testStoreGetFreeItems() {
        String jsonResponse = readDataFromFile("DataResponses/CommerceUnitTests-GetFreeItems.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getCommerce().getFreeStoreItems(null, new OnCallback<ListResponse<StoreItem>>() {
            public void OnResponse(ListResponse<StoreItem> response, Object state) {
                assertNotNull(response);
                List<StoreItem> storeItems = response.getList();
                assertNotNull(storeItems);
                assertTrue(storeItems.size() == 1);
                
                StoreItem storeItem = storeItems.get(0);
                assertEquals(96, storeItem.getStoreItemID());
            }
        });
    }
}
