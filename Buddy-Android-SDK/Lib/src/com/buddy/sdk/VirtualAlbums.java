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
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;

/**
 * Represents a class that can be used to interact with virtual albums. Unlike
 * normal photo albums any user may add existing photos to a virtual album. Only
 * the owner of the virtual album can delete the album however.
 */
public class VirtualAlbums {
    protected BuddyClient client;
    protected AuthenticatedUser user;

    private VirtualAlbumDataModel albumDataModel;

    VirtualAlbums(BuddyClient client, AuthenticatedUser user) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");

        this.client = client;
        this.user = user;

        this.albumDataModel = new VirtualAlbumDataModel(this.client, this.user);
    }

    /**
     * Create a new virtual album.
     * 
     * @param name The name of the new virtual album.
     * @param appTag An optional application tag for the album.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void create(String name, String appTag, Object state,
            OnCallback<Response<VirtualAlbum>> callback) {
        if (this.albumDataModel != null) {
            this.albumDataModel.create(name, appTag, state, callback);
        }
    }

    /**
     * Create a new virtual album. Note that this method internally does two
     * webservice calls, and the IAsyncResult object returned is only valid for
     * the first one.
     * 
     * @param name The name of the new virtual album.
     * @param callback The async callback to call on success or error.
     */
    public void create(String name, OnCallback<Response<VirtualAlbum>> callback) {
        create(name, "", null, callback);
    }

    /**
     * Get a virtual album by its globally unique identifier. All the album
     * photos will be retrieved as well.
     * 
     * @param albumId The ID of the virtual album to retrieve.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void get(int albumId, Object state, OnCallback<Response<VirtualAlbum>> callback) {
        if (this.albumDataModel != null) {
            this.albumDataModel.get(albumId, state, callback);
        }
    }

    /**
     * Get the IDs of all the virtual albums that this user owns.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void getMy(Object state, OnCallback<ListResponse<Integer>> callback) {
        if (this.albumDataModel != null) {
            this.albumDataModel.getMy(state, callback);
        }
    }
}
