package org.flexpay.ab.dao.imp;

import org.flexpay.ab.dao.BuildingsDaoExt;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class BuildingsDaoExtImpl extends HibernateDaoSupport implements BuildingsDaoExt {

	/**
	 * Find building by number
	 *
	 * @param streetId   Building street key
	 * @param districtId Building district key
	 * @param number	 Building number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	@SuppressWarnings ({"unchecked"})
	@NotNull
	public List<BuildingAddress> findBuildings(@NotNull Long streetId, @NotNull Long districtId, @NotNull String number) {
		Object[] params = {districtId, streetId,
						   ApplicationConfig.getBuildingAttributeTypeNumber().getId(), number};
		return getHibernateTemplate().findByNamedQuery("BuildingAddress.findByNumberWithDistrict", params);
	}

	/**
	 * Find building by number
	 *
	 * @param streetId Street key
	 * @param number   Building number
	 * @return Buildingses list
	 */
	@SuppressWarnings ({"unchecked"})
	@NotNull
	public List<BuildingAddress> findBuildings(@NotNull Long streetId, @NotNull String number) {
		Object[] params = {streetId,
						   ApplicationConfig.getBuildingAttributeTypeNumber().getId(), number};
		return getHibernateTemplate().findByNamedQuery("BuildingAddress.findByNumber", params);
	}

	/**
	 * Find Building stub by Buildings id
	 *
	 * @param buildingsId Buildings key
	 * @return Building if found, or <code>null</code> otherwise
	 */
	@SuppressWarnings ({"unchecked"})
	@Nullable
	public Building findBuilding(@NotNull Long buildingsId) {
		List<Building> buildings = getHibernateTemplate().findByNamedQuery("BuildingAddress.findBuilding", buildingsId);
		if (buildings.isEmpty()) {
			return null;
		}

		return buildings.get(0);
	}
}
