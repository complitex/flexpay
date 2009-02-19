package org.flexpay.tc.actions.buildingattributes;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.tc.process.TariffCalcResultExportForBuildingJob;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

public class UploadTCResultsAction extends FPActionSupport {

    private String buildingId;
    private String calculationDate;
    private String date = DateUtil.format(DateUtil.now());

    private ProcessManager processManager;

    @NotNull
    protected String doExecute() throws Exception {

        if (isNotSubmit()) {

            return INPUT;
        }

        Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

        log.info("Building id := {}", buildingId);
        log.info("CalculationDate := {}", calculationDate);
		log.info("Date := {}", date);

        contextVariables.put(TariffCalcResultExportForBuildingJob.CALCULATION_DATE, DateUtil.parseDate(calculationDate, ApplicationConfig.getFutureInfinite()));
        contextVariables.put(TariffCalcResultExportForBuildingJob.BUILDING_ID, buildingId);
        contextVariables.put(TariffCalcResultExportForBuildingJob.PERIOD_BEGIN_DATE, DateUtil.parseBeginDate(date));

        processManager.createProcess("TariffCalcResultExportForBuildingProcess", contextVariables);

        log.info("TariffCalcResultExportForBuildingProcess started with parameters {} and {}", calculationDate, buildingId);

        return REDIRECT_SUCCESS;
    }

    @NotNull
    protected String getErrorResult() {
        return INPUT;
    }

    // form fields
    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setCalculationDate(String calculationDate) {
        this.calculationDate = calculationDate;
    }

    public String getCalculationDate() {
        return calculationDate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    // spring bean properties
    @Required
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

}
