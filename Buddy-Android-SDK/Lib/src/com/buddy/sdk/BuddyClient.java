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

package com.buddy.sdk;

import android.content.Context;
import android.os.Build;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.json.responses.SocialLoginResponse.SocialLogin;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Constants.UserGender;
import com.buddy.sdk.utils.Constants.UserStatus;
import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.web.BuddyHttpClientFactory;
import com.loopj.android.http.AsyncHttpClient;

import java.util.Date;

/**
 * Represents the main class and entry point to the Buddy platform. Use this
 * class to interact with the platform, create and login users and modify
 * general application level properties like Devices and Metadata.
 * <p>
 * 
 * <pre> 
 * 
 *    {@code BuddyClient client = new BuddyClient("APPNAME", "APPPASS");}
 *    {@code client.ping(null, new OnCallback<Response<String>>()}
 *    <code>{</code>
 *        {@code public void OnResponse(Response<String> response, Object state)}
 *        <code>{</code>
 * <code>}</code>
 */
public class BuddyClient {
    private String appName = "";
    private String appPassword = "";
    private UserDataModel userDataModel = null;
    private ServiceDataModel serviceDataModel = null;
    private ApplicationDataModel appDataModel = null;
    private AnalyticsDataModel analyticsDataModel = null;

    private AppMetadata appMeta = null;
    private GameBoards gameBoards = null;
    private Device device = null;
    private Sounds sounds = null;

    private String applicationVersion = "1.0";

    private Context appContext;
    private boolean recordDeviceInfo;

    /**
     * Initializes a new instance of the BuddyClient class. To get an
     * application username and password, go to http://buddy.com, create a new
     * developer account and create a new application.
     * 
     * @param appName The name of the application to use with this client. Can't
     *            be null or empty.
     * @param appPassword The password of the application to use with this
     *            client. Can't be null or empty.
     * @param applicationContext The application context to use with this
     *            client. Can't be null.
     * @param appVersion String that describes the version of the app you are
     *            building. This string will then be used when uploading device
     *            information to buddy or submitting crash reports.
     * @param autoRecordDeviceInfo If true automatically records the current
     *            device profile with the Buddy Service (device type, OS
     *            version, etc.).
     */
    public BuddyClient(String appName, String appPassword, Context applicationContext, String appVersion, boolean autoRecordDeviceInfo) {
        if (appName == null || appName.isEmpty())
            throw new IllegalArgumentException("appName can't be null or empty.");
        if (appPassword == null || appPassword.isEmpty())
            throw new IllegalArgumentException("appPassword can't be null or empty.");
        if (applicationContext == null)
            throw new IllegalArgumentException("applicationContext can't be null.");

        this.appName = appName;
        this.appPassword = appPassword;
        this.appContext = applicationContext;
        this.applicationVersion = appVersion;

        this.userDataModel = new UserDataModel(this);
        this.serviceDataModel = new ServiceDataModel(this);
        this.appDataModel = new ApplicationDataModel(this);
        this.analyticsDataModel = new AnalyticsDataModel(this);
        this.appMeta = new AppMetadata(this);
        this.gameBoards = new GameBoards(this);
        this.device = new Device(this);
        this.sounds = new Sounds(this);

        this.recordDeviceInfo = autoRecordDeviceInfo;

        BuddyHttpClientFactory.setupHttpClient();
    }

    /**
     * Initializes a new instance of the BuddyClient class. To get an
     * application username and password, go to http://buddy.com, create a new
     * developer account and create a new application.
     * 
     * @param appName The name of the application to use with this client. Can't
     *            be null or empty.
     * @param appPassword The password of the application to use with this
     *            client. Can't be null or empty.
     * @param applicationContext The application context to use with this
     *            client. Can't be null.
     */
    public BuddyClient(String appName, String appPassword, Context applicationContext) {
        this(appName, appPassword, applicationContext, "1.0", true);
    }

