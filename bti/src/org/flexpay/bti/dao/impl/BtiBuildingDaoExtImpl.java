package org.flexpay.bti.dao.impl;

import org.flexpay.bti.dao.BtiBuildingDaoExt;
import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.bti.persistence.BuildingAttributeBase;
import org.flexpay.ab.persistence.Town;
import org.flexpay.common.persistence.Stub;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class BtiBuildingDaoExtImpl extends HibernateDaoSupport implements BtiBuildingDaoExt {

	public BtiBuilding readBuildinWithAttributes(Long buildingId) {

		BtiBuilding building = (BtiBuilding) DataAccessUtils.uniqueResult(getHibernateTemplate()
				.findByNamedQuery("BtiBuilding.find", buildingId));

		return setupAttributes(building);
	}

	public BtiBuilding readBuildinWithAttributesByAddress(Long addressId) {
		BtiBuilding building = (BtiBuilding) DataAccessUtils.uniqueResult(getHibernateTemplate()
				.findByNamedQuery("BtiBuilding.findByAddress", addressId));
		return setupAttributes(building);
	}

	private BtiBuilding setupAttributes(BtiBuilding building) {

		if (building == null) {
			return null;
		}

		List<?> simpleAttrs = getHibernateTemplate()
				.findByNamedQuery("BuildingAttributeBase.readSimpleAttributes", building.getId());
		addAttributes(building, simpleAttrs);
		List<?> tmpAttrs = getHibernateTemplate()
				.findByNamedQuery("BuildingAttributeBase.readTmpAttributes", building.getId());
		addAttributes(building, tmpAttrs);

		return building;
	}

	private void addAttributes(BtiBuilding building, List<?> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return;
        }

		for (Object obj : attributes) {
			BuildingAttributeBase attribute = (BuildingAttributeBase) obj;
			building.addAttribute(attribute);
		}
	}

	/**
	 * Find all BtiBuilding in the town
	 * @param town town to search
	 * @return BtiBuilding list in town
	 */
	public List<BtiBuilding> findByTown(Stub<Town> town) {
		return  (List<BtiBuilding>) getHibernateTemplate().findByNamedQuery("BtiBuilding.findByTown", town.getId());
	}
}
