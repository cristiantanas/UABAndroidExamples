package org.uab.android.location.locationmanager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	static final long POLLING_FREQ = 10 * 1000;
	static final float MIN_DISTANCE = 10.0f;
	
	// LocationManager object to request the device's location
	LocationManager locationManager;
	
	// Location object to store the current "best" location
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
		
		// Get a reference to the LocationManager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if ( locationManager == null ) finish();
		
		locationAccuracyTv = (TextView) findViewById(R.id.locationAccuracy);
		locationTimeTv = (TextView) findViewById(R.id.locationTime);
		locationLatitudeTv = (TextView) findViewById(R.id.locationLatitude);
		locationLongitudeTv = (TextView) findViewById(R.id.locationLongitude);
		
		getLocationButton = (Button) findViewById(R.id.getLocation);
		getLocationButton.setOnClickListener(new View.OnClickListener() {
			
			// When the user clicks on the "Get location" button, first obtain the last known location.
			// Update the display based on the obtained value.
			// Finally, request location updates to get new location values.
			@Override
			public void onClick(View v) {
				
				currentBestLocation = getBestLastKnownLocation();
				if ( currentBestLocation != null ) {
					
					updateDisplay(currentBestLocation);
				}
				else {
					
					locationAccuracyTv.setText("No initial reading available.");
				}
				
				requestLocationUpdates();
			}
		});
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		// Unregister the locationListener from location updates
		locationManager.removeUpdates(locationListener);
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
		
		// Get a list of all the location providers available.
		List<String> knownProviders = locationManager.getAllProviders();
		
		for ( String provider : knownProviders ) {
			
			// Get the last known location from each location provider.
			Location location = locationManager.getLastKnownLocation(provider);
			
			if ( location != null ) {
				
				float accuracy = location.getAccuracy();
				
				// Compare location values from different providers based only on the accuracy of the reading.
				if ( accuracy < bestAccuracy ) {
					
					bestLocation = location;
					bestAccuracy = accuracy;
				}
			}
		}
		
		return bestLocation;
	}
	
	/**
	 * Registers the locationListener object to receive location updates from 
	 * the NETWORK_PROVIDER and the GPS_PROVIDER.
	 */
	private void requestLocationUpdates() {
		
		// Register for network location updates
		if ( locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null ) {

			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 	// Location provider
					POLLING_FREQ, 						// Update frequency
					MIN_DISTANCE, 						// Update the location only if the device has moved at least MIN_DISTANCE
					locationListener					// LocationListener object to receive location update callbacks
					);
		}

		// Register for GPS location updates
		if ( locationManager.getProvider(LocationManager.GPS_PROVIDER) != null ) {

			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 		// Location provider
					POLLING_FREQ, 						// Update frequency
					MIN_DISTANCE, 						// Update the location only if the device has moved at least MIN_DISTANCE
					locationListener					// LocationListener object to receive location update callbacks
					);
		}
	}
	
	LocationListener locationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		// The location provider has registered a change in the device's location
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
	};
}
