package org.flexpay.eirc.dao.imp;

import org.flexpay.eirc.dao.OrganisationDaoExt;
import org.flexpay.eirc.persistence.Organisation;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class OrganisationDaoExtImpl extends HibernateDaoSupport implements OrganisationDaoExt {

	/**
	 * Get organisation stub by uniqueId
	 *
	 * @param uniqueId Organisation Eirc number
	 * @return Organisation stub if found, or <code>null</code> if not found
	 */
	public Organisation getOrganisationStub(String uniqueId) {
		List result = getHibernateTemplate().find("select min(id) from Organisation where uniqueId=?", uniqueId);
		if (result.isEmpty()) {
			return null;
		}

		Long id = (Long)result.get(0);

		return id != null ? new Organisation(id) : null;
	}
}
