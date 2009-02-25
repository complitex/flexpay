package org.flexpay.tc.actions.buildingattributes;

import org.flexpay.ab.persistence.Building;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.service.TariffCalculationResultService;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class TCResultsEditAction extends FPActionSupport {

    // form data
    private Map<Integer, String> tariffMap = CollectionUtils.map();
    private String buildingId;
    private String calculationDate;

    // required services
    private TariffCalculationResultService tariffCalculationResultService;

    @NotNull
    protected String doExecute() throws Exception {

        Date calcDate = DateUtil.parseBeginDate(calculationDate);

        for (Integer tariffId : tariffMap.keySet()) {

            if (StringUtils.isEmpty(tariffMap.get(tariffId))) {
                continue;
            }

            Stub<Tariff> tariffStub = new Stub<Tariff>(tariffId.longValue());
            Stub<Building> buildingStub = new Stub<Building>(Long.parseLong(buildingId));

            TariffCalculationResult result = tariffCalculationResultService.
                    findTariffCalcResults(calcDate, tariffStub, buildingStub);

			String valueStr = tariffMap.get(tariffId);
            result.setValue(valueStr == null || valueStr.equals("") ? BigDecimal.ZERO : new BigDecimal(valueStr));
            tariffCalculationResultService.update(result);
        }

        return REDIRECT_SUCCESS;
    }

    @NotNull
    protected String getErrorResult() {
        return INPUT;
    }

    // form data
    public void setTariffMap(Map<Integer, String> tariffMap) {
        this.tariffMap = tariffMap;
    }

    public Map<Integer, String> getTariffMap() {
        return tariffMap;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public void setCalculationDate(String calculationDate) {
        this.calculationDate = calculationDate;
    }

    @Required
    public void setTariffCalculationResultService(TariffCalculationResultService tariffCalculationResultService) {
        this.tariffCalculationResultService = tariffCalculationResultService;
    }

}
