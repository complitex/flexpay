package org.flexpay.common.dao.registry.impl;

import org.flexpay.common.dao.registry.RegistryFileDaoExt;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;

public class RegistryFileDaoExtImpl extends HibernateDaoSupport implements RegistryFileDaoExt {

	/**
	 * Check if registry file is loaded to database
	 *
	 * @param fileId Registry file id
	 * @return <code>true</code> if file already loaded, or <code>false</code> otherwise
	 */
	@Override
	public boolean isLoaded(@NotNull final Long fileId) {
		return (Boolean) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query qCount = session.getNamedQuery("Registry.listRegistries.count");
				qCount.setLong(0, fileId);
				Number count = (Number) qCount.uniqueResult();
				return count.longValue() > 0;
			}
		});
	}

}
