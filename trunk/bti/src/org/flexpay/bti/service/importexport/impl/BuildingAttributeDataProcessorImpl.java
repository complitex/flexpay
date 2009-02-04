package org.flexpay.bti.service.importexport.impl;

import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.bti.persistence.BuildingAttributeType;
import org.flexpay.bti.persistence.BuildingAttributeConfig;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.bti.service.BtiBuildingService;
import org.flexpay.bti.service.importexport.BuildingAttributeData;
import org.flexpay.bti.service.importexport.BuildingAttributeDataProcessor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

@Transactional
public class BuildingAttributeDataProcessorImpl implements BuildingAttributeDataProcessor {

	private Logger log = LoggerFactory.getLogger(getClass());

	private BtiBuildingService buildingService;
	private BuildingAttributeTypeService attributeTypeService;

	/**
	 * Process attribute data for specified time interval
	 *
	 * @param begin Interval begin date
	 * @param end   Interval end date
	 * @param data  Attributes data
	 */
	public void processData(Date begin, Date end, BuildingAttributeData data) {

		BtiBuilding building = buildingService.readWithAttributesByAddress(data.getBuildingAddress());

		for (Map.Entry<String, String> pair : data.getName2Values().entrySet()) {
			BuildingAttributeType type = attributeTypeService.findTypeByName(pair.getKey());
			if (type == null) {
				log.info("No attribute type found by name {}, skipping", pair.getKey());
				continue;
			}
			if (BuildingAttributeConfig.isTemporal(pair.getKey())) {
				building.setTmpAttributeForDates(type, pair.getValue(), begin, end);
			} else {
				building.setNormalAttribute(type, pair.getValue());
			}
		}

		buildingService.updateAttributes(building);
	}

	@Required
	public void setBuildingService(BtiBuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setAttributeTypeService(BuildingAttributeTypeService attributeTypeService) {
		this.attributeTypeService = attributeTypeService;
	}
}
