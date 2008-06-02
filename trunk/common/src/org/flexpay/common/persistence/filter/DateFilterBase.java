package org.flexpay.common.persistence.filter;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.util.DateIntervalUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateFilterBase extends ObjectFilter {

	public DateFilterBase() {
		setDate(getEmptyDate());
	}

	public abstract Date getDate();

	public abstract void setDate(Date date);

	protected abstract Date getEmptyDate();

	public String getStringDate() {
		String dt = DateIntervalUtil.format(getDate());
		return "-".equals(dt) ? "" : dt;
	}

	public void setStringDate(String date) {
		if (StringUtils.isBlank(date)) {
			setDate(getEmptyDate());
		}

		try {
			setDate(new SimpleDateFormat("yyyy/MM/dd").parse(date));
		} catch (ParseException e) {
			setDate(getEmptyDate());
		}
	}

	/**
	 * Check if filter should be applyed
	 *
	 * @return <code>true</code> if applying filter is valid, or <code>false</code> otherwise
	 */
	@Override
	public boolean needFilter() {
		return !getDate().equals(getEmptyDate());
	}
}
