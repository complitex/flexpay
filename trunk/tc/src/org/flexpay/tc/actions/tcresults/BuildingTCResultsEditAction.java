package org.flexpay.tc.actions.tcresults;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.ValidationUtil;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffService;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.Map;
import java.util.List;
import java.math.BigDecimal;

public class BuildingTCResultsEditAction extends FPActionSupport {

    // form data
    private String buildingId;
    private String calculationDate;
    private Map<Long, String> tariffMap = CollectionUtils.map();

    // required services
    private TariffService tariffService;
    private TariffCalculationResultService tariffCalculationResultService;

    @NotNull
    protected String doExecute() throws Exception {

        if (isSubmit()) {
            Date calcDate = DateUtil.parseBeginDate(calculationDate);

            for (Long tariffId : tariffMap.keySet()) {
                String value = tariffMap.get(tariffId);
                if (value.contains(",")) {
                    value = value.replace(",", ".");
                }

                Stub<Tariff> tariffStub = new Stub<Tariff>(tariffId.longValue());
                Stub<Building> buildingStub = new Stub<Building>(Long.parseLong(buildingId));
                TariffCalculationResult result = tariffCalculationResultService.findTariffCalcResults(calcDate, tariffStub, buildingStub);

                result.setValue(StringUtils.isEmpty(value) ? BigDecimal.ZERO : new BigDecimal(value));

                tariffCalculationResultService.update(result);
            }

            return REDIRECT_SUCCESS;
        } else {
            loadTCResults();
            return INPUT;
        }
    }

    private void loadTCResults() {
        Date calcDate = DateUtil.parseBeginDate(calculationDate);

        Stub<BuildingAddress> buildingAddressStub = new Stub<BuildingAddress>(Long.parseLong(buildingId));
        List<TariffCalculationResult> calculationResults = tariffCalculationResultService.getTariffCalcResultsByCalcDateAndAddressId(calcDate, buildingAddressStub);

        for (TariffCalculationResult result : calculationResults) {
            tariffMap.put(result.getTariff().getId(), result.getValue().toString());
        }
    }

    @NotNull
    protected String getErrorResult() {
        return REDIRECT_SUCCESS;
    }

    @Override
    public void validate() {
        for (Long tariffId : tariffMap.keySet()) {
            String valueString = tariffMap.get(tariffId);

            if (StringUtils.isEmpty(valueString)) {
                continue;
            }

            if (!ValidationUtil.checkTariffCalculationResultValue(valueString)) {
                addActionError(getText("tc.error.tc.results.form.contains.errors"));
                return;
            }
        }
    }

    // rendering utility methods
    public String getTariffTranslation(Long tariffId) {
        Tariff tariff = tariffService.readFull(new Stub<Tariff>(tariffId));
        return getTranslation(tariff.getTranslations()).getName();
    }

    public String formatDate(Date date) {
        return DateUtil.format(date);
    }

    public BigDecimal getTotalTariff() {

        BigDecimal total = BigDecimal.ZERO;
        for (String tariffValue : tariffMap.values()) {
            total = total.add(new BigDecimal(tariffValue));
        }

        return total;
    }

    // get/set form data

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(String calculationDate) {
        this.calculationDate = calculationDate;
    }

    public void setTariffMap(Map<Long, String> tariffMap) {
        this.tariffMap = tariffMap;
    }

    public Map<Long, String> getTariffMap() {
        return tariffMap;
    }

    // required services
    @Required
    public void setTariffCalculationResultService(TariffCalculationResultService tariffCalculationResultService) {
        this.tariffCalculationResultService = tariffCalculationResultService;
    }

    @Required
    public void setTariffService(TariffService tariffService) {
        this.tariffService = tariffService;
    }
}