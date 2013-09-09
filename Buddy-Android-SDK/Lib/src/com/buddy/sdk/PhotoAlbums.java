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

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Constants;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a object that can be used to interact with an AuthenticatedUser's
 * photo albums.
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
 *            {@code user.getPhotoAlbums().getAll("", null, new OnCallback<Response<Collection<PhotoAlbum>>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<Collection<PhotoAlbum>> response, Object state)}
 *                <code>{</code>
 *                    {@code Collection<PhotoAlbum> albums = reponse.getResult();}
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class PhotoAlbums {
    private AuthenticatedUser user;
    private BuddyClient client;
    private PicturesDataModel pictureDataModel;

    PhotoAlbums(BuddyClient client, AuthenticatedUser user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");
        if (user == null)
            throw new IllegalArgumentException("user can't be null.");

        this.client = client;
        this.user = user;

        this.pictureDataModel = new PicturesDataModel(this.client, this.user);
    }

    /**
     * Get a photo album by ID. This album doesn't need to be owned by this
     * user.
     * 
     * @param albumId The ID of the album.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void get(int albumId, Object state, OnCallback<Response<PhotoAlbum>> callback) {
        if (this.pictureDataModel != null) {
            this.pictureDataModel.get(albumId, state, callback);
        }
    }

    /**
     * Get a photo album by its name. Note that there can be more than one album
     * with the same name. This method will only return the first one. Call
     * PhotoAlbums.All to get all the albums.
     * 
     * @param albumName The name of the album to retrieve. Can't be null or
     *            empty.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void get(String albumName, Object state, OnCallback<Response<PhotoAlbum>> callback) {
        if (this.pictureDataModel != null) {
            this.pictureDataModel.get(albumName, state, callback);
        }
    }

    /**
     * This method is used create a new album. The album will be owned by this
     * user. Multiple albums can be created with the same name.
     * 
     * @param name The name of the new album.
     * @param isPublic Make the album publicly visible to other users.
     * @param appTag Optionally add a custom application tag for this user.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void create(String name, boolean isPublic, String appTag, Object state,
            OnCallback<Response<PhotoAlbum>> callback) {
        if (this.pictureDataModel != null) {
            this.pictureDataModel.create(name, isPublic, appTag, state, callback);
        }
    }

    /**
     * This method is used create a new album. The album will be owned by this
     * user. Multiple albums can be created with the same name.
     * 
     * @param name The name of the new album.
     * @param callback The async callback to call on success or error.
     */
    public void create(String name, OnCallback<Response<PhotoAlbum>> callback) {
        create(name, false, "", null, callback);
    }

    /**
     * Return all photo albums for this user. Note that this can be a time-consuming
     * operation since all the Picture data is retrieved as well.
     * 
     * @param afterDate Optionally return all albums created after a date.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getAll(Date afterDate, Object state,
            OnCallback<Response<Collection<PhotoAlbum>>> callback) {
        if (this.pictureDataModel != null) {
            this.pictureDataModel.getAll(Utils.convertMinDateStringTo1950(afterDate), state, callback);
        }
    }
    
    /**
     * Return all photo albums for this user. Note that this can be a time-consuming
     * operation since all the Picture data is retrieved as well.
     * 
     * @param callback The async callback to call on success or error.
     */
    public void getAll(OnCallback<Response<Collection<PhotoAlbum>>> callback) {
        getAll(Constants.MinDate, null, callback);

    }
}
