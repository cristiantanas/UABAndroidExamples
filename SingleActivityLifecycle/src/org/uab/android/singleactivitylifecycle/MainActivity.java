package org.uab.android.singleactivitylifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	
	private static final String LOG_TAG = "SINGLE_ACTIVITY_LIFECYCLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, 
    			"onCreate() The activity is not visible and about to be created.");
        setContentView(R.layout.activity_main);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	Log.i(LOG_TAG, 
    			"onStart() The activity is visible and about to be started.");
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.i(LOG_TAG, 
    			"onResume() The activity is visible and has focus (it is now \"resumed\")");
    }
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    	Log.i(LOG_TAG, 
    			"onRestart() The activity is visible and about to be restarted.");
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	Log.i(LOG_TAG,
				"onPause() Another activity is taking focus (this activity is about to be \"paused\")");
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.i(LOG_TAG, 
    			"onStop() The activity is no longer visible (it is now \"stopped\")");
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.i(LOG_TAG, 
    			"onDestroy() The activity is about to be destroyed.");
    }
}
