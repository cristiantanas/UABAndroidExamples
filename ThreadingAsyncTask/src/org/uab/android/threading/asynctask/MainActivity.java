package org.uab.android.threading.asynctask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
				
				// Create a new AsyncTask and pass it the image to load
				new LoadingAsyncTask().execute(R.drawable.grumpycat);
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
	
	private class LoadingAsyncTask extends AsyncTask<Integer, Integer, Bitmap> {

		@Override
		protected void onPreExecute() {
			
			// Set the ProgressBar to indicate that the image is loading
			loadingView.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected Bitmap doInBackground(Integer... resId) {
			
			// Get a Bitmap from a drawable resource
			Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), resId[0]);
			
			// Simulate long-running operation
			for (int i = 1; i < 11; i++) {
				sleep();
				publishProgress(i * 10);
			}
			
			return bitmapImage;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			
			// Update the ProgressBar to show the image loading progress
			loadingView.setProgress(values[0]);
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			
			// Remove the ProgressBar from the View
			loadingView.setVisibility(View.GONE);
			
			// Set the resulting bitmap as the ImageView's source
			imageView.setImageBitmap(result);
		}
		
		private void sleep() {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
