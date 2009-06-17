package org.flexpay.payments.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.Cashbox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

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
	 * Read SpCashbox object by its unique id
	 *
	 * @param stub Cashbox stub
	 * @return Cashbox object, or <code>null</code> if object not found
	 */
	@Secured (Roles.CASHBOX_READ)
	@Nullable
	Cashbox read(@NotNull Stub<Cashbox> stub);

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
	@Nullable
	List<Cashbox> findObjects(Page<Cashbox> pager);

	/**
	 * Get all cashbox stub for payment point
	 *
	 * @param paymentPointId Payment point id
	 * @return List of cashbox objects
	 */
	@Secured (Roles.CASHBOX_READ)
	@Nullable
	List<Cashbox> findCashboxesForPaymentPoint(Long paymentPointId);

}
