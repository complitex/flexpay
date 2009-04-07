package org.flexpay.eirc.dao;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.EircServiceOrganization;

import java.util.Date;

/**
 * Interface provides functionality to prepare quittances
 */
public interface QuittanceDaoExt {

	/**
	 * Generate current snapshot of details and create quittances for the following processing
	 *
	 * @param organizationStub ServiceOrganization stub to generate quittances for
	 * @param townStub		 Town stub to generate quittances in
	 * @param dateFrom		 Period begin date
	 * @param dateTill		 Period end date
	 * @return number of generated quittances
	 */
	long createQuittances(Stub<EircServiceOrganization> organizationStub, Stub<Town> townStub, Date dateFrom, Date dateTill);
}
