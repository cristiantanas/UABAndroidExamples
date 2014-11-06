package org.uab.android.threading.servicenothread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button downloadFileButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		downloadFileButton = (Button) findViewById(R.id.button1);
		downloadFileButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Start a Service to download a file
				Intent downloadFileIntent = new Intent(MainActivity.this, DownloadService.class);
				startService(downloadFileIntent);
			}
		});
	}
}
