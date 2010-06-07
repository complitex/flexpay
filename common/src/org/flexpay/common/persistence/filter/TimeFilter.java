package org.flexpay.common.persistence.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class TimeFilter extends ObjectFilter {

	private static final Logger log = LoggerFactory.getLogger(TimeFilter.class);

	protected int hours;
	protected int minutes;
	protected int seconds;
    protected boolean withSec = true;
	private static final String TIME_FORMAT_WITH_SEC = "HH:mm:ss";
    private static final String TIME_FORMAT = "HH:mm";

	protected TimeFilter() {
	}

	public TimeFilter(Date dt, boolean withSec) {

        this.withSec = withSec;

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dt);

		init(calendar);
	}

	public TimeFilter(Calendar calendar, boolean withSec) {
        this.withSec = withSec;
		init(calendar);
	}

	private void init(Calendar calendar) {

		hours = calendar.get(Calendar.HOUR_OF_DAY);
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
		return new SimpleDateFormat(withSec ? TIME_FORMAT_WITH_SEC : TIME_FORMAT).format(calendar.getTime());
	}

	public void setStringDate(String date) {

		log.debug("Parsing date: {}", date);

		Calendar calendar = new GregorianCalendar();
		try {
			Date dt = new SimpleDateFormat(withSec ? TIME_FORMAT_WITH_SEC : TIME_FORMAT).parse(date);
			calendar.setTime(dt);
			init(calendar);
		} catch (ParseException ex) {
			initDefaults();
		}
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
