package org.uab.android.sensors.compass;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	SensorManager	sensorManager;
	Sensor			sensorAccelerometer;
	Sensor			sensorGeomagneticField;
	
	ImageView	compassPointer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		compassPointer = (ImageView) findViewById(R.id.compassPointer);
		
		// Get a reference to the SensorManager
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		// Get a reference to the accelerometer and magnetometer
		sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorGeomagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		// Exit unless both sensors are available
		if ( sensorAccelerometer == null || sensorGeomagneticField == null ) {
			finish();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Register for sensor updates
		sensorManager.registerListener(
				accelerometerAndMagnetometerListener, 
				sensorAccelerometer, 
				SensorManager.SENSOR_DELAY_UI
				);
		
		sensorManager.registerListener(
				accelerometerAndMagnetometerListener, 
				sensorGeomagneticField, 
				SensorManager.SENSOR_DELAY_UI
				);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		// Unregister all sensors
		sensorManager.unregisterListener(accelerometerAndMagnetometerListener);
	}
	
	
	SensorEventListener accelerometerAndMagnetometerListener = new SensorEventListener() {
		
		float[] accelerometerValues;
		float[] magneticFieldValues;
		
		float currentDegrees = 0f;
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			
			// Acquire accelerometer event data
			
			if ( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER ) {
				
				accelerometerValues = event.values;	 
			}
			
			// Acquire geomagnetic field event data
			
			else if ( event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD ) {
				
				magneticFieldValues = event.values;
			}
			
			// If we have readings from both sensors then use the readings to compute
			// the device's orientation and then update the display.
			
			if ( accelerometerValues != null && magneticFieldValues != null ) {
				
				float[] rotationMatrix = new float[9];
				
				// Use the accelerometer and magnetometer readings to compute the device's
				//  rotation with respect to a real world coordinate system.
				
				boolean success = SensorManager.getRotationMatrix(
						rotationMatrix, 		// Rotation matrix
						null, 					// Inclination matrix
						accelerometerValues, 	// Accelerometer values
						magneticFieldValues		// Magnetic field values
						);
				
				if ( success ) {
					
					float[] orientationMatrix = new float[3];
					
					// Returns the device's orientation given the rotation matrix
					
					SensorManager.getOrientation(rotationMatrix, orientationMatrix);
					
					// Get the rotation, measured in radians, around the Z-axis
					// Note: this assumes the device is held flat and parallel to the ground
					
					float azimuthInRadians = orientationMatrix[0];
					
					// Convert from radians to degrees
					float azimuthInDegrees = (float) Math.toDegrees(azimuthInRadians);
					
					// Create an Animation for the compass pointer
					RotateAnimation rotateAnim = new RotateAnimation(
							currentDegrees, 
							-azimuthInDegrees, 
							Animation.RELATIVE_TO_SELF, 
							.5f, 
							Animation.RELATIVE_TO_SELF, 
							.5f
							);
					rotateAnim.setDuration(250);
					rotateAnim.setFillAfter(true);
					
					// Animate the ImageView holding the compass pointer
					compassPointer.setAnimation(rotateAnim);
					currentDegrees = -azimuthInDegrees;
					
					// Reset sensor data arrays
					accelerometerValues = magneticFieldValues = null;
				}
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}
	};
}
