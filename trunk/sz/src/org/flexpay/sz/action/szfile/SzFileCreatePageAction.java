package org.flexpay.sz.action.szfile;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.sz.persistence.Oszn;
import org.flexpay.sz.service.OsznService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.map;

public class SzFileCreatePageAction extends FPActionSupport {

	private Integer curYear;
	private Integer curMonth;

	private Map<Integer, String> months = map();
	private List<Oszn> osznList = list();

	private OsznService osznService;

	@NotNull
	@Override
	public String doExecute() {

		String[] ms = new DateFormatSymbols(getLocale()).getMonths();

		for (int i = 0; i < ms.length - 1; i++) {
			months.put(i, ms[i]);
		}

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		curMonth = c.get(Calendar.MONTH);
		curYear = c.get(Calendar.YEAR);

		osznList = osznService.getEntities();
		if (osznList.isEmpty()) {
			addActionError(getText("sz.file_upload.oszn_absent"));
			return SUCCESS;
		}

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return osznList.isEmpty() ? "oszn_absent" : SUCCESS;
	}

	public Map<Integer, String> getMonths() {
		return months;
	}

	public List<Oszn> getOsznList() {
		return osznList;
	}

	public Integer getCurYear() {
		return curYear;
	}

	public Integer getCurMonth() {
		return curMonth;
	}

	@Required
    public void setOsznService(OsznService osznService) {
        this.osznService = osznService;
    }

}
