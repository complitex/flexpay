package org.flexpay.eirc.dao.imp;

import org.flexpay.eirc.dao.RegistryFileDaoExt;
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
	public boolean isLoaded(@NotNull final Long fileId) {
		return (Boolean) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query qCount = session.getNamedQuery("SpRegistry.listRegistries.count");
				qCount.setLong(0, fileId);
				Number count = (Number) qCount.uniqueResult();
				return count.longValue() > 0;
			}
		});
	}

}
