package org.uab.android.broadcastreceiver.singlestatic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class RandomBroadcastReceiver extends BroadcastReceiver {
	
	private static final String LOG_TAG = "RANDOM_BROADCAST_RECEIVER";

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.i(LOG_TAG, "Intent with action: " + intent.getAction() + " received!");
		
		// Make the device vibrate for 500ms
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(500);
		
		// Show a Toast to inform the event has been received and processed
		Toast.makeText(context, "Intent received by RandomBroadcastReceiver.", Toast.LENGTH_SHORT)
			.show();
	}

}
