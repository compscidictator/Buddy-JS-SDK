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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.PictureDataResponse;
import com.buddy.sdk.json.responses.SocialLoginResponse;
import com.buddy.sdk.json.responses.UserLocationHistoryDataResponse;
import com.buddy.sdk.json.responses.UserDataResponse;
import com.buddy.sdk.json.responses.UserLocationHistoryDataResponse.LocationHistoryData;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.utils.Constants.UserGender;
import com.buddy.sdk.utils.Constants.UserStatus;
import com.buddy.sdk.web.BuddyWebWrapper;

class UserDataModel extends BaseDataModel {
    private AuthenticatedUser authUser = null;

    UserDataModel(BuddyClient client) {
        this.client = client;
    }

    UserDataModel(BuddyClient client, AuthenticatedUser user) {
        this.client = client;
        this.authUser = user;
    }

    public void socialLogin(String providerName, String providerUserId, String accessToken,
    		final OnResponseCallback callback)
    {
    	BuddyWebWrapper.UserAccount_Profile_SocialLogin(this.client, providerName, providerUserId, accessToken, new OnResponseCallback(){
    		@Override
    		public void OnResponse(BuddyCallbackParams response, Object state){
    			if(response != null){
    				if(response.completed){
    					SocialLoginResponse data = getJson(response.response, SocialLoginResponse.class);
    					if(data != null && data.data.size() > 0){
    						SocialLoginResponse.SocialLogin ids = data.data.get(0);
    						response.responseObj = (Object) ids;
    					} else {
    						response = new BuddyCallbackParams(new Throwable(response.response), response.response);
    					}
    				}
    			} else {
    				response = new BuddyCallbackParams(new ServiceUnknownErrorException(),
    						null);
    			}
    			callback.OnResponse(response, state);
    		}
    	});    	
    }
    
