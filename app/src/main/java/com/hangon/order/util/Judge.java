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
public class Judge {
	private static int judge=0;

	public static int getJudge() {
		return judge;
	}

	public static void setJudge(int judge) {
		Judge.judge = judge;
	}
}
