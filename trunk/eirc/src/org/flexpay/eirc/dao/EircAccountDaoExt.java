package org.flexpay.eirc.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.sorter.EircAccountSorter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface EircAccountDaoExt {

	/**
	 * Find EIRC account by person and apartment identifiers
	 *
	 * @param personId	Person key
	 * @param apartmentId Apartment key
	 * @return EircAccount instance if found, or <code>null</code> otherwise
	 */
	EircAccount findAccount(@NotNull Long personId, @NotNull Long apartmentId);

    @NotNull
    List<EircAccount> findAccounts(@NotNull Collection<? extends EircAccountSorter> sorters,  @NotNull Collection<ObjectFilter> filters, @NotNull Integer output, Page<EircAccount> pager);

}