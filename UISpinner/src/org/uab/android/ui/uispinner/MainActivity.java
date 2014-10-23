package org.uab.android.ui.uispinner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Spinner		colorSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get a reference to the UI elements
		colorSpinner = (Spinner) findViewById(R.id.spinner1);
		
		// Create an ArrayAdapter from a XML resource that holds a list of colors
		ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.
				createFromResource(this, R.array.colors, R.layout.spinner_item);
		
		// Set the Adapter for the Spinner
		colorSpinner.setAdapter(colorAdapter);
		
		// Set an OnItemSelectedListener on the Spinner
		colorSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				String selectedColor = parent.getItemAtPosition(position).toString();
				Toast.makeText(parent.getContext(), selectedColor, Toast.LENGTH_SHORT)
					 .show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}
}
