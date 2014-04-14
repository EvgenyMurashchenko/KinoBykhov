package com.example.kinobykhov;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBase {
	private static final String DB_NAME = "afishaDB.db";
	private static final int DB_VERSION = 1;
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLES = "titles";
	public static final String COLUMN_D = "D";
	public static final String COLUMN_COUNTRY = "country";
	public static final String COLUMN_DATES = "dates";
	public static final String COLUMN_STARTDAY="startDay";
	public static final String COLUMN_ENDDAY="endDay";
	public static final String COLUMN_TIMEFILM="tFilm";
	public static final String COLUMN_DATEID="date_id";
	public static final String COLUMN_FILMID="film_id";

	private final Context fContext;
	private DBHelper mDBHelper;
	private SQLiteDatabase mDB;

	public DBase(Context context) {
		fContext = context;
	}

	// подключение к базе
	public void open() {
		mDBHelper = new DBHelper(fContext, DB_NAME, null, DB_VERSION);
		mDB = mDBHelper.getWritableDatabase();
	}

	public void close() {
		if (mDBHelper != null)
			mDBHelper.close();
	}

	// выборка записей по текущей дате
	public Cursor getAllData(long date) {
		String[] where = {String.valueOf(date),String.valueOf(date)};
		return mDB.rawQuery("select FL." + COLUMN_ID + ", " + COLUMN_TITLES
				+ ", " + COLUMN_D + ", " + COLUMN_COUNTRY
				+ " ," + COLUMN_TIMEFILM + " from films as FL inner join filmDate as FLD inner join timeFilm as FLT on FL."
				+ COLUMN_ID + " = FLT." + COLUMN_FILMID + " and FLD." + COLUMN_ID + " = FLT."+ COLUMN_DATEID + " where "+ COLUMN_STARTDAY + " <= ? and ? <=" + COLUMN_ENDDAY, where);
	}
	
	private class DBHelper extends SQLiteOpenHelper {
		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		public void onCreate(SQLiteDatabase db) {
			ContentValues cv = new ContentValues();
			Resources res = fContext.getResources();

			// таблица с фильмами
			db.execSQL("create table films(" 
					+ COLUMN_ID	+ " integer primary key autoincrement," 
					+ COLUMN_TITLES	+ " text, " 
					+ COLUMN_D + " text, " 
					+ COLUMN_COUNTRY + " text);");
			String[] titles = res.getStringArray(R.array.titles);
			String[] D = res.getStringArray(R.array.D);
			String[] country = res.getStringArray(R.array.country);

			// заполняем ее
			for (int i = 0; i < titles.length; i++) {
				cv.clear();
				cv.put(COLUMN_TITLES, titles[i]);
				cv.put(COLUMN_D, D[i]);
				cv.put(COLUMN_COUNTRY, country[i]);
				db.insert("films", null, cv);
			}

			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			Date date = null;

			// таблица с датами
			db.execSQL("create table filmDate("
					+ COLUMN_ID +" integer primary key autoincrement,"
					+ COLUMN_STARTDAY + " integer,"
					+ COLUMN_ENDDAY + " integer);");
			String[] sDays = res.getStringArray(R.array.sDays);
			String[] eDays = res.getStringArray(R.array.eDays);

			// заполняем ее
			for (int i = 0; i < sDays.length; i++) {
				cv.clear();
				try {
					date = format.parse(sDays[i]);
					cv.put(COLUMN_STARTDAY, date.getTime());
					date = format.parse(eDays[i]);
					cv.put(COLUMN_ENDDAY, date.getTime()+86399000);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				db.insert("filmDate", null, cv);
			}
			
			//таблица со временем показа и айди промежутка и фильма
			db.execSQL("create table timeFilm("
					+ COLUMN_DATEID + " integer,"
					+ COLUMN_TIMEFILM + " text,"
					+ COLUMN_FILMID + " integer);");
			int[] dateId=res.getIntArray(R.array.date_id);
			String[] tFilm=res.getStringArray(R.array.tFilm);
			int[] filmId=res.getIntArray(R.array.film_id);
			for(int i=0; i<dateId.length;i++){
				cv.clear();
				cv.put(COLUMN_DATEID, dateId[i]);
				cv.put(COLUMN_TIMEFILM, tFilm[i]);
				cv.put(COLUMN_FILMID, filmId[i]);
				db.insert("timeFilm", null, cv);
			}
			
			
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			ContentValues cv = new ContentValues();
			Resources res = fContext.getResources();

			db.execSQL("DROP TABLE films");
			db.execSQL("DROP TABLE filmDate");
			db.execSQL("DROP TABLE timeFilm");
			
			// таблица с фильмами
						db.execSQL("create table films(" 
								+ COLUMN_ID	+ " integer primary key autoincrement," 
								+ COLUMN_TITLES	+ " text, " 
								+ COLUMN_D + " text, " 
								+ COLUMN_COUNTRY + " text);");
						String[] titles = res.getStringArray(R.array.titles);
						String[] D = res.getStringArray(R.array.D);
						String[] country = res.getStringArray(R.array.country);

						// заполняем ее
						for (int i = 0; i < titles.length; i++) {
							cv.clear();
							cv.put(COLUMN_TITLES, titles[i]);
							cv.put(COLUMN_D, D[i]);
							cv.put(COLUMN_COUNTRY, country[i]);
							db.insert("films", null, cv);
						}

						SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
						Date date = null;

						// таблица с датами
						db.execSQL("create table filmDate("
								+ COLUMN_ID +" integer primary key autoincrement,"
								+ COLUMN_STARTDAY + " integer,"
								+ COLUMN_ENDDAY + " integer);");
						String[] sDays = res.getStringArray(R.array.sDays);
						String[] eDays = res.getStringArray(R.array.eDays);

						// заполняем ее
						for (int i = 0; i < sDays.length; i++) {
							cv.clear();
							try {
								date = format.parse(sDays[i]);
								cv.put(COLUMN_STARTDAY, date.getTime());
								date = format.parse(eDays[i]);
								cv.put(COLUMN_ENDDAY, date.getTime()+86399000);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							db.insert("filmDate", null, cv);
						}
						
						//таблица со временем показа и айди промежутка и фильма
						db.execSQL("create table timeFilm("
								+ COLUMN_DATEID + " integer,"
								+ COLUMN_TIMEFILM + " text,"
								+ COLUMN_FILMID + " integer);");
						int[] dateId=res.getIntArray(R.array.date_id);
						String[] tFilm=res.getStringArray(R.array.tFilm);
						int[] filmId=res.getIntArray(R.array.film_id);
						for(int i=0; i<dateId.length;i++){
							cv.clear();
							cv.put(COLUMN_DATEID, dateId[i]);
							cv.put(COLUMN_TIMEFILM, tFilm[i]);
							cv.put(COLUMN_FILMID, filmId[i]);
							db.insert("timeFilm", null, cv);
						}
			
		}
	}

}
