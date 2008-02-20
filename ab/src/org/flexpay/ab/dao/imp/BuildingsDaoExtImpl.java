package org.flexpay.ab.dao.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.BuildingsDaoExt;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Street;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.List;

public class BuildingsDaoExtImpl extends HibernateDaoSupport implements BuildingsDaoExt {

	private static Logger log = Logger.getLogger(BuildingsDaoExtImpl.class);

	/**
	 * Find building by number TODO: alter query to support bulk numbers
	 *
	 * @param street   Building street
	 * @param district Building district
	 * @param number   Building number
	 * @param bulk	 Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	public Buildings findBuildings(final Street street, final District district,
								   final String number, final String bulk) {
		Object obj = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				Query query;
				if (StringUtils.isEmpty(bulk)) {
					query = session.getNamedQuery("Buildings.findByNumber");
				} else {
					query = session.getNamedQuery("Buildings.findByBulkNumber")
							.setString(5, bulk);
				}

				List objects = query.setLong(0, district.getId())
						.setLong(1, street.getId())
						.setInteger(2, BuildingAttributeType.TYPE_NUMBER)
						.setString(3, number)
						.setInteger(4, BuildingAttributeType.TYPE_BULK)
						.setMaxResults(1)
						.list();

				return objects.isEmpty() ? null : objects.get(0);
			}
		});

		if (log.isInfoEnabled()) {
			log.info("Fetched: " + obj);
		}

		return (Buildings) obj;
	}
}
