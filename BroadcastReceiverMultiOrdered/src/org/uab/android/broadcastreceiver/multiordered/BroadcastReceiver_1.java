package org.uab.android.broadcastreceiver.multiordered;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class BroadcastReceiver_1 extends BroadcastReceiver {
	
	private static final String LOG_TAG = "BROADCAST_RECEIVER_1";

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.i(LOG_TAG, "Intent with action: " + intent.getAction() + " received!");
		
		// Make the device vibrate for 500ms
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(500);
		
		// Show a Toast to inform the event has been received
		Toast.makeText(context, "Intent received by BroadcastReceiver_1.", Toast.LENGTH_SHORT)
			.show();
	}

}
