package cc.carm.lib.easysql.api.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDateUtils {
	public static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public TimeDateUtils() {
	}

	public static String getCurrentTime() {
		return getFormat().format(new Date());
	}

	public static String getTimeString(long timeMillis) {
		return getFormat().format(new Date(timeMillis));
	}

	public static String getTimeString(Date time) {
		return getFormat().format(time);
	}

	public static long getTimeMillis(String timeString) {
		if (timeString == null) {
			return -1L;
		} else {
			try {
				return format.parse(timeString).getTime();
			} catch (ParseException var2) {
				return -1L;
			}
		}
	}

	public static Date getTimeDate(String timeString) {
		if (timeString == null) {
			return null;
		} else {
			try {
				return format.parse(timeString);
			} catch (ParseException var2) {
				return null;
			}
		}
	}

	public static String toDHMSStyle(long allSeconds) {
		long days = allSeconds / 86400L;
		long hours = allSeconds % 86400L / 3600L;
		long minutes = allSeconds % 3600L / 60L;
		long seconds = allSeconds % 60L;
		String DateTimes;
		if (days > 0L) {
			DateTimes = days + "天" + (hours > 0L ? hours + "小时" : "") + (minutes > 0L ? minutes + "分钟" : "") + (seconds > 0L ? seconds + "秒" : "");
		} else if (hours > 0L) {
			DateTimes = hours + "小时" + (minutes > 0L ? minutes + "分钟" : "") + (seconds > 0L ? seconds + "秒" : "");
		} else if (minutes > 0L) {
			DateTimes = minutes + "分钟" + (seconds > 0L ? seconds + "秒" : "");
		} else {
			DateTimes = seconds + "秒";
		}

		return DateTimes;
	}

	public static DateFormat getFormat() {
		return format;
	}
}
