package org.flexpay.payments.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.orgs.persistence.Organization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface EircRegistryService {

	/**
	 * Find registries
	 *
	 * @param filters ObjectFilters
	 * @param pager   Page
	 * @return list of registries matching specified criteria
	 */
	List<Registry> findObjects(Collection<ObjectFilter> filters, Page<Registry> pager);

	/**
	 * Find registry received from specified sender with a specified number
	 *
	 * @param registryNumber Registry number to search for
	 * @param senderStub	 Sender organization stub
	 * @return Registry reference if found, or <code>null</code> otherwise
	 */
	@Nullable
	Registry getRegistryByNumber(@NotNull Long registryNumber, @NotNull Stub<Organization> senderStub);
}
