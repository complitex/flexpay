package org.flexpay.payments.actions.reports;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import java.util.Date;

public class GenerateReportAction extends FPActionSupport {

	private Date beginDate = DateUtil.now();
	private Date endDate = DateUtil.now();

	@NotNull
	protected String doExecute() throws Exception {

		// TODO implement

		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		
		return SUCCESS;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
