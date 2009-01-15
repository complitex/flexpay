package org.flexpay.tc.actions.sewertype;

import org.flexpay.bti.persistence.SewerType;
import org.flexpay.bti.service.SewerTypesService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class SewerTypesListAction extends FPActionSupport {

	private Page<SewerType> pager = new Page<SewerType>();
	private List<SewerType> sewerTypes = Collections.emptyList();

	private SewerTypesService sewerTypesService;

	@NotNull
	public String doExecute() {

		sewerTypes = sewerTypesService.listSewerTypes(pager);

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
		return SUCCESS;
	}

	public List<SewerType> getSewerTypes() {
		return sewerTypes;
	}

	public Page<SewerType> getPager() {
		return pager;
	}

	public void setPager(Page<SewerType> pager) {
		this.pager = pager;
	}

	@Required
	public void setSewerTypesService(SewerTypesService sewerTypesService) {
		this.sewerTypesService = sewerTypesService;
	}

}
