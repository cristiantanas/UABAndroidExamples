package org.uab.android.bluetooth.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

public class BluetoothManageConnectionThread extends Thread {

	// Connected BluetoothSocket
	BluetoothSocket bluetoothSocket;
	
	// Handler object to communicate results back to the main Thread
	Handler			handler;
	
	// Stream objects to handle transmissions through the socket
	InputStream 	socketInputStream;
	OutputStream	socketOutputStream;
	
	public BluetoothManageConnectionThread(BluetoothSocket socket, Handler handler) {
		
		this.bluetoothSocket = socket;
		this.handler = handler;
		
		// Initialize the InputStream and OutputStream to read and write data through the socket
		try {
			this.socketInputStream = this.bluetoothSocket.getInputStream();
			this.socketOutputStream = this.bluetoothSocket.getOutputStream();
		} catch(IOException e) {
			
			Message msg = this.handler.obtainMessage(MainActivity.BLUETOOTH_ERROR, e.getMessage());
			this.handler.sendMessage(msg);
		}
	}
	
	@Override
	public void run() {
		
		Message msg;
		
		// Buffer store for the stream
		byte[] inputBuffer = new byte[1024];
		
		// Bytes read from the stream
		int bytesRead;
		
		// Read from the InputStream. This call will block until there is something to read from the stream.
		while ( true ) {
			
			try {
				
				bytesRead = socketInputStream.read(inputBuffer);
				
				// Send the obtained data back to the UI Thread
				String stringData = new String(inputBuffer, 0, bytesRead);
				msg = this.handler.obtainMessage(MainActivity.BLUETOOTH_DATA_RECEIVED, stringData);
				this.handler.sendMessage(msg);
				
			} catch (IOException e) {
				
				msg = this.handler.obtainMessage(MainActivity.BLUETOOTH_ERROR, e.getMessage());
				this.handler.sendMessage(msg);
				break;
			}
		}
	}
	
	/**
	 * Sends data to the remote device.
	 * 
	 * @param bytesToWrite Bytes to send to the remote device.
	 */
	public void writeToSocket(byte[] bytesToWrite) {
		
		try {
			
			this.socketOutputStream.write(bytesToWrite);
			
		} catch (IOException e) {
			Message msg = this.handler.obtainMessage(MainActivity.BLUETOOTH_ERROR, e.getMessage());
			this.handler.sendMessage(msg);
		}
	}
}
