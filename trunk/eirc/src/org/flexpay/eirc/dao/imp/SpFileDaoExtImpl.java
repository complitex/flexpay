package org.flexpay.eirc.dao.imp;

import org.flexpay.eirc.dao.SpFileDaoExt;
import org.flexpay.eirc.persistence.SpRegistryType;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class SpFileDaoExtImpl extends HibernateDaoSupport implements SpFileDaoExt {

	public SpRegistryType getRegistryType(int type) {
		getHibernateTemplate().setMaxResults(1);
		List types = getHibernateTemplate().find("from SpRegistryType where typeId=?", type);
		return types.isEmpty() ? null : (SpRegistryType) types.get(0);
	}
}
