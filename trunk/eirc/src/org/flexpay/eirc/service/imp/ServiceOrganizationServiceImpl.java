package org.flexpay.eirc.service.imp;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.ServedBuildingDao;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class ServiceOrganizationServiceImpl extends org.flexpay.orgs.service.imp.ServiceOrganizationServiceImpl
		implements ServiceOrganizationService {

	private BuildingService buildingService;
	private ServedBuildingDao servedBuildingDao;

	public List<ServedBuilding> findServedBuildings(@NotNull Stub<? extends ServiceOrganization> stub, Page<ServedBuilding> pager) {
		return servedBuildingDao.findServedBuildingsByServiceOrganization(stub.getId(), pager);
	}

	@Transactional (readOnly = false)
	public void removeServedBuildings(@NotNull Set<Long> objectIds) throws FlexPayExceptionContainer {
		List<Building> buildings = buildingService.readFull(objectIds, false);
		for (Building bs : buildings) {
			ServedBuilding building = (ServedBuilding) bs;
			if (building != null) {
				building.setServiceOrganization(null);
				buildingService.update(building);
			}
		}
	}

	/**
	 * Create or update served building
	 *
	 * @param servedBuilding Served building to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	public void updateServedBuilding(@NotNull ServedBuilding servedBuilding) throws FlexPayExceptionContainer {
		buildingService.update(servedBuilding);
	}

	@Required
	public void setServedBuildingDao(ServedBuildingDao servedBuildingDao) {
		this.servedBuildingDao = servedBuildingDao;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}
}
