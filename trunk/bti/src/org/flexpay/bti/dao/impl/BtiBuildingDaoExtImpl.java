package org.flexpay.bti.dao.impl;

import org.flexpay.ab.persistence.Town;
import org.flexpay.bti.dao.BtiBuildingDaoExt;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.common.persistence.Stub;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.uniqueResult;

public class BtiBuildingDaoExtImpl extends HibernateDaoSupport implements BtiBuildingDaoExt {

    @Override
	public BtiBuilding readBuildingWithAttributes(Long buildingId) {
		return (BtiBuilding) uniqueResult((List<?>) getHibernateTemplate()
                .findByNamedQuery("BtiBuilding.findBuildingWithAttributes", buildingId));
	}

    @Override
	public BtiBuilding readBuildingWithAttributesByAddress(Long addressId) {
		return (BtiBuilding) uniqueResult((List<?>) getHibernateTemplate()
                .findByNamedQuery("BtiBuilding.findBuildingWithAttributesByAddress", addressId));
	}

	/**
	 * Find all BtiBuilding in the town
	 *
	 * @param town town to search
	 * @return BtiBuilding list in town
	 */
	@SuppressWarnings ({"unchecked"})
    @Override
	public List<BtiBuilding> findByTown(Stub<Town> town) {
		return (List<BtiBuilding>) getHibernateTemplate().findByNamedQuery("BtiBuilding.findByTown", town.getId());
	}

	@SuppressWarnings ({"unchecked"})
	@Override
	public List<BtiBuilding> readBuildingWithAttributes(final Collection<Long> ids) {

		if (ids.isEmpty()) {
			return Collections.emptyList();
		}

		return (List<BtiBuilding>) getHibernateTemplate().executeFind(new HibernateCallback<List<?>>() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException {
				return session.getNamedQuery("BtiBuilding.findBuildingWithAttributesCollection")
						.setParameterList("ids", ids).list();
			}
		});
	}
}
