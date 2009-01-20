package org.flexpay.sz.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.sz.persistence.Oszn;
import org.flexpay.sz.service.OsznService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ImportFileAction extends FPActionSupport {

	private Integer curYear;
	private Integer curMonth;

	private List<Oszn> osznList;

	private OsznService osznService;

	@NotNull
	public String doExecute() {

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		curMonth = c.get(Calendar.MONTH);
		curYear = c.get(Calendar.YEAR);

		osznList = osznService.getEntities();
		if (osznList.isEmpty()) {
			return "oszn_absent";
		}

		return INPUT;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return osznList.isEmpty() ? "oszn_absent" : INPUT;
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
