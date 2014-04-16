package com.example.kinobykhov;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

public class CalendarActivity extends ActionBarActivity implements OnClickListener {

	Button btnSet;
	DatePicker pickerDate;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		
		ActionBar actionBar=getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		btnSet = (Button) findViewById(R.id.btnSet);
		btnSet.setOnClickListener(this);

		pickerDate = (DatePicker) findViewById(R.id.pickerDate);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, pickerDate.getYear());
		cal.set(Calendar.MONTH, pickerDate.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, pickerDate.getDayOfMonth());

		intent.putExtra("cDate", cal.getTimeInMillis());
		setResult(RESULT_OK, intent);
		finish();
	}

}

