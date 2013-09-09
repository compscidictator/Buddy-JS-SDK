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
import java.util.Collections;
import java.util.List;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.Response;

/**
 * Represent a single Buddy photo album. Albums are collections of photo that
 * can be manipulated by their owner (the user that created the album). Albums
 * can be public in which case other users can see them (but no modify).
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
 *            {@code user.getPhotoAlbums().get("Test Album", null, new OnCallback<Response<PhotoAlbum>>()}
 *            <code>{</code>
 *                {@code public void OnResponse(Response<PhotoAlbum> response, Object state)}
 *                <code>{</code>
 *                    {@code PhotoAlbum album = reponse.getResult();}
 *                    {@code album.addPicture(pictureBinaryData, "", 0, 0, null, null);}
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class PhotoAlbum extends PhotoAlbumPublic {
    private PicturesDataModel pictureDataModel;
    List<Picture> pictures;
    private List<Picture> readOnlyPictures;

    private int albumId;

    PhotoAlbum(BuddyClient client, AuthenticatedUser user, int albumId) {
        super(client, user.getId(), null);

        this.user = user;
        this.albumId = albumId;

        this.pictures = new ArrayList<Picture>();

        this.pictureDataModel = new PicturesDataModel(this.client, this.user);
    }

    /**
     * Get the album ID of Album.
     */
    public int getAlbumId() {
        return this.albumId;
    }

    /**
     * Get the list of Pictures in the Album.
     */
    public List<Picture> getPictures() {
        readOnlyPictures = Collections.unmodifiableList(this.pictures);
        return this.readOnlyPictures;
    }

    void addPicture(Picture picture) {
        this.pictures.add(picture);
    }

    /**
     * Add a new picture to this album.
     * 
     * @param blob The image byte array of the picture.
     * @param comment An optional comment for this picture.
     * @param latitude An optional latitude for the picture.
     * @param longitude An optional longitude for the picture.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void addPicture(byte[] blob, String comment, double latitude, double longitude,
            Object state, OnCallback<Response<Picture>> callback) {
        if (blob == null || blob.length == 0)
            throw new IllegalArgumentException("blob can't be null or empty.");

        if (this.pictureDataModel != null) {
            this.pictureDataModel.addPicture(this.albumId, blob, comment, latitude, longitude,
                    state, callback);
        }
    }

    /**
     * Add a new picture to this album.
     * 
     * @param blob The image byte array of the picture.
     * @param callback The async callback to call on success or error.
     */
    public void addPicture(byte[] blob, OnCallback<Response<Picture>> callback) {
        addPicture(blob, "", 0.0, 0.0, null, callback);
    }

    /**
     * Add a new picture to this album with a watermark.
     * 
     * @param blob The image byte array of the picture.
     * @param comment An optional comment for this picture.
     * @param latitude An optional latitude for the picture.
     * @param longitude An optional longitude for the picture.
     * @param watermarkMessage An optional message to watermark the image with.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void addPictureWithWatermark(byte[] blob, String comment, double latitude,
            double longitude, String watermarkMessage, Object state,
            OnCallback<Response<Picture>> callback) {
        if (blob == null || blob.length == 0)
            throw new IllegalArgumentException("blob can't be null or empty.");

        if (this.pictureDataModel != null) {
            this.pictureDataModel.addPictureWithWatermark(blob, this.albumId, comment, latitude,
                    longitude, watermarkMessage, state, callback);
        }
    }

    /**
     * Add a new picture to this album with a watermark.
     * 
     * @param blob The image byte array of the picture.
     * @param callback The async callback to call on success or error.
     */
    public void addPictureWithWatermark(byte[] blob, OnCallback<Response<Picture>> callback) {
        addPictureWithWatermark(blob, "", 0.0, 0.0, "", null, callback);
    }

    /**
     * Delete this photo album.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void delete(Object state, OnCallback<Response<Boolean>> callback) {
        if (this.pictureDataModel != null) {
            this.pictureDataModel.delete(this.albumId, state, callback);
        }
    }
}
