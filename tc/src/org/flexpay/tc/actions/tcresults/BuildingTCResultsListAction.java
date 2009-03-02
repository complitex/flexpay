package org.flexpay.tc.actions.tcresults;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffService;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.ab.persistence.BuildingAddress;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Collections;
import java.math.BigDecimal;

/**
 * Facilitates ability to list all tariff calculation results for particular building
 */
public class BuildingTCResultsListAction extends FPActionSupport {

    private String buildingId;
    
    // This list contains dates of tariff calculation
    private List<String> tariffCalculationDates = CollectionUtils.list();

    // This map (tariff calculation date -> map (tariff_name -> tariff value)    
    private Map<String, Map<Long, BigDecimal>> tcResultsMap = CollectionUtils.treeMap(); // FIXME eliminate map-o-map ;)

    // required services
    private TariffCalculationResultService tariffCalculationResultService;
    private TariffService tariffService;

    @NotNull
    protected String doExecute() throws Exception {

        loadTariffCalculationResults();
        return INPUT;
    }

    private void loadTariffCalculationResults() {
        List<Date> calculationDates = tariffCalculationResultService.getUniqueDates();

        Stub<BuildingAddress> buildingStub = new Stub<BuildingAddress>(Long.parseLong(buildingId));

        for (Date calculationDate : calculationDates) {

            String calcDateString = FastDateFormat.getInstance("dd.MM.yyyy").format(calculationDate);

            List<TariffCalculationResult> calculationResults = tariffCalculationResultService.
                    getTariffCalcResultsByCalcDateAndAddressId(calculationDate, buildingStub);

            Map<Long, BigDecimal> resultMap = CollectionUtils.treeMap();
            for (TariffCalculationResult result : calculationResults) {

                resultMap.put(result.getTariff().getId(), result.getValue());
            }

            if (resultMap.size() > 0) {
                tariffCalculationDates.add(calcDateString);
                tcResultsMap.put(calcDateString, resultMap);
            }
        }
    }

    @NotNull
    protected String getErrorResult() {
        return REDIRECT_SUCCESS;
    }

    // rendering utility methods
    public String getTariffTranslation(Long tariffId) {
        Tariff tariff = tariffService.readFull(new Stub<Tariff>(tariffId));
        return getTranslation(tariff.getTranslations()).getName();
    }

    public boolean tariffCalculationDatesIsEmpty() {
        return tcResultsMap.isEmpty();
    }

    public String formatDate(Date date) {
        return DateUtil.format(date);
    }

    public String formatDateWithUnderlines(Date date) {
        String string = formatDate(date);
        return string.replace("/", "_");
    }

    public BigDecimal getTotalTariff(String calcDate) {
        Map<Long, BigDecimal> tcResults = tcResultsMap.get(calcDate);

        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal tariff : tcResults.values()) {
            total = total.add(tariff);
        }

        return total;
    }

    public List<String> getTariffCalculationDates() {
        return tariffCalculationDates;
    }

    public Map<Long, BigDecimal> getTcResults(String calcDate) {
        return tcResultsMap.get(calcDate);
    }

    // get/set form data
    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
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
