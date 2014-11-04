package org.uab.android.persistentdata.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDataRepository {
	
	// Helper object to obtain a reference to the database
	private DatabaseOpenHelper databaseOpenHelper;
	
	// Reference to the SQLiteDatabase to store and retrieve data
	private SQLiteDatabase				sqliteDatabase;

	// Private constructor for the SQLiteDataRepository class
	// Creates a new DatabaseOpenHelper object and holds a reference to the actual database
	public SQLiteDataRepository(Context context) {
		
		this.databaseOpenHelper = new DatabaseOpenHelper(context);
	}
	
	/**
	 * Opens the database for read-only operations
	 */
	public void openDatabaseForReadOnly() {
		
		this.sqliteDatabase = this.databaseOpenHelper.getReadableDatabase();
	}
	
	/**
	 * Opens the database for read and write operations
	 */
	public void openDatabaseForWrite() {
		
		this.sqliteDatabase = this.databaseOpenHelper.getWritableDatabase();
	}
	
	/**
	 * Saves the information for a particular course.
	 * 
	 * @param courseName Name of the course.
	 * @param numberOfCredits Number of credits of the course.
	 * @param checkBoxState String identifying the state of the 
	 * 						days of the week check boxes.
	 * @param hour Starting hour for the course.
	 */
	public void saveCourse(String courseName, int numberOfCredits, 
			String checkBoxState, String hour) {
		
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.COURSE_NAME, courseName);
		values.put(DatabaseOpenHelper.COURSE_NUM_OF_CREDITS, numberOfCredits);
		values.put(DatabaseOpenHelper.COURSE_DAYS_OF_WEEK, checkBoxState);
		values.put(DatabaseOpenHelper.COURSE_STARTS_AT, hour);
		
		this.sqliteDatabase.insert(
				DatabaseOpenHelper.COURSES_TABLE_NAME, 
				null, 
				values);
	}
	
	/**
	 * Returns a specific course based on its ID.
	 * 
	 * @param courseId The unique course ID.
	 * 
	 * @return An iterator over the obtained registers.
	 */
	public Cursor fetchCourseById(int courseId) {
		
		String selection = DatabaseOpenHelper._ID + " = ?";
		String[] selectionArgs = new String[] { String.valueOf(courseId) };
		
		Cursor result = this.sqliteDatabase.query(
						DatabaseOpenHelper.COURSES_TABLE_NAME, 
						DatabaseOpenHelper.COLUMNS, 
						selection, 
						selectionArgs, 
						null, 
						null, 
						null);
		return result;
	}
	
	/**
	 * Returns all saved courses.
	 * 
	 * @return An iterator over the list of courses from the database.
	 */
	public Cursor fetchAllCourses() {
		
		Cursor result = this.sqliteDatabase.query(
						DatabaseOpenHelper.COURSES_TABLE_NAME, 
						DatabaseOpenHelper.COLUMNS, 
						null, 
						null, 
						null, 
						null, 
						null);
		return result;
	}
	
	/**
	 * Release the database resources
	 */
	public void release() {
		
		if ( sqliteDatabase != null ) {
			sqliteDatabase.close();
			sqliteDatabase = null;
		}
	}
}
