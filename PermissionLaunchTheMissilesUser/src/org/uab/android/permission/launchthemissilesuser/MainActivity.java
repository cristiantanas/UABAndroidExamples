package org.uab.android.permission.launchthemissilesuser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	
	private static final String LAUNCH_MISSILES = "org.uab.android.permission.lauchthemissiles.LAUNCH";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ImageButton launchMissiles = (ImageButton) findViewById(R.id.launchMissiles);
		launchMissiles.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(LAUNCH_MISSILES));
			}
		});
	}
}
