package org.uab.android.persistentdata.sqlite;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class CoursesListActivity extends ListActivity {
	
	private static final int		COURSE_NEW_REQUEST_CODE = 1;
	
	SimpleCursorAdapter		coursesListAdapter;
	SQLiteDataRepository 	repository;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Obtain a reference to the SQLiteDataRepository
		repository = new SQLiteDataRepository(this);
		
		// Open the database for reading
		repository.openDatabaseForReadOnly();
		
		// Get all courses from the database
		Cursor allCourses = repository.fetchAllCourses();
		
		// Create a new Cursor adapter for the courses list
		coursesListAdapter = new SimpleCursorAdapter(
				this, 
				R.layout.courses_list, 
				allCourses, 
				new String[] { DatabaseOpenHelper.COURSE_NAME, DatabaseOpenHelper.COURSE_NUM_OF_CREDITS }, 
				new int[] { R.id.courseName, R.id.courseCredits }, 
				0);
		
		// Set the list adapter to the SimpleCursorAdapter created
		setListAdapter(coursesListAdapter);
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
			// If the user select to add a new course start the form Activity for result
			Intent addNewCourseIntent = new Intent(this, CoursesNewFormActivty.class);
			startActivityForResult(addNewCourseIntent, COURSE_NEW_REQUEST_CODE);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// Check the result from the CourseNewFormActivity
		if ( resultCode == RESULT_OK && requestCode == COURSE_NEW_REQUEST_CODE ) {
			
			// Requery the repository for a list of all courses
			Cursor allCourses = repository.fetchAllCourses();
			
			// Replace the Cursor in the list's adapter
			coursesListAdapter.changeCursor(allCourses);
			
			// Notify the list adapter that it must refresh itself
			coursesListAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onDestroy() {
		
		// Release database resources
		repository.release();
		
		super.onDestroy();
	}
}
