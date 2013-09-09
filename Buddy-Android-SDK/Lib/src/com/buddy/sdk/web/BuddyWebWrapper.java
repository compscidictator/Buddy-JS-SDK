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

package com.buddy.sdk.web;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.buddy.sdk.AuthenticatedUser;
import com.buddy.sdk.BuddyCallbackParams;
import com.buddy.sdk.BuddyClient;
import com.buddy.sdk.BuddyFile;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.Sounds.SoundQuality;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Utils;

public class BuddyWebWrapper {
    private static String TAG = "BuddySDK";
    private static String urlType = "https://";
    private static String endpointUrl = "webservice.buddyplatform.com/Service/v1/BuddyService.ashx";
    
    private enum HttpRequestType{HttpGet, HttpPostUrlEncoded, HttpPostMultipartForm}
    private static String BuildUrl(String apiCall, Map<String, Object> params){
    	String url = urlType + endpointUrl + "?" + apiCall;
    	
    	Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
    	while(it.hasNext())
    	{
    		Map.Entry<String, Object> pair = (Map.Entry<String, Object>)it.next();
    		if(pair.getValue().getClass().equals(BuddyFile.class))
    		{
    			//Ignore
    		}
    		else{
    			String v = null;
    			try{
    				v = URLEncoder.encode(pair.getValue().toString(), "utf-8");
    			}catch(UnsupportedEncodingException ex){}
    			url = url.concat("&" + pair.getKey() + "=" + v);
    		}
    	}
    	return url;
    }
    
    private static List<NameValuePair> MapToList(Map<String, Object> params){
    	List<NameValuePair> pairs = new LinkedList<NameValuePair>();
    	
    	Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
    	while(it.hasNext())
    	{
    		Map.Entry<String, Object> pair = (Map.Entry<String, Object>)it.next();
    		pairs.add(new BasicNameValuePair(pair.getKey(), pair.getValue().toString()));
    	}
    	return pairs;
    }
    
    public static void MakePostReturnStream(String apiCall, Map<String, Object> params, final OnResponseCallback callback){
    	InputStream in = null;
    	int response = -1;
    	URL url = null;
    	try{
    		url = new URL(BuildUrl(apiCall, params));
    	}catch(MalformedURLException ex){}    
    	
		try {
			HttpURLConnection hcn = BuddyHttpClientFactory.createHttpURLConnection(url);
		    hcn.setAllowUserInteraction(false);
		    hcn.setInstanceFollowRedirects(true);
		    hcn.setRequestMethod("GET");
		    hcn.setInstanceFollowRedirects(true);
		    hcn.connect();
		
		    response = hcn.getResponseCode();
		    //if we're going from http to https it won't redirect automagically, we have to act manually
		    if (response == HttpURLConnection.HTTP_MOVED_TEMP) {
		    	String newUrl = hcn.getHeaderField("Location");
		    	hcn = (HttpURLConnection) new URL(newUrl).openConnection();
		    	hcn.setRequestMethod("GET");
		    	hcn.connect();
		    	in = hcn.getInputStream();
	    	//otherwise we can just grab the stream
	    	}else if (response == HttpURLConnection.HTTP_OK){
	    		in = hcn.getInputStream();
	    	}
		}
		catch (Exception ex) {          
		}
		BuddyCallbackParams callbackParams = new BuddyCallbackParams();  
		callbackParams.responseObj = in;
		    
		callback.OnResponse(callbackParams, null); 
    }
    
    private static Response<String> SendFileMultipart(String targetURL, InputStream is, String contentType, String field) {    	  
    	  Response<String> result = new Response<String>();
    	  
    	  String BOUNDARY = "==================================";
    	  HttpURLConnection conn = null;
    	  byte[] buf = new byte[1024];
    	  int responseCode = -1; // Keeps track of any response codes we might get.
    	   
    	  try {
    	   // These strings are sent in the request body. They provide information about the file being uploaded
    	   String contentDisposition = "Content-Disposition: form-data; name=\""+field+"\"; filename=\"" + field + "\"";
    	   contentType = "Content-Type: " + contentType;
    	 
    	   // This is the standard format for a multipart request header
    	   String requestHeader = String.format("--%s\n%s\n%s\n\n", BOUNDARY, contentDisposition, contentType);
    	   
    	   // This is the standard format for a multipart request footer
    	   String requestFooter = String.format("\n--%s--", BOUNDARY);
    	       	     	 
    	   // Make a connect to the server
    	   URL url = new URL(targetURL);
    	   conn = (HttpURLConnection) BuddyHttpClientFactory.createHttpURLConnection(url);
    	   
    	   conn.setDoOutput(true);
    	   conn.setDoInput(true);
    	   conn.setUseCaches(false);
    	   conn.setRequestMethod("POST");
    	   conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
    	 
    	   // Send the body
    	   DataOutputStream dataOS = new DataOutputStream(conn.getOutputStream());
    	   dataOS.writeBytes(requestHeader.toString());
    	   BufferedInputStream b = new BufferedInputStream(is);
    	   
    	   try {
    		int readNum;
    	    while((readNum = b.read(buf)) != -1) {
    	     dataOS.write(buf, 0, readNum); //no doubt here is 0
    	    }
    	   } catch (IOException ex) {
    	    throw new Exception(String.format("Error reading file!"));
    	   }
    	 
    	   dataOS.writeBytes(requestFooter.toString());
    	   dataOS.flush();
    	   dataOS.close();
    	 
    	   // Ensure we got the HTTP 200 response code
    	   responseCode = conn.getResponseCode();
    	   
    	   Scanner s = new Scanner(conn.getInputStream()).useDelimiter("\\A");
    	   String responseMsg = s.hasNext() ? s.next() : "";
    	   result.setResult(responseMsg);
    	 
    	   if (responseCode != 200) {
    	    throw new Exception(String.format("Received the response code %d from the URL %s %s", responseCode, url, responseMsg));
    	   } else {
    	   }
    	  }catch (Exception e){
    	    
    	   // I am not interested on exactly why this failed, only that it has.
    	   // This can be extended by specific Exception handling.
    	    
    	   result.setErrorMessage(e.getMessage());// try to salvage the response code.. probably -1 at this point.
    	  } finally {
    	   if (conn != null) {
    	    conn.disconnect();
    	   }
    	  }
    	 
    	  return result;
	}
    
    public static void MakePostMultiRequest(String apiCall, Map<String, Object> params,
    		final OnResponseCallback callback){
    	    	
    	String url = BuildUrl(apiCall, params);
    	
    	BuddyFile file = null;
    	String fileName = null;
    	
    	Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
    	while(it.hasNext())
    	{
    		Map.Entry<String, Object> pair = (Map.Entry<String, Object>)it.next();
    		if(pair.getValue().getClass().equals(BuddyFile.class))
    		{
    			file = (BuddyFile)pair.getValue();
    			fileName = pair.getKey();
    		}else{}
    	}
    	
    	Response<String> res = SendFileMultipart(url, file.data, file.contentType, fileName);
    	
    	BuddyCallbackParams response = new BuddyCallbackParams();
    	response.response = res.getResult();
    	
    	callback.OnResponse(response, null);
    }
    
    public static void MakePostRequest(String apiCall, Map<String, Object> params, final OnResponseCallback callback)
    {    	
    	List<NameValuePair> pairs = MapToList(params);
    	MakePostRequest(apiCall, pairs, null, callback);
    }
    
    public static void MakeGetRequest(String apiCall, Map<String, Object> params, final OnResponseCallback callback){
    	List<NameValuePair> pairs = MapToList(params);
    	MakeRequest(apiCall, pairs, null, callback);
    }
    
    public static void MakePostRequest(String apiCall, List<NameValuePair> params,
            final Object state, final OnResponseCallback callback) {
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(params);
        } catch (UnsupportedEncodingException e1) {
            BuddyCallbackParams callbackParams = new BuddyCallbackParams(e1, "");
            callback.OnResponse(callbackParams, state);
        }

        String url = urlType + endpointUrl + "?" + apiCall;

        AsyncHttpClient client = BuddyHttpClientFactory.getHttpClient();

        client.post(null, url, entity, "application/x-www-form-urlencoded",
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        Log.d(TAG, "onStart");
                    }

