package org.flexpay.tc.action.tcresults;

import org.apache.commons.lang.time.FastDateFormat;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
	@Override
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

			if (!calculationResults.isEmpty()) {
				tariffCalculationDates.add(calcDateString);
				tcResultsMap.put(calcDateString, calculationResults);
			}
		}
	}

	@NotNull
	@Override
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
		BuildingAddress building = buildingService.readFullAddress(new Stub<BuildingAddress>(id));

		for (BuildingAddress address : buildingService.findAddresesByBuilding(building.getBuildingStub())) {
			if (!building.equals(address)) {
				alternateAddresses.add(buildingService.readFullAddress(stub(address)));
			}
		}

		return alternateAddresses;
	}

	public boolean hasPrimaryStatus(Long id) {
		BuildingAddress building = buildingService.readFullAddress(new Stub<BuildingAddress>(id));
		return building.getPrimaryStatus();
	}

	public String getTariffTranslation(Long tariffId) {
		Tariff tariff = tariffService.readFull(new Stub<Tariff>(tariffId));
		return getTranslation(tariff.getTranslations()).getName();
	}

	public String getTariffCalculationExportCode(Long tariffCalculationId) {
		TariffCalculationResult tariffCalculationResult = tariffCalculationResultService.read(new Stub<TariffCalculationResult>(tariffCalculationId));
		return tariffCalculationResult == null ? "" : tariffCalculationResult.getLastTariffExportLogRecord().getTariffExportCode().getI18nName();
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

	public List<String> getTariffCalculationDates() {
		return tariffCalculationDates;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

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