    /**
     * Initialize and get the AsynHttpClient using specified parameters.
     * 
     * @param socketTimeoutValue Default socket timeout (SO_TIMEOUT in the
     *            Android SDK) in milliseconds which is the timeout for waiting
     *            for data. A timeout value of zero is interpreted as an
     *            infinite timeout. Value of -1 sets the default value of 10
     *            seconds.
     * @param maxConnections Maximum number of connections allowed. Value of -1
     *            sets the default value of 10.
     * @param socketBufferSize Socket buffer size in bytes. Value of -1 sets the
     *            default value of 8192.
     */
    public void setupHttpClient(int socketTimeoutValue, int maxConnections, int socketBufferSize) {
        BuddyHttpClientFactory.setupHttpClient(socketTimeoutValue, maxConnections,
                socketBufferSize);
    }

    /**
     * Get the AsynHttpClient
     */
    public AsyncHttpClient getHttpClient() {
        return BuddyHttpClientFactory.getHttpClient();
    }

    /**
     * Gets the optional string that describes the version of the app you are
     * building. This string is used when uploading device information to Buddy
     * or submitting crash reports. It will default to 1.0.
     */
    public String getAppVersion() {
        return this.applicationVersion;
    }

    /**
     * Gets the application name for this client.
     */
    public String getAppName() {
        return this.appName;
    }

    /**
     * Gets the application password for this client.
     */
    public String getAppPassword() {
        return this.appPassword;
    }

    /**
     * Gets an object that can be used to record device information about this
     * client or upload crashes.
     */
    public Device getDevice() {
        return this.device;
    }

    /**
     * Gets an object that can be used to retrieve Sounds for the client.
     */
    public Sounds getSounds(){
    	return this.sounds;
    }
    
    /**
     * Create a new Buddy user.
     * 
     * @param name The name of the new user. Can't be null or empty.
     * @param password The password of the new user. Can't be null.
     * @param gender Optional new gender for the user.
     * @param age An optional age for the user.
     * @param email An optional new email for the user.
     * @param status An optional new status for the user.
     * @param fuzzLocation Optionally set location fuzzing for this user. When
     *            enabled user location is randomized in searches.
     * @param celebrityMode Optionally set the celebrity mode for this user.
     *            When enabled this user will be absent from all searches.
     * @param appTag An optional custom tag for this user.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void createUser(String name, String password, UserGender gender, int age, String email,
            UserStatus status, Boolean fuzzLocation, Boolean celebrityMode, String appTag,
            Object state, final OnCallback<Response<AuthenticatedUser>> callback) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("name can't be null or empty.");
        if (password == null)
            throw new IllegalArgumentException("password can't be null");
        if (age < 0)
            throw new IllegalArgumentException("age can't be less than 0.");

        this.createUser(name, password, gender, age, email, status, fuzzLocation, celebrityMode,
                appTag, state, new OnResponseCallback() {
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        // Now need to get data from the token
                        if (response.completed) {
                            String token = (String) response.responseObj;
                            login(token, state, callback);
                        } else {
                            if (response.exception != null) {
                                Response<AuthenticatedUser> userResponse = new Response<AuthenticatedUser>();
                                userResponse.setThrowable(response.exception);
                                callback.OnResponse(userResponse, state);
                            }
                        }
                    }

                });
    }

    /**
     * Create a new Buddy user.
     * 
     * @param userName The username of the user. Can't be null or empty.
     * @param userPassword The password of the user. Can't be null or empty.
     * @param callback The async callback to call on success or error.
     */
    public void createUser(String userName, String userPassword,
            final OnCallback<Response<AuthenticatedUser>> callback) {
        createUser(userName, userPassword, UserGender.any, 0, "", UserStatus.Any, false, false, "",
                null, callback);
    }

    public void socialLogin(String providerName, String providerUserId, String accessToken, final OnCallback<Response<AuthenticatedUser>> callback)
    {
    	if(providerName == null || providerName.isEmpty())
    		throw new IllegalArgumentException("providerName can't be null or empty");
    	if(providerUserId == null || providerUserId.isEmpty())
    		throw new IllegalArgumentException("providerUserId can't be null or empty");
    	if(accessToken == null || accessToken.isEmpty())
    		throw new IllegalArgumentException("accessName can't be null or empty");
    	
    	this.socialLogin(providerName, providerUserId, accessToken, new OnResponseCallback() {
    		public void OnResponse(BuddyCallbackParams response, Object state){
    			if(response.completed){
    				SocialLogin login = (SocialLogin)response.responseObj;
    				login(login.UserToken, state, callback);
    			} else {
    				if(response.exception != null)
    				{
    					Response<AuthenticatedUser> userResponse = new Response<AuthenticatedUser>();
    					userResponse.setThrowable(response.exception);
    					callback.OnResponse(userResponse, state);
    				}
    			}
    		}    		
    	});
    }
    
