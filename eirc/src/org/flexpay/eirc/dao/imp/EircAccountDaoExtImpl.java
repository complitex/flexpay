package org.flexpay.eirc.dao.imp;

import org.flexpay.eirc.dao.EircAccountDaoExt;
import org.flexpay.eirc.persistence.EircAccount;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class EircAccountDaoExtImpl extends HibernateDaoSupport implements EircAccountDaoExt {

	/**
	 * Find EIRC account by person and apartment identifiers
	 *
	 * @param personId	Person key
	 * @param apartmentId Apartment key
	 * @return EircAccount instance if found, or <code>null</code> otherwise
	 */
	public EircAccount findAccount(Long personId, Long apartmentId) {
		Object[] params = {personId, apartmentId};
		List accounts = getHibernateTemplate().findByNamedQuery("EircAccount.findByPersonAndApartment", params);
		if (accounts.isEmpty()) {
			return null;
		}

		return (EircAccount) accounts.get(0);
	}
}