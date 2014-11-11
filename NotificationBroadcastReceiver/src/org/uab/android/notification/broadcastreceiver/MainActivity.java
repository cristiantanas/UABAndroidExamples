package org.uab.android.notification.broadcastreceiver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
	
	public static final String IMAGE_VIEW_CONTENT_PATH = "xkcd_latest.png";
	public static final String IMAGE_LOADED_ACTION = "org.uab.android.notification.broadcastreceiver.IMAGE_LOADED";
	
	public static final int IMAGE_VIEW_SET_BITMAP = 0;
	public static final int PROGRESS_BAR_VISIBILITY = 1;
	
	public static final int IS_ALIVE = Activity.RESULT_FIRST_USER;
	
	ImageView 	contentImageView;
	ProgressBar	progressBar;
	Button		loadImageButton;
	
	Handler 	handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			
			switch (msg.what) {
			case PROGRESS_BAR_VISIBILITY:
				// Set ProgressBar visibility
				progressBar.setVisibility((Integer) msg.obj);
				break;
				
			case IMAGE_VIEW_SET_BITMAP:
				// Set ImageView Bitmap
				contentImageView.setImageBitmap((Bitmap) msg.obj);
				break;

			default:
				break;
			}
			
			return true;
		}
	});
	
	/**
	 * Returns a PendingIntent to start this Activity from another Context.
	 * 
	 * @param context The context from which this method is called.
	 * 
	 * @return A PendingIntent to start this Activity.
	 */
	public static PendingIntent getCallingPendingIntent(Context context) {
		
		Intent startMainActivity = new Intent(context, MainActivity.class);
		startMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	// This flag is required in order to start a new process if one is not created.
		
		PendingIntent startMainActivityPendingIntent = PendingIntent
				.getActivity(
						context, 
						0, 
						startMainActivity, 
						PendingIntent.FLAG_UPDATE_CURRENT			// If the PendingIntent already exists, only replace any extra data that has been sent.
						);
		
		return startMainActivityPendingIntent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get a reference to the UI elements
		contentImageView = (ImageView) findViewById(R.id.contentImage);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		loadImageButton = (Button) findViewById(R.id.load);
		
		// Set the Button's OnClickListener
		loadImageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Start a new Thread that simulates a long-running download
				new Thread(
						new LoadImageRunnable(MainActivity.this, handler)
						).start();
				
			}
		});
		
		loadImage();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Register a BroadcastReceiver to receive a IMAGE_LOADED_ACTION broadcast
		IntentFilter intentFilter = new IntentFilter(IMAGE_LOADED_ACTION);
		registerReceiver(loadImageReceiver, intentFilter);
	}
	
	@Override
	protected void onPause() {
		
		// Unregister the BroadcastReceiver if it has been registered
		if ( loadImageReceiver != null ) {
			unregisterReceiver(loadImageReceiver);
		}
		
		super.onPause();
	}
	
	/**
	 * Load a previously "downloaded" image
	 */
	private void loadImage() {
		
		// If the image has been previously downloaded by the app, update the ImageView.
		if ( existsFile(IMAGE_VIEW_CONTENT_PATH) ) {
			
			try {
				
				FileInputStream imageInputStream = openFileInput(IMAGE_VIEW_CONTENT_PATH);
				Bitmap image = BitmapFactory.decodeStream(imageInputStream);
				contentImageView.setImageBitmap(image);
				
			} catch (FileNotFoundException e) {
				Log.e("", "");
			}
		}
	}
	
	/**
	 * Check whether a file with name fileName exists in the application private data.
	 * 
	 * @param fileName Name of the file.
	 * 
	 * @return A boolean indicating if the file exists or not.
	 */
	private boolean existsFile(String fileName) {
		
		String[] listOfFiles = fileList();
		for ( String file : listOfFiles ) {
			
			if ( fileName.equals(file) )
				return true;
		}
		
		return false;
	}
	
	// Broadcast receiver that will handle any IMAGE_LOADED_ACTION broadcast
	BroadcastReceiver loadImageReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			// Make sure this is an ordered broadcast.
			// Let the sender know that the Intent was received by setting result code to MainActivity.IS_ALIVE
			if ( isOrderedBroadcast() )
				setResultCode(IS_ALIVE);
		}
	};
}
