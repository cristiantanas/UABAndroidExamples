package com.example.twoactivitieslifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SecondActivity extends Activity {
	
	private static final String LOG_TAG = "TWO_ACTIVITIES_LIFECYCLE";
	
	private TextView	textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG, 
    			"SECOND_ACTIVITY onCreate() The activity is not visible and about to be created.");
		
		setContentView(R.layout.activity_second);
		
		textView = (TextView) findViewById(R.id.textView1);
		
		String inputText = getIntent().getStringExtra("TEXT");
		textView.setText(inputText);
	}
	
	@Override
    protected void onStart() {
    	super.onStart();
    	Log.i(LOG_TAG, 
    			"SECOND_ACTIVITY onStart() The activity is visible and about to be started.");
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.i(LOG_TAG, 
    			"SECOND_ACTIVITY onResume() The activity is visible and has focus (it is now \"resumed\")");
    }
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    	Log.i(LOG_TAG, 
    			"SECOND_ACTIVITY onRestart() The activity is visible and about to be restarted.");
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	Log.i(LOG_TAG,
				"SECOND_ACTIVITY onPause() Another activity is taking focus (this activity is about to be \"paused\")");
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.i(LOG_TAG, 
    			"SECOND_ACTIVITY onStop() The activity is no longer visible (it is now \"stopped\")");
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.i(LOG_TAG, 
    			"SECOND_ACTIVITY onDestroy() The activity is about to be destroyed.");
    }
}
