
package com.buddy.sdk.unittests;

import com.buddy.sdk.BuddyCallbackParams;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.web.BuddyHttpClientFactory;
import com.buddy.sdk.web.BuddyWebWrapper;

public class WebWrapperTests extends BaseUnitTest {
    private String responseValue = "";

    public void testUserAccount_Defines_GetStatusValues() {
        String jsonValue = "{\"data\":[{\"statusID\":\"1\",\"statusTag\":\"Single\"},{\"statusID\":\"2\",\"statusTag\":\"Dating\"}]}";
        BuddyHttpClientFactory.setUnitTestMode(true);
        BuddyHttpClientFactory.addDummyResponse(jsonValue);

        BuddyWebWrapper.UserAccount_Defines_GetStatusValues(this.applicationName,
                this.applicationPassword, null, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        responseValue = response.response;
                    }
                });

        assertEquals("The Web Wrapper should return the injected json data", jsonValue,
                responseValue);
    }
}
