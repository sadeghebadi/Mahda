package ir.rayacell.mahda.manager;

import ir.smartlab.persindatepicker.PersianDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.widget.TimePicker;

public class DateTimeManager {
	
	public String getDateTime(PersianDatePicker dp , TimePicker tp){
		String datetime = new String();
		datetime = getDate(dp);
		
		datetime += "-";
		datetime += getTime(tp);
		return datetime.toString();
	}

	private String getDate(PersianDatePicker datepicker) {
		
		Date date = datepicker.getDisplayDate();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	     Calendar calendar = Calendar.getInstance();
	     calendar.setTimeInMillis(date.getTime());
	     return formatter.format(calendar.getTime());

//		int day = date.getDay()/*.getDayOfMonth()*/;
//		int month = date.getMonth()/*.getMonth()*/ + 1;
//		int year = date.getYear()/*.getYear()*/;
//
//		String sdate = year + "-";
//		if (month < 10) {
//			sdate += "0" + month + "-";
//		} else {
//			sdate += month + "-";
//		}
//		if (day < 10) {
//			sdate += "0" + day;
//		} else {
//			sdate += day;
//		}
//
//		return date.toString();
	}

	private String getTime(TimePicker timepicker) {
		int hour = timepicker.getCurrentHour();
		int minute = timepicker.getCurrentMinute();
		String time = new String();
		if (hour < 10) {
			time += "0" + hour + "-";
		} else {
			time += hour + "-";
		}

		if (minute < 10) {
			time += "0" + minute;
		} else {
			time += minute;
		}

		return time;
	}
}
