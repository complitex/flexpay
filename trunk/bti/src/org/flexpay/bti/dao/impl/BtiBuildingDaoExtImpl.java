package org.flexpay.bti.dao.impl;

import org.flexpay.ab.persistence.Town;
import org.flexpay.bti.dao.BtiBuildingDaoExt;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.common.persistence.Stub;
import org.hibernate.HibernateException;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.uniqueResult;

public class BtiBuildingDaoExtImpl extends JpaDaoSupport implements BtiBuildingDaoExt {

    @Override
	public BtiBuilding readBuildingWithAttributes(Long buildingId) {
		return (BtiBuilding) uniqueResult((List<?>) getJpaTemplate()
                .findByNamedQuery("BtiBuilding.findBuildingWithAttributes", buildingId));
	}

    @Override
	public BtiBuilding readBuildingWithAttributesByAddress(Long addressId) {
		return (BtiBuilding) uniqueResult((List<?>) getJpaTemplate()
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
		return (List<BtiBuilding>) getJpaTemplate().findByNamedQuery("BtiBuilding.findByTown", town.getId());
	}

	@SuppressWarnings ({"unchecked"})
	@Override
	public List<BtiBuilding> readBuildingWithAttributes(final Collection<Long> ids) {

		if (ids.isEmpty()) {
			return Collections.emptyList();
		}

		return getJpaTemplate().executeFind(new JpaCallback<List<BtiBuilding>>() {
			@Override
			public List<BtiBuilding> doInJpa(EntityManager entityManager) throws HibernateException {
				return entityManager.createNamedQuery("BtiBuilding.findBuildingWithAttributesCollection")
						.setParameter("ids", ids).getResultList();
			}
		});
	}
}
