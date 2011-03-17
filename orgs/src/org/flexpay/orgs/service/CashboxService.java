package org.flexpay.orgs.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.filters.CashboxFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.apache.commons.collections.ArrayStack;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CashboxService {

	/**
	 * Create cashbox
	 *
	 * @param cashbox Cashbox
	 * @return persisted object back
	 * @throws FlexPayExceptionContainer if failure occurs
	 */
	@Secured (Roles.CASHBOX_ADD)
	@NotNull
	Cashbox create(@NotNull Cashbox cashbox) throws FlexPayExceptionContainer;

	/**
	 * Update cashbox
	 *
	 * @param cashbox Cashbox to update for
	 * @return updated object back
	 * @throws FlexPayExceptionContainer if Cashbox object is invalid
	 */
	@Secured (Roles.CASHBOX_CHANGE)
	@NotNull
	Cashbox update(@NotNull Cashbox cashbox) throws FlexPayExceptionContainer;

	/**
	 * Read Cashbox object by its unique id
	 *
	 * @param stub Cashbox stub
	 * @return Cashbox object, or <code>null</code> if object not found
	 */
	@Secured (Roles.CASHBOX_READ)
	@Nullable
	Cashbox read(@NotNull Stub<Cashbox> stub);

	/**
	 * Read Cashbox objects by its unique ids
	 *
	 * @param ids Cachbox ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Cashbox objects found
	 */
	@Secured (Roles.CASHBOX_READ)
	@NotNull
	List<Cashbox> readFull(Collection<Long> ids, boolean preserveOrder);

	/**
	 * Delete cashbox
	 *
	 * @param cashbox Cashbox-object to delete
	 */
	void delete(@NotNull Cashbox cashbox);

	/**
	 * Disable cashboxes
	 *
	 * @param objectIds Identifiers of cashbox to disable
	 */
	@Secured (Roles.CASHBOX_DELETE)
	void disable(Set<Long> objectIds);


	/**
	 * Get all cashbox stub in page mode
	 *
	 * @param pager Page object
	 * @return List of cashbox objects for pager
	 */
	@Secured (Roles.CASHBOX_READ)
	@NotNull
	List<Cashbox> findObjects(Page<Cashbox> pager);

	/**
	 * Get all cashbox stub for payment point
	 *
	 * @param paymentPointId Payment point id
	 * @return List of cashbox objects
	 */
	@Secured (Roles.CASHBOX_READ)
	@NotNull
	List<Cashbox> findCashboxesForPaymentPoint(Long paymentPointId);

	/**
	 * Get all cashbox stub for payment collector
	 *
	 * @param paymentCollectorStub Payment collector stub
	 * @return List of cashbox objects
	 */
	@Secured (Roles.CASHBOX_READ)
	@NotNull
	List<Cashbox> findCashboxesForPaymentCollector(Stub<PaymentCollector> paymentCollectorStub, Page<Cashbox> pager);

	/**
	 * Initialize cashbox filter
	 *
	 * @param filter filter to initialize
	 * @return filter back
	 */
	@Secured(Roles.CASHBOX_READ)
	@NotNull
	CashboxFilter initFilter(@NotNull CashboxFilter filter);

	/**
	 * Initialize cashbox filter
	 *
	 * @param filters filters stack
	 * @param filter filter to bi initialized
	 * @return filter back
	 */
	@Secured(Roles.CASHBOX_READ)
	@NotNull
	CashboxFilter initFilter(@NotNull ArrayStack filters, @NotNull CashboxFilter filter);

	/**
	 * List available cashboxes
	 *
	 * @param filters Filters stack
	 * @param pager   Pager
	 * @return List of available cashboxes
	 */
	@Secured (Roles.CASHBOX_READ)
	@NotNull
	List<Cashbox> listCashboxes(@NotNull ArrayStack filters, @NotNull Page<Cashbox> pager);

}
