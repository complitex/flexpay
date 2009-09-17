package org.flexpay.orgs.dao;

import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentCollectorDescription;

import java.util.List;

public interface PaymentCollectorDao extends OrganisationInstanceDao<PaymentCollectorDescription, PaymentCollector> {

	/**
	 * Find organizations that are not banks except of that has a bank with specified <code>includedBankId</code>
	 *
	 * @param includedBankId Allowed bank key, that organization will also be in a resulting list
	 * @return List of organizations that are not instances of type T
	 */
	List<Organization> findInstancelessOrganizations(Long includedBankId);

}
