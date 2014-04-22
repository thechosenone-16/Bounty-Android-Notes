package com.example.gridview.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.util.Log;

public class Utils {

	public static String convertStringToDate(long dateStr){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(dateStr);
		Log.i("Current Date ", "" + cal.getTime()); 
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String retDate = null;
		try {
			retDate = sdf.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retDate;
	}
}
