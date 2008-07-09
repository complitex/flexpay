package org.flexpay.eirc.service;

import java.util.Date;
import java.util.List;

import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;

public interface QuittanceService {

	/**
	 * Create or update a QuittanceDetails record
	 *
	 * @param details QuittanceDetails to save
	 * @throws FlexPayExceptionContainer if validation failure occurs
	 */
	void save(QuittanceDetails details) throws FlexPayExceptionContainer;
	
	void generateForServiceOrganisation(Long serviceOrganisationId,
			Date dateFrom, Date dateTill);
	
	List<Object> getQuittanceListWithDelimiters(
			Long serviceOrganisationId, Date dateFrom, Date dateTill)
			throws FlexPayException;
	
	String getPayer(Quittance quittance);
	
	String getAddressStr(Quittance quittance, boolean withApartmentNumber) throws FlexPayException;
	
	QuittanceDetails calculateTotalQuittanceDetails(Quittance quittance, ServiceType serviceType);
}
