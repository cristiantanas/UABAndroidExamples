package org.uab.android.bluetooth.connect;

import java.util.ArrayList;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BluetoothNeighboursListActivity extends ListActivity {

	static final String EXTRA_DEVICES = "EXTRA_DEVICES";
	
	BluetoothAdapter		bluetoothAdapter;
	
	ListView				neighboursListView;
	ArrayAdapter<String> 	neighboursListAdapter;
	
	public static Intent getCallingIntent(Context context, ArrayList<String> devices) {
		
		Intent listNeighboursIntent = new Intent(context, BluetoothNeighboursListActivity.class);
		listNeighboursIntent.putStringArrayListExtra(EXTRA_DEVICES, devices);
		return listNeighboursIntent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get a reference to the default BluetoothAdapter
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		// Get a reference to the ListView
		neighboursListView = getListView();
		
		// Create a FooterView for the ListView
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View footerView = layoutInflater.inflate(R.layout.list_footerview, neighboursListView, false);
		
		neighboursListView.setFooterDividersEnabled(true);
		
		// Add the FooterView to the list
		neighboursListView.addFooterView(footerView, null, true);
		
		// Get the list of paired devices already discovered
		ArrayList<String> pairedDevices = getIntent().getStringArrayListExtra(EXTRA_DEVICES);
		
		// Initialize the list's ArrayAdapter
		neighboursListAdapter = new ArrayAdapter<String>(
				this, 						// Context
				R.layout.list_item, 		// View for the list items
				pairedDevices				// Data objects
				);
		neighboursListView.setAdapter(neighboursListAdapter);
		
		// Set the ListView OnItemClickListener
		neighboursListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				// If the user clicked on the ListView's footer start discovering neighbours
				if ( parent.getAdapter().getItemViewType(position) == AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER ) {
					
					bluetoothAdapter.startDiscovery();
				}
				else {
					
					// Cancel neighbours discovery
					bluetoothAdapter.cancelDiscovery();
					
					// Get the device's MAC Address
					String selectedDevice = (String) parent.getItemAtPosition(position);
					String macAddress = selectedDevice.split("\n")[1];
					
					BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress);
					
					// Create an Intent to return the selected bluetooth device to the calling Activity
					Intent dataIntent = new Intent();
					dataIntent.putExtra(BluetoothDevice.EXTRA_DEVICE, bluetoothDevice);
					setResult(RESULT_OK, dataIntent);
					finish();
				}
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Register a BroadcastReceiver for new devices discovery
		IntentFilter discoveryFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(neighboursDiscoveryReceiver, discoveryFilter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		bluetoothAdapter.cancelDiscovery();
		unregisterReceiver(neighboursDiscoveryReceiver);
	}
	
	
	BroadcastReceiver neighboursDiscoveryReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			// A new Bluetooth device has been found
			if ( intent.getAction().equals(BluetoothDevice.ACTION_FOUND) ) {
				
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				neighboursListAdapter.add(device.getName() + "\n" + device.getAddress());
				neighboursListAdapter.notifyDataSetChanged();
			}
			
		}
	};
}
