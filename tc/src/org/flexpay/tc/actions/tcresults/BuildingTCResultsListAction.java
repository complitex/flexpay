package org.flexpay.tc.actions.tcresults;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffService;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
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
    private Map<String, List<TariffCalculationResult>> tcResultsMap = CollectionUtils.treeMap();

    // required services
    private AddressService addressService;
    private TariffCalculationResultService tariffCalculationResultService;
    private TariffService tariffService;
    private BuildingService buildingService;

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

            if (calculationResults.size() > 0) {
                tariffCalculationDates.add(calcDateString);
                tcResultsMap.put(calcDateString, calculationResults);
            }
        }
    }

    @NotNull
    protected String getErrorResult() {
        return REDIRECT_SUCCESS;
    }

    // rendering utility methods
    public String getAddress(@NotNull Long buildingId) throws Exception {

        return addressService.getBuildingsAddress(new Stub<BuildingAddress>(buildingId), getUserPreferences().getLocale());
    }

    public List<BuildingAddress> getAlternateAddresses() throws FlexPayException {

        List<BuildingAddress> alternateAddresses = CollectionUtils.list();

        Long id = Long.parseLong(buildingId);
        BuildingAddress building = buildingService.readFull(new Stub<BuildingAddress>(id));

        for (BuildingAddress address : buildingService.getBuildingBuildings(building.getBuildingStub())) {
            if (!building.equals(address)) {
                alternateAddresses.add(buildingService.readFull(stub(address)));
            }
        }

        return alternateAddresses;
    }

    public boolean hasPrimaryStatus(Long id) {

        BuildingAddress building = buildingService.readFull(new Stub<BuildingAddress>(id));
        return building.getPrimaryStatus();
    }

    public String getTariffTranslation(Long tariffId) {

        Tariff tariff = tariffService.readFull(new Stub<Tariff>(tariffId));
        return getTranslation(tariff.getTranslations()).getName();
    }

    public String getTariffCalculationExportCode(Long tariffCalculationId) {

        TariffCalculationResult tariffCalculationResult = tariffCalculationResultService.read(new Stub<TariffCalculationResult>(tariffCalculationId));
        return tariffCalculationResult == null ? "" : tariffCalculationResult.getTariffExportCode().getI18nName();
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

        List<TariffCalculationResult> tcResults = tcResultsMap.get(calcDate);

        BigDecimal total = BigDecimal.ZERO;
        for (TariffCalculationResult tariff : tcResults) {
            total = total.add(tariff.getValue());
        }

        return total;
    }

    public List<TariffCalculationResult> getTcResults(String calcDate) {
        return tcResultsMap.get(calcDate);
    }

    // get/set form data
    public List<String> getTariffCalculationDates() {
        return tariffCalculationDates;
    }

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

    @Required
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    @Required
    public void setBuildingService(BuildingService buildingService) {
        this.buildingService = buildingService;
    }
}