    public void createUser(String userName, String userPassword, UserGender userGender,
            Integer userAge, String userEmail, UserStatus statusId, Boolean fuzzlocation,
            Boolean celebModeEnabled, String applicationTag, Object state,
            final OnResponseCallback callback) {
        
        BuddyWebWrapper.UserAccount_Profile_Create(client.getAppName(), client.getAppPassword(),
                userName, userPassword, userGender.name(), userAge, userEmail, statusId.getValue(),
                (Integer) (fuzzlocation ? 1 : 0), (Integer) (celebModeEnabled ? 1 : 0),
                applicationTag, this.RESERVED, state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        if (response != null) {
                            if (response.completed) {
                                String result = getJson(response.response, String.class);
                                if (result != null) {
                                    response.responseObj = (Object) result;
                                } else {
                                    response = new BuddyCallbackParams(new Throwable(result),
                                            result);
                                }
                            }
                        } else {
                            response = new BuddyCallbackParams(new ServiceUnknownErrorException(),
                                    null);
                        }
                        callback.OnResponse(response, state);
                    }

                });
    }

    public void userProfileGetFromToken(final String token, Object state,
            final OnCallback<Response<AuthenticatedUser>> callback) {
        
        BuddyWebWrapper.UserAccount_Profile_GetFromUserToken(client.getAppName(),
                client.getAppPassword(), token, state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<AuthenticatedUser> userResponse = new Response<AuthenticatedUser>();
                        if (response != null) {
                            if (response.completed) {
                                AuthenticatedUser user = null;
                                UserDataResponse data = getJson(response.response,
                                        UserDataResponse.class);
                                if (data != null && data.data.size() > 0) {
                                    UserDataResponse.UserData profile = data.data.get(0);
                                    user = new AuthenticatedUser(client, token, profile);
                                    userResponse.setResult(user);
                                } else {
                                    userResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                userResponse.setThrowable(response.exception);
                            }
                        } else {
                            userResponse.setThrowable(new ServiceUnknownErrorException());

                        }
                        callback.OnResponse(userResponse, state);
                    }

                });
    }

    public void userProfileRecover(String userName, String userPassword, Object state,
            final OnCallback<Response<String>> callback) {
        
        BuddyWebWrapper.UserAccount_Profile_Recover(client.getAppName(), client.getAppPassword(),
                userName, userPassword, state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<String> stringResponse = getStringResponse(response);
                        callback.OnResponse(stringResponse, state);
                    }

                });
    }

    public void userProfileRecover(String userName, String userPassword, Object state,
            final OnResponseCallback callback) {
        
        BuddyWebWrapper.UserAccount_Profile_Recover(client.getAppName(), client.getAppPassword(),
                userName, userPassword, state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        if (response != null) {
                            if (response.completed) {
                                String result = getJson(response.response, String.class);
                                if (result != null) {
                                    response.responseObj = (Object) result;
                                } else {
                                    response = new BuddyCallbackParams(new Throwable(result),
                                            result);
                                }
                            }
                        } else {
                            response = new BuddyCallbackParams(new ServiceUnknownErrorException(),
                                    null);
                        }
                        callback.OnResponse(response, state);
                    }

                });
    }

    public void delete(int userId, Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.UserAccount_Profile_DeleteAccount(client.getAppName(),
                client.getAppPassword(), (Integer) userId, state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void checkIfUserNameExists(String userName, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.UserAccount_Profile_CheckUserName(client.getAppName(),
                client.getAppPassword(), userName, this.RESERVED, state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = new Response<Boolean>();
                        if (response != null) {
                            if (response.completed) {
                                // The pass results are in the possible list of
                                // errors so this may not be reached
                                booleanResponse.setResult(false);
                            } else {
                                if (response.exception instanceof BuddyServiceException) {
                                    String errorMessage = response.exception.getMessage();
                                    if (errorMessage.equalsIgnoreCase("UserNameAvailble")) {
                                        booleanResponse.setResult(false);
                                    } else if (errorMessage
                                            .equalsIgnoreCase("UserNameAlreadyInUse")) {
                                        booleanResponse.setResult(true);
                                    } else {
                                        booleanResponse.setThrowable(response.exception);
                                    }
                                } else {
                                    booleanResponse.setThrowable(response.exception);
                                }
                            }
                        } else {
                            booleanResponse.setThrowable(new ServiceUnknownErrorException());

                        }
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void checkIfEmailExists(String userEmail, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.UserAccount_Profile_CheckUserEmail(client.getAppName(),
                client.getAppPassword(), userEmail, this.RESERVED, state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = new Response<Boolean>();
                        if (response != null) {
                            if (response.completed) {
                                // The pass results are in the possible list of
                                // errors so this may not be reached
                                booleanResponse.setResult(false);
                            } else {
                                if (response.exception instanceof BuddyServiceException) {
                                    String errorMessage = response.exception.getMessage();
                                    if (errorMessage.equalsIgnoreCase("UserEmailAvailable")) {
                                        booleanResponse.setResult(false);
                                    } else if (errorMessage.equalsIgnoreCase("UserEmailTaken")) {
                                        booleanResponse.setResult(true);
                                    } else {
                                        booleanResponse.setThrowable(response.exception);
                                    }
                                } else {
                                    booleanResponse.setThrowable(response.exception);
                                }
                            }
                        } else {
                            booleanResponse.setThrowable(new ServiceUnknownErrorException());

                        }
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void update(String userName, String userPassword, UserGender userGender,
            Integer userAge, String userEmail, UserStatus statusId, Boolean fuzzlocation,
            Boolean celebModeEnabled, String applicationTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.UserAccount_Profile_Update(client.getAppName(), client.getAppPassword(),
                authUser.getToken(), userName, userPassword, userGender.name(), userAge,
                userEmail, statusId.getValue(), (Integer) (fuzzlocation ? 1 : 0),
                (Integer) (celebModeEnabled ? 1 : 0), applicationTag, this.RESERVED, state,
                new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void getCheckIns(Date afterDate, Object state,
            final OnCallback<ListResponse<CheckInLocation>> callback) {
        
        BuddyWebWrapper.UserAccount_Location_GetHistory(this.client.getAppName(),
                this.client.getAppPassword(), this.authUser.getToken(),
                Utils.convertStringDate(afterDate, "MM/dd/yyyy"), state, new OnResponseCallback() {
        	
    				@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<CheckInLocation> listResponse = new ListResponse<CheckInLocation>();

                        if (response != null) {
                            if (response.completed) {
                                UserLocationHistoryDataResponse data = getJson(response.response,
                                        UserLocationHistoryDataResponse.class);
                                if (data != null) {
                                    List<CheckInLocation> list = new ArrayList<CheckInLocation>(
                                            data.data.size());

                                    for (LocationHistoryData history : data.data) {
                                        double latitude = Utils.parseDouble(history.Latitude);
                                        double longitude = Utils.parseDouble(history.Longitude);

                                        Date createdDate = Utils
                                                .convertDateString(history.CreatedDate);

                                        CheckInLocation location = new CheckInLocation(latitude,
                                                longitude, createdDate, history.PlaceName, null,
                                                null);

                                        list.add(location);
                                    }

                                    listResponse.setList(list);
                                } else {
                                    listResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                if (response.exception != null) {
                                    listResponse.setThrowable(response.exception);
                                }
                            }
                        } else {
                            listResponse.setThrowable(new ServiceUnknownErrorException());

                        }

                        callback.OnResponse(listResponse, state);
                    }
                });
    }

    public void checkIn(double latitude, double longitude, String comment, String appTag,
            Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.UserAccount_Location_Checkin(this.client.getAppName(),
                this.client.getAppPassword(), this.authUser.getToken(), (float) latitude,
                (float) longitude, comment, appTag, this.RESERVED, state, new OnResponseCallback() {
        	
    				@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> checkInResponse = getBooleanResponse(response);
                        callback.OnResponse(checkInResponse, state);
                    }
                });
    }

    public void deleteProfilePhoto(PicturePublic photo, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Pictures_ProfilePhoto_Delete(this.client.getAppName(),
                this.client.getAppPassword(), this.authUser.getToken(), photo.getPhotoId(),
                state, new OnResponseCallback() {
        	
    				@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> addResponse = getBooleanResponse(response);
                        callback.OnResponse(addResponse, state);
                    }
                });
    }

    public void addProfilePhoto(byte[] blob, String appTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        String encodedImage = new String(blob);
        BuddyWebWrapper.Pictures_ProfilePhoto_Add(this.client.getAppName(),
                this.client.getAppPassword(), this.authUser.getToken(), encodedImage, appTag,
                this.RESERVED, state, new OnResponseCallback() {
        	
    				@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> addResponse = getBooleanResponse(response);
                        callback.OnResponse(addResponse, state);
                    }
                });
    }

    public void setProfilePhoto(PicturePublic photo, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        String photoId = Integer.toString(photo.getPhotoId());

        BuddyWebWrapper.Pictures_ProfilePhoto_Set(client.getAppName(), client.getAppPassword(),
                authUser.getToken(), photoId, state, new OnResponseCallback() {
        	
    				@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> setPhotoResponse = getBooleanResponse(response);
                        callback.OnResponse(setPhotoResponse, state);
                    }
                });
    }

    public void getPicture(int pictureId, Object state, final OnCallback<Response<Picture>> callback) {
        
        BuddyWebWrapper.Pictures_Photo_Get(client.getAppName(), client.getAppPassword(),
                authUser.getToken(), authUser.getId(), pictureId, state,
                new OnResponseCallback() {
        	
    				@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Picture> pictureResponse = new Response<Picture>();

                        if (response != null) {
                            if (response.completed) {
                                PictureDataResponse result = getJson(response.response,
                                        PictureDataResponse.class);

                                if (result != null && result.data.size() > 0) {
                                    PictureDataResponse.PictureData pictureData = result.data
                                            .get(0);

                                    double lat = Utils.parseDouble(pictureData.latitude);
                                    double lon = Utils.parseDouble(pictureData.longitude);

                                    Date addedDate = Utils
                                            .convertDateString(pictureData.addedDateTime);

                                    int photoId = Utils.parseInt(pictureData.photoId);

                                    Picture photo = new Picture(client, authUser,
                                            pictureData.fullPhotoUrl, pictureData.thumbnailUrl,
                                            lat, lon, pictureData.photoComment,
                                            pictureData.applicationTag, addedDate, photoId);

                                    pictureResponse.setResult(photo);
                                } else {
                                    pictureResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                pictureResponse.setThrowable(response.exception);
                            }
                        } else {
                            pictureResponse.setThrowable(new ServiceUnknownErrorException());

                        }

                        callback.OnResponse(pictureResponse, state);
                    }
                });
    }

    public void findUser(String userNameToFetch, final OnCallback<Response<User>> callback){
    	BuddyWebWrapper.UserAccount_Profile_GetFromUserName(client.getAppName(), client.getAppPassword(), authUser.getToken(), 
    			userNameToFetch, null, new OnResponseCallback(){
    		@Override
    		public void OnResponse(BuddyCallbackParams response, Object state) {
    			Response<User> userResponse = new Response<User>();
    			if(response != null){
    				if(response.completed){
    					User user = null;
    					UserDataResponse data = getJson(response.response,
    							UserDataResponse.class);
    					if(data != null &&  data.data.size() > 0) {
                            UserDataResponse.UserData profile = data.data.get(0);
                            user = new User(client, profile);
                            userResponse.setResult(user);
                        } else {
                            userResponse.setThrowable(new BuddyServiceException(
                                    response.response));
                        }
                    } else {
                        userResponse.setThrowable(response.exception);
                    }
                } else {
                    userResponse.setThrowable(new ServiceUnknownErrorException());
                }
                callback.OnResponse(userResponse, state);
    		}	
    	});  	
    }
    
    public void findUser(int userId, Object state, final OnCallback<Response<User>> callback) {
        
        BuddyWebWrapper.UserAccount_Profile_GetFromUserID(client.getAppName(),
                client.getAppPassword(), authUser.getToken(), userId, state,
                new OnResponseCallback() {
        	
    				@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<User> userResponse = new Response<User>();
                        if (response != null) {
                            if (response.completed) {
                                // Create a new User
                                User user = null;
                                UserDataResponse data = getJson(response.response,
                                        UserDataResponse.class);
                                if (data != null && data.data.size() > 0) {
                                    UserDataResponse.UserData profile = data.data.get(0);
                                    user = new User(client, profile);
                                    userResponse.setResult(user);
                                } else {
                                    userResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                userResponse.setThrowable(response.exception);
                            }
                        } else {
                            userResponse.setThrowable(new ServiceUnknownErrorException());

                        }
                        callback.OnResponse(userResponse, state);
                    }

                });
    }
    
    public void findUser(Integer searchDistance, double latitude, double longitude, Integer recordLimit, 
            UserGender gender, Integer ageStart, Integer ageStop, UserStatus statusId, String timeFilter, 
            String appTag, Object state, final OnCallback<ListResponse<User>> callback) {
        
        BuddyWebWrapper.UserAccount_Profile_Search(client.getAppName(), client.getAppPassword(), authUser.getToken(), 
                searchDistance, (float) latitude, (float) longitude, recordLimit, gender.name(), ageStart, ageStop, statusId.getValue(), 
                timeFilter, appTag, this.RESERVED, state, new OnResponseCallback() {
        	
    		@Override
            public void OnResponse(BuddyCallbackParams response, Object state) {
                ListResponse<User> listResponse = new ListResponse<User>();
                if(response != null) {
                    if(response.completed) {
                        List<User> userList = new ArrayList<User>();
                        User user = null;
                        UserDataResponse data = getJson(response.response, UserDataResponse.class);
                        if(data != null) {
                            for(UserDataResponse.UserData profile : data.data) {
                                user = new User(client, profile);
                                userList.add(user);
                            }
                            
                            listResponse.setList(userList);
                        } else {
                            listResponse.setThrowable(new BuddyServiceException(response.response));
                        }
                    } else {
                        listResponse.setThrowable(response.exception);
                    }
                } else {
                    listResponse.setThrowable(new ServiceUnknownErrorException());
                    
                }
                callback.OnResponse(listResponse, state);
            }
            
        });
    }
}
