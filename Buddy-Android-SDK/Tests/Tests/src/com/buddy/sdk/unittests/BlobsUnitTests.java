package com.buddy.sdk.unittests;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import com.buddy.sdk.Blob;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.web.BuddyHttpClientFactory;


public class BlobsUnitTests extends BaseUnitTest {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		createAuthenticatedUser();
	}
	
	public void testGetInfo(){
		String jsonBlob = readDataFromFile("DataResponses/BlobUnitTest-GetBlob.json");
		
		BuddyHttpClientFactory.addDummyResponse(jsonBlob);
		testAuthUser.getBlobs().getInfo((long)14, new OnCallback<Response<Blob>>(){
			public void OnResponse(Response<Blob> response, Object state){
				assertNotNull(response);
				
				Blob blob = response.getResult();
				
				assertNotNull(blob);
			}
		});
	}
	
	public void testGet(){
		InputStream blobStream = getStreamFromFile("test.mp4");
		
		BuddyHttpClientFactory.setDummyReponse(blobStream);
		
		testAuthUser.getBlobs().get((long)14, new OnCallback<Response<InputStream>>(){
			public void OnResponse(Response<InputStream> response, Object state) {
				try{
					assertNotNull(response);
					InputStream stream = response.getResult();
					assertNotNull(stream);
					assertTrue(stream.available() > 0);
				}catch(Exception ex){
					assertTrue(false);
				}
			}
		});	
	}
	
	public void testSearchBlobs(){
		String jsonBlobList = readDataFromFile("DataResponses/BlobUnitTest-GetBlob.json");
		
		BuddyHttpClientFactory.addDummyResponse(jsonBlobList);
		
		testAuthUser.getBlobs().searchBlobs("friendlyName.mp4", "video/", "AppTag", 100, 0.0, 0.0, 10, 20, new OnCallback<ListResponse<Blob>>(){
			public void OnResponse(ListResponse<Blob> response, Object state){
				assertNotNull(response);
				
				List<Blob> list = response.getList();
				assertNotNull(list);
				
				Iterator<Blob> iterator = list.iterator();
				while (iterator.hasNext()){
					Blob blob = iterator.next();
					assertNotNull(blob);
					//TODO More
				}
			}
		});
	}
	
	public void testSearchMyBlobs(){
		String jsonBlobList = readDataFromFile("DataResponses/BlobUnitTest-GetBlob.json");
		
		BuddyHttpClientFactory.addDummyResponse(jsonBlobList);
		
		testAuthUser.getBlobs().searchMyBlobs("friendlyName.mp4", "video/", "AppTag", 100, 0.0, 0.0, 10, 20, new OnCallback<ListResponse<Blob>>(){
			public void OnResponse(ListResponse<Blob> response, Object state){
				assertNotNull(response);
				
				List<Blob> list = response.getList();
				assertNotNull(list);
				
				Iterator<Blob> iterator = list.iterator();
				while (iterator.hasNext()){
					Blob blob = iterator.next();
					assertNotNull(blob);
					//TODO More
				}
			}
		});
	}
	
	public void testGetList(){
		String jsonBlobList = readDataFromFile("DataResponses/BlobUnitTest-GetBlob.json");
		
		BuddyHttpClientFactory.addDummyResponse(jsonBlobList);
		
		testAuthUser.getBlobs().getList(-1, 10, new OnCallback<ListResponse<Blob>>(){
			public void OnResponse(ListResponse<Blob> response, Object state){
				assertNotNull(response);
				
				List<Blob> list = response.getList();
				assertNotNull(list);
				
				Iterator<Blob> iterator = list.iterator();
				while (iterator.hasNext()){
					Blob blob = iterator.next();
					assertNotNull(blob);
					//TODO More
				}
			}
		});
	}
	
	public void testGetMyList(){
		String jsonBlobList = readDataFromFile("DataResponses/BlobUnitTest-GetBlob.json");
		
		BuddyHttpClientFactory.addDummyResponse(jsonBlobList);
		
		testAuthUser.getBlobs().getMyList(10, new OnCallback<ListResponse<Blob>>(){
			public void OnResponse(ListResponse<Blob> response, Object state){
				assertNotNull(response);
				
				List<Blob> list = response.getList();
				assertNotNull(list);
				
				Iterator<Blob> iterator = list.iterator();
				while (iterator.hasNext()){
					Blob blob = iterator.next();
					assertNotNull(blob);
					//TODO More
				}
			}
		});
	}
}
