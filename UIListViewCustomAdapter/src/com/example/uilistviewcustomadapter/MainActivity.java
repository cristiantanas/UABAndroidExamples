package com.example.uilistviewcustomadapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private static final int MAX_USERS = 10;
	
	ListView				userListView;
	UserListCustomAdapter	userAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get a reference to the ListView
		userListView = (ListView) findViewById(R.id.customLv);
		
		// Create a new UserListCustomAdapter for the list from a 
		// randomly generated user list
		userAdapter = new UserListCustomAdapter(this, 
				UserData.generateRandomUsers(MAX_USERS));
		
		// Set the Adapter for the ListView
		userListView.setAdapter(userAdapter);
	}
}
