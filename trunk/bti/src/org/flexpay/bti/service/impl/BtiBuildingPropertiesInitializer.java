package org.flexpay.bti.service.impl;

import org.flexpay.ab.persistence.Building;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.bti.service.BtiBuildingService;
import org.flexpay.common.service.PropertiesInitializer;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;

public class BtiBuildingPropertiesInitializer implements PropertiesInitializer<Building> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private BtiBuildingService btiBuildingService;
	private SessionUtils sessionUtils;

	/**
	 * initialize properties of a single object
	 *
	 * @param obj Object that's properties to initialize
	 */
	@Override
	public void init(@NotNull Building obj) {
		BtiBuilding btiBuilding = btiBuildingService.readWithAttributes(stub(obj));
		if (btiBuilding == null) {
			log.warn("Expected bti building, but not found: #{}", obj.getId());
			return;
		}
		sessionUtils.evict(btiBuilding);
		BtiBuilding building = (BtiBuilding) obj;
		building.setAttributes(btiBuilding.getAttributes());
	}

	/**
	 * initialize properties of a group of objects
	 *
	 * @param objs Objects that's properties to initialize
	 */
	@Override
	public void init(@NotNull Collection<Building> objs) {
		Map<Long, BtiBuilding> map = CollectionUtils.map();
		for (Building building : objs) {
			map.put(building.getId(), (BtiBuilding) building);
		}

		List<BtiBuilding> btiBuildings = btiBuildingService.readWithAttributes(map.keySet());
		for (BtiBuilding building : btiBuildings) {
			sessionUtils.evict(building);
			map.get(building.getId()).setAttributes(building.getAttributes());
		}
	}

	@Required
	public void setBtiBuildingService(BtiBuildingService btiBuildingService) {
		this.btiBuildingService = btiBuildingService;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}
}
