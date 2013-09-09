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

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.VirtualAlbumDataResponse;
import com.buddy.sdk.json.responses.VirtualAlbumDataResponse.VirtualAlbumData;
import com.buddy.sdk.json.responses.VirtualBatchAddDataResponse;
import com.buddy.sdk.json.responses.VirtualPhotoDataResponse;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.web.BuddyWebWrapper;

class VirtualAlbumDataModel extends BaseDataModel {
    private AuthenticatedUser user = null;

    VirtualAlbumDataModel(BuddyClient client, AuthenticatedUser user) {
        this.client = client;
        this.user = user;
    }

    public void create(String albumName, String applicationTag, Object state,
            final OnCallback<Response<VirtualAlbum>> callback) {
        
        BuddyWebWrapper.Pictures_VirtualAlbum_Create(client.getAppName(), client.getAppPassword(),
                user.getToken(), albumName, applicationTag, this.RESERVED, state,
                new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Boolean disableCallback = false;
                        Response<VirtualAlbum> albumResponse = new Response<VirtualAlbum>();
                        if (response != null) {
                            if (response.completed) {
                                int albumId;
								try {
									albumId = Utils.parseInt(response.response, true);

									disableCallback = true;
                                    get(albumId, state, callback);
								} catch (Exception e) {
									albumResponse.setThrowable(e);
								}
                            } else {
                                albumResponse.setThrowable(response.exception);
                            }
                        } else {
                            albumResponse.setThrowable(new ServiceUnknownErrorException());
                        }

                        if (!disableCallback) {
                            callback.OnResponse(albumResponse, state);
                        }
                    }

                });
    }

    public void addPicture(int albumId, int photoId, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Pictures_VirtualAlbum_AddPhoto(client.getAppName(),
                client.getAppPassword(), user.getToken(), albumId, photoId, this.RESERVED,
                state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = new Response<Boolean>();
                        if (response != null) {
                            if (response.completed) {
                                String responseData = getJson(response.response, String.class);
                                if (responseData != null) {
                                    if (Utils.isNumeric(responseData)) {
                                        booleanResponse.setResult(true);
                                    } else {
                                        booleanResponse.setResult(false);
                                        booleanResponse.setErrorMessage(responseData);
                                    }
                                } else {
                                    booleanResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                booleanResponse.setThrowable(response.exception);
                            }
                        } else {
                            booleanResponse.setThrowable(new ServiceUnknownErrorException());
                        }
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void addPictureBatch(int albumId, List<PicturePublic> pictureList, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        // Get Picture ids as a string
        String photoIdList = "";
        for (PicturePublic picture : pictureList) {
            photoIdList += String.valueOf(picture.getPhotoId());
            photoIdList += ";";
        }
        // Remove the last semi colon
        photoIdList = photoIdList.substring(0, photoIdList.length() - 1);

        BuddyWebWrapper.Pictures_VirtualAlbum_AddPhotoBatch(client.getAppName(),
                client.getAppPassword(), user.getToken(), albumId, photoIdList, this.RESERVED,
                state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = new Response<Boolean>();
                        if (response != null) {
                            if (response.completed) {
                                VirtualBatchAddDataResponse responseData = getJson(
                                        response.response, VirtualBatchAddDataResponse.class);
                                if (responseData != null) {
                                    if (responseData.data.size() > 0) {
                                        booleanResponse.setResult(true);
                                    } else {
                                        booleanResponse.setResult(false);
                                        booleanResponse.setErrorMessage("Invalid Batch response");
                                    }
                                } else {
                                    booleanResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                booleanResponse.setThrowable(response.exception);
                            }
                        } else {
                            booleanResponse.setThrowable(new ServiceUnknownErrorException());
                        }
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void delete(int albumId, Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Pictures_VirtualAlbum_DeleteAlbum(client.getAppName(),
                client.getAppPassword(), user.getToken(), albumId, this.RESERVED, state,
                new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void removePicture(int albumId, int photoId, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Pictures_VirtualAlbum_RemovePhoto(client.getAppName(),
                client.getAppPassword(), user.getToken(), albumId, photoId, this.RESERVED,
                state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void update(int albumId, String newAlbumName, String newApplicationTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Pictures_VirtualAlbum_Update(client.getAppName(), client.getAppPassword(),
                user.getToken(), albumId, newAlbumName, newApplicationTag, this.RESERVED,
                state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void updatePicture(int photoId, String newPhotoComment, String newApplicationTag,
            Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Pictures_VirtualAlbum_UpdatePhoto(client.getAppName(),
                client.getAppPassword(), user.getToken(), photoId, newPhotoComment,
                newApplicationTag, this.RESERVED, state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void get(final int albumId, Object state,
            final OnCallback<Response<VirtualAlbum>> callback) {
        
        BuddyWebWrapper.Pictures_VirtualAlbum_GetAlbumInformation(client.getAppName(),
                client.getAppPassword(), user.getToken(), albumId, this.RESERVED, state,
                new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Boolean disableCallback = false;
                        Response<VirtualAlbum> albumResponse = new Response<VirtualAlbum>();
                        if (response != null) {
                            if (response.completed) {
                                VirtualAlbumDataResponse responseData = getJson(response.response,
                                        VirtualAlbumDataResponse.class);
                                if (responseData != null) {
                                    if (responseData.data.size() > 0) {
                                        // The getPictures will callback so
                                        // disable callback from this method
                                        disableCallback = true;
                                        getPictures(albumId, responseData.data.get(0), state,
                                                callback);
                                    } else {
                                        albumResponse.setResult(null);
                                    }
                                } else {
                                    albumResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                albumResponse.setThrowable(response.exception);
                            }
                        } else {
                            albumResponse.setThrowable(new ServiceUnknownErrorException());
                        }

                        if (!disableCallback) {
                            callback.OnResponse(albumResponse, state);
                        }
                    }

                });
    }

    public void getPictures(int albumId, final VirtualAlbumData albumData, Object state,
            final OnCallback<Response<VirtualAlbum>> callback) {
        
        BuddyWebWrapper.Pictures_VirtualAlbum_Get(client.getAppName(), client.getAppPassword(),
                user.getToken(), albumId, state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<VirtualAlbum> albumResponse = new Response<VirtualAlbum>();
                        if (response != null) {
                            if (response.completed) {
                                // Retrieve just a string
                                VirtualPhotoDataResponse responseData = getJson(response.response,
                                        VirtualPhotoDataResponse.class);
                                if (responseData != null) {
                                    VirtualAlbum album = new VirtualAlbum(client, user, albumData);
                                    album.addPictures(responseData.data);
                                    albumResponse.setResult(album);
                                } else {
                                    albumResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                albumResponse.setThrowable(response.exception);
                            }
                        } else {
                            albumResponse.setThrowable(new ServiceUnknownErrorException());
                        }
                        callback.OnResponse(albumResponse, state);
                    }

                });
    }

    public void getMy(Object state, final OnCallback<ListResponse<Integer>> callback) {
        
        BuddyWebWrapper.Pictures_VirtualAlbum_GetMyAlbums(client.getAppName(),
                client.getAppPassword(), user.getToken(), this.RESERVED, state,
                new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<Integer> listResponse = new ListResponse<Integer>();
                        if (response != null) {
                            if (response.completed) {
                                VirtualAlbumDataResponse responseData = getJson(response.response,
                                        VirtualAlbumDataResponse.class);
                                if (responseData != null) {
                                    List<Integer> intList = new ArrayList<Integer>(
                                            responseData.data.size());
                                    for (VirtualAlbumData data : responseData.data) {
                                        intList.add(Utils.parseInt(data.VirtualAlbumID));
                                    }
                                    listResponse.setList(intList);
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
