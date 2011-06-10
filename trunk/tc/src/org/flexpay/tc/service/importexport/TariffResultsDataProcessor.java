package org.flexpay.tc.service.importexport;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.bti.service.importexport.BuildingAttributeData;
import org.flexpay.bti.service.importexport.BuildingAttributeDataProcessor;
import org.flexpay.common.util.DateUtil;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Transactional
public class TariffResultsDataProcessor implements BuildingAttributeDataProcessor {

	private Logger log = LoggerFactory.getLogger(getClass());

	private TariffToAttributeNameMapper tariffToAttributeNameMapper;
	private TariffService tariffService;
	private BuildingService buildingService;
	private TariffCalculationResultService calculationResultService;

	/**
	 * ProcessInstance attribute data for specified time interval
	 *
	 * @param begin Interval begin date
	 * @param end   Interval end date
	 * @param data  Attributes data
	 */
	@Override
	public void processData(Date begin, Date end, BuildingAttributeData data) {

		for (Map.Entry<String, String> entry : data.getName2Values().entrySet()) {
			String attributeName = entry.getKey();

			// get tariff code
			String tariffCode = tariffToAttributeNameMapper.getTariffCodeByAttributeName(attributeName);
			if (StringUtils.isBlank(tariffCode)) {
				log.debug("No tariff code for attribute: {}", attributeName);
				continue;
			}

			// extract value
			String attributeValue = entry.getValue();
			if (StringUtils.isBlank(attributeValue)) {
				log.debug("No value for attribute: {}", attributeName);
				continue;
			}
			BigDecimal amount = getValue(attributeValue);
			if (amount == null) {
				log.debug("Expected decimal, found '{}' for attribute {}", attributeValue, attributeName);
				continue;
			}

			// get tariff and building
			Tariff tariff = tariffService.getTariffByCode(tariffCode);
			if (tariff == null) {
				log.info("No tariff found by code: {}", tariffCode);
				continue;
			}

			Building building = buildingService.findBuilding(data.getBuildingAddress());
			if (building == null) {
				log.warn("No building found by address: {}", data.getBuildingAddress());
				continue;
			}

			calculationResultService.add(amount, DateUtil.now(), begin, building, tariff);
		}
	}

	@Nullable
	private BigDecimal getValue(@NotNull String value) {
		try {
			return new BigDecimal(value.replaceAll(",", "."));
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	@Required
	public void setCalculationResultService(TariffCalculationResultService calculationResultService) {
		this.calculationResultService = calculationResultService;
	}

	@Required
	public void setTariffToAttributeNameMapper(TariffToAttributeNameMapper tariffToAttributeNameMapper) {
		this.tariffToAttributeNameMapper = tariffToAttributeNameMapper;
	}

	@Required
	public void setTariffService(TariffService tariffService) {
		this.tariffService = tariffService;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
