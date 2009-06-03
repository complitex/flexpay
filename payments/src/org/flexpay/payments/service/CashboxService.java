package org.flexpay.payments.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
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
	 * @throws FlexPayException if failure occurs
	 */
	@Secured(Roles.CASHBOX_ADD)
	void create(Cashbox cashbox) throws FlexPayException;

	/**
	 * Update cashbox
	 *
	 * @param cashbox Cashbox to update for
	 * @throws FlexPayException if Cashbox object is invalid
	 */
	@Secured(Roles.CASHBOX_CHANGE)
	void update(Cashbox cashbox) throws FlexPayException;

	/**
	 * Read SpCashbox object by its unique id
	 *
	 * @param stub Cashbox stub
	 * @return Cashbox object, or <code>null</code> if object not found
	 */
	@Secured(Roles.CASHBOX_READ)
	@Nullable
	Cashbox read(@NotNull Stub<Cashbox> stub);

	/**
	 * Delete cashbox
	 *
	 * @param cashbox Cashbox-object to delete
	 */
	@Secured(Roles.CASHBOX_DELETE)
	void delete(Cashbox cashbox);

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
	 * @param pager	Page object
	 * @return List of cashbox objects for pager
	 */
	@Secured(Roles.CASHBOX_READ)
	@Nullable
	List<Cashbox> findObjects(Page<Cashbox> pager);

}
