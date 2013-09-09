
package com.buddy.sdk.unittests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.buddy.sdk.PhotoAlbum;
import com.buddy.sdk.Picture;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.PicturePublic;
import com.buddy.sdk.VirtualAlbum;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.web.BuddyHttpClientFactory;

public class VirtualAlbumUnitTests extends BaseUnitTest {
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createAuthenticatedUser();
    }

    public void testVirtualAlbumCreate() {
        String jsonCreateResponse = readDataFromFile("DataResponses/VirtualAlbumUnitTests-CreateAlbum.json");
        String jsonAlbumInfo = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetAlbumInfo.json");
        String jsonPhotoList = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetEmptyAlbum.json");

        BuddyHttpClientFactory.addDummyResponse(jsonCreateResponse);
        BuddyHttpClientFactory.addDummyResponse(jsonAlbumInfo);
        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);

        testAuthUser.getVirtualAlbums().create(testVirtualAlbumName, "", null,
                new OnCallback<Response<VirtualAlbum>>() {
                    public void OnResponse(Response<VirtualAlbum> response, Object state) {
                        assertNotNull(response);

                        VirtualAlbum album = response.getResult();
                        assertEquals(27467, album.getId());
                    }
                });
    }

    public void testVirtualAlbumGetAlbumInformation() {
        String jsonAlbumInfo = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetAlbumInfo.json");
        String jsonPhotoList = readDataFromFile("DataResponses/VirtualAlbumUnitTests-MyAlbum.json");

        BuddyHttpClientFactory.addDummyResponse(jsonAlbumInfo);
        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);

        testAuthUser.getVirtualAlbums().get(27467, null, new OnCallback<Response<VirtualAlbum>>() {
            public void OnResponse(Response<VirtualAlbum> response, Object state) {
                assertNotNull(response);

                VirtualAlbum album = response.getResult();
                assertEquals(27467, album.getId());
            }
        });
    }

    public void testVirtualAlbumGetEmptyAlbumInformation() {
        String jsonAlbumInfo = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetEmptyAlbumInfo.json");

        BuddyHttpClientFactory.addDummyResponse(jsonAlbumInfo);

        testAuthUser.getVirtualAlbums().get(123456, null, new OnCallback<Response<VirtualAlbum>>() {
            public void OnResponse(Response<VirtualAlbum> response, Object state) {
                assertNotNull(response);

                VirtualAlbum album = response.getResult();
                assertNull(album);
            }
        });
    }

    public void testVirtualAlbumMyAlbums() {
        String jsonPhotoList = readDataFromFile("DataResponses/VirtualAlbumUnitTests-MyAlbum.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);

        testAuthUser.getVirtualAlbums().getMy(null, new OnCallback<ListResponse<Integer>>() {
            public void OnResponse(ListResponse<Integer> response, Object state) {
                assertNotNull(response);

                List<Integer> list = response.getList();
                assertNotNull(list);

                Integer item = list.get(0);
                Integer testInt = 12041;
                assertEquals(testInt, item);
            }
        });
    }

    public void testVirtualAlbumPhotoAdd() {
        String jsonPhotoList = readDataFromFile("DataResponses/PictureUnitTests-GetAllPhotos.json");
        String jsonAlbumList = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetAlbumInfo.json");
        String jsonEmptyAlbum = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetEmptyAlbum.json");
        String jsonAddPhoto = readDataFromFile("DataResponses/VirtualAlbumUnitTests-AddPhoto.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);
        BuddyHttpClientFactory.addDummyResponse(jsonAlbumList);
        BuddyHttpClientFactory.addDummyResponse(jsonEmptyAlbum);
        BuddyHttpClientFactory.addDummyResponse(jsonAddPhoto);

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

                            final PicturePublic pic = pictures.get(0);
                            testAuthUser.getVirtualAlbums().get(12041, null,
                                    new OnCallback<Response<VirtualAlbum>>() {
                                        public void OnResponse(Response<VirtualAlbum> response,
                                                Object state) {
                                            assertNotNull(response);

                                            VirtualAlbum album = response.getResult();
                                            album.addPicture(pic, null,
                                                    new OnCallback<Response<Boolean>>() {

                                                        public void OnResponse(
                                                                Response<Boolean> response,
                                                                Object state) {
                                                            assertNotNull(response);
                                                            assertTrue(response.getResult());
                                                        }
                                                    });
                                        }
                                    });
                            break;
                        }
                    }
                });
    }

    public void testVirtualAlbumPhotoAddBatch() {
        String jsonPhotoList = readDataFromFile("DataResponses/PictureUnitTests-GetAllPhotos.json");
        String jsonAlbumList = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetAlbumInfo.json");
        String jsonEmptyAlbum = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetEmptyAlbum.json");
        String jsonBatchPhoto = readDataFromFile("DataResponses/VirtualAlbumUnitTests-AddBatchPhoto.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);
        BuddyHttpClientFactory.addDummyResponse(jsonAlbumList);
        BuddyHttpClientFactory.addDummyResponse(jsonEmptyAlbum);
        BuddyHttpClientFactory.addDummyResponse(jsonBatchPhoto);

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

                            Picture pic1 = pictures.get(0);
                            Picture pic2 = pictures.get(1);
                            final List<PicturePublic> pictureList = new ArrayList<PicturePublic>();
                            pictureList.add(pic1);
                            pictureList.add(pic2);
                            testAuthUser.getVirtualAlbums().get(12041, null,
                                    new OnCallback<Response<VirtualAlbum>>() {
                                        public void OnResponse(Response<VirtualAlbum> response,
                                                Object state) {
                                            assertNotNull(response);

                                            VirtualAlbum album = response.getResult();
                                            album.addPictureBatch(pictureList, null,
                                                    new OnCallback<Response<Boolean>>() {

                                                        public void OnResponse(
                                                                Response<Boolean> response,
                                                                Object state) {
                                                            assertNotNull(response);
                                                            assertTrue(response.getResult());
                                                        }
                                                    });
                                        }
                                    });
                            break;
                        }
                    }
                });
    }

    public void testVirtualAlbumDelete() {
        String jsonAlbum = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetAlbumInfo.json");
        String jsonPhotoList = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetEmptyAlbum.json");
        String jsonSuccess = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonAlbum);
        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);
        BuddyHttpClientFactory.addDummyResponse(jsonSuccess);

        testAuthUser.getVirtualAlbums().get(12041, null, new OnCallback<Response<VirtualAlbum>>() {
            public void OnResponse(Response<VirtualAlbum> response, Object state) {
                assertNotNull(response);

                VirtualAlbum album = response.getResult();
                album.delete(null, new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }
                });
            }
        });
    }

    public void testVirtualAlbumPhotoRemove() {
        String jsonPhotoList = readDataFromFile("DataResponses/PictureUnitTests-GetAllPhotos.json");
        String jsonAlbumList = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetAlbumInfo.json");
        String jsonEmptyAlbum = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetEmptyAlbum.json");
        String jsonSuccess = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);
        BuddyHttpClientFactory.addDummyResponse(jsonAlbumList);
        BuddyHttpClientFactory.addDummyResponse(jsonEmptyAlbum);
        BuddyHttpClientFactory.addDummyResponse(jsonSuccess);

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

                            final Picture pic = pictures.get(0);
                            testAuthUser.getVirtualAlbums().get(12041, null,
                                    new OnCallback<Response<VirtualAlbum>>() {
                                        public void OnResponse(Response<VirtualAlbum> response,
                                                Object state) {
                                            assertNotNull(response);

                                            VirtualAlbum album = response.getResult();
                                            album.removePicture(pic, null,
                                                    new OnCallback<Response<Boolean>>() {

                                                        public void OnResponse(
                                                                Response<Boolean> response,
                                                                Object state) {
                                                            assertNotNull(response);
                                                            assertTrue(response.getResult());
                                                        }
                                                    });
                                        }
                                    });
                            break;
                        }
                    }
                });
    }

    public void testVirtualAlbumPhotoUpdate() {
        String jsonPhotoList = readDataFromFile("DataResponses/PictureUnitTests-GetAllPhotos.json");
        String jsonAlbumList = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetAlbumInfo.json");
        String jsonEmptyAlbum = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetEmptyAlbum.json");
        String jsonSuccess = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);
        BuddyHttpClientFactory.addDummyResponse(jsonAlbumList);
        BuddyHttpClientFactory.addDummyResponse(jsonEmptyAlbum);
        BuddyHttpClientFactory.addDummyResponse(jsonSuccess);

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

                            final Picture pic = pictures.get(0);
                            testAuthUser.getVirtualAlbums().get(12041, null,
                                    new OnCallback<Response<VirtualAlbum>>() {
                                        public void OnResponse(Response<VirtualAlbum> response,
                                                Object state) {
                                            assertNotNull(response);

                                            VirtualAlbum album = response.getResult();
                                            album.updatePicture(pic, "Test Comment", "", null,
                                                    new OnCallback<Response<Boolean>>() {

                                                        public void OnResponse(
                                                                Response<Boolean> response,
                                                                Object state) {
                                                            assertNotNull(response);
                                                            assertTrue(response.getResult());
                                                        }
                                                    });
                                        }
                                    });
                            break;
                        }
                    }
                });
    }

    public void testVirtualAlbumUpdate() {
        String jsonPhotoList = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetAlbumInfo.json");
        String jsonEmptyAlbum = readDataFromFile("DataResponses/VirtualAlbumUnitTests-GetEmptyAlbum.json");
        String jsonSuccess = readDataFromFile("DataResponses/GenericSuccessResponse.json");

        BuddyHttpClientFactory.addDummyResponse(jsonPhotoList);
        BuddyHttpClientFactory.addDummyResponse(jsonEmptyAlbum);
        BuddyHttpClientFactory.addDummyResponse(jsonSuccess);

        testAuthUser.getVirtualAlbums().get(12041, null, new OnCallback<Response<VirtualAlbum>>() {
            public void OnResponse(Response<VirtualAlbum> response, Object state) {
                assertNotNull(response);

                VirtualAlbum album = response.getResult();
                album.update("Test Album Name", "", null, new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }
                });
            }
        });
    }
}
