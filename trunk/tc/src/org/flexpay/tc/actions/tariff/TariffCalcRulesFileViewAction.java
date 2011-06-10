package org.flexpay.tc.actions.tariff;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.service.TariffCalculationRulesFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class TariffCalcRulesFileViewAction extends FPActionSupport {

	private TariffCalculationRulesFile rulesFile = new TariffCalculationRulesFile();

	private TariffCalculationRulesFileService tariffCalculationRulesFileService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (rulesFile.isNew()) {
			log.error(getText("common.error.invalid_id"));
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}
		rulesFile = tariffCalculationRulesFileService.read(stub(rulesFile));

		if (rulesFile == null) {
			log.error(getText("common.object_not_selected"));
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

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
		return REDIRECT_ERROR;
	}

	public TariffCalculationRulesFile getRulesFile() {
		return rulesFile;
	}

	public void setRulesFile(TariffCalculationRulesFile rulesFile) {
		this.rulesFile = rulesFile;
	}

	@Required
	public void setTariffCalculationRulesFileService(TariffCalculationRulesFileService tariffCalculationRulesFileService) {
		this.tariffCalculationRulesFileService = tariffCalculationRulesFileService;
	}

}
