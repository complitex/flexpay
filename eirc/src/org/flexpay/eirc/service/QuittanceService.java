package org.flexpay.eirc.service;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

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
	 * @param organizationStub ServiceOrganization stub to generate quittances for
	 * @param townStub		 Town stub to generate quittances in
	 * @param dateFrom		 Period begin date
	 * @param dateTill		 Period end date
	 */
	void generateForServiceOrganization(@NotNull Stub<EircServiceOrganization> organizationStub,
										@NotNull Stub<Town> townStub, Date dateFrom, Date dateTill);

	/**
	 * Read full quittance details
	 *
	 * @param stub Quittance stub
	 * @return Quittance if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.QUITTANCE_READ)
	@Nullable
	Quittance readFull(@NotNull Stub<Quittance> stub);

	/**
	 * Find quittance by generated number
	 *
	 * @param quittanceNumber Generated quittance number
	 * @return Quittance if found, or <code>null</code> otherwise
	 * @throws FlexPayException if quittance has invalid format
	 */
	@Secured (Roles.QUITTANCE_READ)
	@Nullable
	Quittance findByNumber(@NotNull String quittanceNumber) throws FlexPayException;

	/**
	 * Find quittance for account for current open period
	 *
	 * @param stub  account stub to get quittance for
	 * @param pager Page
	 * @return list of quittance in current open period
	 */
	@Secured (Roles.QUITTANCE_READ)
	@NotNull
	List<Quittance> getLatestAccountQuittances(@NotNull Stub<EircAccount> stub, Page<Quittance> pager);
}
