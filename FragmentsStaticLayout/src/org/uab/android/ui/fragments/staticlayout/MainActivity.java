package org.uab.android.ui.fragments.staticlayout;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements 
				FormFragment.OnSetHourButtonClickListener,
				TimePickerFragment.OnTimeSetByUserListener {
	
	static final String TIME_PICKER_FRAGMENT_TAG = "TIME_PICKER_FRAGMENT";
	
	FormFragment	formFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get a reference to the FormFragment contained by the Activity
		formFragment = (FormFragment) getFragmentManager().findFragmentById(R.id.formFragment);
	}

	@Override
	public void onSetHourClicked(View v) {
		
		// When user click on the set hour Button create a new TimePickerFragment
		DialogFragment timePickerFragment = new TimePickerFragment();
		timePickerFragment.show(getFragmentManager(), TIME_PICKER_FRAGMENT_TAG);
	}

	@Override
	public void onTimeSet(int hourOfDay, int minute) {
		
		// The user has set the time using the TimePickerDialog
		// Update the time label within the Fragment's layout
		formFragment.updateHourLabel(hourOfDay, minute);
	}
}
