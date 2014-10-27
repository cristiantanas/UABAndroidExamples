package org.uab.android.persistentdata.internalstorage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static final String		LOG_TAG = "PERSISTENT_DATA_INTERNAL_STORAGE_MAIN_ACTIVITY";
	private static final String		FILENAME = "GRRM_Quote.txt";
	
	TextView						quoteTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		quoteTextView = (TextView) findViewById(R.id.quoteTv);
		
		// Check whether the file exists on the underlying file system
		if ( !getFileStreamPath(FILENAME).exists() ) {
			
			// If the text file doesn't exist, then create a new one
			try {
				createNewFile(FILENAME);
			} catch (FileNotFoundException e) {
				Log.e(LOG_TAG, "FileNotFoundException");
			}
		}
		
		// Read the data from the previously created File
		try {
			String fileContents = readInternalStorageFile(FILENAME);
			quoteTextView.setText(fileContents);
		} catch (IOException e) {
			Log.e(LOG_TAG, "IOException");
		}
	}
	
	private void createNewFile(String filename) throws FileNotFoundException {
		
		FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);
		PrintWriter writer = new PrintWriter(
				new BufferedWriter(
						new OutputStreamWriter(fos)
						)
				);
		
		writer.println(GRRMQuote);
		writer.println(Author);
		
		writer.close();
	}
	
	private String readInternalStorageFile(String filename) throws IOException {
		
		FileInputStream fis = openFileInput(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		
		StringBuilder builder = new StringBuilder();
		String textLine = "";
		
		while ( (textLine = reader.readLine()) != null ) {
			
			builder.append(textLine);
		}
		
		return builder.toString();
	}
	
	static final String GRRMQuote = "\"Oh, my sweet summer child,\" Old Nan" +
			" said quietly, \"what do you know of fear?" +
			"Fear is for the winter, my little lord, when the snows fall a " +
			"hundred feet deep and the ice wind comes howling out of the north. " +
			"Fear is for the long night, when the sun hides its face for years " +
			"at a time, and little children are born and live and die all in " +
			"darkness while the direwolves grow gaunt and hungry, and the white " +
			"walkers move through the woods\".";
	
	static final String Author = "― George R.R. Martin, A Game of Thrones";
}