                    @Override
                    public void onSuccess(String success) {
                        Log.d(TAG + "-POST SUCCESS", success);
                        if (callback != null) {
                            BuddyCallbackParams callbackParams = null;
                            Exception exception = Utils.ProcessStandardErrors(success);
                            if (exception == null) {
                                callbackParams = new BuddyCallbackParams();
                                callbackParams.response = success;
                            } else {
                                callbackParams = new BuddyCallbackParams(exception, success);
                            }
                            callback.OnResponse(callbackParams, state);
                        }
                        Log.d(TAG, "onSuccess");
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        if (callback != null) {
                            BuddyCallbackParams callbackParams = new BuddyCallbackParams(e,
                                    response);
                            callback.OnResponse(callbackParams, state);
                        }
                        Log.d(TAG, "onFailure");
                    }

                });
    }

    public static void MakeRequest(String apiCall, Map<String, Object> params, HttpRequestType type, final OnResponseCallback callback){
    	Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
    	while(it.hasNext())
    	{
    		Map.Entry<String, Object> pair = (Map.Entry<String, Object>)it.next();
    		if(pair.getValue().getClass().equals(BuddyFile.class)){
    			type = HttpRequestType.HttpPostMultipartForm;
    		}
    	}
    	
    	switch(type)
    	{
    		case HttpGet:
    			MakeGetRequest(apiCall, params, callback);
    			break;
    		case HttpPostUrlEncoded:
    			MakePostRequest(apiCall, params, callback);
    			break;
    		case HttpPostMultipartForm:
    			MakePostMultiRequest(apiCall, params, callback);
    	}
    }
    
    public static void MakeRequest(String apiCall, List<NameValuePair> params, final Object state,
            final OnResponseCallback callback) {
        RequestParams reqParams = new RequestParams();
        for (NameValuePair pair : params) {
            reqParams.put(pair.getName(), pair.getValue());
        }
        String url = urlType + endpointUrl + "?" + apiCall;

        AsyncHttpClient client = BuddyHttpClientFactory.getHttpClient();

        client.get(null, url, reqParams, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.d(TAG, "onStart");
            }

            @Override
            public void onSuccess(String success) {
                Log.d(TAG + "-POST SUCCESS", success);
                if (callback != null) {
                    BuddyCallbackParams callbackParams = null;
                    Exception exception = Utils.ProcessStandardErrors(success);
                    if (exception == null) {
                        callbackParams = new BuddyCallbackParams();
                        callbackParams.response = success;
                    } else {
                        callbackParams = new BuddyCallbackParams(exception, success);
                    }
                    callback.OnResponse(callbackParams, state);
                }
                Log.d(TAG, "onSuccess");
            }

            @Override
            public void onFailure(Throwable e, String response) {
                if (callback != null) {
                    BuddyCallbackParams callbackParams = new BuddyCallbackParams(e, response);
                    callback.OnResponse(callbackParams, state);
                }
                Log.d(TAG, "onFailure");
            }

        });
    }

    public static void UserAccount_Defines_GetStatusValues(String BuddyApplicationName,
            String BuddyApplicationPassword, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));

        MakeRequest("UserAccount_Defines_GetStatusValues", params, state, callback);
    }

    public static void UserAccount_Identity_AddNewValue(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String IdentityValue,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("IdentityValue", IdentityValue));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("UserAccount_Identity_AddNewValue", params, state, callback);
    }

    public static void UserAccount_Identity_CheckForValues(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String IdentityValue,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("IdentityValue", IdentityValue));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("UserAccount_Identity_CheckForValues", params, state, callback);
    }

    public static void UserAccount_Identity_GetMyList(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("UserAccount_Identity_GetMyList", params, state, callback);
    }

    public static void UserAccount_Identity_RemoveValue(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String IdentityValue,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("IdentityValue", IdentityValue));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("UserAccount_Identity_RemoveValue", params, state, callback);
    }

    public static void UserAccount_Location_Checkin(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Float Latitude, Float Longitude,
            String CheckInComment, String ApplicationTag, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("CheckInComment", CheckInComment));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("UserAccount_Location_Checkin", params, state, callback);
    }

    public static void UserAccount_Location_GetHistory(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String FromDateTime, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("FromDateTime", FromDateTime));

        MakeRequest("UserAccount_Location_GetHistory", params, state, callback);
    }

    public static void UserAccount_Profile_CheckUserEmail(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserEmailToVerify, String RESERVED,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserEmailToVerify", UserEmailToVerify));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("UserAccount_Profile_CheckUserEmail", params, state, callback);
    }

    public static void UserAccount_Profile_CheckUserName(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserNameToVerify, String RESERVED,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserNameToVerify", UserNameToVerify));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("UserAccount_Profile_CheckUserName", params, state, callback);
    }

    public static void UserAccount_Profile_SocialLogin(BuddyClient client,
    		String ProviderName, String ProviderUserId, String AccessToken,
    		final OnResponseCallback callback)
    {
		Map<String, Object> params = new HashMap<String, Object>();
    	
    	addAuth(params, client);
    	params.put("ProviderName", ProviderName);
    	params.put("ProviderUserID", ProviderUserId);
    	params.put("AccessToken", AccessToken);
    	
    	MakeRequest("UserAccount_Profile_SocialLogin", params, HttpRequestType.HttpGet, callback);    	
    }
    
    public static void UserAccount_Profile_Create(String BuddyApplicationName,
            String BuddyApplicationPassword, String NewUserName, String UserSuppliedPassword,
            String NewUserGender, Integer UserAge, String NewUserEmail, Integer StatusID,
            Integer FuzzLocationEnabled, Integer CelebModeEnabled, String ApplicationTag,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("NewUserName", NewUserName));
        params.add(new BasicNameValuePair("UserSuppliedPassword", UserSuppliedPassword));
        params.add(new BasicNameValuePair("NewUserGender", NewUserGender));
        params.add(new BasicNameValuePair("UserAge", String.valueOf(UserAge)));
        params.add(new BasicNameValuePair("NewUserEmail", NewUserEmail));
        params.add(new BasicNameValuePair("StatusID", String.valueOf(StatusID)));
        params.add(new BasicNameValuePair("FuzzLocationEnabled", String
                .valueOf(FuzzLocationEnabled)));
        params.add(new BasicNameValuePair("CelebModeEnabled", String.valueOf(CelebModeEnabled)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("UserAccount_Profile_Create", params, state, callback);
    }

    public static void UserAccount_Profile_DeleteAccount(String BuddyApplicationName,
            String BuddyApplicationPassword, Integer UserProfileID, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserProfileID", String.valueOf(UserProfileID)));

        MakeRequest("UserAccount_Profile_DeleteAccount", params, state, callback);
    }

    public static void UserAccount_Profile_GetFromUserID(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer UserIDToFetch, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserIDToFetch", String.valueOf(UserIDToFetch)));

        MakeRequest("UserAccount_Profile_GetFromUserID", params, state, callback);
    }

    public static void UserAccount_Profile_GetFromUserName(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String UserNameToFetch,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserNameToFetch", UserNameToFetch));

        MakeRequest("UserAccount_Profile_GetFromUserName", params, state, callback);
    }

    public static void UserAccount_Profile_GetFromUserToken(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));

        MakeRequest("UserAccount_Profile_GetFromUserToken", params, state, callback);
    }

    public static void UserAccount_Profile_GetUserIDFromUserToken(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("UserAccount_Profile_GetUserIDFromUserToken", params, state, callback);
    }

    public static void UserAccount_Profile_Recover(String BuddyApplicationName,
            String BuddyApplicationPassword, String username, String usersuppliedpassword,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("usersuppliedpassword", usersuppliedpassword));

        MakeRequest("UserAccount_Profile_Recover", params, state, callback);
    }

    public static void UserAccount_Profile_Search(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer SearchDistance,
            Float Latitude, Float Longitude, Integer RecordLimit, String Gender, Integer AgeStart,
            Integer AgeStop, Integer StatusID, String TimeFilter, String ApplicationTag,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("SearchDistance", String.valueOf(SearchDistance)));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));
        params.add(new BasicNameValuePair("Gender", Gender));
        params.add(new BasicNameValuePair("AgeStart", String.valueOf(AgeStart)));
        params.add(new BasicNameValuePair("AgeStop", String.valueOf(AgeStop)));
        params.add(new BasicNameValuePair("StatusID", String.valueOf(StatusID)));
        params.add(new BasicNameValuePair("TimeFilter", TimeFilter));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("UserAccount_Profile_Search", params, state, callback);
    }

    public static void UserAccount_Profile_Update(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String UserName,
            String UserSuppliedPassword, String UserGender, Integer UserAge, String UserEmail,
            Integer StatusID, Integer FuzzLocationEnabled, Integer CelebModeEnabled,
            String ApplicationTag, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserName", UserName));
        params.add(new BasicNameValuePair("UserSuppliedPassword", UserSuppliedPassword));
        params.add(new BasicNameValuePair("UserGender", UserGender));
        params.add(new BasicNameValuePair("UserAge", String.valueOf(UserAge)));
        params.add(new BasicNameValuePair("UserEmail", UserEmail));
        params.add(new BasicNameValuePair("StatusID", String.valueOf(StatusID)));
        params.add(new BasicNameValuePair("FuzzLocationEnabled", String
                .valueOf(FuzzLocationEnabled)));
        params.add(new BasicNameValuePair("CelebModeEnabled", String.valueOf(CelebModeEnabled)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("UserAccount_Profile_Update", params, state, callback);
    }

    public static void Pictures_Filters_ApplyFilter(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer ExistingPhotoID,
            String FilterName, String FilterParameters, Integer ReplacePhoto, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("ExistingPhotoID", String.valueOf(ExistingPhotoID)));
        params.add(new BasicNameValuePair("FilterName", FilterName));
        params.add(new BasicNameValuePair("FilterParameters", FilterParameters));
        params.add(new BasicNameValuePair("ReplacePhoto", String.valueOf(ReplacePhoto)));

        MakeRequest("Pictures_Filters_ApplyFilter", params, state, callback);
    }

    public static void Pictures_Filters_GetList(String BuddyApplicationName,
            String BuddyApplicationPassword, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));

        MakeRequest("Pictures_Filters_GetList", params, state, callback);
    }

    public static void Pictures_Photo_Add(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String bytesFullPhotoData,
            Integer AlbumID, String PhotoComment, Float Latitude, Float Longitude, String RESERVED,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("bytesFullPhotoData", bytesFullPhotoData));
        params.add(new BasicNameValuePair("AlbumID", String.valueOf(AlbumID)));
        params.add(new BasicNameValuePair("PhotoComment", PhotoComment));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakePostRequest("Pictures_Photo_Add", params, state, callback);
    }

    public static void Pictures_Photo_AddWithWatermark(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String bytesFullPhotoData,
            Integer AlbumID, String PhotoComment, Float Latitude, Float Longitude,
            String WatermarkMessage, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("bytesFullPhotoData", bytesFullPhotoData));
        params.add(new BasicNameValuePair("AlbumID", String.valueOf(AlbumID)));
        params.add(new BasicNameValuePair("PhotoComment", PhotoComment));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("WatermarkMessage", WatermarkMessage));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakePostRequest("Pictures_Photo_AddWithWatermark", params, state, callback);
    }

    public static void Pictures_Photo_Delete(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer PhotoAlbumPhotoID,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("PhotoAlbumPhotoID", String.valueOf(PhotoAlbumPhotoID)));

        MakeRequest("Pictures_Photo_Delete", params, state, callback);
    }

    public static void Pictures_Photo_Get(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer UserProfileID,
            Integer PhotoID, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserProfileID", String.valueOf(UserProfileID)));
        params.add(new BasicNameValuePair("PhotoID", String.valueOf(PhotoID)));

        MakeRequest("Pictures_Photo_Get", params, state, callback);
    }

    public static void Pictures_Photo_SetAppTag(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer UserProfileID,
            Integer PhotoAlbumPhotoID, String ApplicationTag, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserProfileID", String.valueOf(UserProfileID)));
        params.add(new BasicNameValuePair("PhotoAlbumPhotoID", String.valueOf(PhotoAlbumPhotoID)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));

        MakeRequest("Pictures_Photo_SetAppTag", params, state, callback);
    }

    public static void Pictures_PhotoAlbum_Create(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String AlbumName,
            Integer PublicAlbumBit, String ApplicationTag, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("AlbumName", AlbumName));
        params.add(new BasicNameValuePair("PublicAlbumBit", String.valueOf(PublicAlbumBit)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Pictures_PhotoAlbum_Create", params, state, callback);
    }

    public static void Pictures_PhotoAlbum_Delete(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer PhotoAlbumID, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("PhotoAlbumID", String.valueOf(PhotoAlbumID)));

        MakeRequest("Pictures_PhotoAlbum_Delete", params, state, callback);
    }

    public static void Pictures_PhotoAlbum_Get(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer UserProfileID,
            Integer PhotoAlbumID, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserProfileID", String.valueOf(UserProfileID)));
        params.add(new BasicNameValuePair("PhotoAlbumID", String.valueOf(PhotoAlbumID)));

        MakeRequest("Pictures_PhotoAlbum_Get", params, state, callback);
    }

    public static void Pictures_PhotoAlbum_GetAllPictures(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer UserProfileID,
            String SearchFromDateTime, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserProfileID", String.valueOf(UserProfileID)));
        params.add(new BasicNameValuePair("SearchFromDateTime", SearchFromDateTime));

        MakeRequest("Pictures_PhotoAlbum_GetAllPictures", params, state, callback);
    }

    public static void Pictures_PhotoAlbum_GetFromAlbumName(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer UserProfileID,
            String PhotoAlbumName, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserProfileID", String.valueOf(UserProfileID)));
        params.add(new BasicNameValuePair("PhotoAlbumName", PhotoAlbumName));

        MakeRequest("Pictures_PhotoAlbum_GetFromAlbumName", params, state, callback);
    }

    public static void Pictures_PhotoAlbum_GetByDateTime(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer UserProfileID,
            String StartDateTime, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserProfileID", String.valueOf(UserProfileID)));
        params.add(new BasicNameValuePair("StartDateTime", StartDateTime));

        MakeRequest("Pictures_PhotoAlbum_GetByDateTime", params, state, callback);
    }

    public static void Pictures_PhotoAlbum_GetList(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer UserProfileID, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserProfileID", String.valueOf(UserProfileID)));

        MakeRequest("Pictures_PhotoAlbum_GetList", params, state, callback);
    }

    public static void Pictures_ProfilePhoto_Add(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String bytesFullPhotoData,
            String ApplicationTag, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("bytesFullPhotoData", bytesFullPhotoData));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakePostRequest("Pictures_ProfilePhoto_Add", params, state, callback);
    }

    public static void Pictures_ProfilePhoto_Delete(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer ProfilePhotoID,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("ProfilePhotoID", String.valueOf(ProfilePhotoID)));

        MakeRequest("Pictures_ProfilePhoto_Delete", params, state, callback);
    }

    public static void Pictures_ProfilePhoto_GetAll(String BuddyApplicationName,
            String BuddyApplicationPassword, Integer UserProfileID, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserProfileID", String.valueOf(UserProfileID)));

        MakeRequest("Pictures_ProfilePhoto_GetAll", params, state, callback);
    }

    public static void Pictures_ProfilePhoto_GetMyList(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));

        MakeRequest("Pictures_ProfilePhoto_GetMyList", params, state, callback);
    }

    public static void Pictures_ProfilePhoto_Set(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String ProfilePhotoResource,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("ProfilePhotoResource", ProfilePhotoResource));

        MakeRequest("Pictures_ProfilePhoto_Set", params, state, callback);
    }

    public static void Pictures_SearchPhotos_Data(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Float Latitude, Float Longitude,
            Integer RecordLimit, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));

        MakeRequest("Pictures_SearchPhotos_Data", params, state, callback);
    }

    public static void Pictures_SearchPhotos_Nearby(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer SearchDistance,
            Float Latitude, Float Longitude, Integer RecordLimit, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("SearchDistance", String.valueOf(SearchDistance)));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));

        MakeRequest("Pictures_SearchPhotos_Nearby", params, state, callback);
    }

    public static void Pictures_VirtualAlbum_AddPhoto(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer VirtualAlbumID,
            Integer ExistingPhotoID, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("VirtualAlbumID", String.valueOf(VirtualAlbumID)));
        params.add(new BasicNameValuePair("ExistingPhotoID", String.valueOf(ExistingPhotoID)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Pictures_VirtualAlbum_AddPhoto", params, state, callback);
    }

    public static void Pictures_VirtualAlbum_AddPhotoBatch(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer VirtualAlbumID,
            String ExistingPhotoIDBatchString, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("VirtualAlbumID", String.valueOf(VirtualAlbumID)));
        params.add(new BasicNameValuePair("ExistingPhotoIDBatchString", ExistingPhotoIDBatchString));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Pictures_VirtualAlbum_AddPhotoBatch", params, state, callback);
    }

    public static void Pictures_VirtualAlbum_Create(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String AlbumName,
            String ApplicationTag, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("AlbumName", AlbumName));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Pictures_VirtualAlbum_Create", params, state, callback);
    }

    public static void Pictures_VirtualAlbum_DeleteAlbum(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer VirtualAlbumID,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("VirtualAlbumID", String.valueOf(VirtualAlbumID)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Pictures_VirtualAlbum_DeleteAlbum", params, state, callback);
    }

    public static void Pictures_VirtualAlbum_Get(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer VirtualPhotoAlbumID,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("VirtualPhotoAlbumID", String
                .valueOf(VirtualPhotoAlbumID)));

        MakeRequest("Pictures_VirtualAlbum_Get", params, state, callback);
    }

    public static void Pictures_VirtualAlbum_GetAlbumInformation(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer VirtualAlbumID,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("VirtualAlbumID", String.valueOf(VirtualAlbumID)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Pictures_VirtualAlbum_GetAlbumInformation", params, state, callback);
    }

    public static void Pictures_VirtualAlbum_GetMyAlbums(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Pictures_VirtualAlbum_GetMyAlbums", params, state, callback);
    }

    public static void Pictures_VirtualAlbum_RemovePhoto(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer VirtualAlbumID,
            Integer ExistingPhotoID, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("VirtualAlbumID", String.valueOf(VirtualAlbumID)));
        params.add(new BasicNameValuePair("ExistingPhotoID", String.valueOf(ExistingPhotoID)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Pictures_VirtualAlbum_RemovePhoto", params, state, callback);
    }

    public static void Pictures_VirtualAlbum_Update(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer VirtualPhotoAlbumID,
            String NewAlbumName, String NewAppTag, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("VirtualAlbumID", String
                .valueOf(VirtualPhotoAlbumID)));
        params.add(new BasicNameValuePair("NewAlbumName", NewAlbumName));
        params.add(new BasicNameValuePair("NewAppTag", NewAppTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Pictures_VirtualAlbum_Update", params, state, callback);
    }

    public static void Pictures_VirtualAlbum_UpdatePhoto(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer ExistingPhotoID,
            String NewPhotoComment, String NewAppTag, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("ExistingPhotoID", String.valueOf(ExistingPhotoID)));
        params.add(new BasicNameValuePair("NewPhotoComment", NewPhotoComment));
        params.add(new BasicNameValuePair("NewAppTag", NewAppTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Pictures_VirtualAlbum_UpdatePhoto", params, state, callback);
    }

    public static void GeoLocation_Category_GetList(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));

        MakeRequest("GeoLocation_Category_GetList", params, state, callback);
    }

    public static void GeoLocation_Category_Submit(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String NewCategoryName,
            String Comment, String ApplicationTag, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("NewCategoryName", NewCategoryName));
        params.add(new BasicNameValuePair("Comment", Comment));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GeoLocation_Category_Submit", params, state, callback);
    }

    public static void GeoLocation_Location_Add(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Float Latitude, Float Longitude,
            String LocationName, String Address1, String Address2, String LocationCity,
            String LocationState, String LocationZipPostal, Integer CategoryID, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("LocationName", LocationName));
        params.add(new BasicNameValuePair("Address1", Address1));
        params.add(new BasicNameValuePair("Address2", Address2));
        params.add(new BasicNameValuePair("LocationCity", LocationCity));
        params.add(new BasicNameValuePair("LocationState", LocationState));
        params.add(new BasicNameValuePair("LocationZipPostal", LocationZipPostal));
        params.add(new BasicNameValuePair("CategoryID", String.valueOf(CategoryID)));

        MakeRequest("GeoLocation_Location_Add", params, state, callback);
    }

    public static void GeoLocation_Location_Edit(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer ExistingGeoRecordID,
            Float NewLatitude, Float NewLongitude, String NewLocationName, String NewAddress,
            String NewLocationCity, String NewLocationState, String NewLocationZipPostal,
            Integer NewCategoryID, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("ExistingGeoRecordID", String
                .valueOf(ExistingGeoRecordID)));
        params.add(new BasicNameValuePair("NewLatitude", String.valueOf(NewLatitude)));
        params.add(new BasicNameValuePair("NewLongitude", String.valueOf(NewLongitude)));
        params.add(new BasicNameValuePair("NewLocationName", NewLocationName));
        params.add(new BasicNameValuePair("NewAddress", NewAddress));
        params.add(new BasicNameValuePair("NewLocationCity", NewLocationCity));
        params.add(new BasicNameValuePair("NewLocationState", NewLocationState));
        params.add(new BasicNameValuePair("NewLocationZipPostal", NewLocationZipPostal));
        params.add(new BasicNameValuePair("NewCategoryID", String.valueOf(NewCategoryID)));

        MakeRequest("GeoLocation_Location_Edit", params, state, callback);
    }

    public static void GeoLocation_Location_Flag(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer ExistingGeoRecordID,
            String FlagReason, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("ExistingGeoRecordID", String
                .valueOf(ExistingGeoRecordID)));
        params.add(new BasicNameValuePair("FlagReason", FlagReason));

        MakeRequest("GeoLocation_Location_Flag", params, state, callback);
    }

    public static void GeoLocation_Location_GetFromID(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer ExistingGeoID,
            Float Latitude, Float Longitude, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("ExistingGeoID", String.valueOf(ExistingGeoID)));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GeoLocation_Location_GetFromID", params, state, callback);
    }

    public static void GeoLocation_Location_Search(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer SearchDistance,
            Float Latitude, Float Longitude, Integer RecordLimit, String SearchName,
            String SearchCategoryID, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("SearchDistance", String.valueOf(SearchDistance)));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));
        params.add(new BasicNameValuePair("SearchName", SearchName));
        params.add(new BasicNameValuePair("SearchCategoryID", SearchCategoryID));

        MakeRequest("GeoLocation_Location_Search", params, state, callback);
    }

    public static void GeoLocation_Location_SetTag(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer ExistingGeoID,
            String ApplicationTag, String UserTag, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("ExistingGeoID", String.valueOf(ExistingGeoID)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("UserTag", UserTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GeoLocation_Location_SetTag", params, state, callback);
    }

    public static void PushNotifications_Android_GetGroupNames(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));

        MakeRequest("PushNotifications_Android_GetGroupNames", params, state, callback);
    }

    public static void PushNotifications_Android_GetRegisteredDevices(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String GroupName, Integer PageSize,
            Integer CurrentPageNumber, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("GroupName", GroupName));
        params.add(new BasicNameValuePair("PageSize", String.valueOf(PageSize)));
        params.add(new BasicNameValuePair("CurrentPageNumber", String.valueOf(CurrentPageNumber)));

        MakeRequest("PushNotifications_Android_GetRegisteredDevices", params, state, callback);
    }

    public static void PushNotifications_Android_RegisterDevice(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String GroupName,
            String RegistrationID, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("GroupName", GroupName));
        params.add(new BasicNameValuePair("RegistrationID", RegistrationID));

        MakeRequest("PushNotifications_Android_RegisterDevice", params, state, callback);
    }

    public static void PushNotifications_Android_RemoveDevice(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));

        MakeRequest("PushNotifications_Android_RemoveDevice", params, state, callback);
    }

    public static void PushNotifications_Android_SendRawMessage(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String RawMessage,
            String DeliverAfter, String GroupName, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("RawMessage", RawMessage));
        params.add(new BasicNameValuePair("DeliverAfter", DeliverAfter));
        params.add(new BasicNameValuePair("GroupName", GroupName));

        MakeRequest("PushNotifications_Android_SendRawMessage", params, state, callback);
    }

    public static void PushNotifications_WP_GetGroupNames(String BuddyApplicationName,
            String BuddyApplicationPassword, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));

        MakeRequest("PushNotifications_WP_GetGroupNames", params, state, callback);
    }

    public static void PushNotifications_WP_GetRegisteredDevices(String BuddyApplicationName,
            String BuddyApplicationPassword, String GroupName, Integer PageSize,
            Integer CurrentPageNumber, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("GroupName", GroupName));
        params.add(new BasicNameValuePair("PageSize", String.valueOf(PageSize)));
        params.add(new BasicNameValuePair("CurrentPageNumber", String.valueOf(CurrentPageNumber)));

        MakeRequest("PushNotifications_WP_GetRegisteredDevices", params, state, callback);
    }

    public static void PushNotifications_WP_RegisterDevice(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String DeviceURI, String GroupName,
            Boolean EnableTileMessages, Boolean EnableRawMessages, Boolean EnableToastMessages,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("DeviceURI", DeviceURI));
        params.add(new BasicNameValuePair("GroupName", GroupName));
        params.add(new BasicNameValuePair("EnableTileMessages", String.valueOf(EnableTileMessages)));
        params.add(new BasicNameValuePair("EnableRawMessages", String.valueOf(EnableRawMessages)));
        params.add(new BasicNameValuePair("EnableToastMessages", String
                .valueOf(EnableToastMessages)));

        MakeRequest("PushNotifications_WP_RegisterDevice", params, state, callback);
    }

    public static void PushNotifications_WP_RemoveDevice(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));

        MakeRequest("PushNotifications_WP_RemoveDevice", params, state, callback);
    }

    public static void PushNotifications_WP_SendLiveTile(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String ImageURI,
            Integer MessageCount, String DeliverAfter, String GroupName, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("ImageURI", ImageURI));
        params.add(new BasicNameValuePair("MessageCount", String.valueOf(MessageCount)));
        params.add(new BasicNameValuePair("DeliverAfter", DeliverAfter));
        params.add(new BasicNameValuePair("GroupName", GroupName));

        MakeRequest("PushNotifications_WP_SendLiveTile", params, state, callback);
    }

    public static void PushNotifications_WP_SendRawMessage(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String RawMessage,
            String DeliverAfter, String GroupName, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("RawMessage", RawMessage));
        params.add(new BasicNameValuePair("DeliverAfter", DeliverAfter));
        params.add(new BasicNameValuePair("GroupName", GroupName));

        MakeRequest("PushNotifications_WP_SendRawMessage", params, state, callback);
    }

    public static void PushNotifications_WP_SendToastMessage(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String ToastTitle,
            String ToastSubTitle, String ToastParameter, String DeliverAfter, String GroupName,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("ToastTitle", ToastTitle));
        params.add(new BasicNameValuePair("ToastSubTitle", ToastSubTitle));
        params.add(new BasicNameValuePair("ToastParameter", ToastParameter));
        params.add(new BasicNameValuePair("DeliverAfter", DeliverAfter));
        params.add(new BasicNameValuePair("GroupName", GroupName));

        MakeRequest("PushNotifications_WP_SendToastMessage", params, state, callback);
    }

    public static void PushNotifications_Apple_RegisterApplicationCertificate(
            String BuddyApplicationName, String BuddyApplicationPassword,
            String P12CertificateBytes, String CertificatePassword, Integer ProductionBit,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("P12CertificateBytes", P12CertificateBytes));
        params.add(new BasicNameValuePair("CertificatePassword", CertificatePassword));
        params.add(new BasicNameValuePair("ProductionBit", String.valueOf(ProductionBit)));

        MakeRequest("PushNotifications_Apple_RegisterApplicationCertificate", params, state,
                callback);
    }

    public static void PushNotifications_Apple_RegisterDevice(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String GroupName,
            String AppleDeviceToken, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("GroupName", GroupName));
        params.add(new BasicNameValuePair("AppleDeviceToken", AppleDeviceToken));

        MakeRequest("PushNotifications_Apple_RegisterDevice", params, state, callback);
    }

    public static void PushNotifications_Apple_RemoveDevice(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));

        MakeRequest("PushNotifications_Apple_RemoveDevice", params, state, callback);
    }

    public static void PushNotifications_Apple_SendRawMessage(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String DeliverAfter,
            String GroupName, String AppleMessage, String AppleBadge, String AppleSound,
            String CustomItems, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("DeliverAfter", DeliverAfter));
        params.add(new BasicNameValuePair("GroupName", GroupName));
        params.add(new BasicNameValuePair("AppleMessage", AppleMessage));
        params.add(new BasicNameValuePair("AppleBadge", AppleBadge));
        params.add(new BasicNameValuePair("AppleSound", AppleSound));
        params.add(new BasicNameValuePair("CustomItems", CustomItems));

        MakeRequest("PushNotifications_Apple_SendRawMessage", params, state, callback);
    }

    public static void PushNotifications_Apple_GetRegisteredDevices(String BuddyApplicationName,
            String BuddyApplicationPassword, String GroupName, Integer PageSize,
            Integer CurrentPageNumber, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("GroupName", GroupName));
        params.add(new BasicNameValuePair("PageSize", String.valueOf(PageSize)));
        params.add(new BasicNameValuePair("CurrentPageNumber", String.valueOf(CurrentPageNumber)));

        MakeRequest("PushNotifications_Apple_GetRegisteredDevices", params, state, callback);
    }

    public static void PushNotifications_Apple_GetGroupNames(String BuddyApplicationName,
            String BuddyApplicationPassword, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));

        MakeRequest("PushNotifications_Apple_GetGroupNames", params, state, callback);
    }

    public static void PushNotifications_Win8_RegisterDevice(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String DeviceURI, String ClientID,
            String ClientSecret, String GroupName, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("DeviceURI", DeviceURI));
        params.add(new BasicNameValuePair("ClientID", ClientID));
        params.add(new BasicNameValuePair("ClientSecret", ClientSecret));
        params.add(new BasicNameValuePair("GroupName", GroupName));

        MakeRequest("PushNotifications_Win8_RegisterDevice", params, state, callback);
    }

    public static void PushNotifications_Win8_RemoveDevice(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));

        MakeRequest("PushNotifications_Win8_RemoveDevice", params, state, callback);
    }

    public static void PushNotifications_Win8_GetRegisteredDevices(String BuddyApplicationName,
            String BuddyApplicationPassword, String GroupName, Integer PageSize,
            Integer CurrentPageNumber, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("GroupName", GroupName));
        params.add(new BasicNameValuePair("PageSize", String.valueOf(PageSize)));
        params.add(new BasicNameValuePair("CurrentPageNumber", String.valueOf(CurrentPageNumber)));

        MakeRequest("PushNotifications_Win8_GetRegisteredDevices", params, state, callback);
    }

    public static void PushNotifications_Win8_GetGroupNames(String BuddyApplicationName,
            String BuddyApplicationPassword, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));

        MakeRequest("PushNotifications_Win8_GetGroupNames", params, state, callback);
    }

    public static void PushNotifications_Win8_SendLiveTile(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String XMLMessagePayload,
            String DeliverAfter, String GroupName, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("XMLMessagePayload", XMLMessagePayload));
        params.add(new BasicNameValuePair("DeliverAfter", DeliverAfter));
        params.add(new BasicNameValuePair("GroupName", GroupName));

        MakeRequest("PushNotifications_Win8_SendLiveTile", params, state, callback);
    }

    public static void PushNotifications_Win8_SendBadge(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String XMLMessagePayload,
            String DeliverAfter, String GroupName, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("XMLMessagePayload", XMLMessagePayload));
        params.add(new BasicNameValuePair("DeliverAfter", DeliverAfter));
        params.add(new BasicNameValuePair("GroupName", GroupName));

        MakeRequest("PushNotifications_Win8_SendBadge", params, state, callback);
    }

    public static void PushNotifications_Win8_SendToast(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String XMLMessagePayload,
            String DeliverAfter, String GroupName, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("XMLMessagePayload", XMLMessagePayload));
        params.add(new BasicNameValuePair("DeliverAfter", DeliverAfter));
        params.add(new BasicNameValuePair("GroupName", GroupName));

        MakeRequest("PushNotifications_Win8_SendToast", params, state, callback);
    }

    public static void Messages_SentMessages_Get(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String FromDateTime, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("FromDateTime", FromDateTime));

        MakeRequest("Messages_SentMessages_Get", params, state, callback);
    }

    public static void Messages_Messages_Get(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String FromDateTime, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("FromDateTime", FromDateTime));

        MakeRequest("Messages_Messages_Get", params, state, callback);
    }

    public static void Messages_Message_Send(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String MessageString,
            Integer ToUserID, String ApplicationTag, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("MessageString", MessageString));
        params.add(new BasicNameValuePair("ToUserID", String.valueOf(ToUserID)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Messages_Message_Send", params, state, callback);
    }

    public static void GroupMessages_Message_Send(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer GroupChatID,
            String MessageContent, Float Latitude, Float Longitude, String ApplicationTag,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("GroupChatID", String.valueOf(GroupChatID)));
        params.add(new BasicNameValuePair("MessageContent", MessageContent));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GroupMessages_Message_Send", params, state, callback);
    }

    public static void GroupMessages_Message_Get(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer GroupChatID,
            String FromDateTime, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("GroupChatID", String.valueOf(GroupChatID)));
        params.add(new BasicNameValuePair("FromDateTime", FromDateTime));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GroupMessages_Message_Get", params, state, callback);
    }

    public static void GroupMessages_Membership_RemoveUser(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer UserProfileIDToRemove,
            Integer GroupChatID, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserProfileIDToRemove", String
                .valueOf(UserProfileIDToRemove)));
        params.add(new BasicNameValuePair("GroupChatID", String.valueOf(GroupChatID)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GroupMessages_Membership_RemoveUser", params, state, callback);
    }

    public static void GroupMessages_Membership_JoinGroup(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer GroupChatID,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("GroupChatID", String.valueOf(GroupChatID)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GroupMessages_Membership_JoinGroup", params, state, callback);
    }

    public static void GroupMessages_Membership_GetMyList(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GroupMessages_Membership_GetMyList", params, state, callback);
    }

    public static void GroupMessages_Membership_GetAllGroups(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GroupMessages_Membership_GetAllGroups", params, state, callback);
    }

    public static void GroupMessages_Membership_DepartGroup(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer GroupChatID,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("GroupChatID", String.valueOf(GroupChatID)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GroupMessages_Membership_DepartGroup", params, state, callback);
    }

    public static void GroupMessages_Membership_AddNewMember(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer GroupChatID,
            Integer UserProfileToAdd, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("GroupChatID", String.valueOf(GroupChatID)));
        params.add(new BasicNameValuePair("UserProfileToAdd", String.valueOf(UserProfileToAdd)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GroupMessages_Membership_AddNewMember", params, state, callback);
    }

    public static void GroupMessages_Manage_DeleteGroup(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer GroupChatID,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("GroupChatID", String.valueOf(GroupChatID)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GroupMessages_Manage_DeleteGroup", params, state, callback);
    }

    public static void GroupMessages_Manage_CreateGroup(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String GroupName,
            String GroupSecurity, String ApplicationTag, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("GroupName", GroupName));
        params.add(new BasicNameValuePair("GroupSecurity", GroupSecurity));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GroupMessages_Manage_CreateGroup", params, state, callback);
    }

    public static void GroupMessages_Manage_CheckForGroup(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String GroupName, String RESERVED,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("GroupName", GroupName));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("GroupMessages_Manage_CheckForGroup", params, state, callback);
    }

    public static void Friends_Block_Add(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer UserProfileToBlock,
            String ApplicationTag, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserProfileToBlock", String.valueOf(UserProfileToBlock)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Friends_Block_Add", params, state, callback);
    }

    public static void Friends_Block_GetList(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));

        MakeRequest("Friends_Block_GetList", params, state, callback);
    }

    public static void Friends_Block_Remove(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer BlockedProfileID,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("BlockedProfileID", String.valueOf(BlockedProfileID)));

        MakeRequest("Friends_Block_Remove", params, state, callback);
    }

    public static void Friends_FriendRequest_Accept(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer FriendProfileID,
            String ApplicationTag, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("FriendProfileID", String.valueOf(FriendProfileID)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Friends_FriendRequest_Accept", params, state, callback);
    }

    public static void Friends_FriendRequest_Add(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer FriendProfileID,
            String ApplicationTag, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("FriendProfileID", String.valueOf(FriendProfileID)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Friends_FriendRequest_Add", params, state, callback);
    }

    public static void Friends_FriendRequest_Deny(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer FriendProfileID,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("FriendProfileID", String.valueOf(FriendProfileID)));

        MakeRequest("Friends_FriendRequest_Deny", params, state, callback);
    }

    public static void Friends_FriendRequest_Get(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String FromDateTime, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("FromDateTime", FromDateTime));

        MakeRequest("Friends_FriendRequest_Get", params, state, callback);
    }

    public static void Friends_FriendRequest_GetSentRequests(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String FromDateTime, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("FromDateTime", FromDateTime));

        MakeRequest("Friends_FriendRequest_GetSentRequests", params, state, callback);
    }

    public static void Friends_Friends_GetList(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String FromDateTime, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("FromDateTime", FromDateTime));

        MakeRequest("Friends_Friends_GetList", params, state, callback);
    }

    public static void Friends_Friends_Remove(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer FriendProfileID,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("FriendProfileID", String.valueOf(FriendProfileID)));

        MakeRequest("Friends_Friends_Remove", params, state, callback);
    }

    public static void Friends_Friends_Search(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer TimeFilter,
            Integer SearchDistance, Float Latitude, Float Longitude, Integer RecordLimit,
            Integer PageSize, Integer PageNumber, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("TimeFilter", String.valueOf(TimeFilter)));
        params.add(new BasicNameValuePair("SearchDistance", String.valueOf(SearchDistance)));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));
        params.add(new BasicNameValuePair("PageSize", String.valueOf(PageSize)));
        params.add(new BasicNameValuePair("PageNumber", String.valueOf(PageNumber)));

        MakeRequest("Friends_Friends_Search", params, state, callback);
    }

    public static void Game_Score_Add(String BuddyApplicationName, String BuddyApplicationPassword,
            String UserTokenOrID, double ScoreLatitude, double ScoreLongitude, String ScoreRank,
            Float ScoreValue, String ScoreBoardName, String ApplicationTag,
            Integer OneScorePerPlayerBit, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("ScoreLatitude", String.valueOf(ScoreLatitude)));
        params.add(new BasicNameValuePair("ScoreLongitude", String.valueOf(ScoreLongitude)));
        params.add(new BasicNameValuePair("ScoreRank", ScoreRank));
        params.add(new BasicNameValuePair("ScoreValue", String.valueOf(ScoreValue)));
        params.add(new BasicNameValuePair("ScoreBoardName", ScoreBoardName));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("OneScorePerPlayerBit", String
                .valueOf(OneScorePerPlayerBit)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_Score_Add", params, state, callback);
    }

    public static void Game_Score_DeleteAllScoresForUser(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_Score_DeleteAllScoresForUser", params, state, callback);
    }

    public static void Game_Score_GetBoardHighScores(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String ScoreBoardName,
            Integer RecordLimit, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("ScoreBoardName", ScoreBoardName));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_Score_GetBoardHighScores", params, state, callback);
    }

    public static void Game_Score_GetBoardLowScores(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String ScoreBoardName,
            Integer RecordLimit, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("ScoreBoardName", ScoreBoardName));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_Score_GetBoardLowScores", params, state, callback);
    }

    public static void Game_Score_GetScoresForUser(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, Integer RecordLimit,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_Score_GetScoresForUser", params, state, callback);
    }

    public static void Game_Score_SearchScores(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, Integer SearchDistance,
            double SearchLatitude, double SearchLongitude, Integer RecordLimit, String SearchBoard,
            Integer TimeFilter, Integer MinimumScore, String AppTag, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("SearchDistance", String.valueOf(SearchDistance)));
        params.add(new BasicNameValuePair("SearchLatitude", String.valueOf(SearchLatitude)));
        params.add(new BasicNameValuePair("SearchLongitude", String.valueOf(SearchLongitude)));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));
        params.add(new BasicNameValuePair("SearchBoard", SearchBoard));
        params.add(new BasicNameValuePair("TimeFilter", String.valueOf(TimeFilter)));
        params.add(new BasicNameValuePair("MinimumScore", String.valueOf(MinimumScore)));
        params.add(new BasicNameValuePair("AppTag", AppTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_Score_SearchScores", params, state, callback);
    }

    public static void Game_Player_Add(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String PlayerName,
            Float PlayerLatitude, Float PlayerLongitude, String PlayerRank, String PlayerBoardName,
            String ApplicationTag, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("PlayerName", PlayerName));
        params.add(new BasicNameValuePair("PlayerLatitude", String.valueOf(PlayerLatitude)));
        params.add(new BasicNameValuePair("PlayerLongitude", String.valueOf(PlayerLongitude)));
        params.add(new BasicNameValuePair("PlayerRank", PlayerRank));
        params.add(new BasicNameValuePair("PlayerBoardName", PlayerBoardName));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_Player_Add", params, state, callback);
    }

    public static void Game_Player_Update(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String PlayerName,
            Float PlayerLatitude, Float PlayerLongitude, String PlayerRank, String PlayerBoardName,
            String ApplicationTag, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("PlayerName", PlayerName));
        params.add(new BasicNameValuePair("PlayerLatitude", String.valueOf(PlayerLatitude)));
        params.add(new BasicNameValuePair("PlayerLongitude", String.valueOf(PlayerLongitude)));
        params.add(new BasicNameValuePair("PlayerRank", PlayerRank));
        params.add(new BasicNameValuePair("PlayerBoardName", PlayerBoardName));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_Player_Update", params, state, callback);
    }

    public static void Game_Player_Delete(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_Player_Delete", params, state, callback);
    }

    public static void Game_Player_GetPlayerInfo(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_Player_GetPlayerInfo", params, state, callback);
    }

    public static void Game_Player_SearchPlayers(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, Integer SearchDistance,
            Float SearchLatitude, Float SearchLongitude, Integer RecordLimit, String SearchBoard,
            Integer TimeFilter, Integer MinimumScore, String ApplicationTag, String PlayerRank,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("PlayerRank", PlayerRank));
        params.add(new BasicNameValuePair("SearchDistance", String.valueOf(SearchDistance)));
        params.add(new BasicNameValuePair("SearchLatitude", String.valueOf(SearchLatitude)));
        params.add(new BasicNameValuePair("SearchLongitude", String.valueOf(SearchLongitude)));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));
        params.add(new BasicNameValuePair("SearchBoard", SearchBoard));
        params.add(new BasicNameValuePair("TimeFilter", String.valueOf(TimeFilter)));
        params.add(new BasicNameValuePair("MinimumScore", String.valueOf(MinimumScore)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_Player_SearchPlayers", params, state, callback);
    }

    public static void Game_State_GetAll(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_State_GetAll", params, state, callback);
    }

    public static void Game_State_Get(String BuddyApplicationName, String BuddyApplicationPassword,
            String UserTokenOrID, String GameStateKey, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("GameStateKey", GameStateKey));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_State_Get", params, state, callback);
    }

    public static void Game_State_Remove(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String GameStateKey,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("GameStateKey", GameStateKey));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_State_Remove", params, state, callback);
    }

    public static void Game_State_Update(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserTokenOrID, String GameStateKey,
            String GameStateValue, String ApplicationTag, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("GameStateKey", GameStateKey));
        params.add(new BasicNameValuePair("GameStateValue", GameStateValue));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_State_Update", params, state, callback);
    }

    public static void Game_State_Add(String BuddyApplicationName, String BuddyApplicationPassword,
            String UserTokenOrID, String GameStateKey, String GameStateValue,
            String ApplicationTag, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserTokenOrID", UserTokenOrID));
        params.add(new BasicNameValuePair("GameStateKey", GameStateKey));
        params.add(new BasicNameValuePair("GameStateValue", GameStateValue));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Game_State_Add", params, state, callback);
    }

    public static void Analytics_CrashRecords_Add(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String AppVersion,
            String DeviceOSVersion, String DeviceType, String MethodName, String StackTrace,
            String Metadata, Float Latitude, Float Longitude, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("AppVersion", AppVersion));
        params.add(new BasicNameValuePair("DeviceOSVersion", DeviceOSVersion));
        params.add(new BasicNameValuePair("DeviceType", DeviceType));
        params.add(new BasicNameValuePair("MethodName", MethodName));
        params.add(new BasicNameValuePair("StackTrace", StackTrace));
        params.add(new BasicNameValuePair("MetaData", Metadata));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));

        MakeRequest("Analytics_CrashRecords_Add", params, state, callback);
    }

    public static void Analytics_DeviceInformation_Add(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String DeviceOSVersion,
            String DeviceType, Float Latitude, Float Longitude, String AppName, String AppVersion,
            String Metadata, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("DeviceOSVersion", DeviceOSVersion));
        params.add(new BasicNameValuePair("DeviceType", DeviceType));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("AppName", AppName));
        params.add(new BasicNameValuePair("AppVersion", AppVersion));
        params.add(new BasicNameValuePair("MetaData", Metadata));

        MakeRequest("Analytics_DeviceInformation_Add", params, state, callback);
    }

    public static void Analytics_Session_Start(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String SessionName,
            String StartAppTag, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("SessionName", SessionName));
        params.add(new BasicNameValuePair("StartAppTag", StartAppTag));

        MakeRequest("Analytics_Session_Start", params, state, callback);
    }

    public static void Analytics_Session_End(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String SessionID, String EndAppTag,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("SessionID", SessionID));
        params.add(new BasicNameValuePair("EndAppTag", EndAppTag));

        MakeRequest("Analytics_Session_End", params, state, callback);
    }

    public static void Analytics_Session_RecordMetric(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String SessionID, String MetricKey,
            String MetricValue, String AppTag, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("SessionID", SessionID));
        params.add(new BasicNameValuePair("MetricKey", MetricKey));
        params.add(new BasicNameValuePair("MetricValue", MetricValue));
        params.add(new BasicNameValuePair("AppTag", AppTag));

        MakeRequest("Analytics_Session_RecordMetric", params, state, callback);
    }

    public static void Metadata_UserMetadataValue_BatchSum(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String UserMetaKeyCollection,
            String SearchDistanceCollection, Float Latitude, Float Longitude, String TimeFilter,
            String ApplicationTag, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserMetaKeyCollection", UserMetaKeyCollection));
        params.add(new BasicNameValuePair("SearchDistanceCollection", SearchDistanceCollection));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("TimeFilter", TimeFilter));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("MetaData_UserMetaDataValue_BatchSum", params, state, callback);
    }

    public static void Metadata_UserMetadataValue_Delete(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String MetaKey, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("MetaKey", MetaKey));

        MakeRequest("MetaData_UserMetaDataValue_Delete", params, state, callback);
    }

    public static void Metadata_UserMetadataValue_DeleteAll(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));

        MakeRequest("MetaData_UserMetaDataValue_DeleteAll", params, state, callback);
    }

    public static void Metadata_UserMetadataValue_Get(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String MetaKey, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("MetaKey", MetaKey));

        MakeRequest("MetaData_UserMetaDataValue_Get", params, state, callback);
    }

    public static void Metadata_UserMetadataValue_GetAll(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));

        MakeRequest("MetaData_UserMetaDataValue_GetAll", params, state, callback);
    }

    public static void Metadata_UserMetadataValue_Search(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, Integer SearchDistance,
            Float Latitude, Float Longitude, Integer RecordLimit, String MetaKeySearch,
            String MetaValueSearch, String TimeFilter, Integer SortValueAsFloat,
            Integer SortDirection, String DisableCache, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("SearchDistance", String.valueOf(SearchDistance)));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));
        params.add(new BasicNameValuePair("MetaKeySearch", MetaKeySearch));
        params.add(new BasicNameValuePair("MetaValueSearch", MetaValueSearch));
        params.add(new BasicNameValuePair("TimeFilter", TimeFilter));
        params.add(new BasicNameValuePair("SortValueAsFloat", String.valueOf(SortValueAsFloat)));
        params.add(new BasicNameValuePair("SortDirection", String.valueOf(SortDirection)));
        params.add(new BasicNameValuePair("DisableCache", DisableCache));

        MakeRequest("MetaData_UserMetaDataValue_Search", params, state, callback);
    }

    public static void Metadata_UserMetadataValue_Set(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String MetaKey, String MetaValue,
            Float MetaLatitude, Float MetaLongitude, String ApplicationTag, String RESERVED,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("MetaKey", MetaKey));
        params.add(new BasicNameValuePair("MetaValue", MetaValue));
        params.add(new BasicNameValuePair("MetaLatitude", String.valueOf(MetaLatitude)));
        params.add(new BasicNameValuePair("MetaLongitude", String.valueOf(MetaLongitude)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("MetaData_UserMetaDataValue_Set", params, state, callback);
    }

    public static void Metadata_UserMetadataValue_Sum(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String MetaKey,
            Integer SearchDistance, Float Latitude, Float Longitude, String TimeFilter,
            String ApplicationTag, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("MetaKey", MetaKey));
        params.add(new BasicNameValuePair("SearchDistance", String.valueOf(SearchDistance)));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("TimeFilter", TimeFilter));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("MetaData_UserMetaDataValue_Sum", params, state, callback);
    }

    public static void Metadata_UserMetadataValue_BatchSet(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String MetaKeys, String Values,
            double Latitude, double Longitude, String ApplicationTag, String RESERVED,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("UserMetaKeyCollection", MetaKeys));
        params.add(new BasicNameValuePair("UserMetaValueCollection", Values));
        params.add(new BasicNameValuePair("MetaLatitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("MetaLongitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("MetaData_UserMetaDataValue_BatchSet", params, state, callback);
    }

    public static void Metadata_ApplicationMetadataCounter_Decrement(String BuddyApplicationName,
            String BuddyApplicationPassword, String SocketMetaKey, String DecrementValueAmount,
            Float MetaLatitude, Float MetaLongitude, String ApplicationTag, String RESERVED,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("SocketMetaKey", SocketMetaKey));
        params.add(new BasicNameValuePair("DecrementValueAmount", DecrementValueAmount));
        params.add(new BasicNameValuePair("MetaLatitude", String.valueOf(MetaLatitude)));
        params.add(new BasicNameValuePair("MetaLongitude", String.valueOf(MetaLongitude)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("MetaData_ApplicationMetaDataCounter_Decrement", params, state, callback);
    }

    public static void Metadata_ApplicationMetadataCounter_Increment(String BuddyApplicationName,
            String BuddyApplicationPassword, String SocketMetaKey, String IncrementValueAmount,
            Float MetaLatitude, Float MetaLongitude, String ApplicationTag, String RESERVED,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("SocketMetaKey", SocketMetaKey));
        params.add(new BasicNameValuePair("IncrementValueAmount", IncrementValueAmount));
        params.add(new BasicNameValuePair("MetaLatitude", String.valueOf(MetaLatitude)));
        params.add(new BasicNameValuePair("MetaLongitude", String.valueOf(MetaLongitude)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("MetaData_ApplicationMetaDataCounter_Increment", params, state, callback);
    }

    public static void Metadata_ApplicationMetadataValue_BatchSum(String BuddyApplicationName,
            String BuddyApplicationPassword, String ApplicationMetaKeyCollection,
            String SearchDistanceCollection, Float Latitude, Float Longitude, Integer TimeFilter,
            String ApplicationTag, String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("ApplicationMetaKeyCollection",
                ApplicationMetaKeyCollection));
        params.add(new BasicNameValuePair("SearchDistanceCollection", SearchDistanceCollection));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("TimeFilter", String.valueOf(TimeFilter)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("MetaData_ApplicationMetaDataValue_BatchSum", params, state, callback);
    }

    public static void Metadata_ApplicationMetadataValue_Delete(String BuddyApplicationName,
            String BuddyApplicationPassword, String SocketMetaKey, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("SocketMetaKey", SocketMetaKey));

        MakeRequest("MetaData_ApplicationMetaDataValue_Delete", params, state, callback);
    }

    public static void Metadata_ApplicationMetadataValue_DeleteAll(String BuddyApplicationName,
            String BuddyApplicationPassword, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));

        MakeRequest("MetaData_ApplicationMetaDataValue_DeleteAll", params, state, callback);
    }

    public static void Metadata_ApplicationMetadataValue_Get(String BuddyApplicationName,
            String BuddyApplicationPassword, String SocketMetaKey, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("SocketMetaKey", SocketMetaKey));

        MakeRequest("MetaData_ApplicationMetaDataValue_Get", params, state, callback);
    }

    public static void Metadata_ApplicationMetadataValue_GetAll(String BuddyApplicationName,
            String BuddyApplicationPassword, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));

        MakeRequest("MetaData_ApplicationMetaDataValue_GetAll", params, state, callback);
    }

    public static void Metadata_ApplicationMetadataValue_SearchData(String BuddyApplicationName,
            String BuddyApplicationPassword, String SearchDistance, Float Latitude,
            Float Longitude, Integer RecordLimit, String MetaKeySearch, String MetaValueSearch,
            String TimeFilter, Number MetaValueMin, Number MetaValueMax, Integer SearchAsFloat,
            String SortResultsDirection, String DisableCache, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("SearchDistance", SearchDistance));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));
        params.add(new BasicNameValuePair("MetaKeySearch", MetaKeySearch));
        params.add(new BasicNameValuePair("MetaValueSearch", MetaValueSearch));
        params.add(new BasicNameValuePair("TimeFilter", TimeFilter));
        params.add(new BasicNameValuePair("MetaValueMin", String.valueOf(MetaValueMin)));
        params.add(new BasicNameValuePair("MetaValueMax", String.valueOf(MetaValueMax)));
        params.add(new BasicNameValuePair("SearchAsFloat", String.valueOf(SearchAsFloat)));
        params.add(new BasicNameValuePair("SortResultsDirection", SortResultsDirection));
        params.add(new BasicNameValuePair("DisableCache", DisableCache));

        MakeRequest("MetaData_ApplicationMetaDataValue_SearchData", params, state, callback);
    }

    public static void Metadata_ApplicationMetadataValue_SearchNearby(String BuddyApplicationName,
            String BuddyApplicationPassword, String SearchDistance, Float Latitude,
            Float Longitude, Integer RecordLimit, String MetaKeySearch, String MetaValueSearch,
            String TimeFilter, Number MetaValueMin, Number MetaValueMax, Integer SearchAsFloat,
            String SortResultsDirection, String DisableCache, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("SearchDistance", SearchDistance));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));
        params.add(new BasicNameValuePair("MetaKeySearch", MetaKeySearch));
        params.add(new BasicNameValuePair("MetaValueSearch", MetaValueSearch));
        params.add(new BasicNameValuePair("TimeFilter", TimeFilter));
        params.add(new BasicNameValuePair("MetaValueMin", String.valueOf(MetaValueMin)));
        params.add(new BasicNameValuePair("MetaValueMax", String.valueOf(MetaValueMax)));
        params.add(new BasicNameValuePair("SearchAsFloat", String.valueOf(SearchAsFloat)));
        params.add(new BasicNameValuePair("SortResultsDirection", SortResultsDirection));
        params.add(new BasicNameValuePair("DisableCache", DisableCache));

        MakeRequest("MetaData_ApplicationMetaDataValue_SearchNearby", params, state, callback);
    }

    public static void Metadata_ApplicationMetadataValue_Set(String BuddyApplicationName,
            String BuddyApplicationPassword, String SocketMetaKey, String SocketMetaValue,
            Float MetaLatitude, Float MetaLongitude, String ApplicationTag, String RESERVED,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("SocketMetaKey", SocketMetaKey));
        params.add(new BasicNameValuePair("SocketMetaValue", SocketMetaValue));
        params.add(new BasicNameValuePair("MetaLatitude", String.valueOf(MetaLatitude)));
        params.add(new BasicNameValuePair("MetaLongitude", String.valueOf(MetaLongitude)));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("MetaData_ApplicationMetaDataValue_Set", params, state, callback);
    }

    public static void Metadata_ApplicationMetadataValue_Sum(String BuddyApplicationName,
            String BuddyApplicationPassword, String SocketMetaKey, String SearchDistance,
            Float Latitude, Float Longitude, String TimeFilter, String ApplicationTag,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("SocketMetaKey", SocketMetaKey));
        params.add(new BasicNameValuePair("SearchDistance", SearchDistance));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("TimeFilter", TimeFilter));
        params.add(new BasicNameValuePair("ApplicationTag", ApplicationTag));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("MetaData_ApplicationMetaDataValue_Sum", params, state, callback);
    }

    public static void Service_DateTime_Get(String BuddyApplicationName,
            String BuddyApplicationPassword, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));

        MakeRequest("Service_DateTime_Get", params, state, callback);
    }

    public static void Service_Ping_Get(String BuddyApplicationName,
            String BuddyApplicationPassword, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));

        MakeRequest("Service_Ping_Get", params, state, callback);
    }

    public static void Service_Version_Get(String BuddyApplicationName,
            String BuddyApplicationPassword, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));

        MakeRequest("Service_Version_Get", params, state, callback);
    }

    public static void Application_Users_GetEmailList(String BuddyApplicationName,
            String BuddyApplicationPassword, Integer FirstRow, Integer LastRow, String RESERVED,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("FirstRow", String.valueOf(FirstRow)));
        params.add(new BasicNameValuePair("LastRow", String.valueOf(LastRow)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Application_Users_GetEmailList", params, state, callback);
    }

    public static void Application_Users_GetProfileList(String BuddyApplicationName,
            String BuddyApplicationPassword, Integer FirstRow, Integer LastRow, String RESERVED,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("FirstRow", String.valueOf(FirstRow)));
        params.add(new BasicNameValuePair("LastRow", String.valueOf(LastRow)));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Application_Users_GetProfileList", params, state, callback);
    }

    public static void Application_Metrics_GetStats(String BuddyApplicationName,
            String BuddyApplicationPassword, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Application_Metrics_GetStats", params, state, callback);
    }

    public static void Commerce_Receipt_Save(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String TotalCost,
            Integer TotalQuantity, Integer StoreItemID, String StoreName, String ReceiptData,
            String CustomTransactionID, String AppData, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("TotalCost", TotalCost));
        params.add(new BasicNameValuePair("TotalQuantity", String.valueOf(TotalQuantity)));
        params.add(new BasicNameValuePair("StoreItemID", String.valueOf(StoreItemID)));
        params.add(new BasicNameValuePair("StoreName", StoreName));
        params.add(new BasicNameValuePair("ReceiptData", ReceiptData));
        params.add(new BasicNameValuePair("CustomTransactionID", CustomTransactionID));
        params.add(new BasicNameValuePair("AppData", AppData));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Commerce_Receipt_Save", params, state, callback);
    }

    public static void Commerce_Receipt_GetForUserAndTransactionID(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String CustomTransactionID,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("CustomTransactionID", CustomTransactionID));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Commerce_Receipt_GetForUserAndTransactionID", params, state, callback);
    }

    public static void Commerce_Receipt_GetForUser(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String FromDateTime,
            String RESERVED, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("FromDateTime", FromDateTime));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Commerce_Receipt_GetForUser", params, state, callback);
    }

    public static void Commerce_Store_GetAllItems(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Commerce_Store_GetAllItems", params, state, callback);
    }

    public static void Commerce_Store_GetActiveItems(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Commerce_Store_GetActiveItems", params, state, callback);
    }

    public static void Commerce_Store_GetFreeItems(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String RESERVED, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("RESERVED", RESERVED));

        MakeRequest("Commerce_Store_GetFreeItems", params, state, callback);
    }

    public static void StartupData_Location_GetMetroList(String BuddyApplicationName,
            String BuddyApplicationPassword, Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));

        MakeRequest("StartupData_Location_GetMetroList", params, state, callback);
    }

    public static void StartupData_Location_GetFromMetroArea(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, String MetroName, int RecordLimit,
            Object state, final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("MetroName", MetroName));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));

        MakeRequest("StartupData_Location_GetFromMetroArea", params, state, callback);
    }

    public static void StartupData_Location_Search(String BuddyApplicationName,
            String BuddyApplicationPassword, String UserToken, int SearchDistance, double Latitude,
            double Longitude, int RecordLimit, String SearchName, Object state,
            final OnResponseCallback callback) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("BuddyApplicationName", BuddyApplicationName));
        params.add(new BasicNameValuePair("BuddyApplicationPassword", BuddyApplicationPassword));
        params.add(new BasicNameValuePair("UserToken", UserToken));
        params.add(new BasicNameValuePair("SearchDistance", String.valueOf(SearchDistance)));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(Latitude)));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(Longitude)));
        params.add(new BasicNameValuePair("RecordLimit", String.valueOf(RecordLimit)));
        params.add(new BasicNameValuePair("SearchName", SearchName));

        MakeRequest("StartupData_Location_Search", params, state, callback);
    }
    
    public static void Blobs_Blob_DeleteBlob(BuddyClient client, AuthenticatedUser user, long blobID, 
    		final OnResponseCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("BlobID", blobID);
    	
    	MakeRequest("Blobs_Blob_DeleteBlob", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Blobs_Blob_AddBlob(BuddyClient client, AuthenticatedUser user, String friendlyName, String appTag, 
    		double latitude, double longitude, BuddyFile blobData, final OnResponseCallback callback)
    {
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("FriendlyName", friendlyName);
    	params.put("AppTag", appTag);
    	params.put("Latitude", latitude);
    	params.put("Longitude", longitude);
    	params.put("blobData", blobData);
    	
    	MakeRequest("Blobs_Blob_AddBlob", params, HttpRequestType.HttpPostMultipartForm, callback);
    }
    
    public static void Blobs_Blob_GetBlobInfo(BuddyClient client, AuthenticatedUser user, long blobID,
    		final OnResponseCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("BlobID", blobID);
    	
    	MakeRequest("Blobs_Blob_GetBlobInfo", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Blobs_Blob_SearchBlobs(BuddyClient client, AuthenticatedUser user, String friendlyName, 
    		String mimeType, String appTag, int searchDistance, double searchLatitude, double searchLongitude, int timeFilter,
    		int recordLimit, final OnResponseCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("FriendlyName", friendlyName);
    	params.put("MimeType", mimeType);
    	params.put("AppTag", appTag);
    	params.put("SearchDistance", searchDistance);
    	params.put("SearchLatitude", searchLatitude);
    	params.put("SearchLongitude", searchLongitude);
    	params.put("TimeFilter", timeFilter);
    	params.put("RecordLimit", recordLimit);
    	
    	MakeRequest("Blobs_Blob_SearchBlobs", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Blobs_Blob_SearchMyBlobs(BuddyClient client, AuthenticatedUser user, String friendlyName, 
    		String mimeType, String appTag, int searchDistance, double searchLatitude, double searchLongitude, int timeFilter,
    		int recordLimit, final OnResponseCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("FriendlyName", friendlyName);
    	params.put("MimeType", mimeType);
    	params.put("AppTag", appTag);
    	params.put("SearchDistance", searchDistance);
    	params.put("SearchLatitude", searchLatitude);
    	params.put("SearchLongitude", searchLongitude);
    	params.put("TimeFilter", timeFilter);
    	params.put("RecordLimit", recordLimit);
    	
    	MakeRequest("Blobs_Blob_SearchMyBlobs", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Blobs_Blob_GetBlobList(BuddyClient client, AuthenticatedUser user, long userID, int recordLimit,
    		final OnResponseCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("UserID", userID);
    	params.put("RecordLimit", recordLimit);
    	
    	MakeRequest("Blobs_Blob_GetBlobList", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Blobs_Blob_GetMyBlobList(BuddyClient client, AuthenticatedUser user, int recordLimit,
    		final OnResponseCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("RecordLimit", recordLimit);
    	
    	MakeRequest("Blobs_Blob_GetMyBlobList", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Blobs_Blob_EditInfo(BuddyClient client, AuthenticatedUser user, long blobID , String friendlyName, String appTag,
    		final OnResponseCallback callback)
    {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	addAuth(params, client, user);
    	params.put("BlobID", blobID);
    	params.put("FriendlyName", friendlyName);
    	params.put("AppTag", appTag);
    	
    	MakeRequest("Blobs_Blob_EditInfo", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Blobs_Blob_GetBlob(BuddyClient client, AuthenticatedUser user, long blobID, final OnResponseCallback callback)
    {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	addAuth(params, client, user);
    	params.put("BlobID", blobID);
    	
		MakePostReturnStream("Blobs_Blob_GetBlob", params, callback);
    }
    
    public static void Videos_Video_DeleteVideo(BuddyClient client, AuthenticatedUser user, long videoID, 
    		final OnResponseCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("VideoID", videoID);
    	
    	MakeRequest("Videos_Video_DeleteVideo", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Videos_Video_AddVideo(BuddyClient client, AuthenticatedUser user, String friendlyName, String appTag, 
    		double latitude, double longitude, BuddyFile videoData, final OnResponseCallback callback)
    {
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("FriendlyName", friendlyName);
    	params.put("AppTag", appTag);
    	params.put("Latitude", latitude);
    	params.put("Longitude", longitude);
    	params.put("videoData", videoData);
    	
    	MakeRequest("Videos_Video_AddVideo", params, HttpRequestType.HttpPostMultipartForm, callback);
    }
    
    public static void Videos_Video_GetVideoInfo(BuddyClient client, AuthenticatedUser user, long videoID,
    		final OnResponseCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("VideoID", videoID);
    	
    	MakeRequest("Videos_Video_GetVideoInfo", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Videos_Video_SearchVideos(BuddyClient client, AuthenticatedUser user, String friendlyName, 
    		String mimeType, String appTag, int searchDistance, double searchLatitude, double searchLongitude, int timeFilter,
    		int recordLimit, final OnResponseCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("FriendlyName", friendlyName);
    	params.put("MimeType", mimeType);
    	params.put("AppTag", appTag);
    	params.put("SearchDistance", searchDistance);
    	params.put("SearchLatitude", searchLatitude);
    	params.put("SearchLongitude", searchLongitude);
    	params.put("TimeFilter", timeFilter);
    	params.put("RecordLimit", recordLimit);
    	
    	MakeRequest("Videos_Video_SearchVideos", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Videos_Video_SearchMyVideos(BuddyClient client, AuthenticatedUser user, String friendlyName, 
    		String mimeType, String appTag, int searchDistance, double searchLatitude, double searchLongitude, int timeFilter,
    		int recordLimit, final OnResponseCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("FriendlyName", friendlyName);
    	params.put("MimeType", mimeType);
    	params.put("AppTag", appTag);
    	params.put("SearchDistance", searchDistance);
    	params.put("SearchLatitude", searchLatitude);
    	params.put("SearchLongitude", searchLongitude);
    	params.put("TimeFilter", timeFilter);
    	params.put("RecordLimit", recordLimit);
    	
    	MakeRequest("Videos_Video_SearchMyVideos", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Videos_Video_GetVideoList(BuddyClient client, AuthenticatedUser user, long userID, int recordLimit,
    		final OnResponseCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("UserID", userID);
    	params.put("RecordLimit", recordLimit);
    	
    	MakeRequest("Videos_Video_GetVideoList", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Videos_Video_GetMyVideoList(BuddyClient client, AuthenticatedUser user, int recordLimit,
    		final OnResponseCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	addAuth(params, client, user);
    	params.put("RecordLimit", recordLimit);
    	
    	MakeRequest("Videos_Video_GetMyVideoList", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Videos_Video_EditInfo(BuddyClient client, AuthenticatedUser user, long videoID , String friendlyName, String appTag,
    		final OnResponseCallback callback)
    {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	addAuth(params, client, user);
    	params.put("VideoID", videoID);
    	params.put("FriendlyName", friendlyName);
    	params.put("AppTag", appTag);
    	
    	MakeRequest("Videos_Video_EditInfo", params, HttpRequestType.HttpGet, callback);
    }
    
    public static void Videos_Video_GetVideo(BuddyClient client, AuthenticatedUser user, long videoID, final OnResponseCallback callback)
    {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	addAuth(params, client, user);
    	params.put("VideoID", videoID);
    	
    	
		MakePostReturnStream("Videos_Video_GetVideo", params, callback);
    }
    
    public static void Sound_Sounds_GetSound(BuddyClient client, String soundName, SoundQuality quality, final OnResponseCallback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	addAuth(params, client);
    	params.put("SoundName", soundName);
    	params.put("Quality", quality.name());
    	
    	MakePostReturnStream("Sound_Sounds_GetSound", params, callback);
    }
    
    private static void addAuth(Map<String, Object> params, BuddyClient client, AuthenticatedUser user){
    	addAuth(params, client);
    	addToken(params, user);
    }
    
    private static void addAuth(Map<String, Object> params, BuddyClient client){
    	params.put("BuddyApplicationName", client.getAppName());
    	params.put("BuddyApplicationPassword", client.getAppPassword());
    }
    
    private static void addToken(Map<String, Object> params, AuthenticatedUser user){
    	params.put("UserToken", user.getToken());    	
    }
    
}
