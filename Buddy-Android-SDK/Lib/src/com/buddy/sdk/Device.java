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

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Utils;

/**
 * Represents an object that can be used to record device analytics, like device
 * types and app crashes.
 * <p>
 * 
 * <pre>
 * 
 *    {@code BuddyClient client = new BuddyClient("APPNAME", "APPPASS", "APPVERSION", false);}
 *    
 *    {@code BuddyClient client = new BuddyClient("APPNAME", "APPPASS", "APPVERSION", false);}
 *    {@code client.login("username", "password", new OnCallback<Response<AuthenticatedUser>>()}
 *    <code>{</code>
 *      {@code public void OnResponse(Response<AuthenticatedUser> response, Object state)}
 *      <code>{</code>
 *          {@code client.getDevice().recordInformation("OSVERSION", "DEVICETYPE", user, 0.0, 0.0, null, new OnCallback<Response<Boolean>>()}
 *          <code>{</code>
 *              {@code public void OnResponse(Response<Boolean> response, Object state)}
 *        <code>{</code>
 *        <code>}</code>
 *    <code>});</code>
 *      <code>}</code>
 *    <code>});</code> *    
 *    
 *    

 * </pre>
 */
public class Device {
    protected BuddyClient client;
    private AnalyticsDataModel analyticsDataModel = null;

    Device(BuddyClient client) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");
        this.client = client;
        this.analyticsDataModel = new AnalyticsDataModel(this.client);
    }

    /**
     * Record runtime device type information. This info will be uploaded to the
     * Buddy service and can later be used for analytics purposes.
     * 
     * @param osVersion The OS version of the device running this code.
     * @param deviceType The type of device running this app.
     * @param user The user that's registering this device information.
     * @param callback The callback to call upon success or error.
     */
    public void recordInformation(String osVersion, String deviceType, AuthenticatedUser user, final OnCallback<Response<Boolean>> callback) {
        recordInformation(osVersion, deviceType, user, "1.0", 0, 0, "", null, callback);
    }

    /**
     * Record runtime device type information. This info will be uploaded to the
     * Buddy service and can later be used for analytics purposes.
     * 
     * @param osVersion The OS version of the device running this code.
     * @param deviceType The type of device running this app.
     * @param user The user that's registering this device information.
     * @param appVersion The optional version of this application.
     * @param latitude The optional latitude where this report was submitted.
     * @param longitude The optional longitude where this report was submitted.
     * @param metadata An optional application specific metadata string to
     *            include with the report.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call upon success or error.
     */
    public void recordInformation(String osVersion, String deviceType, AuthenticatedUser user, String appVersion, 
            double latitude, double longitude, String metadata, Object state,
            final OnCallback<Response<Boolean>> callback) {
        if (osVersion == null)
            throw new IllegalArgumentException("osVersion can't be null.");
        if (deviceType == null)
            throw new IllegalArgumentException("deviceType can't be null.");
        if (user == null)
            throw new IllegalArgumentException("user can't be null.");
        if (latitude > 90.0 || latitude < -90.0)
            throw new IllegalArgumentException(
                    "latitude can't be bigger than 90.0 or smaller than -90.0.");
        if (longitude > 180.0 || longitude < -180.0)
            throw new IllegalArgumentException(
                    "longitude can't be bigger than 180.0 or smaller than -180.0.");

        if (this.analyticsDataModel != null) {
            this.analyticsDataModel.recordInformation(user, osVersion, deviceType, latitude,
                    longitude, appVersion, metadata,
                    state, callback);
        }
    }

    /**
     * Record runtime crash information for this app. This could be exceptions,
     * errors or your own custom crash information.
     * 
     * @param methodName The method name or location where the error happened.
     * @param osVersion The OS version of the device running this code.
     * @param deviceType The type of device running this app.
     * @param user The user that's registering this device information.
     * @param stackTrace The optional stack trace of where the error happened.
     * @param appVersion The optional version of this application.
     * @param latitude The optional latitude where this report was submitted.
     * @param longitude The optional longitude where this report was submitted.
     * @param metadata An optional application specific metadata string to
     *            include with the report.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The callback to call upon success or error.
     */
    public void recordCrash(String methodName, String osVersion, String deviceType, AuthenticatedUser user, 
            String stackTrace, String appVersion, double latitude, double longitude,
            String metadata, Object state, OnCallback<Response<Boolean>> callback) {
        if (Utils.isNullOrEmpty(methodName))
            throw new IllegalArgumentException("methodName can't be null.");
       if (osVersion == null)
            throw new IllegalArgumentException("osVersion can't be null.");
        if (deviceType == null)
            throw new IllegalArgumentException("deviceType can't be null.");
        if (user == null)
            throw new IllegalArgumentException("user can't be null.");
        if (latitude > 90.0 || latitude < -90.0)
            throw new IllegalArgumentException(
                    "latitude can't be bigger than 90.0 or smaller than -90.0.");
        if (longitude > 180.0 || longitude < -180.0)
            throw new IllegalArgumentException(
                    "longitude can't be bigger than 180.0 or smaller than -180.0.");

        if (this.analyticsDataModel != null) {
            this.analyticsDataModel.recordCrash(user, appVersion, osVersion,
                    deviceType, methodName, stackTrace, metadata, latitude, longitude, state,
                    callback);
        }
    }

    /**
     * Record runtime crash information for this app. This could be exceptions,
     * errors, or your own custom crash information.
     * 
     * @param methodName The method name or location where the error happened.
     * @param osVersion The OS version of the device running this code.
     * @param deviceType The type of device running this app.
     * @param user The user that's registering this device information.
     * @param callback The callback to call upon success or error.
     */
    public void recordCrash(String methodName, String osVersion, String deviceType, AuthenticatedUser user,
            OnCallback<Response<Boolean>> callback) {
        recordCrash(methodName, osVersion, deviceType, user, "", "1.0", 0, 0, "", null, callback);
    }
}
