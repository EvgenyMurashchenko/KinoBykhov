package com.example.kinobykhov;

import java.text.SimpleDateFormat;
import static com.example.kinobykhov.constants.Constans.*;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements OnClickListener,
		LoaderCallbacks<Cursor>, TextWatcher {

	Button btnCalendar;
	TextView tvDate;
	ListView lvMain;
	DBase db;
	SimpleCursorAdapter scAdapter;
	SimpleDateFormat sdf;

	static long dateLong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set the current or selected date in the TextView
		sdf = new SimpleDateFormat("d MMMM yyyy");
		
		// currentDate
		if(dateLong == 0) dateLong = System.currentTimeMillis();
	
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvDate.setText(sdf.format(dateLong));
		tvDate.addTextChangedListener(this);

		// open connection
		db = new DBase(this);
		db.open();

		// where to fill
		String[] from = new String[] { COLUMN_TITLES, COLUMN_D, COLUMN_COUNTRY,
				COLUMN_TIMEFILM };
		// to fill
		int[] to = new int[] { R.id.tvTitle, R.id.tvD, R.id.tvCountry,
				R.id.tvTime };

		scAdapter = new SimpleCursorAdapter(this, R.layout.item, null, from,
				to, 0);

		// ListView with affiche
		lvMain = (ListView) findViewById(R.id.lvMain);
		lvMain.setAdapter(scAdapter);

		getSupportLoaderManager().initLoader(0, null, this);

		// button select the date
		btnCalendar = (Button) findViewById(R.id.btnCalendar);
		btnCalendar.setOnClickListener(this);
	}

	protected void onDestroy() {
		super.onDestroy();
		// закрываем подключение при выходе
		db.close();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, CalendarActivity.class);
		startActivityForResult(intent, 1);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

			// from CalendarActivity coming selected date of type long
			dateLong = data.getLongExtra("cDate", RESULT_OK);
			tvDate.setText(sdf.format(dateLong));
		} else
			return;

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
		return new MyCursorLoader(this, db);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		scAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	static class MyCursorLoader extends CursorLoader {

		DBase db;

		public MyCursorLoader(Context context, DBase db) {
			super(context);
			this.db = db;
		}

		public Cursor loadInBackground() {
			Cursor cursor = db.getAllData(dateLong);
			return cursor;
		}
	}

	@Override
	// if date changed in tvDate, then re-create affiche
	public void afterTextChanged(Editable s) {
		getSupportLoaderManager().getLoader(0).forceLoad();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
}
