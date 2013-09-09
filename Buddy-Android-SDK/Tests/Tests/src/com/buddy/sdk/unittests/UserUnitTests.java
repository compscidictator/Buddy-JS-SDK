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

import java.util.List;

import com.buddy.sdk.AuthenticatedUser;
import com.buddy.sdk.BuddyClient;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.CheckInLocation;
import com.buddy.sdk.User;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.utils.Constants;
import com.buddy.sdk.web.BuddyHttpClientFactory;

public class UserUnitTests extends BaseUnitTest {
    private static String testToken = "UT-0d499bf5-a408-4021-a477-8e2080373729";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        testClient = new BuddyClient(applicationName, applicationPassword, this.getInstrumentation().getContext(), "0.1", true);
    }

    public void testCreateUser() {
        String jsonValueUser = readDataFromFile("DataResponses/validUserResponse.json");
        String jsonDeviceReportingResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(testToken);
        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);
        BuddyHttpClientFactory.addDummyResponse(jsonDeviceReportingResponse);

        testClient.createUser(testCreateUserName, testCreateUserPassword, testCreateUserGender,
                testCreateUserAge, testCreateUserEmail, testCreateUserStatusId,
                testCreateUserFuzzlocation, testCreateUserCelebModeEnabled,
                testCreateUserApplicationTag, null, new OnCallback<Response<AuthenticatedUser>>() {
                    public void OnResponse(Response<AuthenticatedUser> response, Object state) {
                        assertNotNull(response);
                        AuthenticatedUser user = response.getResult();

                        assertEquals("User token received should be " + testToken, testToken,
                                user.getToken());
                    }
                });
    }

    public void testCreateUserOverload() {
        String jsonValueUser = readDataFromFile("DataResponses/validUserResponse.json");
        String jsonDeviceReportingResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(testToken);
        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);
        BuddyHttpClientFactory.addDummyResponse(jsonDeviceReportingResponse);

        testClient.createUser(testCreateUserName, testCreateUserPassword,
                new OnCallback<Response<AuthenticatedUser>>() {
                    public void OnResponse(Response<AuthenticatedUser> response, Object state) {
                        assertNotNull(response);
                        AuthenticatedUser user = response.getResult();

                        assertEquals("User token received should be " + testToken, testToken,
                                user.getToken());
                    }
                });
    }

    public void testCreateUserAlreadyExists() {
        String jsonValueUser = readDataFromFile("DataResponses/UserUnitTests-UserExists.json");
        String jsonDeviceReportingResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(testToken);
        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);
        BuddyHttpClientFactory.addDummyResponse(jsonDeviceReportingResponse);

        testClient.createUser(testCreateUserName, testCreateUserPassword, testCreateUserGender,
                testCreateUserAge, testCreateUserEmail, testCreateUserStatusId,
                testCreateUserFuzzlocation, testCreateUserCelebModeEnabled,
                testCreateUserApplicationTag, null, new OnCallback<Response<AuthenticatedUser>>() {
                    public void OnResponse(Response<AuthenticatedUser> response, Object state) {
                        assertNotNull(response);
                        assertFalse(response.isCompleted());

                        String errorMessage = response.getThrowable().getMessage();
                        assertEquals("UserNameAlreadyInUse", errorMessage);
                    }
                });
    }

    public void testCreateUserIncorrectAppName() {
        String jsonValueUser = readDataFromFile("DataResponses/GenericWrongSocketErrorResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);

        testClient.createUser(testCreateUserName, testCreateUserPassword, testCreateUserGender,
                testCreateUserAge, testCreateUserEmail, testCreateUserStatusId,
                testCreateUserFuzzlocation, testCreateUserCelebModeEnabled,
                testCreateUserApplicationTag, null, new OnCallback<Response<AuthenticatedUser>>() {
                    public void OnResponse(Response<AuthenticatedUser> response, Object state) {
                        assertNotNull(response);
                        assertFalse(response.isCompleted());

                        String errorMessage = response.getThrowable().getMessage();
                        assertEquals("WrongSocketLoginOrPass", errorMessage);
                    }
                });
    }

    public void testCreateUserIncorrectAppPassword() {
        String jsonValueUser = readDataFromFile("DataResponses/GenericWrongSocketErrorResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);

        testClient.createUser(testCreateUserName, testCreateUserPassword, testCreateUserGender,
                testCreateUserAge, testCreateUserEmail, testCreateUserStatusId,
                testCreateUserFuzzlocation, testCreateUserCelebModeEnabled,
                testCreateUserApplicationTag, null, new OnCallback<Response<AuthenticatedUser>>() {
                    public void OnResponse(Response<AuthenticatedUser> response, Object state) {
                        assertNotNull(response);
                        assertFalse(response.isCompleted());

                        String errorMessage = response.getThrowable().getMessage();
                        assertEquals("WrongSocketLoginOrPass", errorMessage);
                    }
                });
    }

    public void testUserLogin() {
        String jsonValueUser = readDataFromFile("DataResponses/validUserResponse.json");
        String jsonDeviceReportingResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(testToken);
        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);
        BuddyHttpClientFactory.addDummyResponse(jsonDeviceReportingResponse);

        testClient.login(testUserName, testUserPassword, null,
                new OnCallback<Response<AuthenticatedUser>>() {
                    public void OnResponse(Response<AuthenticatedUser> response, Object state) {
                        assertNotNull(response);

                        AuthenticatedUser user = response.getResult();

                        assertEquals("User token received should be " + testToken, testToken,
                                user.getToken());
                    }
                });

    }

    public void testSocialLogin() {
    	String jsonValueSocial = readDataFromFile("DataResponses/SocialLogin.json");
        String jsonValueUser = readDataFromFile("DataResponses/validUserResponse.json");
        String jsonDeviceReportingResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
    	
    	BuddyHttpClientFactory.addDummyResponse(jsonValueSocial);
    	BuddyHttpClientFactory.addDummyResponse(jsonValueUser);
    	BuddyHttpClientFactory.addDummyResponse(jsonDeviceReportingResponse);
    
    	testClient.socialLogin("Facebook", "123456", "AccessToken", new OnCallback<Response<AuthenticatedUser>>(){
    		public void OnResponse(Response<AuthenticatedUser> response, Object state){
    			assertNotNull(response);
    			
    			AuthenticatedUser user = response.getResult();
    			
    			assertNotNull(user);
    			
    		}
    		
    	});
    }
    
    public void testUserLoginBadUserName() {
        String jsonValueUser = readDataFromFile("DataResponses/GenericBadUserNameErrorResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);

        testClient.login("BadName", testUserPassword, null,
                new OnCallback<Response<AuthenticatedUser>>() {
                    public void OnResponse(Response<AuthenticatedUser> response, Object state) {
                        assertNotNull(response);

                        String errorMessage = response.getThrowable().getMessage();
                        assertEquals("SecurityFailedBadUserName", errorMessage);
                    }
                });

    }

    public void testUserGetFromToken() {
        String jsonValueUser = readDataFromFile("DataResponses/validUserResponse.json");
        String jsonDeviceReportingResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);
        BuddyHttpClientFactory.addDummyResponse(jsonDeviceReportingResponse);

        testClient.login(testToken, null, new OnCallback<Response<AuthenticatedUser>>() {
            public void OnResponse(Response<AuthenticatedUser> response, Object state) {
                assertNotNull(response);

                AuthenticatedUser user = response.getResult();

                assertEquals("User token received should be " + testToken, testToken,
                        user.getToken());
            }
        });

    }

    public void testUserLoginBadToken() {
        try {
            testClient.login(null, null, new OnCallback<Response<AuthenticatedUser>>() {
                public void OnResponse(Response<AuthenticatedUser> response, Object state) {
                }
            });
        } catch (IllegalArgumentException ex) {
            assertEquals("token can't be null or empty.", ex.getMessage());
        } catch (Exception ex2) {
            fail("Incorrect exception");
        }
    }

    public void testCheckUserNameAlreadyExists() {
        String jsonValueUser = readDataFromFile("DataResponses/UserUnitTests-UsernameExists.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);

        testClient.checkIfUserNameExists(testCheckUserName, null, new OnCallback<Response<Boolean>>() {
            public void OnResponse(Response<Boolean> response, Object state) {
                assertNotNull(response);
                assertTrue(response.getResult());
            }
        });
    }

    public void testCheckUserNameDoesNotExists() {
        String jsonValueUser = readDataFromFile("DataResponses/UserUnitTests-UsernameAvailable.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);

        testClient.checkIfUserNameExists(testCheckUserName, null, new OnCallback<Response<Boolean>>() {
            public void OnResponse(Response<Boolean> response, Object state) {
                assertNotNull(response);
                assertFalse(response.getResult());
            }
        });
    }

    public void testCheckUserEmailAlreadyExists() {
        String jsonValueUser = readDataFromFile("DataResponses/UserUnitTests-EmailExists.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);

        testClient.checkIfEmailExists(testCheckUserEmail, null, new OnCallback<Response<Boolean>>() {
            public void OnResponse(Response<Boolean> response, Object state) {
                assertNotNull(response);
                assertTrue(response.getResult());
            }
        });
    }

    public void testCheckUserEmailDoesNotExist() {
        String jsonValueUser = readDataFromFile("DataResponses/UserUnitTests-EmailAvailable.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);

        testClient.checkIfEmailExists(testCheckUserEmail, null, new OnCallback<Response<Boolean>>() {
            public void OnResponse(Response<Boolean> response, Object state) {
                assertNotNull(response);
                assertFalse(response.getResult());
            }
        });
    }

    public void testDeleteUser() {
        createAuthenticatedUser();

        String jsonValueUser = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);

        BuddyHttpClientFactory.setUnitTestMode(true);

        testAuthUser.delete(null, new OnCallback<Response<Boolean>>() {
            public void OnResponse(Response<Boolean> response, Object state) {
                assertNotNull(response);
                assertTrue(response.getResult());
            }
        });
    }

    public void testCheckIn() {
        createAuthenticatedUser();

        String jsonValueUser = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);

        BuddyHttpClientFactory.setUnitTestMode(true);

        testAuthUser.checkIn(0.0, 0.0, "test check in", "test tag", null,
                new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }
                });
    }

    public void testUpdateUser() {
        createAuthenticatedUser();

        String jsonValueUser = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);

        BuddyHttpClientFactory.setUnitTestMode(true);

        testAuthUser.update("", "", testCreateUserGender, testCreateUserAge, "",
                testCreateUserStatusId, testCreateUserFuzzlocation, testCreateUserCelebModeEnabled,
                testCreateUserApplicationTag, null, new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }
                });
    }

    public void testGetCheckIns() {
        createAuthenticatedUser();

        String jsonValueUser = readDataFromFile("DataResponses/UserUnitTests-GetCheckinResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);

        BuddyHttpClientFactory.setUnitTestMode(true);

        testAuthUser.getCheckIns(Constants.MinDate, null,
                new OnCallback<ListResponse<CheckInLocation>>() {
                    public void OnResponse(ListResponse<CheckInLocation> response, Object state) {
                        assertNotNull(response);
                        List<CheckInLocation> list = response.getList();
                        assertNotNull(list);

                        CheckInLocation location = list.get(0);
                        assertEquals("test check in", location.getPlaceName());
                    }
                });
    }

    public void testGetUserFromId() {
        createAuthenticatedUser();

        String jsonValueUser = readDataFromFile("DataResponses/validUserResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);

        testAuthUser.findUser(Integer.valueOf(testUserId), null, new OnCallback<Response<User>>() {
            public void OnResponse(Response<User> response, Object state) {
                assertNotNull(response);
                User user = response.getResult();
                assertNotNull(user);
                assertEquals("Test User 1", user.getName());
            }
        });
    }
    
    public void testFindUserUserName(){
    	createAuthenticatedUser();
    	
        String jsonValueUser = readDataFromFile("DataResponses/validUserResponse.json");
        
        BuddyHttpClientFactory.addDummyResponse(jsonValueUser);
                        
        testAuthUser.findUser("FalseUserName", new OnCallback<Response<User>>() {
        	public void OnResponse(Response<User> response, Object state) {
        		assertNotNull(response);
        		User user = response.getResult();
        		assertNotNull(user);
        		assertEquals("Test User 1", user.getName());
        	}        	
        });
    }
}
