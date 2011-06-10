package org.flexpay.tc.actions.tariff;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.service.TariffCalculationRulesFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class TariffCalcRulesFilesListAction extends FPActionWithPagerSupport<TariffCalculationRulesFile> {

	private List<TariffCalculationRulesFile> rulesFiles = Collections.emptyList();

	private TariffCalculationRulesFileService tariffCalculationRulesFileService;

	@NotNull
	@Override
	public String doExecute() {

		rulesFiles = tariffCalculationRulesFileService.listTariffCalculationRulesFiles(getPager());

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

	@Required
	public void setTariffCalculationRulesFileService(TariffCalculationRulesFileService tariffCalculationRulesFileService) {
		this.tariffCalculationRulesFileService = tariffCalculationRulesFileService;
	}

}
