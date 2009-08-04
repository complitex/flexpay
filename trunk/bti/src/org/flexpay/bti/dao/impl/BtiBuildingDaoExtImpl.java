package org.flexpay.bti.dao.impl;

import org.flexpay.ab.persistence.Town;
import org.flexpay.bti.dao.BtiBuildingDaoExt;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.common.persistence.Stub;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class BtiBuildingDaoExtImpl extends HibernateDaoSupport implements BtiBuildingDaoExt {

	public BtiBuilding readBuildingWithAttributes(Long buildingId) {

		return (BtiBuilding) DataAccessUtils.uniqueResult(getHibernateTemplate()
				.findByNamedQuery("BtiBuilding.findBuildingWithAttributes", buildingId));
	}

	public BtiBuilding readBuildingWithAttributesByAddress(Long addressId) {
		return (BtiBuilding) DataAccessUtils.uniqueResult(getHibernateTemplate()
				.findByNamedQuery("BtiBuilding.findBuildingWithAttributesByAddress", addressId));
	}

	/**
	 * Find all BtiBuilding in the town
	 *
	 * @param town town to search
	 * @return BtiBuilding list in town
	 */
	@SuppressWarnings ({"unchecked"})
	public List<BtiBuilding> findByTown(Stub<Town> town) {
		return (List<BtiBuilding>) getHibernateTemplate().findByNamedQuery("BtiBuilding.findByTown", town.getId());
	}

}
