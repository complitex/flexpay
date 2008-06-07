package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.EircAccount;

public interface EircAccountDaoExt {

	/**
	 * Find EIRC account by person and apartment identifiers
	 *
	 * @param personId Person key
	 * @param apartmentId Apartment key
	 * @return EircAccount instance if found, or <code>null</code> otherwise
	 */
	EircAccount findAccount(Long personId, Long apartmentId);
}