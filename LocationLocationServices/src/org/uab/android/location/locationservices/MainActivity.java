package org.uab.android.location.locationservices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends Activity implements 
				GooglePlayServicesClient.ConnectionCallbacks,
				GooglePlayServicesClient.OnConnectionFailedListener, 
				com.google.android.gms.location.LocationListener {
	
	static final long POLLING_FREQ = 10 * 1000;
	static final long FASTEST_UPDATE_FREQ = 2 * 1000;
	static final float MIN_DISTANCE = 10.0f;
	
	// LocationClient object to request the device's location
	LocationClient	locationClient;
	
	// LocationRequest object that holds accuracy and frequency parameters
	LocationRequest	locationRequest;
	
	// Current best location estimate
	Location		currentBestLocation;
	
	TextView	locationAccuracyTv;
	TextView	locationTimeTv;
	TextView	locationLatitudeTv;
	TextView	locationLongitudeTv;
	
	Button		getLocationButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Create a new LocationClient object. This class will handle callbacks.
		locationClient = new LocationClient(this, this, this);
		
		// Create and define the LocationRequest
		locationRequest = LocationRequest.create();
		
		// Use high accuracy
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		
		// Update every 10 seconds
		locationRequest.setInterval(POLLING_FREQ);
		
		// Receive updates no more often that every 2 seconds
		locationRequest.setFastestInterval(FASTEST_UPDATE_FREQ);
		
		locationAccuracyTv = (TextView) findViewById(R.id.locationAccuracy);
		locationTimeTv = (TextView) findViewById(R.id.locationTime);
		locationLatitudeTv = (TextView) findViewById(R.id.locationLatitude);
		locationLongitudeTv = (TextView) findViewById(R.id.locationLongitude);
		
		getLocationButton = (Button) findViewById(R.id.getLocation);
		getLocationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Connect to LocationServices
				locationClient.connect();
			}
		});
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		if ( locationClient != null ) {
			// Stop updates
			locationClient.removeLocationUpdates(this);
			
			// Disconnect from LocationServices
			locationClient.disconnect();
		}
	}
	
	/**
	 * Updates the TextViews with the information on the new location.
	 * 
	 * @param location Location object with the new location value.
	 */
	private void updateDisplay(Location location) {
		
		locationAccuracyTv.setText("Accuracy: " + location.getAccuracy());
		locationTimeTv.setText("Time: " + 
				new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
						.format(new Date(location.getTime()))
						);
		locationLatitudeTv.setText("Latitude: " + location.getLatitude());
		locationLongitudeTv.setText("Longitude: " + location.getLongitude());
	}
	
	/**
	 * Changes the text color of all the TextViews.
	 * 
	 * @param color The color of the text.
	 */
	private void setTextViewColor(int color) {
		
		locationAccuracyTv.setTextColor(color);
		locationTimeTv.setTextColor(color);
		locationLatitudeTv.setTextColor(color);
		locationLongitudeTv.setTextColor(color);
	}
	
	/**
	 * Obtains the last best known location from all the available location providers.
	 * 
	 * @return The last best known location.
	 */
	private Location getBestLastKnownLocation() {
		
		Location bestLocation = null;
		float bestAccuracy = Float.MAX_VALUE;
		
		// Get the best most recent location currently available
		currentBestLocation = locationClient.getLastLocation();
		
		if ( currentBestLocation != null ) {
			
			float accuracy = currentBestLocation.getAccuracy();
			
			if ( accuracy < bestAccuracy ) {
				
				bestLocation = currentBestLocation;
				bestAccuracy = accuracy;
			}
		}
		
		return bestLocation;
	}
	
	/**
	 * Request location updates from the LocationServices
	 */
	private void requestLocationUpdates() {
		
		// Get additional location updates
		locationClient.requestLocationUpdates(locationRequest, this);
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		
		if ( isGooglePlayServicesAvailable() ) {
			
			// Get best last known location measurement
			currentBestLocation = getBestLastKnownLocation();
			
			// Update the display with the obtained location measurement
			if ( currentBestLocation != null ) {
				
				updateDisplay(currentBestLocation);
			}
			else {
				
				locationAccuracyTv.setText("No initial reading available.");
			}
			
			// Request additional location updates
			requestLocationUpdates();
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onLocationChanged(Location location) {
		
		// Keep the new location value if there is no previous value or 
		// if the accuracy of the new value is better than the accuracy of the current value
		if ( currentBestLocation == null
				|| currentBestLocation.getAccuracy() > location.getAccuracy() ) {
			
			currentBestLocation = location;
			
			// Update the display with the new location info
			updateDisplay(location);
			
			// Change the TextViews text color to green
			setTextViewColor(Color.GREEN);
		}
	}
	
	/**
	 * Check whether GooglePlay Services are available on the device.
	 * 
	 * @return GooglePlay Services status on the device.
	 */
	private boolean isGooglePlayServicesAvailable() {
		
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		return (ConnectionResult.SUCCESS == resultCode);
	}
	
}
