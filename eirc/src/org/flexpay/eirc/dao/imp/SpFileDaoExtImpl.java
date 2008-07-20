package org.flexpay.eirc.dao.imp;

import org.flexpay.eirc.dao.SpFileDaoExt;
import org.flexpay.eirc.persistence.SpRegistryType;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.jetbrains.annotations.NonNls;

import java.util.List;

public class SpFileDaoExtImpl extends HibernateDaoSupport implements SpFileDaoExt {

	public SpRegistryType getRegistryType(int code) {
		getHibernateTemplate().setMaxResults(1);
		@NonNls String hql = "from SpRegistryType where code=?";
		List types = getHibernateTemplate().find(hql, code);
		return types.isEmpty() ? null : (SpRegistryType) types.get(0);
	}

	/**
	 * Clear current session
	 */
	public void clearSession() {
		getHibernateTemplate().clear();
	}
}
