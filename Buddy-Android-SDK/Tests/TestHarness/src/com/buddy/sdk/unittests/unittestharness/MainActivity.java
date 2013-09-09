package com.buddy.sdk.unittests.unittestharness;

import com.buddy.sdk.AuthenticatedUser;
import com.buddy.sdk.BuddyClient;
import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.responses.Response;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	String appName = "Buddy Android SDK test app";
	String appPassword = "FD6DCE88-BA4C-44A9-8CA6-EF0BAECAF283";
	String testUserName = "Test User 1";
	String testPassword = "!TestUser1";
	EditText txtUsername;
	EditText txtPassword;
	TextView logView;
	BuddyClient client;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btn = (Button) findViewById(R.id.verifyButton);
		txtUsername = (EditText) findViewById(R.id.editUser);
		txtPassword = (EditText) findViewById(R.id.editPassword);
        logView = (TextView) findViewById(R.id.testLog);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				VerifyBuddyConnection();

			}
		});
		
		txtUsername.setText(testUserName);
		txtPassword.setText(testPassword);
	}

	private void VerifyBuddyConnection() {
		client = new BuddyClient(appName, appPassword, this.getApplicationContext());
		client.login(txtUsername.getText().toString(), txtPassword.getText().toString(), 
		        null, new OnCallback<Response<AuthenticatedUser>>() {

					public void OnResponse(
							Response<AuthenticatedUser> response, Object state) {
						try {
							if (response != null) {
								if (response.isCompleted()) {
								    logView.setText("User Logged in");
									Log.i("BuddyTests", "User Logged in");

									AuthenticatedUser user = response
											.getResult();
									Log.i("BuddyTests", user.getEmail());
								} else {
								    logView.setText(response.getThrowable().getMessage());
								}
							}

						} catch (Exception e) {
							Toast.makeText(MainActivity.this,
									"Exception: " + e.toString(),
									Toast.LENGTH_LONG).show();
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}