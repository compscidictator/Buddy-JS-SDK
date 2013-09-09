/* Copyright (C) 2012 Buddy Platform, Inc.
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

package com.buddy.sdk.unittests;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.graphics.drawable.Drawable;

import com.buddy.sdk.PhotoAlbumPublic;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.PicturePublic;

import com.buddy.sdk.responses.Response;

import com.buddy.sdk.unittests.utils.Utils;
import com.buddy.sdk.utils.Constants;
import com.buddy.sdk.web.BuddyHttpClientFactory;
import com.buddy.sdk.PhotoAlbum;
import com.buddy.sdk.Picture;

import com.buddy.sdk.unittests.R;

public class PicturesUnitTests extends BaseUnitTest {
    private static int testAlbumId = 2196998;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createAuthenticatedUser();
    }

    public void testPhotoAlbumCreate() {
        String jsonResponse = readDataFromFile("DataResponses/PictureUnitTests-CreateAlbum.json");
        String jsonGetResponse = readDataFromFile("DataResponses/PictureUnitTests-GetAlbum.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonGetResponse);

        testAuthUser.getPhotoAlbums().create(testAlbumName, testPublicAlbumBit,
                testAlbumApplicationTag, null, new OnCallback<Response<PhotoAlbum>>() {
                    public void OnResponse(Response<PhotoAlbum> response, Object state) {
                        PhotoAlbum album = response.getResult();
                        assertEquals(3182262, album.getAlbumId());
                    }

                });
    }

    public void testPhotoAlbumDelete() {
        String jsonResponse = readDataFromFile("DataResponses/PictureUnitTests-GetAlbum.json");
        String jsonDeleteResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonDeleteResponse);

        testAuthUser.getPhotoAlbums().get(testAlbumId, null,
                new OnCallback<Response<PhotoAlbum>>() {

                    @Override
                    public void OnResponse(Response<PhotoAlbum> response, Object state) {
                        assertNotNull(response);

                        PhotoAlbum album = response.getResult();
                        album.delete(null, new OnCallback<Response<Boolean>>() {
                            public void OnResponse(Response<Boolean> response, Object state) {
                                assertNotNull(response);
                                assertTrue(response.getResult());
                            }

                        });
                    }

                });
    }

    public void testPhotoAlbumGet() {
        String jsonResponse = readDataFromFile("DataResponses/PictureUnitTests-GetAlbum.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getPhotoAlbums().get(testAlbumId, null,
                new OnCallback<Response<PhotoAlbum>>() {

                    @Override
                    public void OnResponse(Response<PhotoAlbum> response, Object state) {
                        assertNotNull(response);

                        PhotoAlbum album = response.getResult();
                        List<Picture> pictures = album.getPictures();
                        assertNotNull(pictures);

                        Picture pic = pictures.get(0);
                        assertEquals("Test Photo", pic.getComment());
                    }

                });
    }

    public void testPhotoAlbumGetByName() {
        String jsonAlbumList = readDataFromFile("DataResponses/PictureUnitTests-GetAlbumList.json");
        String jsonPhotoList = readDataFromFile("DataResponses/PictureUnitTests-GetAlbumPhotos.json");

        BuddyHttpClientFactory.addDummyResponse(jsonAlbumList);
        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);

        testAuthUser.getPhotoAlbums().get(testAlbumName, null,
                new OnCallback<Response<PhotoAlbum>>() {
                    public void OnResponse(Response<PhotoAlbum> response, Object state) {
                        assertNotNull(response);

                        PhotoAlbum album = response.getResult();
                        List<Picture> pictures = album.getPictures();
                        assertNotNull(pictures);

                        Picture pic = pictures.get(0);
                        assertEquals(4541022, pic.getPhotoId());
                    }
                });
    }

    public void testPhotoAddWithWatermark() {
        String jsonResponse = readDataFromFile("DataResponses/PictureUnitTests-GetAlbum.json");
        String jsonAddResponse = readDataFromFile("DataResponses/PictureUnitTests-AddPhoto.json");
        String jsonGetResponse = readDataFromFile("DataResponses/PictureUnitTests-GetPhoto.json");

        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonAddResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonGetResponse);

        testAuthUser.getPhotoAlbums().get(testAlbumId, null,
                new OnCallback<Response<PhotoAlbum>>() {

                    @Override
                    public void OnResponse(Response<PhotoAlbum> response, Object state) {
                        assertNotNull(response);

                        PhotoAlbum album = response.getResult();
                        Drawable image = getInstrumentation().getTargetContext().getResources()
                                .getDrawable(R.drawable.ic_launcher);
                        testImageBytes = Utils.getEncodedImageBytes(image);

                        // Need to create our Picture class
                        album.addPictureWithWatermark(testImageBytes, testPhotoComment,
                                testLatitude, testLongitude, testWatermarkMessage, null,
                                new OnCallback<Response<Picture>>() {

                                    @Override
                                    public void OnResponse(Response<Picture> response, Object state) {
                                        assertNotNull(response);

                                        Picture pic = response.getResult();
                                        assertEquals(6498650, pic.getPhotoId());
                                    }

                                });
                    }

                });
    }

    public void testPhotoAdd() {
        String jsonResponse = readDataFromFile("DataResponses/PictureUnitTests-GetAlbum.json");
        String jsonAddResponse = readDataFromFile("DataResponses/PictureUnitTests-AddPhoto.json");
        String jsonGetResponse = readDataFromFile("DataResponses/PictureUnitTests-GetPhoto.json");

        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonAddResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonGetResponse);

        testAuthUser.getPhotoAlbums().get(testAlbumId, null,
                new OnCallback<Response<PhotoAlbum>>() {

                    @Override
                    public void OnResponse(Response<PhotoAlbum> response, Object state) {
                        assertNotNull(response);

                        PhotoAlbum album = response.getResult();
                        Drawable image = getInstrumentation().getTargetContext().getResources()
                                .getDrawable(R.drawable.ic_launcher);
                        testImageBytes = Utils.getEncodedImageBytes(image);

                        // Need to create our Picture class
                        album.addPicture(testImageBytes, testPhotoComment, testLatitude,
                                testLongitude, null, new OnCallback<Response<Picture>>() {

                                    @Override
                                    public void OnResponse(Response<Picture> response, Object state) {
                                        assertNotNull(response);

                                        Picture pic = response.getResult();
                                        assertEquals(6498650, pic.getPhotoId());
                                    }

                                });
                    }

                });
    }

    public void testPhotoAddIncorrectEscape() {
        String jsonResponse = readDataFromFile("DataResponses/PictureUnitTests-GetAlbum.json");
        String jsonAddResponse = readDataFromFile("DataResponses/GenericHTTP400Response.json");

        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonAddResponse);

        testAuthUser.getPhotoAlbums().get(testAlbumId, null,
                new OnCallback<Response<PhotoAlbum>>() {

                    @Override
                    public void OnResponse(Response<PhotoAlbum> response, Object state) {
                        assertNotNull(response);

                        PhotoAlbum album = response.getResult();
                        Drawable image = getInstrumentation().getTargetContext().getResources()
                                .getDrawable(R.drawable.ic_launcher);
                        testImageBytes = Utils.getEncodedImageBytes(image);

                        // Need to create our Picture class
                        album.addPicture(testImageBytes, testPhotoComment, testLatitude,
                                testLongitude, null, new OnCallback<Response<Picture>>() {

                                    @Override
                                    public void OnResponse(Response<Picture> response, Object state) {
                                        assertNotNull(response);
                                        String errorMessage = response.getThrowable().getMessage();
                                        assertEquals("HTTP 400 Response", errorMessage);
                                    }

                                });
                    }

                });
    }

    public void testPhotoAddIncorrectPhotoAlbumId() {
        String jsonResponse = readDataFromFile("DataResponses/PictureUnitTests-GetAlbum.json");
        String jsonAddResponse = readDataFromFile("DataResponses/PictureUnitTests-BadAlbumId.json");

        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonAddResponse);

        testAuthUser.getPhotoAlbums().get(testAlbumId, null,
                new OnCallback<Response<PhotoAlbum>>() {

                    @Override
                    public void OnResponse(Response<PhotoAlbum> response, Object state) {
                        assertNotNull(response);

                        PhotoAlbum album = response.getResult();
                        Drawable image = getInstrumentation().getTargetContext().getResources()
                                .getDrawable(R.drawable.ic_launcher);
                        testImageBytes = Utils.getEncodedImageBytes(image);

                        // Need to create our Picture class
                        album.addPicture(testImageBytes, testPhotoComment, testLatitude,
                                testLongitude, null, new OnCallback<Response<Picture>>() {

                                    @Override
                                    public void OnResponse(Response<Picture> response, Object state) {
                                        assertNotNull(response);
                                        String errorMessage = response.getThrowable().getMessage();
                                        assertEquals("PhotoAlbumDoesNotExist", errorMessage);
                                    }

                                });
                    }

                });
    }

    public void testPhotoSetAppTag() {
        String jsonPhotoList = readDataFromFile("DataResponses/PictureUnitTests-GetAllPhotos.json");
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getPhotoAlbums().getAll(null, null,
                new OnCallback<Response<Collection<PhotoAlbum>>>() {
                    public void OnResponse(Response<Collection<PhotoAlbum>> response, Object state) {
                        assertNotNull(response);

                        Collection<PhotoAlbum> list = response.getResult();
                        assertNotNull(list);

                        Iterator<PhotoAlbum> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            PhotoAlbum album = iterator.next();
                            List<Picture> pictures = album.getPictures();
                            assertNotNull(pictures);

                            Picture pic = pictures.get(0);
                            pic.setAppTag(testPhotoIdTag, null,
                                    new OnCallback<Response<Boolean>>() {
                                        public void OnResponse(Response<Boolean> response,
                                                Object state) {
                                            assertNotNull(response);
                                            assertTrue(response.getResult());
                                        }

                                    });
                            break;
                        }
                    }
                });
    }

    public void testPhotoAlbumGetAllPictures() {
        String jsonPhotoList = readDataFromFile("DataResponses/PictureUnitTests-GetAllPhotos.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);

        testAuthUser.getPhotoAlbums().getAll(null, null,
                new OnCallback<Response<Collection<PhotoAlbum>>>() {
                    public void OnResponse(Response<Collection<PhotoAlbum>> response, Object state) {
                        assertNotNull(response);

                        Collection<PhotoAlbum> list = response.getResult();
                        assertNotNull(list);

                        Iterator<PhotoAlbum> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            PhotoAlbum album = iterator.next();
                            List<Picture> pictures = album.getPictures();
                            assertNotNull(pictures);

                            Picture pic = pictures.get(0);
                            assertEquals("Test Photo", pic.getComment());
                            break;
                        }
                    }
                });
    }

    public void testPhotoAlbumGetAllPicturesNoAlbums() {
        String jsonPhotoList = readDataFromFile("DataResponses/PictureUnitTests-BadAlbumId.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);

        testAuthUser.getPhotoAlbums().getAll(null, null,
                new OnCallback<Response<Collection<PhotoAlbum>>>() {
                    public void OnResponse(Response<Collection<PhotoAlbum>> response, Object state) {
                        assertNotNull(response);

                        assertNotNull(response);
                        String errorMessage = response.getThrowable().getMessage();
                        assertEquals("PhotoAlbumDoesNotExist", errorMessage);
                    }
                });
    }

    public void testPhotoAlbumGetAllPicturesNoResults() {
        String jsonPhotoList = readDataFromFile("DataResponses/GenericEmptyResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);

        testAuthUser.getPhotoAlbums().getAll(null, null,
                new OnCallback<Response<Collection<PhotoAlbum>>>() {
                    public void OnResponse(Response<Collection<PhotoAlbum>> response, Object state) {
                        assertNotNull(response);

                        Collection<PhotoAlbum> list = response.getResult();
                        assertNotNull(list);
                        assertTrue(list.size() == 0);
                    }
                });
    }

    public void testAlbumDelete() {
        String jsonResponse = readDataFromFile("DataResponses/PictureUnitTests-GetAlbum.json");
        String jsonDeleteResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonDeleteResponse);

        testAuthUser.getPhotoAlbums().get(testAlbumId, null,
                new OnCallback<Response<PhotoAlbum>>() {

                    @Override
                    public void OnResponse(Response<PhotoAlbum> response, Object state) {
                        assertNotNull(response);

                        PhotoAlbum album = response.getResult();
                        album.delete(null, new OnCallback<Response<Boolean>>() {
                            public void OnResponse(Response<Boolean> response, Object state) {
                                assertNotNull(response);
                                assertTrue(response.getResult());
                            }

                        });
                    }

                });
    }

    public void testSearchForAlbums() {
        String jsonPhotoList = readDataFromFile("DataResponses/PictureUnitTests-SearchAlbum.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);

        testAuthUser.searchForAlbums(testSearchDistance, testSearchLatitude, testSearchLongitude,
                testMaxSearchRecords, null,
                new OnCallback<Response<Collection<PhotoAlbumPublic>>>() {
                    public void OnResponse(Response<Collection<PhotoAlbumPublic>> response,
                            Object state) {
                        assertNotNull(response);

                        Collection<PhotoAlbumPublic> list = response.getResult();
                        assertNotNull(list);

                        Iterator<PhotoAlbumPublic> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            PhotoAlbumPublic album = iterator.next();
                            List<PicturePublic> pictures = album.getPublicPictures();
                            assertNotNull(pictures);

                            PicturePublic pic = pictures.get(0);
                            assertEquals(6078641, pic.getPhotoId());
                            break;
                        }
                    }
                });
    }

    public void testPicturesGetFilterList() {
        String jsonPhotoList = readDataFromFile("DataResponses/PictureUnitTests-GetAllPhotos.json");
        String jsonResponse = readDataFromFile("DataResponses/PictureUnitTests-GetFilters.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getPhotoAlbums().getAll(Constants.MinDate, null,
                new OnCallback<Response<Collection<PhotoAlbum>>>() {
                    public void OnResponse(Response<Collection<PhotoAlbum>> response, Object state) {
                        assertNotNull(response);

                        Collection<PhotoAlbum> list = response.getResult();
                        assertNotNull(list);

                        Iterator<PhotoAlbum> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            PhotoAlbum album = iterator.next();
                            List<Picture> pictures = album.getPictures();
                            assertNotNull(pictures);

                            Picture pic = pictures.get(0);
                            pic.supportedFilters(null,
                                    new OnCallback<Response<Map<String, String>>>() {

                                        public void OnResponse(
                                                Response<Map<String, String>> response, Object state) {
                                            Map<String, String> map = response.getResult();
                                            assertNotNull(map);

                                            String value = map.get("Meme Generator");
                                            assertTrue(value.contains("Text Top"));
                                        }
                                    });
                            break;
                        }
                    }
                });
    }

    public void testPicturesApplyFilter() {
        String jsonPhotoList = readDataFromFile("DataResponses/PictureUnitTests-GetAllPhotos.json");
        String jsonResponse = readDataFromFile("DataResponses/PictureUnitTests-ApplyFilter.json");
        String jsonGetResponse = readDataFromFile("DataResponses/PictureUnitTests-GetPhoto.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonGetResponse);

        testAuthUser.getPhotoAlbums().getAll(Constants.MinDate, null,
                new OnCallback<Response<Collection<PhotoAlbum>>>() {
                    public void OnResponse(Response<Collection<PhotoAlbum>> response, Object state) {
                        assertNotNull(response);

                        Collection<PhotoAlbum> list = response.getResult();
                        assertNotNull(list);

                        Iterator<PhotoAlbum> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            PhotoAlbum album = iterator.next();
                            List<Picture> pictures = album.getPictures();
                            assertNotNull(pictures);

                            Picture pic = pictures.get(0);
                            pic.applyFilter("Basic Operations", "Crop Left=20;Crop Right=30", null,
                                    new OnCallback<Response<Picture>>() {

                                        public void OnResponse(Response<Picture> response,
                                                Object state) {
                                            Picture picture = response.getResult();
                                            assertEquals(6498650, picture.getPhotoId());
                                        }
                                    });
                            break;
                        }
                    }
                });
    }
    
    public void testPhotoDelete() {
        String jsonPhotoList = readDataFromFile("DataResponses/PictureUnitTests-GetAllPhotos.json");
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getPhotoAlbums().getAll(null, null,
                new OnCallback<Response<Collection<PhotoAlbum>>>() {
                    public void OnResponse(Response<Collection<PhotoAlbum>> response, Object state) {
                        assertNotNull(response);

                        Collection<PhotoAlbum> list = response.getResult();
                        assertNotNull(list);

                        Iterator<PhotoAlbum> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            PhotoAlbum album = iterator.next();
                            List<Picture> pictures = album.getPictures();
                            assertNotNull(pictures);

                            Picture pic = pictures.get(0);
                            pic.delete(null, new OnCallback<Response<Boolean>>() {
                                public void OnResponse(Response<Boolean> response, Object state) {
                                    assertNotNull(response);
                                    assertTrue(response.getResult());
                                }

                            });
                            break;
                        }
                    }
                });
    }
}
