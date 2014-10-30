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
	SQLiteDataRepository 	sqliteDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sqliteDatabase = new SQLiteDataRepository(this);
		Cursor allCourses = sqliteDatabase.fetchAllCourses();
		
		coursesListAdapter = new SimpleCursorAdapter(
				this, 
				R.layout.courses_list, 
				allCourses, 
				new String[] { DatabaseOpenHelper.COURSE_NAME, DatabaseOpenHelper.COURSE_NUM_OF_CREDITS }, 
				new int[] { R.id.courseName, R.id.courseCredits }, 
				0);
		
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
			
			Cursor allCourses = sqliteDatabase.fetchAllCourses();
			coursesListAdapter.changeCursor(allCourses);
			coursesListAdapter.notifyDataSetChanged();
		}
	}
}
