package org.uab.android.bluetooth.connect;

import java.util.UUID;

public class ConnectionInfo {

	// An identifiable name for the Bluetooth service.
	// When a client attempts to connect with a Bluetooth device, it will 
	// carry a UUID that uniquely identifies the service with which it wants 
	// to connect. The UUID from the client must match the stored UUID in the 
	// server for the connection to be accepted.
	//
	// More on UUID: http://en.wikipedia.org/wiki/Universally_unique_identifier
	public static UUID BLUETOOTH_APP_UUID = 
			UUID.fromString("66702e86-c33e-4d47-9b55-7f7ea66c704a");
}
