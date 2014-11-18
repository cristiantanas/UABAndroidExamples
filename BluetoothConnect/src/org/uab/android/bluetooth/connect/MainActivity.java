package org.uab.android.bluetooth.connect;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	// Bluetooth Message IDs
	public static final int BLUETOOTH_ERROR = 500;
	public static final int BLUETOOTH_SOCKET_CONNECTED = 200;
	public static final int BLUETOOTH_DATA_RECEIVED = 201;

	// Request codes
	static final int BLUETOOTH_ENABLE_REQ = 101;
	static final int BLUETOOTH_CHOOSE_NEIGHBOUR = 102;
	
	// Represent the local Bluetooth adapter (Bluetooth radio).
	// Is the entry-point for all Bluetooth interaction.
	BluetoothAdapter	bluetoothAdapter;
	
	// Determines if the Bluetooth adapter is connected in server or in client mode
	boolean 			isInServerMode;
	
	// Layout Views references
	ProgressBar			progressBar;
	
	TextView			statusTv;
	TextView			bluetoothDataTv;
	
	Button				deviceDiscoveryButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		statusTv = (TextView) findViewById(R.id.statusTv);
		statusTv.setText(R.string.clientMode);
		bluetoothDataTv = (TextView) findViewById(R.id.bluetoothDataTv);
		
		deviceDiscoveryButton = (Button) findViewById(R.id.deviceDiscoveryBt);
		
		// Discover local device's neighbours if users clicks on this Button
		deviceDiscoveryButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				discoverNeighbours();
			}
		});
		
		// Set the Bluetooth adapter in server mode if the user long-clicks this Button
		deviceDiscoveryButton.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				
				statusTv.setText(R.string.serverMode);
				
				isInServerMode = true;
				enableServerMode();
				return true;
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
		
		Toast.makeText(this, getString(R.string.appInfo), Toast.LENGTH_LONG)
			.show();
	}
	
	/**
	 * Discovers Bluetooth devices that are in the local device's range (in a separate Thread).
	 */
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
	
	/**
	 * Configures the Bluetooth adapter in server mode (in a separate Thread).
	 */
	private void enableServerMode() {
		
		setButtonsEnabled(false);
		new BluetoothAcceptConnectionThread(bluetoothConnectionHandler).start();
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
		// If the user has selected a Bluetooth device to connect to, initiate a connection request
		else if ( requestCode == BLUETOOTH_CHOOSE_NEIGHBOUR ) {
			
			if ( resultCode == RESULT_OK ) {
				
				BluetoothDevice selectedDevice = data
						.getParcelableExtra(
								BluetoothDevice.EXTRA_DEVICE
								);
				connectToBluetoothDevice(selectedDevice);
			}
		}
	}
	
	/**
	 * Connects to a specific Bluetooth device.
	 * 
	 * @param device The device to connect to.
	 */
	private void connectToBluetoothDevice(BluetoothDevice device) {
		
		statusTv.setText(R.string.connecting);
		progressBar.setVisibility(View.VISIBLE);
		
		// Start a new Thread to connect to the Bluetooth device since 
		// a bluetooth connection is blocking!
		new BluetoothConnectThread(device, bluetoothConnectionHandler)
			.start();
	}
	
	// Handler object to process incoming messages from worker threads.
	Handler bluetoothConnectionHandler = new Handler(new Handler.Callback() {
		
		// Managing connection Thread
		BluetoothManageConnectionThread connectionThread;
		
		@Override
		public boolean handleMessage(Message msg) {
			
			switch (msg.what) {
			// There has been an exception with the Bluetooth service. Inform the user.
			case BLUETOOTH_ERROR:
				
				progressBar.setVisibility(View.INVISIBLE);
				statusTv.setText(R.string.bluetoothError);
				
				bluetoothDataTv.setText((String) msg.obj);
				break;
				
			// There has been a successful connection between two Bluetooth devices
			case BLUETOOTH_SOCKET_CONNECTED:
				
				progressBar.setVisibility(View.INVISIBLE);
				statusTv.setText(R.string.connected);
				
				connectionThread = (BluetoothManageConnectionThread) msg.obj;
				
				// If in client mode send some data to the server
				if ( !isInServerMode ) {
					String echoMessage = getString(R.string.echoMessage);
					connectionThread.writeToSocket(echoMessage.getBytes());
				}
				
				break;
				
			// Data has been received by one of the Bluetooth devices
			case BLUETOOTH_DATA_RECEIVED:
				// Display the data received from the server
				String receivedData = (String) msg.obj;
				bluetoothDataTv.setText(receivedData);
				
				// If in server mode, echo the received data
				if ( isInServerMode ) {
					connectionThread.writeToSocket(receivedData.getBytes());
				}
				break;

			default:
				break;
			}
			
			return true;
		}
	});
	
	/**
	 * Sets the state of the device discovery Button.
	 * 
	 * @param state Boolean indicating the state of the Button.
	 */
	private void setButtonsEnabled(boolean state) {
		
		deviceDiscoveryButton.setEnabled(state);
	}
}
