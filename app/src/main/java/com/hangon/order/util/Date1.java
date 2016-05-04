package com.hangon.order.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.ObjectInputStream.GetField;
import java.text.Format;
import java.util.*;

/**
 * 测试日期类Calendar
 * @author Administrator
 *
 */
public class Date1{
	public String GetTime(){
		Calendar c1=new GregorianCalendar();
		c1.setTime(new Date());//建议使用
		String currenttime =(c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.MONTH)+1)+"-"+c1.get(Calendar.DATE)+" "+
				+c1.get(Calendar.HOUR_OF_DAY)+":"+c1.get(Calendar.MINUTE)+":"+c1.get(Calendar.SECOND));
		return currenttime;
	}

}
