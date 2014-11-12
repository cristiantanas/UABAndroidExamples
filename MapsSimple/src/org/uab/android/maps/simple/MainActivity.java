package org.uab.android.maps.simple;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {
	
	// The Map object
	private GoogleMap		mGMap;
	
	// CameraPosition targeting Syndey, Australia
	static final CameraPosition SYDNEY = 
			new CameraPosition.Builder()
				.target(new LatLng(-33.891614, 151.276417))
				.zoom(15.5f)
				.bearing(300)
				.tilt(45)
				.build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setUpMapIfNeeded();
	}
	
	/**
	 * Obtains a reference to the Map object from the underlying fragment if needed.
	 */
	private void setUpMapIfNeeded() {
		
		// Check if we didn't already instantiated the map
		if ( mGMap == null ) {
			
			mGMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			
			// Check if we were able to successfully return a map object
			if ( mGMap != null ) {
				
				// Perform setup operation on the map object
				setUpMap();
			}
			
		}
	}
	
	/**
	 * Sets up initial configuration on the Map object
	 */
	private void setUpMap() {
		
		// Use terrain maps
		mGMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		
		// Animate the current map view to Sydney, Australia.
		mGMap.animateCamera(CameraUpdateFactory.newCameraPosition(SYDNEY));
		
		// Add a marker on the map at the specified location
		mGMap.addMarker(new MarkerOptions()
				.position(new LatLng(-33.891614, 151.276417))
				.title("Sydney")
				.snippet("Population: 4,692,813 (January 1st 2012)")
				);
	}
}
