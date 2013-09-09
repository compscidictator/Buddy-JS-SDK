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
import com.buddy.sdk.json.responses.ProfilePictureDataResponse;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.web.BuddyWebWrapper;

class BaseUserDataModel extends BaseDataModel {
    private AuthenticatedUser authUser = null;

    BaseUserDataModel(BuddyClient client, AuthenticatedUser user) {
        this.client = client;
        this.authUser = user;
    }

    public void getProfilePhotos(int profileID, Object state,
            final OnCallback<ListResponse<PicturePublic>> callback) {
        
        BuddyWebWrapper.Pictures_ProfilePhoto_GetAll(client.getAppName(), client.getAppPassword(),
                profileID, state, new OnResponseCallback() {
        	
            		@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<PicturePublic> listResponse = new ListResponse<PicturePublic>();

                        if (response != null) {
                            if (response.completed) {
                                ProfilePictureDataResponse result = getJson(response.response,
                                        ProfilePictureDataResponse.class);
                                if (result != null) {
                                    List<PicturePublic> profilePhotos = new ArrayList<PicturePublic>(
                                            result.data.size());

                                    for (ProfilePictureDataResponse.ProfilePictureData photoData : result.data) {
                                        double lat = Utils.parseDouble(photoData.latitude);
                                        double lon = Utils.parseDouble(photoData.longitude);

                                        Date dateAdded = Utils
                                                .convertDateString(photoData.addedDateTime);

                                        int photoID = Utils.parseInt(photoData.photoID);

                                        PicturePublic photo = new PicturePublic(client,
                                                photoData.fullPhotoURL,
                                                photoData.thumbnailPhotoURL, lat, lon,
                                                photoData.photoComment, null, dateAdded, photoID,
                                                authUser);

                                        profilePhotos.add(photo);
                                    }

                                    listResponse.setList(profilePhotos);
                                } else {
                                    listResponse.setThrowable(new BuddyServiceException(
                                            response.response));
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
