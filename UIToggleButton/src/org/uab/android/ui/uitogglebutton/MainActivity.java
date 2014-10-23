package org.uab.android.ui.uitogglebutton;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	RelativeLayout	background;
	ToggleButton	toggleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get a reference to the UI elements
		background = (RelativeLayout) findViewById(R.id.relativeLayout);
		toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
		
		// Set an OnClickListener on the toggle button
		toggleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Toggle the background color between light and dark
				if ( toggleButton.isChecked() )
					background.setBackgroundColor(0xFF000000);
				
				else
					background.setBackgroundColor(0xFFF3F3F3);
			}
		});
	}
}
