package org.uab.android.bluetooth.connect;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BluetoothAcceptConnectionThread extends Thread {
	
	// Socket for listening incoming connection requests
	BluetoothServerSocket 	serverSocket;
	
	// Reference to the local Bluetooth adapter
	BluetoothAdapter		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	// Handler object to communicate results back to the MainActivity
	Handler					handler;
	
	public BluetoothAcceptConnectionThread(Handler handler) {
		
		this.handler = handler;
		
		// Get a BluetoothServerSocket
		try {
			
			this.serverSocket = this.bluetoothAdapter
					.listenUsingRfcommWithServiceRecord(
							"Bluetooth Demo", 					// An arbitrary name (could be the application name).
							ConnectionInfo.BLUETOOTH_APP_UUID	// UUID for the Bluetooth service
							);
		} catch (IOException e) { 
			Message msg = this.handler.obtainMessage(MainActivity.BLUETOOTH_ERROR, e.getMessage());
			this.handler.sendMessage(msg);
		}
	}

	@Override
	public void run() {
		
		BluetoothSocket clientSocket;
		
		// Start listening for connection requests
		while ( true ) {
			
			// This is a blocking call and it only returns when either a connection has been accepted 
			// or an exception has occurred.
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				break;
			}
			
			// If the connection has been accepted
			if ( clientSocket != null ) {
				
				// Manage the connection in a separate Thread
				manageConnectedSocket(clientSocket);
				
				// Close the BluetoothServerSocket as typically only one connection is allows per RFCOMM channel
				try {
					serverSocket.close();
				} catch (IOException e) {
					Log.e("BLUETOOTH_CONNECT", e.getMessage());
				}
				
				break;
			}
		}
	}
	
	/**
	 * Initiates a Thread for transferring data.
	 * 
	 * @param socket BluetoothSocket for which the connection has been accepted.
	 */
	private void manageConnectedSocket(BluetoothSocket socket) {
		
		BluetoothManageConnectionThread connectionThread = 
				new BluetoothManageConnectionThread(
						socket, 								// Incoming BluetoothSocket
						this.handler									// Handler object to communicate results back to the main Thread
						);
		
		Message msg = this.handler.obtainMessage(MainActivity.BLUETOOTH_SOCKET_CONNECTED, connectionThread);
		this.handler.sendMessage(msg);
		
		connectionThread.start();
	}
}
