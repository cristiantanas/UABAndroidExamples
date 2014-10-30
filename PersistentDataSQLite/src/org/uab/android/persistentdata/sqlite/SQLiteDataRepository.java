package org.uab.android.persistentdata.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class SQLiteDataRepository {
	
	private DatabaseOpenHelper		databaseOpenHelper;

	public SQLiteDataRepository(Context context) {
		
		this.databaseOpenHelper = new DatabaseOpenHelper(context);
	}
	
	public void saveCourse(String courseName, int numberOfCredits, 
			String checkBoxState, String hour) {
		
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.COURSE_NAME, courseName);
		values.put(DatabaseOpenHelper.COURSE_NUM_OF_CREDITS, numberOfCredits);
		values.put(DatabaseOpenHelper.COURSE_DAYS_OF_WEEK, checkBoxState);
		values.put(DatabaseOpenHelper.COURSE_STARTS_AT, hour);
		
		this.databaseOpenHelper.getWritableDatabase().insert(
				DatabaseOpenHelper.COURSES_TABLE_NAME, 
				null, 
				values);
	}
	
	public Cursor fetchCourseById(int courseId) {
		
		String selection = DatabaseOpenHelper._ID + " = ?";
		String[] selectionArgs = new String[] { String.valueOf(courseId) };
		
		Cursor result = this.databaseOpenHelper.getReadableDatabase()
				.query(
						DatabaseOpenHelper.COURSES_TABLE_NAME, 
						DatabaseOpenHelper.COLUMNS, 
						selection, 
						selectionArgs, 
						null, 
						null, 
						null);
		return result;
	}
	
	public Cursor fetchAllCourses() {
		
		Cursor result = this.databaseOpenHelper.getReadableDatabase()
				.query(
						DatabaseOpenHelper.COURSES_TABLE_NAME, 
						DatabaseOpenHelper.COLUMNS, 
						null, 
						null, 
						null, 
						null, 
						null);
		return result;
	}
}
