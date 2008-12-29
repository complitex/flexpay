package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.PaymentsCollector;
import org.flexpay.eirc.persistence.PaymentsCollectorDescription;

import java.util.List;

public interface PaymentsCollectorDao extends OrganisationInstanceDao<PaymentsCollectorDescription, PaymentsCollector> {

	/**
	 * Find organizations that are not banks except of that has a bank with specified <code>includedBankId</code>
	 *
	 * @param includedBankId Allowed bank key, that organization will also be in a resulting list
	 * @return List of organizations that are not instances of type T
	 */
	List<Organization> findInstancelessOrganizations(Long includedBankId);
}
