package org.uab.android.bluetooth.connect;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BluetoothConnectThread extends Thread {
	
	// Bluetooth device to connect to
	BluetoothDevice		connectToDevice;
	
	// Bluetooth socket for the remote bluetooth connection
	BluetoothSocket		remoteBluetoothSocket;
	
	// UI Thread handler to communicate back connection results
	Handler				handler;
	
	public BluetoothConnectThread(BluetoothDevice device, Handler handler) {
		
		this.connectToDevice = device;
		this.handler = handler;
		
		// Initialize the BluetoothSocket that will connect to the BluetoothDevice
		try {
			this.remoteBluetoothSocket = this.connectToDevice
					.createRfcommSocketToServiceRecord(
							ConnectionInfo.BLUETOOTH_APP_UUID	// Service record UUID to search a RFCOMM channel
							);
		} catch (IOException e) {
			
			Message msg = this.handler.obtainMessage(MainActivity.BLUETOOTH_ERROR, e.getMessage());
			this.handler.sendMessage(msg);
		}
	}

	@Override
	public void run() {
		
		// Make sure to stop the neighbour discovery before connecting to a device
		if ( BluetoothAdapter.getDefaultAdapter().isDiscovering() )
			BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
		
		// Connect to the Bluetooth device
		try {
			this.remoteBluetoothSocket.connect();	// This performs an SDP lookup on the remote device in order to match the UUID.
		} catch (IOException e) {
			
			try { 
				this.remoteBluetoothSocket.close(); 
			} catch (IOException closeException) {
				Log.e("BLUETOOTH_CONNECT", closeException.getMessage());
			}
			
			Message msg = this.handler.obtainMessage(MainActivity.BLUETOOTH_ERROR, e.getMessage());
			this.handler.sendMessage(msg);
		}
		
		// Manage the connected socket in a separate Thread
		manageConnectedSocket(this.remoteBluetoothSocket);
	}
	
	/**
	 * Initiates a Thread for transferring data.
	 * 
	 * @param socket BluetoothSocket for which the connection has been accepted.
	 */
	private void manageConnectedSocket(BluetoothSocket socket) {
		
		// Send a Message back to MainActivity informing that the socket has been connected 
		// and pass in a reference to the connection Thread
		BluetoothManageConnectionThread connectionThread = 
				new BluetoothManageConnectionThread(
						socket,
						this.handler
						);

		Message msg = this.handler.obtainMessage(MainActivity.BLUETOOTH_SOCKET_CONNECTED, connectionThread);
		this.handler.sendMessage(msg);

		connectionThread.start();
	}
}
