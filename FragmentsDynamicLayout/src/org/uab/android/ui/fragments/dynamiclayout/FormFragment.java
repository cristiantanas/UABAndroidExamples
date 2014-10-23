package org.uab.android.ui.fragments.dynamiclayout;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class FormFragment extends Fragment {
	
	OnSetHourButtonClickListener 	setHourListener;
	Activity						parentActivity;
	
	AutoCompleteTextView	classesAutoCompleteTextView;
	TextView				defaultHourTextView;
	Button					setHourButton;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Enforces that the Activity implements the OnSetHourButtonClickListener
		try {
			setHourListener = (OnSetHourButtonClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnSetHourButtonClickListener.");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate the Fragment's layout from XML
		View layoutView = inflater.inflate(R.layout.fragment_form, container, false);
		return layoutView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Get a reference to the parent Activity
		parentActivity = getActivity();
		
		// Get a reference to the UI element
		classesAutoCompleteTextView = (AutoCompleteTextView) 
				parentActivity.findViewById(R.id.classAutoComplete);

		// Set an ArrayAdapter containing pre-define classes names
		ArrayAdapter<CharSequence> classesAdapter = ArrayAdapter.
				createFromResource(parentActivity, R.array.classes, R.layout.list_item);
		classesAutoCompleteTextView.setAdapter(classesAdapter);
		
		setHourButton = (Button) parentActivity.findViewById(R.id.setHourButton);
		
		// Set an OnClickListener for the set hour Button
		setHourButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				setHourListener.onSetHourClicked(v);
			}
		});
		
		defaultHourTextView = (TextView) parentActivity.findViewById(R.id.defaultHourLabel);
	}
	
	// The Activity will call this function in order to display the new time set
	public void updateHourLabel(int hourOfDay, int minute) {
		
		// Set the hour and minute on the corresponding TextView
		defaultHourTextView.setText(hourOfDay + ":" + minute);
	}
	
	public interface OnSetHourButtonClickListener {
		void onSetHourClicked(View v);
	}
}
