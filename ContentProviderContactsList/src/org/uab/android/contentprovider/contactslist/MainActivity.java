package org.uab.android.contentprovider.contactslist;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get a reference to the ContentResolver
		ContentResolver contentResolver = getContentResolver();
		
		// Query the ContentResolver for the name of all contacts stored in the device
		Cursor contactsListCursor = contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, 
				new String[] {ContactsContract.Contacts.DISPLAY_NAME}, 
				null, 
				null, 
				null);
		
		// Create an ArrayList of String from the Cursor previously obtained
		List<String> contacts = new ArrayList<String>();
		if (contactsListCursor.moveToFirst()) {
			do {
				
				contacts.add(
						contactsListCursor.getString(
								contactsListCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
								)
						);
				
			} while (contactsListCursor.moveToNext());
		}
		
		// Create an ArrayAdapter for the ArrayList previously created
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
		
		// Set the list's adapter
		setListAdapter(adapter);
	}
}
