package com.nunet.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
 
	public  static String format(String string,Calendar mCalendar){
		SimpleDateFormat fmtOut = new SimpleDateFormat(string);
		return fmtOut.format(mCalendar.getTime());
	}
	
}
