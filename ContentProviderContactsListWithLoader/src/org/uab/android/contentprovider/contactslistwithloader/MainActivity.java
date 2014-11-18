package org.uab.android.contentprovider.contactslistwithloader;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity {
	
	static final int CONTACTS_LOADER = 0;
	
	SimpleCursorAdapter cursorAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create a SimpleCursorAdapter for the ListView passing a null cursor
		this.cursorAdapter = new SimpleCursorAdapter(
				this, 
				android.R.layout.simple_list_item_1, 
				null, 
				new String[] { Contacts.DISPLAY_NAME }, 
				new int[] { android.R.id.text1 }, 
				0
				);
		
		setListAdapter(this.cursorAdapter);
		
		// Prepare the Loader. Either re-connect with an existing one or start a new one
		getLoaderManager().initLoader(
				CONTACTS_LOADER, 		// Unique identifier for the Loader
				null, 					// Optional arguments to supply to the loader at construction
				loaderCallbacks			// Interface the LoaderManager will call to report about changes in the state of the Loader
				);
	}
	
	LoaderCallbacks<Cursor> loaderCallbacks = new LoaderCallbacks<Cursor>() {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			// A new Loader has to be created.
			// Return a CursorLoader that will take care of creating a Cursor for the 
			// data being displayed.
			CursorLoader cursorLoader = new CursorLoader(
					MainActivity.this, 
					Contacts.CONTENT_URI, 
					new String[] { Contacts._ID, Contacts.DISPLAY_NAME }, 
					null, 
					null, 
					Contacts.DISPLAY_NAME
					);
			
			return cursorLoader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			
			// Swap the new cursor in. The old cursor is automatically closed by the framework.
			cursorAdapter.swapCursor(data);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> arg0) {
			
			cursorAdapter.swapCursor(null);
		}
	};
}
