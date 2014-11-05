package org.uab.android.contentprovider.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class CoursesListActivity extends ListActivity {
	
	private static final int		COURSE_NEW_REQUEST_CODE = 1;
	
	ArrayAdapter<String> 	adapter;
	ContentResolver 		contentResolver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.contentResolver = getContentResolver();
		
		Cursor coursesListCursor = this.contentResolver.query(
				CoursesProvider.CONTENT_URI, 
				new String[] {CoursesProvider.COURSE_NAME}, 
				null, 
				null, 
				null);
		
		this.adapter = new ArrayAdapter<String>(
				this, 
				android.R.layout.simple_list_item_1, 
				fromCursor(coursesListCursor));
		setListAdapter(this.adapter);
	}
	
	private List<String> fromCursor(Cursor cursor) {
		
		List<String> courses = new ArrayList<String>();
		if (cursor.moveToFirst()) {
			do {
				
				courses.add(
						cursor.getString(
								cursor.getColumnIndex(CoursesProvider.COURSE_NAME)
								)
						);
				
			} while (cursor.moveToNext());
		}
		
		return courses;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.courses_list_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch ( item.getItemId() ) {
		case R.id.addNewCourse:
			Intent addNewCourseIntent = new Intent(this, CoursesNewFormActivty.class);
			startActivityForResult(addNewCourseIntent, COURSE_NEW_REQUEST_CODE);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if ( resultCode == RESULT_OK && requestCode == COURSE_NEW_REQUEST_CODE ) {
			
			Cursor courses = this.contentResolver.query(
					CoursesProvider.CONTENT_URI, 
					new String[] {CoursesProvider.COURSE_NAME}, 
					null, 
					null, 
					null);
			
			adapter.clear();
			adapter.addAll(fromCursor(courses));
			adapter.notifyDataSetChanged();
		}
	}
}
