package org.flexpay.tc.actions.tariff;

import org.apache.commons.lang.time.FastDateFormat;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.process.TariffCalcResultExportJob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TariffCalcResultExportAction extends FPActionSupport {

	@NonNls
	protected static final String MODAL = "modal";

	private String date;
	private List<String> allDates;
	private String tariffBegin;
	private String modal = "";

	private ProcessManager processManager;
	private TariffCalculationResultService tariffCalculationResultService;

	@NotNull
	protected String doExecute() throws Exception {

		log.debug("modal = {}", modal);
		log.debug("date = {}", date);
		log.debug("tariffBegin = {}", tariffBegin);

		if (hisModal()) {
			return MODAL;
		}

		if (isNotSubmit()) {
			allDates = formatDates(tariffCalculationResultService.getUniqueDates());
			return INPUT;
		}

		if (date == null) {
			addActionError(getText("tc.error.date_must_be_set"));
			return REDIRECT_SUCCESS;
		}

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
		contextVariables.put(TariffCalcResultExportJob.CALCULATION_DATE, DateUtil.parseDate(date, ApplicationConfig.getFutureInfinite()));
		processManager.createProcess("TariffCalcResultExportProcess", contextVariables);

		log.debug("Export tariff calculation result process started succesfully");

		return REDIRECT_SUCCESS;
	}

	private List<String> formatDates(List<Date> dates) {
		List<String> formattedDates = new ArrayList<String>();
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

	public boolean hisModal() {
		return modal != null && !modal.equals("");
	}

	public String getModal() {
		return modal;
	}

	public void setModal(String modal) {
		this.modal = modal;
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
