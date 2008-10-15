package org.flexpay.eirc.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

public interface QuittanceService {

	/**
	 * Create or update a QuittanceDetails record
	 *
	 * @param details QuittanceDetails to save
	 * @throws FlexPayExceptionContainer if validation failure occurs
	 */
	void save(QuittanceDetails details) throws FlexPayExceptionContainer;

	/**
	 * Create quittances for requested period.
	 *
	 * @param stub	 ServiceOrganisation stub to generate quittances for
	 * @param dateFrom Period begin date
	 * @param dateTill Period end date
	 */
	void generateForServiceOrganisation(Stub<ServiceOrganisation> stub, Date dateFrom, Date dateTill);

	String getPayer(Quittance quittance);

	/**
	 * Get a list of Quittances separated with addresses, used to divide quittances by bulks
	 *
	 * @param stub	 ServiceOrganisation stub
	 * @param dateFrom Period begin date
	 * @param dateTill Period end date
	 * @return List of Quittances
	 */
	@NotNull
	List<Quittance> getQuittances(Stub<ServiceOrganisation> stub, Date dateFrom, Date dateTill);

	/**
	 * Read full quittance details
	 *
	 * @param stub Quittance stub
	 * @return Quittance if found, or <code>null</code> otherwise
	 */
	@Nullable
	Quittance readFull(@NotNull Stub<Quittance> stub);
}
