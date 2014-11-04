package org.uab.android.broadcastreceiver.multiordered;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	public static final String	SEND_ORDERED_BROADCAST = "org.uab.android.broadcastreceiver.singlestatic.SEND_ORDERED_BROADCAST";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button sendOBroadcastButton = (Button) findViewById(R.id.button1);
		sendOBroadcastButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Send an ordered broadcast to receivers having the android.permission.VIBRATE permission
				sendOrderedBroadcast(
						new Intent(SEND_ORDERED_BROADCAST), 
						android.Manifest.permission.VIBRATE);
			}
		});
	}
}
