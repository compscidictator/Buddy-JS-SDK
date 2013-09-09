package com.buddy.sdk.unittests;

import java.util.List;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.MetroArea;
import com.buddy.sdk.Startup;
import com.buddy.sdk.Startups;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.web.BuddyHttpClientFactory;

public class StartupUnitTests extends BaseUnitTest {
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createAuthenticatedUser();
    }

    public void testStartupGetMetroAreaList() {
        String jsonResponse = readDataFromFile("DataResponses/StartupUnitTests-GetMetroAreaList.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        Startups startup = testAuthUser.getStartups();

        startup.getMetroAreaList(null, new OnCallback<ListResponse<MetroArea>>() {

            @Override
            public void OnResponse(ListResponse<MetroArea> response, Object state) {
                assertNotNull(response);

                List<MetroArea> list = response.getList();
                assertNotNull(list);

                MetroArea item = list.get(0);
                assertEquals("Seattle Area", item.getMetroName());
                assert(item.getStartupCount() > 0);
                assert(item.getIconURL().length() > 0);
                        
            }

        });
    	
    }
    
    public void testStartupGetFromMetroArea() {
        String jsonResponse = readDataFromFile("DataResponses/StartupUnitTests-GetFromMetroList.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        Startups startup = testAuthUser.getStartups();

        startup.getFromMetroArea("Seattle Area", 10, null, new OnCallback<ListResponse<Startup>>() {

            @Override
            public void OnResponse(ListResponse<Startup> response, Object state) {
                assertNotNull(response);

                List<Startup> list = response.getList();
                assertNotNull(list);

                Startup item = list.get(0);
                assertEquals("Seattle", item.getCity());
            }

        });
    	
    }
    
    public void testStartupFind() {
        String jsonResponse = readDataFromFile("DataResponses/StartupUnitTests-FindList.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        Startups startup = testAuthUser.getStartups();

        startup.find(1000000000, 46.3, -122.3, 10, "", null, new OnCallback<ListResponse<Startup>>() {

            @Override
            public void OnResponse(ListResponse<Startup> response, Object state) {
                assertNotNull(response);

                List<Startup> list = response.getList();
                assertNotNull(list);

                Startup item = list.get(0);
                assertEquals("Vancouver", item.getCity());
            }

        });
    	
    }
}
