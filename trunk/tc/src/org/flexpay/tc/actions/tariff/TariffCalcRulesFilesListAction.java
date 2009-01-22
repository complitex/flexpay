package org.flexpay.tc.actions.tariff;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.service.TariffCalculationRulesFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class TariffCalcRulesFilesListAction extends FPActionSupport {

	private Page<TariffCalculationRulesFile> pager = new Page<TariffCalculationRulesFile>();
	private List<TariffCalculationRulesFile> rulesFiles = Collections.emptyList();

	private TariffCalculationRulesFileService tariffCalculationRulesFileService;

	@NotNull
	public String doExecute() {

		rulesFiles = tariffCalculationRulesFileService.listTariffCalculationRulesFiles(pager);

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

	public List<TariffCalculationRulesFile> getRulesFiles() {
		return rulesFiles;
	}

	public Page<TariffCalculationRulesFile> getPager() {
		return pager;
	}

	public void setPager(Page<TariffCalculationRulesFile> pager) {
		this.pager = pager;
	}

	@Required
	public void setTariffCalculationRulesFileService(TariffCalculationRulesFileService tariffCalculationRulesFileService) {
		this.tariffCalculationRulesFileService = tariffCalculationRulesFileService;
	}

}
