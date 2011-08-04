package org.flexpay.tc.action.tariff;

import org.apache.commons.lang.time.FastDateFormat;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.tc.process.TariffCalcResultExportJob;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;

public class TariffCalcResultExportAction extends FPActionSupport {

	private String date;
	private List<String> allDates;
	private String tariffBegin;

	private ProcessManager processManager;
	private TariffCalculationResultService tariffCalculationResultService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (isNotSubmit()) {
			allDates = formatDates(tariffCalculationResultService.getUniqueDates());
			return INPUT;
		}

		if (date == null) {
			addActionError(getText("tc.error.date_must_be_set"));
			return REDIRECT_SUCCESS;
		}

		Map<String, Object> contextVariables = CollectionUtils.map();
		contextVariables.put(TariffCalcResultExportJob.CALCULATION_DATE, DateUtil.parseDate(date, ApplicationConfig.getFutureInfinite()));
		contextVariables.put(TariffCalcResultExportJob.PERIOD_BEGIN_DATE, DateUtil.parseDate(tariffBegin, ApplicationConfig.getFutureInfinite()));
		processManager.startProcess("TariffCalcResultExportProcess", contextVariables);

		log.debug("Export tariff calculation result process started succesfully");

		return REDIRECT_SUCCESS;
	}

	private List<String> formatDates(List<Date> dates) {
		List<String> formattedDates = list();
		FastDateFormat df = FastDateFormat.getInstance("yyyy/MM/dd");
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getAllDates() {
		return allDates;
	}

	public void setTariffBegin(String tariffBegin) {
		this.tariffBegin = tariffBegin;
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
