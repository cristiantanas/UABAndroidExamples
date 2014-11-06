package org.uab.android.threading.handlermessages;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final int PROGRESS_BAR_VISIBILITY = 0;
	private static final int PROGRESS_BAR_UPDATE = 1;
	private static final int IMAGE_VIEW_SET_BITMAP = 2;
	
	Button		loadImageButton;
	Button		sayHelloButton;
	
	ImageView	imageView;
	ProgressBar	loadingView;
	
	Handler loadHandler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			
			switch (msg.what) {
			case PROGRESS_BAR_VISIBILITY:
				// Set the ProgressBar visibility
				loadingView.setVisibility((Integer) msg.obj);
				break;

			case PROGRESS_BAR_UPDATE:
				// Update the progress units in the ProgressBar
				loadingView.setProgress((Integer) msg.obj);
				break;
				
			case IMAGE_VIEW_SET_BITMAP:
				// Set the ImageView's bitmap 
				imageView.setImageBitmap((Bitmap) msg.obj);
				break;
			}
			
			return true;
		}
	});

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
			
			// Send a message indicating that the image is loading
			Message msg = this.handler.obtainMessage(PROGRESS_BAR_VISIBILITY, View.VISIBLE);
			this.handler.sendMessage(msg);
			
			// Get a Bitmap from a drawable resource
			final Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), this.resId);
			
			// Simulate long-running operation
			for (int i = 1; i < 11; i++) {
				sleep();
				msg = this.handler.obtainMessage(PROGRESS_BAR_UPDATE, i * 10);
				this.handler.sendMessage(msg);
			}
			
			// Send a message to hide the ProgressBar
			msg = this.handler.obtainMessage(PROGRESS_BAR_VISIBILITY, View.GONE);
			this.handler.sendMessage(msg);
			
			// Send a message to set the decoded bitmap as the ImageView's source
			msg = this.handler.obtainMessage(IMAGE_VIEW_SET_BITMAP, bitmapImage);
			this.handler.sendMessage(msg);
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
