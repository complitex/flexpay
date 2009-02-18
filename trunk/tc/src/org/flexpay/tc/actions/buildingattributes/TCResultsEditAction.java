package org.flexpay.tc.actions.buildingattributes;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.Map;
import java.math.BigDecimal;

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

            Long tariffIdLong = tariffId.longValue();
            Long buildingIdLong = Long.parseLong(buildingId);

            TariffCalculationResult result = tariffCalculationResultService.
                    findTariffCalcResults(calcDate, tariffIdLong, buildingIdLong);

            BigDecimal value = NumberUtils.createBigDecimal(tariffMap.get(tariffId));
            result.setValue(value);
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

    // required services
    @Required
    public void setTariffCalculationResultService(TariffCalculationResultService tariffCalculationResultService) {
        this.tariffCalculationResultService = tariffCalculationResultService;
    }
}
