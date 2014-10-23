package org.uab.android.ui.uibutton;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Button	pressMeButton;
	
	private int	pressCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get a reference to the button in the UI
		pressMeButton = (Button) findViewById(R.id.button1);
		
		// Set an OnClickListener on this Button
		// Called each time the user clicks the Button
		pressMeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Modify the label of the buttton to show the press count
				pressMeButton.setText("You pressed me: " + ++pressCount);
			}
		});
	}
}
