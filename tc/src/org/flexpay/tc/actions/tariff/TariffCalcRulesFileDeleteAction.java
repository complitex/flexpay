package org.flexpay.tc.actions.tariff;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.service.TariffCalculationRulesFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class TariffCalcRulesFileDeleteAction extends FPActionWithPagerSupport<TariffCalculationRulesFile> {

	private Set<Long> objectIds = set();

	private TariffCalculationRulesFileService tariffCalculationRulesFileService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		tariffCalculationRulesFileService.disableByIds(objectIds);

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

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setTariffCalculationRulesFileService(TariffCalculationRulesFileService tariffCalculationRulesFileService) {
		this.tariffCalculationRulesFileService = tariffCalculationRulesFileService;
	}

}
