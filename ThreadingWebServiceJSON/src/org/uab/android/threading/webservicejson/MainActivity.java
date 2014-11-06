package org.uab.android.threading.webservicejson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	// URL of the web service we must connect to
	private static final String WEBSERVICE_URL = "http://donkikochan.uab.cat/09/services.php?test";

	TextView	textView;
	ProgressBar	progressBar;
	Button		loadButton;

	// Handler to handle the connection to the web service 
	Handler 	loadDataHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get a reference to the UI elements
		textView = (TextView) findViewById(R.id.textView1);

		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		loadButton = (Button) findViewById(R.id.button1);

		// Set the Load data button's OnClickListener
		loadButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Create a new Thread to load the data from the web service
				new Thread(
						new LoadDataFromWebService(loadDataHandler, WEBSERVICE_URL)
						).start();
			}
		});
	}
	
	/**
	 * Connects to a web service and retrieves the specified data.
	 * Uses a Handler to handle back the response to the UI Thread.
	 */
	private class LoadDataFromWebService implements Runnable {

		// UI Thread handler
		Handler handler;
		
		// URL of the web service
		String	url;
		
		public LoadDataFromWebService(Handler handler, String url) {
			this.handler = handler;
			this.url = url;
		}
		
		@Override
		public void run() {
			
			HttpURLConnection httpUrlConnection = null;
			
			// Set the ProgressBar to be visible
			this.handler.post(new Runnable() {
				
				@Override public void run() {
					progressBar.setVisibility(View.VISIBLE);
				}
			});
			
			try {
				// Connect to the web service
				httpUrlConnection = (HttpURLConnection) 
						new URL(url).openConnection();
				
				// Get a reference to the web service's InputStream
				InputStream in = httpUrlConnection.getInputStream();
				
				// Read the raw data from the InputStream
				final String webserviceResponse = readFromInputStream(in);
				
				// Parse the JSON data response from the web service
				final String jsonResponse = new JsonParser(webserviceResponse).parse();
				
				// Hide the ProgressBar from the View hierarchy and show the 
				// web service's response in the TextView
				this.handler.post(new Runnable() {
					
					@Override public void run() {
						progressBar.setVisibility(View.GONE);
						textView.setText(jsonResponse);
					}
				});
				
			} catch (MalformedURLException e) {
				Log.e("LOAD_DATA_FROM_WEBSERVICE", "MalformedURLException");
				
			} catch (IOException e) {
				Log.e("LOAD_DATA_FROM_WEBSERVICE", "IOException");
				
			} catch (JSONException e) {
				Log.e("LOAD_DATA_FROM_WEBSERVICE", "JSONException");
			}
			finally {
				if ( httpUrlConnection != null )
					httpUrlConnection.disconnect();
			}
			
		}
		
		/**
		 * Reads raw data from an InputStream.
		 * 
		 * @param in The InputStream to read from.
		 * 
		 * @return A String representation of the InputStream's data
		 */
		private String readFromInputStream(InputStream in) {
			
			BufferedReader bufferedReader = null;
			StringBuilder responseBuilder = new StringBuilder();
			
			try {
				
				bufferedReader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				
				while ( (line = bufferedReader.readLine()) != null ) {
					responseBuilder.append(line);
				}
				
			} catch (IOException e) {
				Log.e("LOAD_DATA_FROM_WEBSERVICE", "IOException");
			}
			finally {
				
				if ( bufferedReader != null ) {
					 
					try { bufferedReader.close();
					} catch (IOException e) {
						Log.e("LOAD_DATA_FROM_WEBSERVICE", "IOException");
					}
				}
			}
			
			return responseBuilder.toString();
		}
		
	}
}
