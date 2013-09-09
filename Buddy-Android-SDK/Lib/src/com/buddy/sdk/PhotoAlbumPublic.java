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

/**
 * Represents a public photo album. Public albums are returned from album
 * searches.
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
 *                <code>}</code>
 *            <code>});</code>
 *        <code>}</code>
 *    <code>});</code>
 * </pre>
 */
public class PhotoAlbumPublic {
    protected AuthenticatedUser user;
    protected BuddyClient client;
    List<PicturePublic> pictures;
    private List<PicturePublic> readOnlyPictures;

    private int userId;
    private String albumName;

    PhotoAlbumPublic(BuddyClient client, int userId, String albumName) {
        if (client == null)
            throw new IllegalArgumentException("client can't be null.");

        this.client = client;
        this.albumName = albumName;
        this.userId = userId;

        this.pictures = new ArrayList<PicturePublic>();
    }
    
    /**
     * Gets the user ID of the user that owns this album.
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * Gets the name of the album.
     */
    public String getAlbumName() {
        return this.albumName;
    }

    /**
     * Get the list of Pictures in the Album.
     */
    public List<PicturePublic> getPublicPictures() {
        readOnlyPictures = Collections.unmodifiableList(this.pictures);
        return this.readOnlyPictures;
    }
    
    void addPublicPicture(PicturePublic picture) {
        this.pictures.add(picture);
    }
}
