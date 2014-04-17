package com.example.kinobykhov;

import static com.example.kinobykhov.constants.Constans.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {

		super(context, name, factory, version);
	}

	public void onCreate(SQLiteDatabase db) {
		Resources res = DBase.fContext.getResources();

		// table with films
		createTable(db, res.getStringArray(R.array.titles),
				res.getStringArray(R.array.D),
				res.getStringArray(R.array.country));

		// table with dates
		createTable(db, res.getStringArray(R.array.sDays),
				res.getStringArray(R.array.eDays));

		// table with time and id_timeslot and id_film
		createTable(db, res.getIntArray(R.array.date_id),
				res.getStringArray(R.array.tFilm),
				res.getIntArray(R.array.film_id));

	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Resources res = DBase.fContext.getResources();

		db.execSQL("DROP TABLE films");
		db.execSQL("DROP TABLE filmDate");
		db.execSQL("DROP TABLE timeFilm");

		// table with films
		createTable(db, res.getStringArray(R.array.titles),
				res.getStringArray(R.array.D),
				res.getStringArray(R.array.country));

		// table with dates
		createTable(db, res.getStringArray(R.array.sDays),
				res.getStringArray(R.array.eDays));

		// table with time and id_timeslot and id_film
		createTable(db, res.getIntArray(R.array.date_id),
				res.getStringArray(R.array.tFilm),
				res.getIntArray(R.array.film_id));

	}

	private void createTable(SQLiteDatabase dBase, String[] titles, String[] D,
			String[] country) {

		ContentValues cv = new ContentValues();

		dBase.execSQL("create table films(" + COLUMN_ID
				+ " integer primary key autoincrement," + COLUMN_TITLES
				+ " text, " + COLUMN_D + " text, " + COLUMN_COUNTRY + " text);");

		for (int i = 0; i < titles.length; i++) {
			cv.clear();
			cv.put(COLUMN_TITLES, titles[i]);
			cv.put(COLUMN_D, D[i]);
			cv.put(COLUMN_COUNTRY, country[i]);
			dBase.insert("films", null, cv);
		}

	}

	private void createTable(SQLiteDatabase dBase, String[] sDays,
			String[] eDays) {
		ContentValues cv = new ContentValues();

		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		Date date = null;

		dBase.execSQL("create table filmDate(" + COLUMN_ID
				+ " integer primary key autoincrement," + COLUMN_STARTDAY
				+ " integer," + COLUMN_ENDDAY + " integer);");
		// заполняем ее
		for (int i = 0; i < sDays.length; i++) {
			cv.clear();
			try {
				date = format.parse(sDays[i]);
				cv.put(COLUMN_STARTDAY, date.getTime());
				date = format.parse(eDays[i]);
				cv.put(COLUMN_ENDDAY, date.getTime() + 86399000);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			dBase.insert("filmDate", null, cv);
		}

	}

	private void createTable(SQLiteDatabase dBase, int[] dateId,
			String[] tFilm, int[] filmId) {
		ContentValues cv = new ContentValues();

		dBase.execSQL("create table timeFilm(" + COLUMN_DATEID + " integer,"
				+ COLUMN_TIMEFILM + " text," + COLUMN_FILMID + " integer);");

		for (int i = 0; i < dateId.length; i++) {
			cv.clear();
			cv.put(COLUMN_DATEID, dateId[i]);
			cv.put(COLUMN_TIMEFILM, tFilm[i]);
			cv.put(COLUMN_FILMID, filmId[i]);
			dBase.insert("timeFilm", null, cv);
		}

	}
}
