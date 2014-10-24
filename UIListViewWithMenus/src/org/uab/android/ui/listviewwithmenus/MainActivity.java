package org.uab.android.ui.listviewwithmenus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements 
				SearchView.OnQueryTextListener {
	
	ListView	listOfColors;
	ArrayAdapter<String>	listArrayAdapter;
	List<String>			countriesList = new ArrayList<String>();
	
	boolean					searchViewCollapsed = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get a reference to the ListView
		listOfColors = (ListView) findViewById(R.id.listView1);
		
		// Register the ListView for a Context Menu
		registerForContextMenu(listOfColors);
		
		// Create a new List from a static array of Strings
		// This is necessary in order to remove elements from the List
		Collections.addAll(countriesList, COUNTRIES);
		
		// Create an ArrayAdapter from the previously created list
		listArrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, countriesList);
		
		// Set the Adapter on the ListView
		listOfColors.setAdapter(listArrayAdapter);
		
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
	
	// This method is invoked to create the options menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.activity_main_menu, menu);
		
		// Register to listen for changes in the search action button (SearchView)
		MenuItem searchMenuItem = menu.findItem(R.id.search);
		searchMenuItem.setOnActionExpandListener(new OnActionExpandListener() {
			
			// This method is invoked when the user presses the search icon
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				
				searchViewCollapsed = false;
				return true;
			}
			
			// This method is invoked when the search view is dismissed
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				
				searchViewCollapsed = true;
				return true;
			}
		});
		
		// Get a reference to the SearchView
		SearchView searchView = (SearchView) searchMenuItem.getActionView();
		
		// Set a listener for query text events in the SearchView
		searchView.setOnQueryTextListener(this);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	// This method is invoked when an item in the options menu has been selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.help:
			Toast.makeText(this, "Has obtenido ayuda!", Toast.LENGTH_SHORT)
				 .show();
			return true;
			
		case R.id.moreHelp:
			Toast.makeText(this, "Has obtenido m‡s ayuda!", Toast.LENGTH_SHORT)
				 .show();
			return true;
			
		case R.id.evenMoreHelp:
			return true;
		
		case R.id.add:
			Toast.makeText(this, "En estos momentos no puedes a–adir m‡s elementos.", Toast.LENGTH_SHORT)
			 	 .show();
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// This method is invoked to create the context menu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.list_context_menu, menu);
	}
	
	// This method is invoked when an item in the context menu has been selected
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int selectedItemPosition = info.position;
		
		switch (item.getItemId()) {
		case R.id.remove:
			// Remove an element from the countries list and then notify the Adapter 
			// that the data has changed
			countriesList.remove(selectedItemPosition);
			listArrayAdapter.notifyDataSetChanged();
			return true;
			
		case R.id.share:
			Toast.makeText(this, "Has seleccionado la opci—n de compartir.", Toast.LENGTH_SHORT)
		 	 	 .show();
			return true;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	// This method is invoked when the user introduces text in the SearchView
	@Override
	public boolean onQueryTextChange(String newText) {
		
		// There is known bug for ListView/ArrayAdapter with a search filter (https://code.google.com/p/android/issues/detail?id=9666)
		// The main issue is that when adding a Filter to the ArrayAdapter, this creates a copy of his items list.
		// The next time notifyDataSetChanged() is called it will check the mOriginalValue array instead of mObjects in the ArrayAdapter.
		// A possible solution is to re-set the ArrayAdapter for the ListView when the user has finished searching within the SearchView
		if ( newText.isEmpty() )
			restartListArrayAdapter();
		
		else
			listArrayAdapter.getFilter().filter(newText);
		
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		
		return false;
	}
	
	// Function that re-sets the ArrayAdapter for the ListView
	private void restartListArrayAdapter() {
		
		listOfColors.setAdapter(null);
		listArrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, countriesList);
		listOfColors.setAdapter(listArrayAdapter);
	}
	
	static final String[] COUNTRIES = new String[] { "Afghanistan", "Albania",
		"Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",
		"Antarctica", "Antigua and Barbuda", "Argentina", "Armenia",
		"Aruba", "Australia", "Austria", "Azerbaijan", "Bahrain",
		"Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin",
		"Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina",
		"Botswana", "Bouvet Island", "Brazil",
		"British Indian Ocean Territory", "British Virgin Islands",
		"Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cote d'Ivoire",
		"Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
		"Central African Republic", "Chad", "Chile", "China",
		"Christmas Island", "Cocos (Keeling) Islands", "Colombia",
		"Comoros", "Congo", "Cook Islands", "Costa Rica", "Croatia",
		"Cuba", "Cyprus", "Czech Republic",
		"Democratic Republic of the Congo", "Denmark", "Djibouti",
		"Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt",
		"El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
		"Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji",
		"Finland", "Former Yugoslav Republic of Macedonia", "France",
		"French Guiana", "French Polynesia", "French Southern Territories",
		"Gabon", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece",
		"Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala",
		"Guinea", "Guinea-Bissau", "Guyana", "Haiti",
		"Heard Island and McDonald Islands", "Honduras", "Hong Kong",
		"Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq",
		"Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
		"Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
		"Latvia", "Lebanon", "Lesotho", "Liberia", "Libya",
		"Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Madagascar",
		"Malawi", "Malaysia", "Maldives", "Mali", "Malta",
		"Marshall Islands", "Martinique", "Mauritania", "Mauritius",
		"Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
		"Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
		"Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
		"New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria",
		"Niue", "Norfolk Island", "North Korea", "Northern Marianas",
		"Norway", "Oman", "Pakistan", "Palau", "Panama",
		"Papua New Guinea", "Paraguay", "Peru", "Philippines",
		"Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
		"Reunion", "Romania", "Russia", "Rwanda", "Sqo Tome and Principe",
		"Saint Helena", "Saint Kitts and Nevis", "Saint Lucia",
		"Saint Pierre and Miquelon", "Saint Vincent and the Grenadines",
		"Samoa", "San Marino", "Saudi Arabia", "Senegal", "Seychelles",
		"Sierra Leone", "Singapore", "Slovakia", "Slovenia",
		"Solomon Islands", "Somalia", "South Africa",
		"South Georgia and the South Sandwich Islands", "South Korea",
		"Spain", "Sri Lanka", "Sudan", "Suriname",
		"Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland",
		"Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
		"The Bahamas", "The Gambia", "Togo", "Tokelau", "Tonga",
		"Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
		"Turks and Caicos Islands", "Tuvalu", "Virgin Islands", "Uganda",
		"Ukraine", "United Arab Emirates", "United Kingdom",
		"United States", "United States Minor Outlying Islands", "Uruguay",
		"Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam",
		"Wallis and Futuna", "Western Sahara", "Yemen", "Yugoslavia",
		"Zambia", "Zimbabwe" };
}
