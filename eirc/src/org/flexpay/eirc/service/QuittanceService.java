package org.flexpay.eirc.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.jetbrains.annotations.NotNull;

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
	 * @param dateFrom Period begin date
	 * @param dateTill Period end date
	 */
	void generateForServiceOrganisation(Date dateFrom, Date dateTill);

	/**
	 * Get a list of Quittances separated with addresses, used to divide quittances by bulks
	 *
	 * @param stub	 ServiceOrganisation stub
	 * @param dateFrom Period begin date
	 * @param dateTill Period end date
	 * @return List of Quittances
	 * @throws FlexPayException if failure occurs
	 * @deprecated 
	 */
	List<Object> getQuittanceListWithDelimiters(
			@NotNull Stub<ServiceOrganisation> stub, Date dateFrom, Date dateTill)
			throws FlexPayException;

	String getPayer(Quittance quittance);

	String getAddressStr(Quittance quittance, boolean withApartmentNumber) throws FlexPayException;

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
}
