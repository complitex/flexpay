package org.flexpay.ab.dao.impl;

import org.flexpay.ab.dao.BuildingDaoExt;
import org.flexpay.ab.persistence.Building;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BuildingDaoExtImpl extends HibernateDaoSupport implements BuildingDaoExt {

	@Override
	public void deleteBuilding(final Building building) {

		getHibernateTemplate().deleteAll(building.getBuildingses());

		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				Long buildingId = building.getId();
				if (buildingId == null || buildingId <= 0) {
					return null;
				}
/*
				session.getNamedQuery("Building.deleteAddressAttributes")
						.setLong(0, buildingId).executeUpdate();
				session.getNamedQuery("Building.deleteBuildingAddresses")
						.setLong(0, buildingId).executeUpdate();
*/
				session.getNamedQuery("Building.deleteBuilding")
						.setLong(0, buildingId).executeUpdate();
				return null;
			}
		});

	}

}
