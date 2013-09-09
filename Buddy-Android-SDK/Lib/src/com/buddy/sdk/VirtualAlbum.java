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
import java.util.Date;
import java.util.List;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.json.responses.VirtualAlbumDataResponse.VirtualAlbumData;
import com.buddy.sdk.json.responses.VirtualPhotoDataResponse.VirtualPhotoData;

import com.buddy.sdk.responses.Response;
import com.buddy.sdk.utils.Utils;

/**
 * Represents a single virtual album. Unlike normal photo albums any user may
 * add existing photos to a virtual album. Only the owner of the virtual album
 * can delete the album however.
 */
public class VirtualAlbum {
    protected AuthenticatedUser user;
    protected BuddyClient client;
    private List<PicturePublic> pictures;
    private List<PicturePublic> readOnlyPictures;
    private VirtualAlbumDataModel albumDataModel;
    private int ID;
    private String name;
    private String thumbnailUrl;
    private int ownerUserId;
    private String applicationTag;
    private Date createdOn;
    private Date lastUpdated;

    VirtualAlbum(BuddyClient client, AuthenticatedUser user, VirtualAlbumData info) {
        this.client = client;
        this.user = user;
        this.pictures = new ArrayList<PicturePublic>();

        this.ID = Integer.valueOf(info.VirtualAlbumID);
        this.applicationTag = info.ApplicationTag;
        this.createdOn = Utils.convertDateString(info.CreatedDateTime, "MM/dd/yyyy hh:mm:ss aa");
        this.lastUpdated = Utils.convertDateString(info.LastUpdatedDateTime,
                "MM/dd/yyyy hh:mm:ss aa");
        this.name = info.PhotoAlbumName;
        this.ownerUserId = Utils.parseInt(info.UserID);
        this.thumbnailUrl = info.PhotoAlbumThumbnail;

        this.albumDataModel = new VirtualAlbumDataModel(this.client, this.user);
    }

    /**
     * Gets the globally unique ID of the virtual album.
     */
    public int getId() {
        return this.ID;
    }

    /**
     * Gets the name of the virtual album.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the thumbnail for the virtual album.
     */
    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    /**
     * Gets the user ID of the owner of this virtual album.
     */
    public int getOwnerUserId() {
        return this.ownerUserId;
    }

    /**
     * Gets the optional application tag for this virtual album.
     */
    public String getApplicationTag() {
        return this.applicationTag;
    }

    /**
     * Gets the date this virtual album was created.
     */
    public Date getCreatedOn() {
        return this.createdOn;
    }

    /**
     * Gets the date this virtual album was last updated.
     */
    public Date getLastUpdated() {
        return this.lastUpdated;
    }

    public List<PicturePublic> getPictures() {
        readOnlyPictures = Collections.unmodifiableList(this.pictures);
        return this.readOnlyPictures;
    }

    void addPictures(List<VirtualPhotoData> photoData) {
        for (VirtualPhotoData data : photoData) {
            PicturePublic picture = new PicturePublic(this.client, data);
            this.pictures.add(picture);
        }
    }

    /**
     * Delete this virtual album.
     * 
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void delete(Object state, OnCallback<Response<Boolean>> callback) {
        if (this.albumDataModel != null) {
            this.albumDataModel.delete(this.ID, state, callback);
        }
    }

    /**
     * Add an existing (uploaded) photo to a virtual album. This photo can be
     * either private or public (either PicturePublic and Picture will work).
     * 
     * @param picture The picture to add to the virtual albums. Either
     *            PicturePublic or Picture works.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void addPicture(PicturePublic picture, Object state,
            OnCallback<Response<Boolean>> callback) {
        if (picture == null)
            throw new IllegalArgumentException("picture can't be null.");

        if (this.albumDataModel != null) {
            this.albumDataModel.addPicture(this.ID, picture.getPhotoId(), state, callback);
        }
    }

    /**
     * Add a list of pictures to this virtual album.
     * 
     * @param pictures The list of pictures to add to this photo album. Either
     *            PicturePublic or Picture works.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void addPictureBatch(List<PicturePublic> pictures, Object state,
            OnCallback<Response<Boolean>> callback) {
        if (pictures == null || pictures.size() == 0)
            throw new IllegalArgumentException("pictures can't be null or empty.");

        if (this.albumDataModel != null) {
            this.albumDataModel.addPictureBatch(this.ID, pictures, state, callback);
        }
    }

    /**
     * Remove a picture from this virtual album.
     * 
     * @param picture The picture to add to the virtual albums. Either
     *            PicturePublic or Picture works.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void removePicture(PicturePublic picture, Object state,
            OnCallback<Response<Boolean>> callback) {
        if (picture == null)
            throw new IllegalArgumentException("picture can't be null.");

        if (this.albumDataModel != null) {
            this.albumDataModel.removePicture(this.ID, picture.getPhotoId(), state, callback);
        }
    }

    /**
     * Update this virtual album's name and application tag.
     * 
     * @param newName The new name for the album.
     * @param newAppTag An optional new application tag for the album.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void update(String newName, String newAppTag, Object state,
            OnCallback<Response<Boolean>> callback) {
    	if (Utils.isNullOrEmpty(newName))
    		throw new IllegalArgumentException("newName can't be null or empty.");
    	
    	if (this.albumDataModel != null) {
            this.albumDataModel.update(this.ID, newName, newAppTag, state, callback);
        }
    }

    /**
     * Update this virtual album's name and application tag
     * 
     * @param newName The new name for the album.
     * @param callback The async callback to call on success or error.
     */
    public void update(String newName, OnCallback<Response<Boolean>> callback) {
        update(newName, "", null, callback);
    }

    /**
     * Update virtual album picture comment or application tag.
     * 
     * @param picture The picture to add to the virtual albums. Either
     *            PicturePublic or Picture works.
     * @param newComment The new comment to set for the picture.
     * @param newAppTag An optional new application tag for the picture.
     * @param state An optional user defined object that will be passed to the
     *            callback.
     * @param callback The async callback to call on success or error.
     */
    public void updatePicture(PicturePublic picture, String newComment, String newAppTag,
            Object state, OnCallback<Response<Boolean>> callback) {
        if (picture == null)
            throw new IllegalArgumentException("picture can't be null.");

        if (this.albumDataModel != null) {
            this.albumDataModel.updatePicture(picture.getPhotoId(), newComment, newAppTag, state,
                    callback);
        }
    }

    /**
     * Update virtual album picture comment or application tag.
     * 
     * @param picture The picture to add to the virtual albums. Either
     *            PicturePublic or Picture works.
     * @param newComment The new comment to set for the picture.
     * @param callback The async callback to call on success or error.
     */
    public void updatePicture(PicturePublic picture, String newComment, OnCallback<Response<Boolean>> callback) {
        updatePicture(picture, newComment, "", null, callback);
    }
}
