package org.flexpay.tc.actions.tariff;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.process.TariffCalculationJob;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

public class TariffCalculateAction extends FPActionWithPagerSupport<TariffCalculationRulesFile> {

	private Long ruleId;

	private ProcessManager processManager;

	@NotNull
	public String doExecute() throws Exception {

		if (ruleId == null || ruleId <= 0) {
			addActionError(getText("tc.error.incorrect_rule_id"));
			return REDIRECT_SUCCESS;
		}

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
		contextVariables.put(TariffCalculationJob.RULES_ID, ruleId);
		processManager.createProcess("TariffCalculationProcess", contextVariables);

		log.debug("Calculation tariff process for rules with id {} started succesfully");

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

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
