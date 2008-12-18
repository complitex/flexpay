package org.flexpay.eirc.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.RegistryDao;
import org.flexpay.eirc.dao.RegistryDaoExt;
import org.flexpay.eirc.dao.OrganizationDao;
import org.flexpay.eirc.dao.RegistryContainerDao;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.RegistryContainer;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.flexpay.eirc.persistence.filters.RegistryTypeFilter;
import org.flexpay.eirc.service.RegistryRecordService;
import org.flexpay.eirc.service.RegistryService;
import org.springframework.transaction.annotation.Transactional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public class RegistryServiceImpl implements RegistryService {
	private Logger log = Logger.getLogger(getClass());

	private RegistryDao registryDao;
	private RegistryDaoExt registryDaoExt;
	private RegistryContainerDao registryContainerDao;
	private OrganizationDao organizationDao;

	private RegistryRecordService registryRecordService;

	/**
	 * Create SpRegistry
	 *
	 * @param registry SpRegistry object
	 * @return created SpRegistry object
	 */
	@Transactional(readOnly = false)
	public SpRegistry create(SpRegistry registry) throws FlexPayException {
		registry.setRecipient(organizationDao.read(registry.getRecipient().getId()));
		registry.setSender(organizationDao.read(registry.getSender().getId()));
		registryDao.create(registry);

		for (RegistryContainer container : registry.getContainers()) {
			registryContainerDao.create(container);
		}

		if (log.isDebugEnabled()) {
			log.debug("Created SpRegistry: " + registry);
		}

		return registry;
	}

	/**
	 * Get all SpRegistry by spFile id in page mode
	 *
	 * @param pager Page object
	 * @return List of SpRegistry objects for pager
	 */
	@Transactional(readOnly = false)
	public List<SpRegistry> findObjects(Page<SpRegistry> pager, Long spFileId) {
		return registryDao.findObjects(pager, spFileId);
	}

	/**
	 * Read SpRegistry object by its unique id
	 *
	 * @param stub Registry stub
	 * @return SpRegistry object, or <code>null</code> if object not found
	 */
	@Nullable 
	public SpRegistry read(@NotNull Stub<SpRegistry> stub) {
		SpRegistry registry = registryDao.readFull(stub.getId());
		if (registry == null) {
			if (log.isDebugEnabled()) {
				log.debug("Registry #" + stub + " not found");
			}
			return null;
		}
		registry.setErrorsNumber(registryRecordService.getErrorsNumber(registry));

		return registry;
	}

	/**
	 * Read Registry with containers included
	 *
	 * @param stub Registry stub
	 * @return Registry if found, or <code>null</code> otherwise
	 */
	public SpRegistry readWithContainers(@NotNull Stub<SpRegistry> stub) {
		List<SpRegistry> registries = registryDao.listRegistryWithContainers(stub.getId());
		if (registries.isEmpty()) {
			return null;
		}

		return registries.get(0);
	}

	/**
	 * Update SpRegistry
	 *
	 * @param spRegistry SpRegistry to update for
	 * @return Updated SpRegistry object
	 * @throws FlexPayException if SpRegistry object is invalid
	 */
	@Transactional(readOnly = false)
	public SpRegistry update(SpRegistry spRegistry) throws FlexPayException {
		registryDao.update(spRegistry);

		return spRegistry;
	}

	@Transactional(readOnly = false)
	public void delete(SpRegistry spRegistry) {
		registryDao.delete(spRegistry);
	}

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
	public List<SpRegistry> findObjects(OrganizationFilter senderFilter, OrganizationFilter recipientFilter,
										RegistryTypeFilter typeFilter, Date fromDate, Date tillDate, Page pager) {
		return registryDaoExt.findRegistries(senderFilter, recipientFilter,
				typeFilter, fromDate, tillDate, pager);
	}

	/**
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	public Collection<SpRegistry> findObjects(@NotNull Set<Long> objectIds) {
		return registryDaoExt.findRegistries(objectIds);
	}

	/**
	 * Find registry recieved from specified sender with a specified number
	 *
	 * @param registryNumber Registry number to search for
	 * @param senderStub	 Sender organization stub
	 * @return Registry reference if found, or <code>null</code> otherwise
	 */
	public SpRegistry getRegistryByNumber(@NotNull Long registryNumber, @NotNull Stub<Organization> senderStub) {

		List<SpRegistry> registries = registryDao.findRegistriesByNumber(registryNumber, senderStub.getId());
		if (registries.isEmpty()) {
			return null;
		}

		return registries.get(0);
	}

	/**
	 * @param registryDao the spRegistryDao to set
	 */
	public void setSpRegistryDao(RegistryDao registryDao) {
		this.registryDao = registryDao;
	}

	public void setRegistryDaoExt(RegistryDaoExt registryDaoExt) {
		this.registryDaoExt = registryDaoExt;
	}

	public void setSpRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	public void setOrganizationDao(OrganizationDao organizationDao) {
		this.organizationDao = organizationDao;
	}

	public void setRegistryContainerDao(RegistryContainerDao registryContainerDao) {
		this.registryContainerDao = registryContainerDao;
	}
}
