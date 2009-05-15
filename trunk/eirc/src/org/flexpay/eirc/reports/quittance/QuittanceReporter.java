package org.flexpay.eirc.reports.quittance;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public interface QuittanceReporter {

	/**
	 * Get a list of Quittances used to divide quittances by bulks
	 *
	 * @param stub	 ServiceOrganization stub
	 * @param dateFrom Period begin date
	 * @param dateTill Period end date
	 * @return List of Quittances
	 * @throws Exception if failure occurs
	 */
	@NotNull
	QuittancePrintInfoData getPrintData(Stub<? extends ServiceOrganization> stub, Date dateFrom, Date dateTill)
			throws Exception;
}
