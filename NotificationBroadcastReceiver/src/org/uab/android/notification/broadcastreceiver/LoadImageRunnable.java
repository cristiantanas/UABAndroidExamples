package org.uab.android.notification.broadcastreceiver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class LoadImageRunnable implements Runnable {
	
	private static final int SIM_NETWORK_DELAY = 5000;
	private static final int NOTIFICATION_ID = 1;
	
	private static boolean success = false;
	
	Context context;
	Handler handler;
	
	public LoadImageRunnable(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	@Override
	public void run() {
		
		// Send a Message to the MainActivity to set the ProgressBar visibility to View.VISIBLE
		Message msg = this.handler.obtainMessage(MainActivity.PROGRESS_BAR_VISIBILITY, View.VISIBLE);
		this.handler.sendMessage(msg);
		
		// Simulate a long-running operation
		try {
			Thread.sleep(SIM_NETWORK_DELAY);
		} catch (InterruptedException e) {
			Log.e("", "");
		}
		
		// Get the image from the raw directory of the application
		InputStream imageInputStream = context.getResources()
				.openRawResource(R.raw.efficiency);
		Bitmap imageBitmap = BitmapFactory.decodeStream(imageInputStream);
		
		success = imageBitmap != null;
		if ( success ) {
			// If the "download" was successful, save the image to the application's internal storage
			saveImageToFile(imageBitmap);
		}
		
		// Notify with a broadcast that the image loading process has completed.
		notifyImageLoaded();
		
		try { 
			imageInputStream.close();
		} catch (IOException e) {
			Log.e("", "");
		}
		
		// Send a Message to the MainActivity to set the ProgressBar visibility to View.GONE
		msg = this.handler.obtainMessage(MainActivity.PROGRESS_BAR_VISIBILITY, View.GONE);
		this.handler.sendMessage(msg);
		
		// Send a Message to the MainActivity to set the ImageView's Bitmap
		msg = this.handler.obtainMessage(MainActivity.IMAGE_VIEW_SET_BITMAP, imageBitmap);
		this.handler.sendMessage(msg);
		
	}
	
	/**
	 * Save a Bitmap to the application's internal storage for later use.
	 * 
	 * @param imageBitmap The Bitmap image to be stored.
	 */
	private void saveImageToFile(Bitmap imageBitmap) {
		
		try {
			
			FileOutputStream imageOutputStream = this.context.openFileOutput(
					MainActivity.IMAGE_VIEW_CONTENT_PATH,
					Context.MODE_PRIVATE
					);
			imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOutputStream);
			
			imageOutputStream.flush();
			imageOutputStream.close();
			
		} catch (FileNotFoundException e) {
			Log.e("", "");
			
		} catch (IOException e) {
			Log.e("", "");
		}
		
	}

	/**
	 * Sends an ordered broadcast to inform the loading process has completed.
	 */
	private void notifyImageLoaded() {
		
		// Sends an ordered broadcast to determine whether MainActivity is
		// active and in the foreground. Creates a new BroadcastReceiver
		// to receive a result indicating the state of MainActivity
		context.sendOrderedBroadcast(
				new Intent(MainActivity.IMAGE_LOADED_ACTION), 
				null, 
				resultReceiver, 	// Register a BroadcastReceiver to get the result back from the invoked receiver
				null, 
				0, 
				null, 
				null);
	}
	
	/**
	 * BroadcastReceiver to get back results from the invoked receiver.
	 * The MainActivity, if in foreground, will set its result to MainActivity.IS_ALIVE.
	 * Thus, this BroadcastReceiver can determine whether a notification should be issued or not.
	 */
	BroadcastReceiver resultReceiver = new BroadcastReceiver() {
		
		final String failMsg = "Image loading has failed. Please retry Later.";
		final String successMsg = "Image loading completed successfully.";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			// Check the result code
			if ( getResultCode() != MainActivity.IS_ALIVE ) {
				
				// Create a PendingIntent using the MainActivity helper function
				PendingIntent activityPendingIntent = MainActivity
						.getCallingPendingIntent(context);
				
				// Use R.layout.custom_notification for the layout of the notification 
				RemoteViews notificationContentView = new RemoteViews(
						context.getPackageName(), 
						R.layout.custom_notification);
				
				// Set the notification View's text to reflect whether the download completed successfully
				notificationContentView.setTextViewText(
						R.id.notificationContent, 
						success ? successMsg : failMsg);
				
				// Builds the notification through the NotificationCompat.Builder class
				NotificationCompat.Builder notificationBuilder = 
						new NotificationCompat.Builder(context)
					.setSmallIcon(android.R.drawable.ic_dialog_info)
					.setContent(notificationContentView)
					.setContentIntent(activityPendingIntent)
					.setAutoCancel(true);
				
				// Send the notification
				NotificationManager notificationManager = (NotificationManager) 
						context.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(
						NOTIFICATION_ID,
						notificationBuilder.build()
						);
			}
		}
	};

}
