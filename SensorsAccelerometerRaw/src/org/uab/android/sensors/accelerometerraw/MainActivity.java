package org.uab.android.sensors.accelerometerraw;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	SensorManager	sensorManager;
	Sensor			sensorAccelerometer;
	
	static final long 	UPDATE_THRESHOLD = 500;
	long				lastUpdate;

	TextView	accelerometerXValue;
	TextView	accelerometerYValue;
	TextView	accelerometerZValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		accelerometerXValue = (TextView) findViewById(R.id.xvalue);
		accelerometerYValue = (TextView) findViewById(R.id.yvalue);
		accelerometerZValue = (TextView) findViewById(R.id.zvalue);
		
		// Get a reference to the SensorManager
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		// Get a reference to the accelerometer
		sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if ( sensorAccelerometer == null ) finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Register a listener to obtain accelerometer measurements
		sensorManager.registerListener(
				accelerometerListener, 				// SensorEventListener interface implementation
				sensorAccelerometer, 				// Sensor to register the listener for
				SensorManager.SENSOR_DELAY_UI		// Sensor data update rate
				);
		
		lastUpdate = System.currentTimeMillis();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		// Unregister the SensorEventListener
		sensorManager.unregisterListener(accelerometerListener);
	}
	
	SensorEventListener accelerometerListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			
			// A new reading has been obtained
			if ( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER ) {
				
				// Refresh the display only if a certain time has passed since the last reading
				long currentTime = System.currentTimeMillis();
				if ( currentTime - lastUpdate > UPDATE_THRESHOLD ) {
					
					lastUpdate = currentTime;
					
					float rawX = event.values[0];		// Acceleration force along the X axis (including gravity (m/s2)
					float rawY = event.values[1];		// Acceleration force along the Y axis (including gravity (m/s2)
					float rawZ = event.values[2];		// Acceleration force along the Z axis (including gravity (m/s2)
					
					accelerometerXValue.setText("Accelerometer X-axis value: " + String.valueOf(rawX));
					accelerometerYValue.setText("Accelerometer Y-axis value: " + String.valueOf(rawY));
					accelerometerZValue.setText("Accelerometer Z-axis value: " + String.valueOf(rawZ));
				}
			}
			
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}
	};
}
