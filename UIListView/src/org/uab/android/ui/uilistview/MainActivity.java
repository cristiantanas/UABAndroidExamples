package org.uab.android.ui.uilistview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	ListView	listOfColors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listOfColors = (ListView) findViewById(R.id.listView1);
		
		// Create a new Adapter from a XML resource containing a list of colors
		ArrayAdapter<CharSequence> colorsArrayAdapter = ArrayAdapter
				.createFromResource(this, R.array.colors, R.layout.list_item);
		
		// Set the Adapter on the ListView
		listOfColors.setAdapter(colorsArrayAdapter);
		
		// Enable filtering when the user types in the virtual keyboard
		listOfColors.setTextFilterEnabled(true);
		
		// Set an OnItemClickListener on the ListView
		listOfColors.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				CharSequence itemText = ((TextView) view).getText();
				Toast.makeText(MainActivity.this, itemText, Toast.LENGTH_SHORT)
					 .show();
			}
		});
	}
}
