package org.flexpay.ab.dao.imp;

import org.flexpay.ab.dao.StreetDaoExt;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.Date;

public class StreetDaoExtImpl extends HibernateDaoSupport implements StreetDaoExt {

	public void invalidateTypeTemporals(final Long streetId, final Date futureInfinity, final Date invalidDate) {

		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createQuery("update StreetTypeTemporal t set t.invalidDate = :invalidDate where t.object.id = :streetId and t.invalidDate=:futureInfinity")
						.setDate("invalidDate", invalidDate)
						.setLong("streetId", streetId)
						.setDate("futureInfinity", futureInfinity)
						.executeUpdate();
			}
		});
	}
}
