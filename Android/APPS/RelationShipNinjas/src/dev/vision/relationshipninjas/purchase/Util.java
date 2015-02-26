package dev.vision.relationshipninjas.purchase;

import java.util.Calendar;
import java.util.Date;

public class Util {
	
	private final static long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;

	private static long getDateToLong(Calendar date) {
		date.clear(Calendar.HOUR);
		date.clear(Calendar.SECOND);
		date.clear(Calendar.MILLISECOND);
	    return date.getTimeInMillis();
	}

	public static int getSignedDiffInDays(Calendar beginDate, Calendar endDate) {
	    long beginMS = getDateToLong(beginDate);
	    long endMS = getDateToLong(endDate);
	    long diff = (endMS - beginMS) / (MILLISECS_PER_DAY);
	    return (int)diff;
	}

	public static int getUnsignedDiffInDays(Calendar beginDate, Calendar endDate) {
	    return Math.abs(getSignedDiffInDays(beginDate, endDate));
	}

}
