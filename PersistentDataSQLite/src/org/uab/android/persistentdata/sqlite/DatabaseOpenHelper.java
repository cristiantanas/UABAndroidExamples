package org.uab.android.persistentdata.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	// Variable holding the name of the database to be created
	private static final String		DATABASE_NAME = "courses";
	private static final int		DATABASE_VERSION = 1;
	
	// Variables holding the name of the table and columns to be created
	static final String		COURSES_TABLE_NAME = "ASIGNATURAS";
	static final String		_ID = "_id";
	static final String		COURSE_NAME = "asignatura";
	static final String		COURSE_NUM_OF_CREDITS = "creditos";
	static final String		COURSE_DAYS_OF_WEEK = "dias";
	static final String		COURSE_STARTS_AT = "hora";
	
	// Static variable containing the name of all the table's columns
	static final String[] COLUMNS = {
		_ID,
		COURSE_NAME,
		COURSE_NUM_OF_CREDITS,
		COURSE_DAYS_OF_WEEK,
		COURSE_STARTS_AT
	};
	
	// Variable holding the SQL instruction to create a new table 
	private static final String CREATE_COURSES_TABLE = 
			"CREATE TABLE " + COURSES_TABLE_NAME + "( " +
					_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COURSE_NAME + " TEXT NOT NULL, " +
					COURSE_NUM_OF_CREDITS + " INTEGER NOT NULL, " +
					COURSE_DAYS_OF_WEEK + " TEXT, " +
					COURSE_STARTS_AT + " TEXT)";
	
	// Public constructor. Delegates the construction to the SQLiteOpenHelper class
	public DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Create and initialize the database if not present
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_COURSES_TABLE);
	}

	// Update the database if any changes have occurred (changes in the version number)
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
