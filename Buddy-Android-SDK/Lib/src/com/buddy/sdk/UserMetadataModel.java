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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.BatchSumDataResponse;
import com.buddy.sdk.json.responses.MetadataItemDataResponse;
import com.buddy.sdk.json.responses.MetadataSearchDataResponse;
import com.buddy.sdk.json.responses.MetaSumDataResponse;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.web.BuddyWebWrapper;

class UserMetadataModel extends BaseDataModel {
    private String userToken;
    private UserMetadata metadata;

    UserMetadataModel(BuddyClient client, UserMetadata metadata, String userToken) {
        this.client = client;
        this.userToken = userToken;
        this.metadata = metadata;
    }

    public void getAll(Object state, final OnCallback<Response<Map<String, MetadataItem>>> callback) {

        BuddyWebWrapper.Metadata_UserMetadataValue_GetAll(this.client.getAppName(),
                this.client.getAppPassword(), this.userToken, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Map<String, MetadataItem>> getResponse = new Response<Map<String, MetadataItem>>();

                        if (response != null) {
                            if (response.completed) {
                                MetadataItemDataResponse result = getJson(response.response,
                                        MetadataItemDataResponse.class);
                                if (result != null) {
                                    Map<String, MetadataItem> map = new HashMap<String, MetadataItem>();

                                    for (MetadataItemDataResponse.MetadataValueData resultData : result.data) {
                                        double lat = Utils.parseDouble(resultData.metaLatitude);
                                        double lon = Utils.parseDouble(resultData.metaLongitude);

                                        Date lastUpdated = Utils
                                                .convertDateString(resultData.lastUpdateDate);

                                        MetadataItem item = new MetadataItem(client, metadata,
                                                null, userToken, resultData.metaKey,
                                                resultData.metaValue, lat, lon, lastUpdated, null);

                                        map.put(resultData.metaKey, item);
                                    }

                                    getResponse.setResult(map);
                                } else {
                                    getResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                getResponse.setThrowable(response.exception);
                            }
                        } else {
                            getResponse.setThrowable(new ServiceUnknownErrorException());
                        }

                        callback.OnResponse(getResponse, state);
                    }

                });
    }

    public void get(String key, Object state, final OnCallback<Response<MetadataItem>> callback) {

        BuddyWebWrapper.Metadata_UserMetadataValue_Get(this.client.getAppName(),
                this.client.getAppPassword(), this.userToken, key, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<MetadataItem> getResponse = new Response<MetadataItem>();

                        if (response != null) {
                            if (response.completed) {
                                MetadataItemDataResponse result = getJson(response.response,
                                        MetadataItemDataResponse.class);

                                if (result != null && result.data != null && result.data.size() > 0) {
                                    MetadataItemDataResponse.MetadataValueData resultData = result.data
                                            .get(0);

                                    double lat = Utils.parseDouble(resultData.metaLatitude);
                                    double lon = Utils.parseDouble(resultData.metaLongitude);

                                    Date dateAdded = Utils
                                            .convertDateString(resultData.lastUpdateDate);

                                    MetadataItem value = new MetadataItem(client, metadata, null,
                                            userToken, resultData.metaKey, resultData.metaValue,
                                            lat, lon, dateAdded, null);

                                    getResponse.setResult(value);
                                } else {
                                    getResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                getResponse.setThrowable(response.exception);
                            }
                        } else {
                            getResponse.setThrowable(new ServiceUnknownErrorException());
                        }

                        callback.OnResponse(getResponse, state);
                    }

                });
    }

    public void set(String key, String value, double latitude, double longitude, String appTag,
            Object state, final OnCallback<Response<Boolean>> callback) {

        BuddyWebWrapper.Metadata_UserMetadataValue_Set(this.client.getAppName(),
                this.client.getAppPassword(), this.userToken, key, value, (float) latitude,
                (float) longitude, appTag, this.RESERVED, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> setResponse = getBooleanResponse(response);
                        callback.OnResponse(setResponse, state);
                    }

                });
    }

    public void delete(String key, Object state, final OnCallback<Response<Boolean>> callback) {

        BuddyWebWrapper.Metadata_UserMetadataValue_Delete(this.client.getAppName(),
                this.client.getAppPassword(), this.userToken, key, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> deleteResponse = getBooleanResponse(response);
                        callback.OnResponse(deleteResponse, state);
                    }

                });
    }

    public void deleteAll(Object state, final OnCallback<Response<Boolean>> callback) {

        BuddyWebWrapper.Metadata_UserMetadataValue_DeleteAll(this.client.getAppName(),
                this.client.getAppPassword(), this.userToken, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> deleteResponse = getBooleanResponse(response);
                        callback.OnResponse(deleteResponse, state);
                    }

                });
    }

    public void find(Integer searchDistanceMeters, double latitude, double longitude,
            Integer numberOfResults, String withKey, String withValue, Integer updatedMinutesAgo,
            boolean searchAsFloat, boolean sortAscending, boolean disableCache, Object state,
            final OnCallback<Response<Map<String, MetadataItem>>> callback) {

        BuddyWebWrapper.Metadata_UserMetadataValue_Search(this.client.getAppName(),
                this.client.getAppPassword(), this.userToken, searchDistanceMeters,
                (float) latitude, (float) longitude, numberOfResults, withKey, withValue,
                String.valueOf(updatedMinutesAgo), searchAsFloat ? 1 : 0, sortAscending ? 1 : 0,
                disableCache ? "true" : "", state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Map<String, MetadataItem>> searchResponse = new Response<Map<String, MetadataItem>>();

                        if (response != null) {
                            if (response.completed) {
                                MetadataSearchDataResponse result = getJson(response.response,
                                        MetadataSearchDataResponse.class);
                                if (result != null) {
                                    Map<String, MetadataItem> map = new HashMap<String, MetadataItem>();

                                    for (MetadataSearchDataResponse.MetadataSearchData resultData : result.data) {
                                        double lat = Utils.parseDouble(resultData.metaLatitude);
                                        double lon = Utils.parseDouble(resultData.metaLongitude);

                                        MetadataItem item = new MetadataItem(client, metadata,
                                                null, userToken, resultData, lat, lon);

                                        map.put(resultData.metaKey, item);
                                    }

                                    searchResponse.setResult(map);
                                } else {
                                    searchResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                searchResponse.setThrowable(response.exception);
                            }
                        } else {
                            searchResponse.setThrowable(new ServiceUnknownErrorException());
                        }

                        callback.OnResponse(searchResponse, state);
                    }

                });
    }

    public void sum(final String forKeys, int withinDistance, double latitude, double longitude,
            int updatedMinutesAgo, String withAppTag, Object state,
            final OnCallback<Response<MetadataSum>> callback) {

        BuddyWebWrapper.Metadata_UserMetadataValue_Sum(this.client.getAppName(),
                this.client.getAppPassword(), this.userToken, forKeys, withinDistance,
                (float) latitude, (float) longitude, String.valueOf(updatedMinutesAgo), withAppTag,
                this.RESERVED, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<MetadataSum> sumResponse = new Response<MetadataSum>();

                        if (response != null) {
                            if (response.completed) {
                                MetaSumDataResponse result = getJson(response.response,
                                        MetaSumDataResponse.class);
                                if (result != null) {
                                    double totalValue = 0.0;

                                    MetaSumDataResponse.MetaSumData data = result.data.get(0);

                                    String totalValueData = data.totalValue;

                                    if (Utils.isNullOrEmpty(totalValueData) == false)
                                        totalValue = Utils.parseDouble(totalValueData);

                                    int keyCount = Utils.parseInt(data.keyCount);

                                    MetadataSum sum = new MetadataSum(totalValue, keyCount, forKeys);

                                    sumResponse.setResult(sum);
                                } else {
                                    sumResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                sumResponse.setThrowable(response.exception);
                            }
                        } else {
                            sumResponse.setThrowable(new ServiceUnknownErrorException());
                        }

                        callback.OnResponse(sumResponse, state);
                    }

                });
    }

    public void batchSum(String forKeys, String withinDistance, double latitude, double longitude,
            int updatedMinutesAgo, String withAppTag, Object state,
            final OnCallback<ListResponse<MetadataSum>> callback) {

        if (withinDistance.equals("-1")) {
            for (int i = 0; i < forKeys.split(";").length - 1; i++) {
                withinDistance += ";-1";
            }
        }

        BuddyWebWrapper.Metadata_UserMetadataValue_BatchSum(this.client.getAppName(),
                this.client.getAppPassword(), this.userToken, forKeys, withinDistance,
                (float) latitude, (float) longitude, String.valueOf(updatedMinutesAgo), withAppTag,
                this.RESERVED, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<MetadataSum> listResponse = new ListResponse<MetadataSum>();

                        if (response != null) {
                            if (response.completed) {
                                BatchSumDataResponse result = getJson(response.response,
                                        BatchSumDataResponse.class);
                                if (result != null) {
                                    List<MetadataSum> sums = new ArrayList<MetadataSum>();

                                    for (BatchSumDataResponse.BatchSumData sumData : result.data) {
                                        double sumVal = Utils.parseDouble(sumData.totalValue);
                                        int countedVal = Utils.parseInt(sumData.keyCount);

                                        MetadataSum sum = new MetadataSum(sumVal, countedVal,
                                                sumData.metaKey);

                                        sums.add(sum);
                                    }

                                    listResponse.setList(sums);
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
    
    public void batchSet(String metaKeys, String values, double latitude, double longitude,
            String withAppTag, Object state, final OnCallback<Response<Boolean>> callback) {

        BuddyWebWrapper.Metadata_UserMetadataValue_BatchSet(this.client.getAppName(),
                this.client.getAppPassword(), this.userToken, metaKeys, values, (float) latitude,
                (float) longitude, withAppTag, this.RESERVED, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }
                });
    }
}
