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

package com.buddy.sdk.unittests;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.buddy.sdk.AuthenticatedUser;
import com.buddy.sdk.BuddyClient;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.User;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Constants.UserGender;
import com.buddy.sdk.utils.Constants.UserStatus;
import com.buddy.sdk.web.BuddyHttpClientFactory;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;
import android.util.Log;

public class BaseUnitTest extends InstrumentationTestCase {
    protected final String TAG = "BuddySDK Unit Tests";
    protected final int SIGNAL_TIMEOUT = 60;

    protected String applicationName = "Buddy Android SDK test app";
    protected String applicationPassword = "FD6DCE88-BA4C-44A9-8CA6-EF0BAECAF283";

    protected static AuthenticatedUser testAuthUser = null;
    protected static BuddyClient testClient = null;
    protected static User testUser = null;

    // Create User
    protected String testCreateUserName = "TestUser1234567890";
    protected String testCreateUserPassword = "TestUser1234567890";
    protected UserGender testCreateUserGender = UserGender.male;
    protected int testCreateUserAge = 40;
    protected String testCreateUserEmail = "TestUser1234567890@gmail.com";
    protected UserStatus testCreateUserStatusId = UserStatus.Married;
    protected Boolean testCreateUserFuzzlocation = false;
    protected Boolean testCreateUserCelebModeEnabled = false;
    protected String testCreateUserApplicationTag = "";

    // Check user
	protected String testCheckUserName = "TestCheckUser1234567890";
    protected String testCheckUserEmail = "TestCheckUser1234567890@gmail.com";
    protected String testCheckUserNameInvalid = "TestCheckUser";
    protected String testCheckUserEmailInvalid = "TestCheckUser@gmail.com";

    // Login user
    protected String testUserName = "Test User 1";
    protected String testUserPassword = "!TestUser1";
    protected String testToken = "UT-53d622ec-747e-4791-9ab9-4b73a9c7fdae";
    protected Integer testUserIdInteger = 2731819;
    protected String testUserId = testUserIdInteger.toString();

    protected Boolean testPass = true;
    protected String testFailMessage = "";

    // Search Users
    protected Integer testSearchDistance = 40075000; // Ignore search distance
                                                     // if this value is set
    protected float testSearchLongitude = 0;
    protected float testSearchLatitude = 0;
    protected int testMaxSearchRecords = 10;
    protected UserGender testSearchUserGender = UserGender.male;
    protected int testMinAge = 20;
    protected int testMaxAge = 50;
    protected UserStatus testSearchUserStatusId = UserStatus.Married;
    protected String testSearchTimeFilter = "240"; // Excludes users who logged
                                                   // in longer then 4 hours ago

    // Push Notifications
    protected String testRegistrationId = "1234567890";
    protected String testGroupName = "";

    // Picture Albums
    protected String testAlbumName = "Test Album";
    protected Boolean testPublicAlbumBit = false;
    protected String testAlbumApplicationTag = "";
    protected String testEncodedImage = "";
    protected byte[] testImageBytes = null;
    protected String testPhotoComment = "Test Photo";
    protected float testLatitude = 0;
    protected float testLongitude = 0;
    protected String testWatermarkMessage = "Test Watermark";

    // Virtual albums
    protected String testVirtualAlbumName = "Test Virtual Album";
    protected int testVirtualAlbumId = 0;

    protected String testPhotoIdTag = "http://testtag.com";

    // Service API
    protected int testNumberRecords = 10;

    // Application API
    protected int testFirstRow = 1;
    protected int testLastRow = 10;

    // Game Score API
    protected float testGameLatitude = 0;
    protected float testGameLongitude = 0;
    protected String testScoreRank = "Great";
    protected float testScoreValue = 10;
    protected String testScoreBoardName = "Test Board";
    protected int testRecordLimit = 10;
    protected int testGameSearchDistance = 100;

    protected String testPlayerName = "Test Player";
    protected String testPlayerBoard = "Test Player Board";
    protected String testPlayerRank = "Excellent";

    // Game state
    protected String testStateKey = "Test Key";
    protected String testStateValue = "Test State Value";

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected void checkResults() {
        // Check results
        if (!testPass) {
            fail(testFailMessage);
        }
    }

    protected static String readInputStreamAsString(InputStream in) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while (result != -1) {
            byte b = (byte) result;
            buf.write(b);
            result = bis.read();
        }
        return buf.toString();
    }

    protected String readDataFromFile(String fileName) {
        try {
            AssetManager m = getInstrumentation().getContext().getAssets();
            InputStream is = m.open(fileName);
            String data = readInputStreamAsString(is);
            return data;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    protected InputStream getStreamFromFile(String fileName){
    	try{AssetManager m = getInstrumentation().getContext().getAssets();
    		return m.open(fileName);
    	}catch(Exception e){
    		// TODO Auto-generated catch block
            e.printStackTrace();
            return null;
    	}
    }
    
    protected void createAuthenticatedUser() {
        String jsonValue = testToken;
        String jsonValueUser = readDataFromFile("DataResponses/validUserResponse.json");
        String jsonDeviceReportingResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValue);
        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);
        BuddyHttpClientFactory.addDummyResponse(jsonDeviceReportingResponse);

        BuddyHttpClientFactory.setUnitTestMode(true);

        testClient = new BuddyClient(applicationName, applicationPassword, this.getInstrumentation().getContext());

        testClient.login(testUserName, testUserPassword, null,
                new OnCallback<Response<AuthenticatedUser>>() {
                    public void OnResponse(Response<AuthenticatedUser> response, Object state) {
                        testAuthUser = response.getResult();
                        assertNotNull(testAuthUser);
                    }
                });

        Log.d(TAG, "Auth User Created");
    }

    protected void createAuthenticatedUserAnd2ndUser() {
        String jsonValue = testToken;
        String jsonValueUser = readDataFromFile("DataResponses/validUserResponse.json");
        String jsonDeviceReportingResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        String json2ndUser = readDataFromFile("DataResponses/valid2ndUserResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValue);
        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);
        BuddyHttpClientFactory.addDummyResponse(jsonDeviceReportingResponse);
        BuddyHttpClientFactory.addDummyResponse(json2ndUser);

        BuddyHttpClientFactory.setUnitTestMode(true);

        testClient = new BuddyClient(applicationName, applicationPassword, this.getInstrumentation().getContext(), "0.1", true);

        testClient.login(testUserName, testUserPassword, null,
                new OnCallback<Response<AuthenticatedUser>>() {
                    public void OnResponse(Response<AuthenticatedUser> response, Object state) {
                        testAuthUser = response.getResult();
                        assertNotNull(testAuthUser);
                        testAuthUser.findUser(1736820, null, new OnCallback<Response<User>>() {
                            public void OnResponse(Response<User> response, Object state) {
                                testUser = response.getResult();
                                assertNotNull(testAuthUser);
                            }
                        });
                    }
                });

        Log.d(TAG, "Auth User Created");
    }
 }
