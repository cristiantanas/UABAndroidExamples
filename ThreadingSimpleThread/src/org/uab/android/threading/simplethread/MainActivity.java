package org.uab.android.threading.simplethread;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Button		loadImageButton;
	Button		sayHelloButton;
	
	ImageView	imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imageView = (ImageView) findViewById(R.id.imageView1);
		
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
				// This doen't work in Android
				if ( bitmapImage != null ) 
					imageView.setImageBitmap(bitmapImage);
				
			}
		}).start(); // Executes the newly created thread
		
	}
}
