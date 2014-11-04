package org.uab.android.broadcastreceiver.singlestatic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	public static final String	SEND_RANDOM_BROADCAST = "org.uab.android.broadcastreceiver.singlestatic.SEND_RANDOM_BROADCAST";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get a reference to the Send Broadcast button
		Button sendBroadcastButton = (Button) findViewById(R.id.button1);
		
		// Set an OnClickListener to the button
		sendBroadcastButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Send a broadcast event with the SEND_RANDOM_BROADCAST action
				// This event is delivered to only those receivers having the android.permission.VIBRATE permission
				sendBroadcast(
						new Intent(SEND_RANDOM_BROADCAST), 
						android.Manifest.permission.VIBRATE);
			}
		});
	}
}
