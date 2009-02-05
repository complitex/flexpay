package org.flexpay.tc.actions.tariff;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.process.TariffCalculationJob;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class TariffCalculateAction extends FPActionWithPagerSupport<TariffCalculationRulesFile> {

	private Long id;
	private Date calcDate;

	private ProcessManager processManager;

	@NotNull
	public String doExecute() throws Exception {

		if (id == null || id <= 0) {
			addActionError(getText("tc.error.incorrect_rule_id"));
			return REDIRECT_SUCCESS;
		}

		if (isNotSubmit()) {
			return INPUT;
		}

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
		contextVariables.put(TariffCalculationJob.RULES_ID, id);
		contextVariables.put(TariffCalculationJob.CALC_DATE, calcDate);
		processManager.createProcess("TariffCalculationProcess", contextVariables);

		log.debug("Calculation tariff process for rules with id {} for date {} started succesfully", id, calcDate);

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCalcDate(Date calcDate) {
		this.calcDate = calcDate;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
