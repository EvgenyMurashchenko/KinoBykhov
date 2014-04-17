package com.example.kinobykhov;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import static com.example.kinobykhov.constants.Constans.*;

public class DBase {
	
	private static final String DB_NAME = "afishaDB.db";
	private static final int DB_VERSION = 1;

	static Context fContext;
	private DBHelper mDBHelper;
	private SQLiteDatabase mDB;

	public DBase(Context context) {
		fContext = context;
	}

	// connection to the database
	public void open() {
		mDBHelper = new DBHelper(fContext, DB_NAME, null, DB_VERSION);
		mDB = mDBHelper.getWritableDatabase();
	}

	public void close() {
		if (mDBHelper != null)
			mDBHelper.close();
	}

	// records by current date
	public Cursor getAllData(long date) {
		String[] where = {String.valueOf(date),String.valueOf(date)};
		return mDB.rawQuery("select FL." + COLUMN_ID + ", " + COLUMN_TITLES
				+ ", " + COLUMN_D + ", " + COLUMN_COUNTRY
				+ " ," + COLUMN_TIMEFILM + " from films as FL inner join filmDate as FLD inner join timeFilm as FLT on FL."
				+ COLUMN_ID + " = FLT." + COLUMN_FILMID + " and FLD." + COLUMN_ID + " = FLT."+ COLUMN_DATEID + " where "+ COLUMN_STARTDAY + " <= ? and ? <=" + COLUMN_ENDDAY, where);
	}
	
}