    /**
     * Login an existing user with their username and password.
     * 
     * @param userName The username of the user. Can't be null or empty.
     * @param userPassword The password of the user. Can't be null or empty.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void login(String userName, String userPassword, Object state,
            final OnCallback<Response<AuthenticatedUser>> callback) {
        this.userProfileRecover(userName, userPassword, state, new OnResponseCallback() {
            public void OnResponse(BuddyCallbackParams response, Object state) {
                // Now need to get data from the token

                if (response.completed) {
                    String token = (String) response.responseObj;
                    login(token, state, callback);
                } else {
                    if (response.exception != null) {
                        Response<AuthenticatedUser> userResponse = new Response<AuthenticatedUser>();
                        userResponse.setThrowable(response.exception);
                        callback.OnResponse(userResponse, state);
                    }
                }
            }

        });
    }

    /**
     * Login an existing user with their secret token. Each user is assigned a
     * token on creation, you can store it instead of a username/password
     * combination.
     * 
     * @param token The private token of the user to login.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void login(String token, Object state,
            final OnCallback<Response<AuthenticatedUser>> callback) {
        this.userProfileGetFromToken(token, state, callback);
    }

    /**
     * Check if another user with the same name already exists in the system.
     * 
     * @param name The name of the user to be verified. Can't be null or empty.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void checkIfUserNameExists(String name, Object state,
            final OnCallback<Response<Boolean>> callback) {
        if (Utils.isNullOrEmpty(name))
            throw new IllegalArgumentException("name can't be null or empty.");

        if (this.userDataModel != null) {
            this.userDataModel.checkIfUserNameExists(name, state, callback);
        }
    }

    /**
     * Check if another user with the same email already exists in the system.
     * 
     * @param email The email to be verified. Can't be null or empty.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void checkIfEmailExists(String email, Object state,
            final OnCallback<Response<Boolean>> callback) {
        if (Utils.isNullOrEmpty(email))
            throw new IllegalArgumentException("email can't be null or empty.");

        if (this.userDataModel != null) {
            this.userDataModel.checkIfEmailExists(email, state, callback);
        }
    }

    /**
     * Gets an object that can be used to manipulate application-level metadata.
     * Metadata is used to store custom values on the platform.
     */
    public AppMetadata getAppMeta() {
        return appMeta;
    }

    /**
     * Gets an object that can be used to retrieve high score rankings or search
     * for game boards in this application.
     */
    public GameBoards getGameBoards() {
        return gameBoards;
    }

    /**
     * Get the current Buddy webservice date/time.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getServiceTime(Object state, OnCallback<Response<Date>> callback) {
        if (this.serviceDataModel != null) {
            this.serviceDataModel.getServiceTime(state, callback);
        }
    }

    /**
     * Ping the service.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error. The first
     *            parameter is a string "Pong" if this method was successful.
     */
    public void ping(Object state, OnCallback<Response<String>> callback) {
        if (this.serviceDataModel != null) {
            this.serviceDataModel.ping(state, callback);
        }
    }

    /**
     * Get the current version of the service that is being used by this SDK.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getServiceVersion(Object state, OnCallback<Response<String>> callback) {
        if (this.serviceDataModel != null) {
            this.serviceDataModel.getServiceVersion(state, callback);
        }
    }

    /**
     * Gets a list of emails for all registered users for this app.
     * 
     * @param fromRow Used for paging, retrieve only records starting fromRow.
     * @param pageSize Used for paging, specify page size.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getUserEmails(int fromRow, int pageSize, Object state,
            OnCallback<ListResponse<String>> callback) {
        if (this.appDataModel != null) {
            this.appDataModel.getUserEmails(fromRow, fromRow + pageSize, state, callback);
        }
    }

    /**
     * Gets a list of emails for all registered users for this app.
     * 
     * @param fromRow Used for paging, retrieve only records starting fromRow.
     * @param callback The async callback to call on success or error.
     */
    public void getUserEmails(int fromRow, OnCallback<ListResponse<String>> callback) {
        getUserEmails(fromRow, 10, null, callback);
    }

