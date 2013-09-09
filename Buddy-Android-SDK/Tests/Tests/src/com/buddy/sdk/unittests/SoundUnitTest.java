package com.buddy.sdk.unittests;

import java.io.IOException;
import java.io.InputStream;

import com.buddy.sdk.Sounds.SoundQuality;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.web.BuddyHttpClientFactory;
import com.buddy.sdk.Callbacks.OnCallback;

public class SoundUnitTest extends BaseUnitTest {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		createAuthenticatedUser();
	}
	
	public void testGetSound(){
		InputStream soundStream = getStreamFromFile("test.mp4");
		
		BuddyHttpClientFactory.setDummyReponse(soundStream);
		
		testClient.getSounds().get("european_city_ambience_01_120_loop", SoundQuality.Low, new OnCallback<Response<InputStream>>(){
			public void OnResponse(Response<InputStream> response, Object state){
				try{
					assertNotNull(response);
					InputStream stream = response.getResult();
					assertNotNull(stream);
					assertTrue(stream.available() > 0);
				}catch(IOException ex){
					assertTrue(false);
				}
			}
		});
	}
}
