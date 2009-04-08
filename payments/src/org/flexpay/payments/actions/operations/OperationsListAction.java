package org.flexpay.payments.actions.operations;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class OperationsListAction extends FPActionSupport {

	private Page pager = new Page();

	private Date beginDate = DateUtil.now();
	private Date endDate = DateUtil.now();

	@NotNull
	protected String doExecute() throws Exception {


		pager.setPageSize(10);
		pager.setPageNumber(1);
		pager.setTotalElements(19);

		// TODO implement

		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		
		return SUCCESS;
	}

	public Page getPager() {
		return pager;
	}

	public void setPager(Page pager) {
		this.pager = pager;
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
