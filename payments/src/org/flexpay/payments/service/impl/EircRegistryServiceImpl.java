package org.flexpay.payments.service.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.RegistryDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.sorter.RegistrySorter;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.dao.EircRegistryDaoExt;
import org.flexpay.payments.service.EircRegistryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional (readOnly = true)
public class EircRegistryServiceImpl implements EircRegistryService {

	private EircRegistryDaoExt eircRegistryDaoExt;
	private RegistryDao registryDao;

    @Override
	public List<Registry> findObjects(RegistrySorter registrySorter, Collection<ObjectFilter> filters, Page<Registry> pager) {
		return eircRegistryDaoExt.findRegistries(registrySorter, filters, pager);
	}

	/**
	 * Find registry received from specified sender with a specified number
	 *
	 * @param registryNumber Registry number to search for
	 * @param senderStub	 Sender organization stub
	 * @return Registry reference if found, or <code>null</code> otherwise
	 */
    @Override
	public Registry getRegistryByNumber(@NotNull Long registryNumber, @NotNull Stub<Organization> senderStub) {

		List<Registry> registries = registryDao.findRegistriesByNumber(registryNumber, senderStub.getId());
		if (registries.isEmpty()) {
			return null;
		}

		return registries.get(0);
	}

	@Required
	public void setRegistryDaoExt(EircRegistryDaoExt eircRegistryDaoExt) {
		this.eircRegistryDaoExt = eircRegistryDaoExt;
	}

	@Required
	public void setRegistryDao(RegistryDao registryDao) {
		this.registryDao = registryDao;
	}
}
