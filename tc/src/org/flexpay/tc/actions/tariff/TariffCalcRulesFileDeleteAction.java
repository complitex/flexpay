package org.flexpay.tc.actions.tariff;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.service.TariffCalculationRulesFileService;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class TariffCalcRulesFileDeleteAction extends FPActionWithPagerSupport<TariffCalculationRulesFile> {

	private Long id;

	private TariffCalculationRulesFileService tariffCalculationRulesFileService;

	@NotNull
	public String doExecute() {

		if (id == null || id <= 0) {
			addActionError(getText("tc.error.incorrect_rule_id"));
			return REDIRECT_SUCCESS;
		}

		tariffCalculationRulesFileService.delete(new Stub<TariffCalculationRulesFile>(id));

		return REDIRECT_SUCCESS;
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
		return REDIRECT_SUCCESS;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Required
	public void setTariffCalculationRulesFileService(TariffCalculationRulesFileService tariffCalculationRulesFileService) {
		this.tariffCalculationRulesFileService = tariffCalculationRulesFileService;
	}

}
