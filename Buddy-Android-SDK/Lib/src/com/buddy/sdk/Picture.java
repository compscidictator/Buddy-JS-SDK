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

import java.util.Date;
import java.util.Map;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.Response;

/**
 * Represents a single picture on the Buddy Platform. Pictures can be accessed
 * through an AuthenticatedUser, either by using the PhotoAlbums property to
 * retrieve Pictures that belong to the user, or using the SearchForAlbums
 * method to find public Pictures.
 * <p>
 * 
 * <pre>
 * {@code private AuthenticatedUser user = null;}
 * 
 *    {@code BuddyClient client = new BuddyClient("APPNAME", "APPPASS");}
 *    {@code client.login("username", "password", new OnCallback<Response<AuthenticatedUser>>()}
 *    <code>{</code>
 *        {@code public void OnResponse(Response<AuthenticatedUser> response, Object state)}
 *        <code>{</code>
 *            {@code user = response.getResult();}
 *            {@code user.searchForAlbums(100, 0, 0, 10, null, new OnCallback<Response<Collection<PhotoAlbumPublic>>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<Collection<PhotoAlbumPublic>> response, Object state)}
 *                <code>{</code>
 *                    {@code Collection<PhotoAlbumPublic> albums = reponse.getResult();}
 *                    {@code Picture pic = albums.get(0);}
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class Picture extends PicturePublic {
    private PicturesDataModel pictureDataModel;

    public Picture(BuddyClient client, AuthenticatedUser user, String fullUrl, String thumbnailUrl,
            double latitude, double longitude, String comment, String appTag, Date addedDate,
            int photoId) {
        super(client, fullUrl, thumbnailUrl, latitude, longitude, comment, appTag, addedDate,
                photoId, user);

        this.pictureDataModel = new PicturesDataModel(client, user);
    }

    /**
     * Delete this picture. Note that this object will no longer be valid after
     * this method is called. Subsequent calls will fail.
     * 
     * @param callback The async callback to call on success or error.
     */
    public void delete(Object state, OnCallback<Response<Boolean>> callback) {
        if (this.pictureDataModel != null) {
            this.pictureDataModel.deletePhoto(this.photoId, state, callback);
        }
    }
    
    /**
     * Apply a filter to this picture. A new picture is created and returned
     * after the filter is applied.
     * 
     * @param filterName The name of the filter to apply. Cant't be null or
     *            empty.
     * @param filterParams A semi-colon separated list of filter parameter names
     *            and values. For example: "CropLeft=30;CropRight=40."
     * @param callback The async callback to call on success or error.
     */
    public void applyFilter(String filterName, String filterParams, Object state,
            final OnCallback<Response<Picture>> callback) {
        if (this.pictureDataModel != null) {
            this.pictureDataModel.applyFilter(this.getPhotoId(), filterName, filterParams, state,
                    callback);
        }
    }

    /**
     * Returns a list of supported filters that can be applied to this picture.
     * Example filters are: Hue Shift, Crop, etc.
     * 
     * @param callback The async callback to call on success or error.
     */
    public void supportedFilters(Object state, OnCallback<Response<Map<String, String>>> callback) {
        if (this.pictureDataModel != null) {
            this.pictureDataModel.supportedFilters(state, callback);
        }
    }

    /**
     * Sets the AppTag (metadata) for an existing photo in an existing photo
     * album, for the specified Photo ID.
     * 
     * @param appTag The metadata to save with the photo.
     * @param callback The async callback to call on success or error.
     */
    public void setAppTag(String appTag, Object state, OnCallback<Response<Boolean>> callback) {
        if (this.pictureDataModel != null) {
            this.pictureDataModel.setAppTag(this.getPhotoId(), appTag, state, callback);
        }
    }
}
