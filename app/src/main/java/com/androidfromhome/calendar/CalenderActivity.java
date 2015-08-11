package com.androidfromhome.calendar;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.androidfromhome.calendar.adapter.AndroidListAdapter;
import com.androidfromhome.calendar.adapter.CalendarAdapter;
import com.androidfromhome.calendar.util.CalendarCollection;

public class CalenderActivity extends Activity {
	public GregorianCalendar cal_month, cal_month_copy;
	private CalendarAdapter cal_adapter;
	private TextView tv_month;
	private ListView lv_android;
	private AndroidListAdapter list_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calender);

		//Termine
		CalendarCollection.date_collection_arr=new ArrayList<CalendarCollection>();
		CalendarCollection.date_collection_arr.add(new CalendarCollection("2015-07-23","Raum 223"));
		CalendarCollection.date_collection_arr.add(new CalendarCollection("2015-11-01", "Raum 242"));
		CalendarCollection.date_collection_arr.add(new CalendarCollection("2015-08-14", "Raum 223"));

		getWidget();
		
		cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
		cal_month_copy = (GregorianCalendar) cal_month.clone();
		cal_adapter = new CalendarAdapter(this, cal_month,CalendarCollection.date_collection_arr);
		
		
		
		tv_month = (TextView) findViewById(R.id.tv_month);
		tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

		ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPreviousMonth();
				refreshCalendar();
			}
		});

		ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();

			}
		});


		GridView gridview = (GridView) findViewById(R.id.gv_calendar);
		gridview.setAdapter(cal_adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
			
				((CalendarAdapter) parent.getAdapter()).setSelected(v,position);
				String selectedGridDate = CalendarAdapter.day_string
						.get(position);
				
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*","");
				int gridvalue = Integer.parseInt(gridvalueString);

				if ((gridvalue > 10) && (position < 8)) {
					setPreviousMonth();
					refreshCalendar();
				} else if ((gridvalue < 7) && (position > 28)) {
					setNextMonth();
					refreshCalendar();
				}
				((CalendarAdapter) parent.getAdapter()).setSelected(v,position);


				((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, CalenderActivity.this);
			}
			
		});
	


	}

	public void getWidget(){


		lv_android = (ListView) findViewById(R.id.lv_android);
		list_adapter=new AndroidListAdapter(CalenderActivity.this,R.layout.list_item, CalendarCollection.date_collection_arr);
		lv_android.setAdapter(list_adapter);

	}
	
	protected void setNextMonth() {
		if (cal_month.get(GregorianCalendar.MONTH) == cal_month
				.getActualMaximum(GregorianCalendar.MONTH)) {
			cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
					cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			cal_month.set(GregorianCalendar.MONTH,
					cal_month.get(GregorianCalendar.MONTH) + 1);
		}

	}

	protected void setPreviousMonth() {
		if (cal_month.get(GregorianCalendar.MONTH) == cal_month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
					cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			cal_month.set(GregorianCalendar.MONTH,
					cal_month.get(GregorianCalendar.MONTH) - 1);
		}

	}

	public void refreshCalendar() {
		cal_adapter.refreshDays();
		cal_adapter.notifyDataSetChanged();
		tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
	}

}
