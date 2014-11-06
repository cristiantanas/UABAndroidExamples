package org.uab.android.threading.handlerrunnable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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
	
	// Create a Handler object to process incoming operations from child threads
	Handler		loadHandler = new Handler();

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
				
				// Create a new Thread and pass it the Runnable to execute
				new Thread(
						new LoadingThread(loadHandler, R.drawable.grumpycat)
						).start();
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
	
	private class LoadingThread implements Runnable {
		
		Handler 	handler;
		int 		resId;
		
		public LoadingThread(Handler handler, int resId) {
			this.handler = handler;
			this.resId = resId;
		}

		@Override
		public void run() {
			
			// Set the ProgressBar indicating that the image is loading
			this.handler.post(new Runnable() {
				
				@Override public void run() {
					loadingView.setVisibility(View.VISIBLE);
				}
			});
			
			// Get a Bitmap from a drawable resource
			final Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), this.resId);
			
			// Simulate long-running operation
			for (int i = 1; i < 11; i++) {
				sleep();
				final int step = i;
				handler.post(new Runnable() {
					
					@Override public void run() {
						loadingView.setProgress(step * 10);
					}
				});
			}
			
			// Hide the ProgressBar
			this.handler.post(new Runnable() {
				
				@Override public void run() {
					loadingView.setVisibility(View.GONE);
				}
			});
			
			// Set the decoded bitmap as the ImageView's source
			this.handler.post(new Runnable() {
				
				@Override public void run() {
					imageView.setImageBitmap(bitmapImage);
				}
			});
		}
		
	}
	
	private void sleep() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
