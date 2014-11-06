package org.uab.android.threading.runonuithread;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Button		loadImageButton;
	Button		sayHelloButton;
	
	ImageView	imageView;
	ProgressBar	loadingView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imageView = (ImageView) findViewById(R.id.imageView1);
		loadingView = (ProgressBar) findViewById(R.id.loadingView);
		
		loadImageButton = (Button) findViewById(R.id.loadImage);
		loadImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				loadImage();
			}
		});
		
		sayHelloButton = (Button) findViewById(R.id.sayhello);
		sayHelloButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(MainActivity.this, "Hello class!",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void loadImage() {
		
		// Set the ProgressBar indicating that the image is loading
		loadingView.setVisibility(View.VISIBLE);
		
		// Create a new Thread to load the image
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				// Get a Bitmap from a drawable resource
				final Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.grumpycat);
				
				// Simulate a long-running operation
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				// Set the decoded bitmap as the ImageView's source
				if ( bitmapImage != null ) {
					
					// The Activity.runOnUiThred() method runs in the UIThread
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							loadingView.setVisibility(View.GONE);
							imageView.setImageBitmap(bitmapImage);
						}
					});
				}
					
				
			}
		}).start(); // Executes the newly created thread
		
	}
}
