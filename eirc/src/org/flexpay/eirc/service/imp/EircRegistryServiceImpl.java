package org.flexpay.eirc.service.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.RegistryTypeFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.imp.RegistryServiceImpl;
import org.flexpay.eirc.dao.EircRegistryDaoExt;
import org.flexpay.eirc.service.EircRegistryService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

@Transactional (readOnly = true)
public class EircRegistryServiceImpl extends RegistryServiceImpl implements EircRegistryService {

	private EircRegistryDaoExt eircRegistryDaoExt;

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

}
