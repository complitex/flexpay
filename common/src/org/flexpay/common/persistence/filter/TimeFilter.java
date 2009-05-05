package org.flexpay.common.persistence.filter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public abstract class TimeFilter extends ObjectFilter {
	protected int hours;
	protected int minutes;
	protected int seconds;

	protected TimeFilter() {
	}

	public TimeFilter(Date dt) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dt);

		init(calendar);
	}

	public TimeFilter(Calendar calendar) {

		init(calendar);
	}

	private void init(Calendar calendar) {

		hours = calendar.get(Calendar.HOUR);
		minutes = calendar.get(Calendar.MINUTE);
		seconds = calendar.get(Calendar.SECOND);
	}

	public int getHours() {
		return hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public String getStringDate() {
		Calendar calendar = new GregorianCalendar(1900, 1, 1, hours, minutes, seconds);
		return new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
	}

	public void setStringDate(String date) {
		Calendar calendar = new GregorianCalendar();
		try {
			Date dt = new SimpleDateFormat("HH:mm:ss").parse(date);
			calendar.setTime(dt);
		} catch (ParseException ex) {
			initDefaults();
		}

		init(calendar);
	}

	protected abstract void initDefaults();

	public Date setTime(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return setTime(calendar).getTime();
	}

	public Calendar setTime(Calendar calendar) {

		calendar.set(Calendar.HOUR, hours);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, seconds);
		return calendar;
	}
}
