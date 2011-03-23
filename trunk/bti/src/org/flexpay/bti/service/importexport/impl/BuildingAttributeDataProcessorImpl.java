package org.flexpay.bti.service.importexport.impl;

import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.bti.persistence.building.BuildingAttribute;
import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.bti.service.BtiBuildingService;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.bti.service.importexport.BuildingAttributeData;
import org.flexpay.bti.service.importexport.BuildingAttributeDataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.Map;

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
		if (building == null) {
			throw new RuntimeException("Building not found, id=" + data.getBuildingAddress()
									   + ", row=" + data.getRowNum());
		}

		for (Map.Entry<String, String> pair : data.getName2Values().entrySet()) {
			BuildingAttributeType type = attributeTypeService.findTypeByName(pair.getKey());
			if (type == null) {
				log.info("No attribute type found by name {}, skipping", pair.getKey());
				continue;
			}
			BuildingAttribute attribute = new BuildingAttribute();
			attribute.setAttributeType(type);
			attribute.setStringValue(pair.getValue());
			if (type.isTemp()) {
				building.setTmpAttributeForDates(attribute, begin, end);
			} else {
				building.setNormalAttribute(attribute);
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
