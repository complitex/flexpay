package org.flexpay.payments.service.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.RegistryDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.RegistryTypeFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.payments.dao.EircRegistryDaoExt;
import org.flexpay.payments.service.EircRegistryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

@Transactional (readOnly = true)
public class EircRegistryServiceImpl implements EircRegistryService {
    private Logger log = LoggerFactory.getLogger(getClass());

	private EircRegistryDaoExt eircRegistryDaoExt;
    private RegistryDao registryDao;

	/**
	 * Find registries
	 *
	 * @param senderFilter	sender organization filter
	 * @param recipientFilter recipient organization filter
	 * @param typeFilter	  registry type filter
	 * @param fromDate		registry generation start date
	 * @param tillDate		registry generation end date
	 * @param pager		   Page
	 * @return list of registries matching specified criteria
	 */
	public List<Registry> findObjects(OrganizationFilter senderFilter, OrganizationFilter recipientFilter,
									  RegistryTypeFilter typeFilter, Date fromDate, Date tillDate, Page pager) {
		return eircRegistryDaoExt.findRegistries(senderFilter, recipientFilter,
				typeFilter, fromDate, tillDate, pager);
	}

	/**
	 * Find registry recieved from specified sender with a specified number
	 *
	 * @param registryNumber Registry number to search for
	 * @param senderStub	 Sender organization stub
	 * @return Registry reference if found, or <code>null</code> otherwise
	 */
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
