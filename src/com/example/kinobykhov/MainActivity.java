package com.example.kinobykhov;

import java.text.SimpleDateFormat;

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
import android.util.Log;
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

		// ��������� ������� ��� ��������� ���� � ��������� ������
		sdf = new SimpleDateFormat("d MMMM yyyy");
		dateLong = System.currentTimeMillis();

		// currentDate
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvDate.setText(sdf.format(dateLong));
		tvDate.addTextChangedListener(this);

		// ��������� �����������
		db = new DBase(this);
		db.open();

		// ������ ���������
		String[] from = new String[] { DBase.COLUMN_TITLES, DBase.COLUMN_D,
				DBase.COLUMN_COUNTRY, DBase.COLUMN_TIMEFILM };
		// ����
		int[] to = new int[] { R.id.tvTitle, R.id.tvD, R.id.tvCountry,
				R.id.tvTime };

		scAdapter = new SimpleCursorAdapter(this, R.layout.item, null, from, to, 0);
		
		//ListView � ������
		lvMain = (ListView) findViewById(R.id.lvMain);
		lvMain.setAdapter(scAdapter);

		getSupportLoaderManager().initLoader(0, null, this);
		
		// ������ ������ ����
		btnCalendar = (Button) findViewById(R.id.btnCalendar);
		btnCalendar.setOnClickListener(this);
	}

	protected void onDestroy() {
		super.onDestroy();
		// ��������� ����������� ��� ������
		db.close();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, CalendarActivity.class);
		startActivityForResult(intent, 1);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			
			// �� CalendarActivity �������� ��������� ���� ���� long
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
	// ���� ���������� ���� � tvDate, �� ������ ��� ��� ��������� �����
	public void afterTextChanged(Editable s) {
		getSupportLoaderManager().getLoader(0).forceLoad();
		Log.d("myLogs", "text chenge");
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
}
