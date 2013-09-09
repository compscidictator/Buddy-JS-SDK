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
import java.util.List;

import android.util.SparseArray;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.PlacesCategoryDataResponse;
import com.buddy.sdk.json.responses.PlacesCategoryDataResponse.PlacesCategoryData;
import com.buddy.sdk.json.responses.PlacesDataResponse;
import com.buddy.sdk.json.responses.PlacesDataResponse.PlacesData;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.web.BuddyWebWrapper;

class PlacesDataModel extends BaseDataModel {
    private AuthenticatedUser authUser = null;

    PlacesDataModel(BuddyClient client, AuthenticatedUser user) {
        this.client = client;
        this.authUser = user;
    }

    public void setTag(Integer existingGeoID, String applicationTag, String userTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.GeoLocation_Location_SetTag(client.getAppName(), client.getAppPassword(),
                this.authUser.getToken(), existingGeoID, applicationTag, userTag,
                this.RESERVED, state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });

    }

    public void find(Integer searchDistance, double latitude, double longitude,
            Integer recordLimit, String searchName, Integer searchCategoryID, Object state,
            final OnCallback<ListResponse<Place>> callback) {
        
        BuddyWebWrapper.GeoLocation_Location_Search(client.getAppName(), client.getAppPassword(),
                this.authUser.getToken(), searchDistance, (float) latitude, (float) longitude,
                recordLimit, searchName, searchCategoryID >= 0 ? String.valueOf(searchCategoryID)
                        : "", state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<Place> listResponse = new ListResponse<Place>();
                        if (response != null) {
                            if (response.completed) {
                                PlacesDataResponse data = getJson(response.response,
                                        PlacesDataResponse.class);
                                if (data != null) {
                                    List<Place> placeList = new ArrayList<Place>(data.data.size());
                                    for (PlacesData placeData : data.data) {
                                        Place place = new Place(client, authUser, placeData);
                                        placeList.add(place);
                                    }
                                    listResponse.setList(placeList);
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

    public void get(Integer existingGeoID, double latitude, double longitude, Object state,
            final OnCallback<Response<Place>> callback) {
        
        BuddyWebWrapper.GeoLocation_Location_GetFromID(client.getAppName(),
                client.getAppPassword(), this.authUser.getToken(), existingGeoID,
                (float) latitude, (float) longitude, this.RESERVED, state,
                new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Place> listResponse = new Response<Place>();
                        if (response != null) {
                            if (response.completed) {
                                PlacesDataResponse data = getJson(response.response,
                                        PlacesDataResponse.class);
                                if (data != null) {
                                    if (data.data.size() > 0) {
                                        Place place = new Place(client, authUser, data.data.get(0));
                                        listResponse.setResult(place);
                                    } else {
                                        listResponse.setResult(null);
                                    }
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

    public void getCategories(Object state, final OnCallback<Response<SparseArray<String>>> callback) {
        BuddyWebWrapper.GeoLocation_Category_GetList(client.getAppName(), client.getAppPassword(),
                this.authUser.getToken(), state, new OnResponseCallback() {

        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<SparseArray<String>> categoryResponse = new Response<SparseArray<String>>();
                        if (response != null) {
                            if (response.completed) {
                                PlacesCategoryDataResponse data = getJson(response.response,
                                        PlacesCategoryDataResponse.class);
                                if (data != null) {
                                    SparseArray<String> map = new SparseArray<String>(data.data
                                            .size());
                                    for (PlacesCategoryData categoryData : data.data) {
                                        Integer categoryId = Utils
                                                .parseInt(categoryData.categoryID);
                                        map.put(categoryId, categoryData.categoryName);
                                    }
                                    categoryResponse.setResult(map);
                                } else {
                                    categoryResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                categoryResponse.setThrowable(response.exception);
                            }
                        } else {
                            categoryResponse.setThrowable(new ServiceUnknownErrorException());

                        }
                        callback.OnResponse(categoryResponse, state);
                    }

                });
    }
}
