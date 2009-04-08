package org.flexpay.payments.actions.operations;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;

public class OperationsListAction extends FPActionSupport {

	private Page pager = new Page();

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
}
