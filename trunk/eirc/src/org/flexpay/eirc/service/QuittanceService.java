package org.flexpay.eirc.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.QuittanceDaoExt;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.payments.persistence.ServiceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

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
	 * @param options Quittances generation options
	 */
	void generateForServiceOrganization(QuittanceDaoExt.CreateQuittancesOptions options);

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
     * Find quittance by generated number and service type
     *
     * @param quittanceNumber Generated quittance number
     * @param serviceTypeStub service type stub
     * @return Quittance if found, or <code>null</code> otherwise
     * @throws org.flexpay.common.exception.FlexPayException
     *          if <code>quittanceNumber</code> has invalid format
     */
    @Secured (Roles.QUITTANCE_READ)
    @Nullable
    Quittance findByNumberAndServiceType(@NotNull String quittanceNumber, @NotNull Stub<ServiceType> serviceTypeStub) throws FlexPayException;

	/**
	 * Find quittance for account for current open period
	 *
	 * @param account  account to get quittance for
	 * @return list of quittance in current open period
	 */
	@Secured (Roles.QUITTANCE_READ)
	@NotNull
	List<Quittance> getLatestAccountQuittances(@NotNull EircAccount account);

    /**
     * Find quittance for account for current open period by service type
     *
     * @param account  account to get quittance for
     * @param serviceTypeStub service type stub
     *
     * @return list of quittance in current open period
     */
    @Secured (Roles.QUITTANCE_READ)
    @NotNull
    List<Quittance> getLatestAccountQuittances(@NotNull EircAccount account, @NotNull Stub<ServiceType> serviceTypeStub);

    /**
     * Find quittance for accounts for current open period
     *
     * @param accounts  accounts to get quittance for
     * @return list of quittance in current open period
     */
    @Secured (Roles.QUITTANCE_READ)
    @NotNull
    List<Quittance> getLatestAccountsQuittances(@NotNull List<EircAccount> accounts);

    /**
     * Find quittance for accounts for current open period by service type
     *
     * @param accounts  accounts to get quittance for
     * @param serviceTypeStub service type stub
     *
     * @return list of quittance in current open period
     */
    @Secured (Roles.QUITTANCE_READ)
    @NotNull
    List<Quittance> getLatestAccountsQuittances(@NotNull List<EircAccount> accounts, @NotNull Stub<ServiceType> serviceTypeStub);

    /**
     * Find quittance by list of consumers
     *
     * @param consumers consumers list
     * @return list of quittance in current open period
     */
    @Secured (Roles.QUITTANCE_READ)
    @NotNull
    List<Quittance> getQuittances(@NotNull List<Consumer> consumers);

    /**
     * Find quittance by list of consumers
     *
     * @param consumers consumers list
     * @return list of quittance in current open period
     */
    @Secured (Roles.QUITTANCE_READ)
    @NotNull
    List<Quittance> getQuittancesByApartments(@NotNull List<Consumer> consumers);

    /**
     * Find list of quittance details by quittance id
     *
     * @param stub quittance stub
     * @return list of quittance detailes for quittance
     */
    @Secured (Roles.QUITTANCE_READ)
    @NotNull
    List<QuittanceDetails> getQuittanceDetailsByQuittanceId(@NotNull Stub<Quittance> stub);

}
