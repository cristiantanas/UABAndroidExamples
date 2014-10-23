package org.uab.android.ui.fragments.dynamiclayout;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements 
				TimePickerDialog.OnTimeSetListener {
	
	OnTimeSetByUserListener onTimeSetListener;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		// Get an instance of a Calendar to set the current time
		Calendar c = Calendar.getInstance();
		int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		// Create a new dialog that displays a TimePicker
		TimePickerDialog timePickerDialog = new TimePickerDialog(
				getActivity(),
				this,
				hourOfDay,
				minute,
				DateFormat.is24HourFormat(getActivity()));
		return timePickerDialog;
	}

	// This function is invoked when the users set the hour and minute within the TimePicker
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		
		try {
			
			// Forward the event to the Activity containing the DialogFragment
			onTimeSetListener = (OnTimeSetByUserListener) getActivity();
			onTimeSetListener.onTimeSet(hourOfDay, minute);
			
		} catch (ClassCastException e) {
			throw new ClassCastException(getActivity().toString() + " must implement OnTimeSetByUserListener.");
		}
	}
	
	// Interface to notify the Activity the user has set the time through the TimePickerDialog
	public interface OnTimeSetByUserListener {
		void onTimeSet(int hourOfDay, int minute);
	}
}