    /**
     * Gets a list of profiles for all registered users for this app.
     * 
     * @param fromRow Used for paging, retrieve only records starting fromRow.
     * @param pageSize Used for paging, specify page size.
     * @param state An optional user defined object that will be passed to the
     *            callback
     * @param callback The async callback to call on success or error.
     */
    public void getUserProfiles(int fromRow, int pageSize, Object state,
            OnCallback<ListResponse<User>> callback) {
        if (this.appDataModel != null) {
            this.appDataModel.getUserProfiles(fromRow, fromRow + pageSize, state, callback);
        }
    }

    /**
     * Gets a list of profiles for all registered users for this app.
     * 
     * @param fromRow Used for paging, retrieve only records starting fromRow.
     * @param callback The async callback to call on success or error.
     */
    public void getUserProfiles(int fromRow, OnCallback<ListResponse<User>> callback) {
        getUserProfiles(fromRow, 10, null, callback);
    }

    /**
     * This method will return a list of statistics for the application covering
     * items such as total users, photos, etc.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getApplicationStatistics(Object state,
            OnCallback<ListResponse<ApplicationStatistics>> callback) {
        if (this.appDataModel != null) {
            this.appDataModel.getApplicationStatistics(state, callback);
        }
    }

    private void socialLogin(String providerName, String providerUserId, String accessToken, final OnResponseCallback callback)
    {
    	if(this.userDataModel != null){
    		this.userDataModel.socialLogin(providerName, providerUserId, accessToken, callback);
    	}
    }
    
    private void createUser(String userName, String userPassword, UserGender userGender,
            Integer userAge, String userEmail, UserStatus statusId, Boolean fuzzlocation,
            Boolean celebModeEnabled, String applicationTag, Object state,
            final OnResponseCallback callback) {
        if (this.userDataModel != null) {
            this.userDataModel.createUser(userName, userPassword, userGender, userAge, userEmail,
                    statusId, fuzzlocation, celebModeEnabled, applicationTag, state, callback);
        }
    }

    private void userProfileRecover(String userName, String userPassword, Object state,
            final OnResponseCallback callback) {
        if (this.userDataModel != null) {
            this.userDataModel.userProfileRecover(userName, userPassword, state, callback);
        }
    }

    private void userProfileGetFromToken(String token, Object state,
            final OnCallback<Response<AuthenticatedUser>> callback) {
        if (Utils.isNullOrEmpty(token))
            throw new IllegalArgumentException("token can't be null or empty.");
        if (this.userDataModel != null) {

            final OnCallback<Response<AuthenticatedUser>> parentCallback = 
                    this.GetRecordDeviceInformationCallback(callback, this.appContext);

            this.userDataModel.userProfileGetFromToken(token, state, parentCallback);
        }
    }
    
    private OnCallback<Response<AuthenticatedUser>> GetRecordDeviceInformationCallback(OnCallback<Response<AuthenticatedUser>> callback, Context appContext) {
        if (this.recordDeviceInfo)
        {
            this.recordDeviceInfo = false;
            return new RecordDeviceInformationCallback(this, appContext, callback);
        }
        else
        {
            return callback;
        }        
    }

    private class RecordDeviceInformationCallback implements OnCallback<Response<AuthenticatedUser>>
    {
        final OnCallback<Response<AuthenticatedUser>> childCallback;
        final Context applicationContext;
        final BuddyClient buddyClient;

        public RecordDeviceInformationCallback(BuddyClient client, Context appContext,
                OnCallback<Response<AuthenticatedUser>> callback) {
            this.buddyClient = client;
            this.applicationContext = appContext;
            this.childCallback = callback;
        }

        @Override
        public void OnResponse(Response<AuthenticatedUser> response,
                Object state) {

            if (response.isCompleted()) {
                String applicationId = this.applicationContext.getPackageName();
                
                this.buddyClient.device.recordInformation(Build.VERSION.RELEASE, Build.MANUFACTURER
                    + " : " + Build.MODEL, response.getResult(), this.buddyClient.getAppVersion(),
                    0, 0, applicationId, null, null);
            }
            
            this.childCallback.OnResponse(response, state);
        }
    }

    /**
     * Starts an analytics session.
     * 
     * @param user The user that is starting this session.
     * @param sessionName The name of the session.
     * @param appTag An optional custom tag to include with the session.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call upon success or error.
     */
    public void startSession(AuthenticatedUser user, String sessionName, String appTag,
            Object state, OnCallback<Response<String>> callback) {
        if (user == null || user.getToken() == null)
            throw new IllegalArgumentException(
                    "An AuthenticatedUser value is required for parameter user.");
        if (Utils.isNullOrEmpty(sessionName))
            throw new IllegalArgumentException("sessionName must not be null or empty.");

        if (this.analyticsDataModel != null) {
            this.analyticsDataModel.startSession(user, sessionName, appTag, state, callback);
        }
    }

