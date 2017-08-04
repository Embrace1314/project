package top.warmheart.workerunion.pm.server.util;

import java.util.Calendar;
import java.util.Date;

import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.server.exception.WhInvalidYearMonthException;

public class WhYearMonthUtil {

	/**
	 * 校验年月
	 * 
	 * @param year
	 * @param month
	 * @param startDate
	 * @throws Exception
	 */
	public static void check(Integer year, Integer month, Date startDate) throws Exception {
		WhNull.check(year);
		WhNull.check(startDate);
		WhNull.check(startDate);

		YearMonth day = new YearMonth(year, month);
		checkYearMonth(day);

		// 校验时间比当前日期小
		if (day.compareTo(new YearMonth(new Date())) > 0) {
			throw new WhInvalidYearMonthException();
		}
		// 校验时间比开工日期大
		if (day.compareTo(new YearMonth(startDate)) < 0) {
			throw new WhInvalidYearMonthException();
		}
	}

	/**
	 * 校验YearMonth的合法性
	 * 
	 * @param day
	 */
	public static void checkYearMonth(YearMonth day) {
		if (day.getYear() <= 0 || day.getMonth() <= 0 || day.getMonth() > 12) {
			throw new IllegalArgumentException();
		}
	}

}

class YearMonth implements Comparable<Object> {
	private Integer year;
	private Integer month;

	public YearMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		this.year = calendar.get(Calendar.YEAR);
		this.month = calendar.get(Calendar.MONTH) + 1;
	}

	public YearMonth(Integer year, Integer month) {
		super();
		this.year = year;
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getMonthCount() {
		return (year - 1) * 12 + month;
	}

	@Override
	public int compareTo(Object object) {
		if (this == object) {
			return 0;
		}

		if (null == object || !(object instanceof YearMonth)) {
			throw new RuntimeException();
		}

		return this.getMonthCount() - ((YearMonth) object).getMonthCount();
	}

}