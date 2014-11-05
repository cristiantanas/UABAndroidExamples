package org.uab.android.contentprovider.sqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class CoursesProvider extends ContentProvider {
	
	/* ========COURSES_PROVIDER CONTRACT===============================================
	 * Defines
	 * 		. CONTENT_URI
	 * 		. COURSES_PROVIDER COLUMNS
	 */
	public static final Uri CONTENT_URI = Uri
			.parse("content://org.uab.android.contentprovider.sqlite.provider/courses");
	
	public static final String COURSE_NAME = DatabaseOpenHelper.COURSE_NAME;
	public static final String COURSE_CREDITS = DatabaseOpenHelper.COURSE_NUM_OF_CREDITS;
	public static final String COURSE_DAYS = DatabaseOpenHelper.COURSE_DAYS_OF_WEEK;
	public static final String COURSE_HOUR = DatabaseOpenHelper.COURSE_STARTS_AT;
	/* ================================================================================= */
	
	private static final int ALL_COURSES = 1;
	private static final int COURSE_BY_CREDITS = 2;
	
	private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		uriMatcher.addURI("org.uab.android.contentprovider.sqlite.provider", 
				"courses", ALL_COURSES);
		
		uriMatcher.addURI("org.uab.android.contentprovider.sqlite.provider", 
				"courses/#", COURSE_BY_CREDITS);
	}
	
	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		
		SQLiteDataRepository coursesDb = new SQLiteDataRepository(getContext());
		coursesDb.openDatabaseForReadOnly();
		Cursor result = null;
		
		switch ( uriMatcher.match(uri) ) {
		case ALL_COURSES:
			result = coursesDb.fetchAllCourses(projection);
			break;
			
		case COURSE_BY_CREDITS:
			int credits = Integer.parseInt(uri.getLastPathSegment());
			result = coursesDb.fetchCourseByCredits(projection, credits);

		default:
			break;
		}
		
		
		return result;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		if ( uriMatcher.match(uri) == ALL_COURSES ) {
			SQLiteDataRepository coursesDb = new SQLiteDataRepository(getContext());
			coursesDb.openDatabaseForWrite();
		
			long rowId = coursesDb.insert(values);
			if ( rowId > 0 ) {
				Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
				return resultUri;
			}
		}
			
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}
	
	@Override
	public String getType(Uri uri) {
		return null;
	}

}
