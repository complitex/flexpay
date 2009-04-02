package org.flexpay.eirc.service.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.ServedBuildingDao;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class ServiceOrganizationServiceImpl extends org.flexpay.orgs.service.imp.ServiceOrganizationServiceImpl
		implements ServiceOrganizationService {

	private ServedBuildingDao servedBuildingDao;

	public List<ServedBuilding> findServedBuildings(@NotNull Collection<Long> ids) {
		return servedBuildingDao.findServedBuildings(ids);
	}

	public List<ServedBuilding> findServedBuildings(@NotNull Stub<? extends ServiceOrganization> stub, Page<ServedBuilding> pager) {
		return servedBuildingDao.findServedBuildingsByServiceOrganization(stub.getId(), pager);
	}

	@Transactional (readOnly = false)
	public void removeServedBuildings(@NotNull Set<Long> objectIds) {
		for (Long id : objectIds) {
			ServedBuilding building = servedBuildingDao.read(id);
			if (building != null) {
				building.setServiceOrganization(null);
				servedBuildingDao.update(building);
			}
		}
	}

	/**
	 * Create or update served building
	 *
	 * @param servedBuilding Served building to save
	 */
	@Transactional (readOnly = false)
	public void saveServedBuilding(@NotNull ServedBuilding servedBuilding) {
		if (servedBuilding.isNew()) {
			servedBuilding.setId(null);
			servedBuildingDao.create(servedBuilding);
		} else {
			servedBuildingDao.update(servedBuilding);
		}
	}

	@Required
	public void setServedBuildingDao(ServedBuildingDao servedBuildingDao) {
		this.servedBuildingDao = servedBuildingDao;
	}
}
