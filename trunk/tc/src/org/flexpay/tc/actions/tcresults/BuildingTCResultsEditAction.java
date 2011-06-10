package org.flexpay.tc.actions.tcresults;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.ValidationUtil;
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

public class BuildingTCResultsEditAction extends FPActionSupport {

	private String buildingId;
	private String calculationDate;

	private Map<Long, String> tariffMap = CollectionUtils.map();
	private Map<Long, String> tariffMapDBValues = CollectionUtils.map();

	private AddressService addressService;
	private BuildingService buildingService;
	private TariffService tariffService;
	private TariffCalculationResultService tariffCalculationResultService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		loadTCResults();

		if (isSubmit()) {
			if (!doValidate()) {
				return INPUT;
			}
			updateTCResults();
			return REDIRECT_SUCCESS;
		}

		loadTCResultsValues();
		return INPUT;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	private void loadTCResults() {

		Date calcDate = DateUtil.parseBeginDate(calculationDate);

		Stub<BuildingAddress> buildingAddressStub = new Stub<BuildingAddress>(Long.parseLong(buildingId));
		List<TariffCalculationResult> calculationResults = tariffCalculationResultService.getTariffCalcResultsByCalcDateAndAddressId(calcDate, buildingAddressStub);

		for (TariffCalculationResult result : calculationResults) {
			tariffMapDBValues.put(result.getTariff().getId(), result.getValue().toString());
		}
	}

	private void loadTCResultsValues() {

		for (Long typeId : tariffMapDBValues.keySet()) {
			tariffMap.put(typeId, tariffMapDBValues.get(typeId));
		}

	}

	private void updateTCResults() throws FlexPayException {

		Date calcDate = DateUtil.parseBeginDate(calculationDate);

		for (Long tariffId : tariffMap.keySet()) {
			String value = tariffMap.get(tariffId);
			if (value.contains(",")) {
				value = value.replace(",", ".");
			}

			Stub<Tariff> tariffStub = new Stub<Tariff>(tariffId);
			Stub<Building> buildingStub = new Stub<Building>(Long.parseLong(buildingId));
			TariffCalculationResult result = tariffCalculationResultService.findTariffCalcResults(calcDate, tariffStub, buildingStub);

			BigDecimal oldValue = result.getValue();
			BigDecimal newValue = StringUtils.isEmpty(value) ? BigDecimal.ZERO : new BigDecimal(value);
			result.setValue(newValue);

			if (!newValue.equals(oldValue)) {
				result.setLastTariffExportLogRecord(null);
			}

			tariffCalculationResultService.update(result);
		}
	}

	public boolean doValidate() {

		for (Long tariffId : tariffMap.keySet()) {
			String valueString = tariffMap.get(tariffId);

			if (StringUtils.isEmpty(valueString)) {
				continue;
			}

			if (!ValidationUtil.checkTariffCalculationResultValue(valueString)) {
				addActionError(getText("tc.error.tc.results.form.contains.errors"));
				return false;
			}
		}

		return true;
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

	public String formatDate(Date date) {

		return DateUtil.format(date);
	}

	public BigDecimal getTotalTariff() {

		BigDecimal total = BigDecimal.ZERO;
		for (String tariffValue : tariffMapDBValues.values()) {
			total = total.add(new BigDecimal(tariffValue));
		}

		return total;
	}

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
