package org.flexpay.common.persistence.filter;

import org.flexpay.common.util.DateUtil;

import java.util.Date;

public abstract class DateFilterBase extends ObjectFilter {

	public DateFilterBase() {
		setDate(getEmptyDate());
	}

	public DateFilterBase(Date date) {
		setDate(date);
	}

	public abstract Date getDate();

	public abstract void setDate(Date date);

	protected abstract Date getEmptyDate();

	public boolean dateIsEmpty() {
		return getEmptyDate().equals(getDate());
	}

	public boolean dateIsNotEmpty() {
		return !dateIsEmpty();
	}

	public String getStringDate() {
		String dt = DateUtil.format(getDate());
		return "-".equals(dt) ? "" : dt;
	}

	public void setStringDate(String date) {
		setDate(DateUtil.parseDate(date, getEmptyDate()));
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
