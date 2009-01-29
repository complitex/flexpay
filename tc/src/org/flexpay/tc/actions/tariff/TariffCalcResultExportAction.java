package org.flexpay.tc.actions.tariff;

import org.apache.commons.lang.time.FastDateFormat;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TariffCalcResultExportAction extends FPActionSupport {

	private Date date;
	private List<String> allDates;

	private ProcessManager processManager;
	private TariffCalculationResultService tariffCalculationResultService;

	@NotNull
	protected String doExecute() throws Exception {

		if (!isSubmit()) {
			allDates = formatDates(tariffCalculationResultService.getUniqueDates());
			return INPUT;
		}

		log.debug("Date - {}", date);

		if (date == null) {
			addActionError(getText("tc.error.date_must_be_set"));
			return REDIRECT_SUCCESS;
		}

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
		contextVariables.put("date", date);
		processManager.createProcess("TariffCalcResultExportProcess", contextVariables);

		log.debug("Export tariff calculation result process started succesfully");

		return REDIRECT_SUCCESS;
	}

	private List<String> formatDates(List<Date> dates) {
		List<String> formattedDates = new ArrayList<String>();
		FastDateFormat df = FastDateFormat.getInstance("dd.MM.yyyy");
		for (Date date : dates) {
			formattedDates.add(df.format(date));
		}
		return formattedDates;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<String> getAllDates() {
		return allDates;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

	@Required
	public void setTariffCalculationResultService(TariffCalculationResultService tariffCalculationResultService) {
		this.tariffCalculationResultService = tariffCalculationResultService;
	}

}
