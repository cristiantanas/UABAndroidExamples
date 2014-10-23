package org.uab.android.ui.uiformexample;

import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends Activity {
	
	AutoCompleteTextView	classesAutoCompleteTextView;
	TextView				defaultHourTextView;
	Button					setHourButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get a reference to the UI element
		classesAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.classAutoComplete);
		
		// Set an ArrayAdapter containing pre-define classes names
		ArrayAdapter<CharSequence> classesAdapter = ArrayAdapter.
				createFromResource(this, R.array.classes, R.layout.list_item);
		classesAutoCompleteTextView.setAdapter(classesAdapter);
		
		setHourButton = (Button) findViewById(R.id.setHourButton);
		
		// Set an OnClickListener for the set hour Button
		setHourButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				// Get an instance of a Calendar to set the current time
				Calendar c = Calendar.getInstance();
				int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);
				
				// Create a new dialog that displays a TimePicker
				TimePickerDialog timePickerDialog = new TimePickerDialog(
						MainActivity.this, 
						timeSetListener, 
						hourOfDay, 
						minute, 
						true);
				timePickerDialog.show();
			}
		});
		
		defaultHourTextView = (TextView) findViewById(R.id.defaultHourLabel);
	}
	
	// Implementation of the OnTimeSetListener interface
	OnTimeSetListener timeSetListener = new OnTimeSetListener() {
		
		// This method is called when the user click done on the TimePicker
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			// Set the hour and minute on the corresponding TextView
			defaultHourTextView.setText(hourOfDay + ":" + minute);
		}
	};
}
