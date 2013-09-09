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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.FilterListDataResponse;
import com.buddy.sdk.json.responses.PictureDataResponse;
import com.buddy.sdk.json.responses.PictureDataResponse.PictureData;
import com.buddy.sdk.json.responses.SearchPictureDataResponse;

import com.buddy.sdk.responses.Response;

import com.buddy.sdk.utils.Constants;
import com.buddy.sdk.utils.Utils;
import com.buddy.sdk.web.BuddyWebWrapper;

class PicturesDataModel extends BaseDataModel {
    private AuthenticatedUser user = null;

    PicturesDataModel(BuddyClient client, AuthenticatedUser user) {
        this.client = client;
        this.user = user;
    }

    public void getAll(Date searchTime, Object state,
            final OnCallback<Response<Collection<PhotoAlbum>>> callback) {
        
        String dateParam = searchTime == null ? Utils.convertStringDate(Constants.MinDate,
                "MM/dd/yyyy") : Utils.convertStringDate(searchTime, "MM/dd/yyyy");
        BuddyWebWrapper.Pictures_PhotoAlbum_GetAllPictures(client.getAppName(),
                client.getAppPassword(), user.getToken(), user.getId(), dateParam, state,
                new OnResponseCallback() {
        	
        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Collection<PhotoAlbum>> pictureResponse = new Response<Collection<PhotoAlbum>>();
                        if (response != null) {
                            if (response.completed) {
                                PictureDataResponse data = getJson(response.response,
                                        PictureDataResponse.class);
                                if (data != null) {
                                    Collection<PhotoAlbum> albums = createAlbumsFromPictureData(data);

                                    pictureResponse.setResult(albums);
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

    public void get(final String albumName, Object state,
            final OnCallback<Response<PhotoAlbum>> callback) {
        
        BuddyWebWrapper.Pictures_PhotoAlbum_GetFromAlbumName(client.getAppName(),
                client.getAppPassword(), user.getToken(), user.getId(), albumName, state,
                new OnResponseCallback() {
        	
        			@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<PhotoAlbum> albumResponse = new Response<PhotoAlbum>();

                        boolean passedResponse = false;

                        if (response != null) {
                            if (response.completed) {
                                PictureDataResponse result = getJson(response.response,
                                        PictureDataResponse.class);
                                if (result != null) {
                                    List<PictureData> results = result.data;

                                    if (results.size() > 0) {
                                        int albumID = Utils.parseInt(results.get(0).albumId);

                                        passedResponse = true;

                                        user.getPhotoAlbums().get(albumID, state, callback);
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

                        if (passedResponse == false)
                            callback.OnResponse(albumResponse, state);
                    }
                });
    }

    public void get(final int albumId, Object state, final OnCallback<Response<PhotoAlbum>> callback) {
        
        BuddyWebWrapper.Pictures_PhotoAlbum_Get(client.getAppName(), client.getAppPassword(),
                user.getToken(), user.getId(), albumId, state, new OnResponseCallback() {
        	
					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<PhotoAlbum> albumResponse = new Response<PhotoAlbum>();

                        if (response != null) {
                            if (response.completed) {
                                PictureDataResponse result = getJson(response.response,
                                        PictureDataResponse.class);
                                if (result != null) {
                                    PhotoAlbum album = new PhotoAlbum(client, user, albumId);

                                    for (PictureDataResponse.PictureData photoListItem : result.data) {
                                        double lat = Utils.parseDouble(photoListItem.latitude);
                                        double lon = Utils.parseDouble(photoListItem.longitude);

                                        int photoId = Utils.parseInt(photoListItem.photoId);

                                        Picture photoListPic = new Picture(
                                                client,
                                                user,
                                                photoListItem.fullPhotoUrl,
                                                photoListItem.thumbnailUrl,
                                                lat,
                                                lon,
                                                photoListItem.photoComment,
                                                photoListItem.applicationTag,
                                                Utils.convertDateString(photoListItem.addedDateTime),
                                                photoId);

                                        album.addPicture(photoListPic);
                                    }

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

    public void deletePhoto(int photoId, Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Pictures_Photo_Delete(client.getAppName(), client.getAppPassword(),
                user.getToken(), photoId, state, new OnResponseCallback() {
        	
					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> deleteResponse = getBooleanResponse(response);
                        callback.OnResponse(deleteResponse, state);
                    }
                });
    }

    public void create(String albumName, boolean publicAlbumBit, String albumApplicationTag,
            Object state, final OnCallback<Response<PhotoAlbum>> callback) {
        
        BuddyWebWrapper.Pictures_PhotoAlbum_Create(client.getAppName(), client.getAppPassword(),
                user.getToken(), albumName, publicAlbumBit ? 1 : 0, albumApplicationTag,
                this.RESERVED, state, new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<PhotoAlbum> albumResponse = new Response<PhotoAlbum>();

                        boolean passedResponse = false;

                        if (response != null) {
                            if (response.completed) {
                                String result = getJson(response.response, String.class);
                                if (result != null && Utils.isNumeric(result)) {
                                	try {
	                                    passedResponse = true;
	
	                                    get(Integer.parseInt(result), state, callback);
                                	} 
                                	catch (NumberFormatException ex) {
                                		passedResponse = false;
                                		albumResponse.setThrowable(ex);
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

                        if (passedResponse == false)
                            callback.OnResponse(albumResponse, state);
                    }

                });

    }

    public void addPicture(int albumId, byte[] blob, String comment, double latitude,
            double longitude, Object state, final OnCallback<Response<Picture>> callback) {
        
        String encodedImage = new String(blob);
        BuddyWebWrapper.Pictures_Photo_Add(client.getAppName(), client.getAppPassword(),
                user.getToken(), encodedImage, albumId, comment, (float) latitude, (float) longitude,
                this.RESERVED, state, new OnResponseCallback() {
        	
					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        final Response<Picture> pictureResponse = new Response<Picture>();

                        boolean passedResponse = false;

                        if (response != null) {
                            if (response.completed) {
                                // Retrieve just a string
                                String result = getJson(response.response, String.class);
                                if (result != null) {
                                    int pictureId = Utils.parseInt(result);

                                    passedResponse = true;

                                    user.getPicture(pictureId, state, callback);
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

                        if (passedResponse == false)
                            callback.OnResponse(pictureResponse, state);

                    }
                });
    }

    public void addPictureWithWatermark(byte[] blob, int albumId, String photoComment,
            double latitude, double longitude, String watermarkMessage, Object state,
            final OnCallback<Response<Picture>> callback) {
        
        String encodedImage = new String(blob);
        BuddyWebWrapper.Pictures_Photo_AddWithWatermark(client.getAppName(),
                client.getAppPassword(), user.getToken(), encodedImage, albumId, photoComment,
                (float) latitude, (float) longitude, watermarkMessage, this.RESERVED, state,
                new OnResponseCallback() {

					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        final Response<Picture> pictureResponse = new Response<Picture>();

                        boolean passedResponse = false;

                        if (response != null) {
                            if (response.completed) {
                                // Retrieve just a string
                                String result = getJson(response.response, String.class);

                                if (result != null) {
                                    int pictureId = Utils.parseInt(result);

                                    passedResponse = true;

                                    user.getPicture(pictureId, state, callback);
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

                        if (passedResponse == false)
                            callback.OnResponse(pictureResponse, state);
                    }

                });
    }

    public void applyFilter(int photoID, String filterName, String filterParams, Object state,
            final OnCallback<Response<Picture>> callback) {
        
        BuddyWebWrapper.Pictures_Filters_ApplyFilter(client.getAppName(), client.getAppPassword(),
                user.getToken(), photoID, filterName, filterParams, 0, state,
                new OnResponseCallback() {
        	
					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        final Response<Picture> pictureResponse = new Response<Picture>();

                        boolean passedResponse = false;

                        if (response != null) {
                            if (response.completed) {
                                // Retrieve just a string
                                String result = getJson(response.response, String.class);
                                if (result != null) {
                                    int pictureId = Utils.parseInt(result);

                                    passedResponse = true;

                                    user.getPicture(pictureId, state, callback);
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

                        if (passedResponse == false)
                            callback.OnResponse(pictureResponse, state);
                    }

                });
    }

    public void supportedFilters(Object state,
            final OnCallback<Response<Map<String, String>>> callback) {
        
        BuddyWebWrapper.Pictures_Filters_GetList(client.getAppName(), client.getAppPassword(),
                state, new OnResponseCallback() {
        	
					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Map<String, String>> filtersResponse = new Response<Map<String, String>>();

                        if (response != null) {
                            if (response.completed) {
                                FilterListDataResponse result = getJson(response.response,
                                        FilterListDataResponse.class);
                                if (result != null) {
                                    Map<String, String> filterNameParamMap = new HashMap<String, String>();

                                    for (FilterListDataResponse.FilterListData filter : result.data) {
                                        filterNameParamMap.put(filter.filterName,
                                                filter.parameterList);
                                    }

                                    filtersResponse.setResult(filterNameParamMap);
                                } else {
                                    filtersResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                filtersResponse.setThrowable(response.exception);
                            }
                        } else {
                            filtersResponse.setThrowable(new ServiceUnknownErrorException());

                        }

                        callback.OnResponse(filtersResponse, state);
                    }
                });
    }

    public void setAppTag(int photoId, String appTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Pictures_Photo_SetAppTag(client.getAppName(), client.getAppPassword(),
                user.getToken(), user.getId(), photoId, appTag, state,
                new OnResponseCallback() {
        	
					@Override        	
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> setTagResponse = getBooleanResponse(response);
                        callback.OnResponse(setTagResponse, state);
                    }
                });
    }

    public void delete(int albumId, Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Pictures_PhotoAlbum_Delete(client.getAppName(), client.getAppPassword(),
                user.getToken(), albumId, state, new OnResponseCallback() {
        	
					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }
                });
    }

    public void searchForAlbums(int searchDistance, float latitude, float longitude,
            int recordLimit, Object state,
            final OnCallback<Response<Collection<PhotoAlbumPublic>>> callback) {
        
        BuddyWebWrapper.Pictures_SearchPhotos_Nearby(client.getAppName(), client.getAppPassword(),
                user.getToken(), searchDistance, latitude, longitude, recordLimit, state,
                new OnResponseCallback() {
        	
					@Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Collection<PhotoAlbumPublic>> pictureResponse = new Response<Collection<PhotoAlbumPublic>>();
                        if (response != null) {
                            if (response.completed) {
                                SearchPictureDataResponse data = getJson(response.response,
                                        SearchPictureDataResponse.class);
                                if (data != null) {
                                    Collection<PhotoAlbumPublic> albums = createSearchAlbumsFromPictureData(data);

                                    pictureResponse.setResult(albums);
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

    private Collection<PhotoAlbum> createAlbumsFromPictureData(PictureDataResponse data) {
        
        Map<String, PhotoAlbum> pictureMap = new HashMap<String, PhotoAlbum>();

        for (PictureDataResponse.PictureData pictureData : data.data) {
            Picture picture = new Picture(client, user, pictureData.fullPhotoUrl,
                    pictureData.thumbnailUrl, Utils.parseDouble(pictureData.latitude),
                    Utils.parseDouble(pictureData.longitude), pictureData.photoComment,
                    pictureData.applicationTag, Utils.convertDateString(pictureData.addedDateTime),
                    Integer.parseInt(pictureData.photoId));

            // We may get multiple albums of data
            if (!pictureMap.containsKey(pictureData.albumId)) {
                // Create the PhotoAlbum - Add the Picture to the PhotoAlbum and
                // add the Album to the Map
                PhotoAlbum album = new PhotoAlbum(client, user,
                        Integer.parseInt(pictureData.albumId));
                album.addPicture(picture);
                pictureMap.put(pictureData.albumId, album);
            } else {
                // Get the map entry for the AlbumId - create the Picture and
                // add to the album
                PhotoAlbum album = pictureMap.get(pictureData.albumId);
                if (album != null) {
                    album.addPicture(picture);
                    // Replace the map item
                    pictureMap.put(pictureData.albumId, album);
                }
            }
        }

        return pictureMap.values();
    }

    private Collection<PhotoAlbumPublic> createSearchAlbumsFromPictureData(
            SearchPictureDataResponse data) {
        
        Map<String, PhotoAlbumPublic> pictureMap = new HashMap<String, PhotoAlbumPublic>();

        for (SearchPictureDataResponse.SearchPictureData pictureData : data.data) {
            PicturePublic picture = new PicturePublic(client, null, pictureData, user.getId(), user);

            // We may get multiple albums of data
            if (!pictureMap.containsKey(pictureData.albumName)) {
                // Create the PhotoAlbum - Add the Picture to the PhotoAlbum and
                // add the Album to the Map
                PhotoAlbumPublic album = new PhotoAlbumPublic(client, user.getId(),
                        pictureData.albumName);
                album.addPublicPicture(picture);
                pictureMap.put(pictureData.albumName, album);
            } else {
                // Get the map entry for the AlbumId - create the Picture and
                // add to the album
                PhotoAlbumPublic album = pictureMap.get(pictureData.albumName);
                if (album != null) {
                    album.addPublicPicture(picture);
                    // Replace the map item
                    pictureMap.put(pictureData.albumName, album);
                }
            }
        }

        return pictureMap.values();
    }

}