    /**
     * Starts an analytics session.
     * 
     * @param user The user that is starting this session.
     * @param sessionName The name of the session.
     * @param callback The callback to call upon success or error.
     */
    public void startSession(AuthenticatedUser user, String sessionName,
            OnCallback<Response<String>> callback) {
        startSession(user, sessionName, "", null, callback);
    }

    /**
     * Ends an analytics session.
     * 
     * @param user The user that is starting this session.
     * @param sessionId The id of the session, returned from startSession.
     * @param appTag An optional custom tag to include with the session.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call upon success or error.
     */
    public void endSession(AuthenticatedUser user, String sessionId, String appTag, Object state,
            OnCallback<Response<Boolean>> callback) {
        if (user == null || user.getToken() == null)
            throw new IllegalArgumentException(
                    "An AuthenticatedUser value is required for parmaeter user.");

        if (this.analyticsDataModel != null) {
            this.analyticsDataModel.endSession(user, sessionId, appTag, state, callback);
        }
    }

    /**
     * Ends an analytics session.
     * 
     * @param user The user that is starting this session.
     * @param sessionId The id of the session, returned from startSession.
     * @param callback The callback to call upon success or error.
     */
    public void endSession(AuthenticatedUser user, String sessionId,
            OnCallback<Response<Boolean>> callback) {
        endSession(user, sessionId, "", null, callback);
    }

    /**
     * Records a session metric value.
     * 
     * @param user The user that is starting this session.
     * @param sessionId The id of the session, returned from startSession.
     * @param metricKey A custom key describing the metric.
     * @param metricValue The value to set.
     * @param appTag An optional custom tag to include with the session.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call upon success or error.
     */
    public void recordSessionMetric(AuthenticatedUser user, String sessionId, String metricKey,
            String metricValue, String appTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        if (user == null || user.getToken() == null)
            throw new IllegalArgumentException(
                    "An AuthenticatedUser value is required.");
        if (Utils.isNullOrEmpty(metricKey))
            throw new IllegalArgumentException("metricKey must not be null or empty.");
        if (metricValue == null)
            throw new IllegalArgumentException("metricValue must not be null.");

        if (this.analyticsDataModel != null) {
            this.analyticsDataModel.recordSessionMetric(user, sessionId, metricKey, metricValue,
                    appTag, state, callback);
        }
    }

    /**
     * Records a session metric value.
     * 
     * @param user The user that is starting this session.
     * @param sessionId The id of the session, returned from startSession.
     * @param metricKey A custom key describing the metric.
     * @param metricValue The value to set.
     * @param callback The callback to call upon success or error.
     */
    public void recordSessionMetric(AuthenticatedUser user, String sessionId, String metricKey,
            String metricValue, final OnCallback<Response<Boolean>> callback) {
        recordSessionMetric(user, sessionId, metricKey, metricValue, "", null, callback);
    }
}
