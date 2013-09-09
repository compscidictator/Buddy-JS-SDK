
package com.buddy.sdk.unittests;

import java.util.List;
import android.util.SparseArray;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Place;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.web.BuddyHttpClientFactory;

public class GeoLocationUnitTests extends BaseUnitTest {
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createAuthenticatedUser();
    }

    public void testLocationCategoryGetList() {
        String jsonResponse = readDataFromFile("DataResponses/GeoLocationUnitTests-GetCategories.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getPlaces().getCategories(null,
                new OnCallback<Response<SparseArray<String>>>() {
                    public void OnResponse(Response<SparseArray<String>> response, Object state) {
                        assertNotNull(response);
                        SparseArray<String> array = response.getResult();
                        assertNotNull(array);
                        assertTrue(array.size() > 1);

                        String category = array.get(659);
                        assertEquals("Travel & Tourism > Wineries & Vineyards", category);
                    }

                });
    }

    public void testLocationGetFromId() {
        String jsonResponse = readDataFromFile("DataResponses/GeoLocationUnitTests-LocationGet.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getPlaces().get(46254905, 0, 0, null, new OnCallback<Response<Place>>() {
            public void OnResponse(Response<Place> response, Object state) {
                assertNotNull(response);
                Place place = response.getResult();
                assertEquals(46254905, place.getId());
            }

        });
    }

    public void testLocationSearch() {
        String jsonResponse = readDataFromFile("DataResponses/GeoLocationUnitTests-LocationGet.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getPlaces().find(100, 0, 0, 10, "", 870, null,
                new OnCallback<ListResponse<Place>>() {
                    public void OnResponse(ListResponse<Place> response, Object state) {
                        assertNotNull(response);
                        List<Place> list = response.getList();
                        assertNotNull(list);

                        Place place = list.get(0);
                        assertEquals(46254905, place.getId());
                    }

                });
    }
}
