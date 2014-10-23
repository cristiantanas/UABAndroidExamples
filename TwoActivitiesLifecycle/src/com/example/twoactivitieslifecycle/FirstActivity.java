package com.example.twoactivitieslifecycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FirstActivity extends Activity {
	
	private static final String LOG_TAG = "TWO_ACTIVITIES_LIFECYCLE";
	
	private EditText	sendInfoEditText;
	private Button		sendMeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, 
    			"FIRST_ACTIVITY onCreate() The activity is not visible and about to be created.");
        
        setContentView(R.layout.activity_first);
        
        // Get a reference to the UI objects
        sendInfoEditText = (EditText) findViewById(R.id.editText1);
        sendMeButton = (Button) findViewById(R.id.button1);
        
        // Implement the Button's onClick functionality
        sendMeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Get the text entered by the user
				String enteredText = sendInfoEditText.getText().toString();
				
				// Create an Intent to start the new Activity
				Intent gotoSecondActivityIntent = new Intent(
						FirstActivity.this, 
						SecondActivity.class);
				
				// Add the text entered by the user as an Intent extra
				gotoSecondActivityIntent.putExtra("TEXT", enteredText);
				
				// Start SecondActivity
				startActivity(gotoSecondActivityIntent);
			}
		});
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	Log.i(LOG_TAG, 
    			"FIRST_ACTIVITY onStart() The activity is visible and about to be started.");
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.i(LOG_TAG, 
    			"FIRST_ACTIVITY onResume() The activity is visible and has focus (it is now \"resumed\")");
    }
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    	Log.i(LOG_TAG, 
    			"FIRST_ACTIVITY onRestart() The activity is visible and about to be restarted.");
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	Log.i(LOG_TAG,
				"FIRST_ACTIVITY onPause() Another activity is taking focus (this activity is about to be \"paused\")");
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.i(LOG_TAG, 
    			"FIRST_ACTIVITY onStop() The activity is no longer visible (it is now \"stopped\")");
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.i(LOG_TAG, 
    			"FIRST_ACTIVITY onDestroy() The activity is about to be destroyed.");
    }
}
