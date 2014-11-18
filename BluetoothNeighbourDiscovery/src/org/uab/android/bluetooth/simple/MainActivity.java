package org.uab.android.bluetooth.simple;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	static final int BLUETOOTH_ENABLE_REQ = 101;
	static final int BLUETOOTH_CHOOSE_NEIGHBOUR = 102;
	
	BluetoothAdapter	bluetoothAdapter;
	
	Button				deviceDiscoveryButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		deviceDiscoveryButton = (Button) findViewById(R.id.deviceDiscoveryBt);
		deviceDiscoveryButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				discoverNeighbours();
			}
		});
		
		// Get a reference to the BluetoothAdapter and exit if not available
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if ( bluetoothAdapter == null ) finish();
		
		// Check if Bluetooth is enabled and launch an Intent through the system settings if not
		if ( !bluetoothAdapter.isEnabled() ) {
			
			Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetooth, BLUETOOTH_ENABLE_REQ);
		}
		else {
			
			setButtonsEnabled(true);
		}
	}
	
	private void discoverNeighbours() {
		
		// Check whether there are already paired devices
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		
		ArrayList<String> pairedDevicesInfo = new ArrayList<String>();
		
		if ( pairedDevices.size() > 0 ) { 
			
			for ( BluetoothDevice device : pairedDevices ) { // Loop through the paired devices
				
				pairedDevicesInfo.add(device.getName() + "\n" + device.getAddress());
			}
		}
		
		// Start an Activity that shows a list of possible neighbours and allows the 
					// user to select a device to connect to.
		Intent listNeighboursIntent = BluetoothNeighboursListActivity
				.getCallingIntent(this, pairedDevicesInfo);
		startActivityForResult(listNeighboursIntent, BLUETOOTH_CHOOSE_NEIGHBOUR);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// If the user did not enable Bluetooth exit
		if ( requestCode == BLUETOOTH_ENABLE_REQ ) {
			
			if (resultCode == RESULT_OK)  {
				setButtonsEnabled(true);
			}
			else {
				finish();
			}
		}
		else if ( requestCode == BLUETOOTH_CHOOSE_NEIGHBOUR ) {
			
			if ( resultCode == RESULT_OK ) {
				
				// TODO Connect to the Bluetooth device
			}
		}
	}
	
	private void setButtonsEnabled(boolean state) {
		
		deviceDiscoveryButton.setEnabled(state);
	}
}
