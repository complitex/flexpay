package org.flexpay.eirc.service.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.dao.OrganizationDao;
import org.flexpay.common.dao.registry.RegistryContainerDao;
import org.flexpay.common.dao.registry.RegistryDao;
import org.flexpay.eirc.dao.RegistryDaoExt;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.common.persistence.filter.RegistryTypeFilter;
import org.flexpay.eirc.service.RegistryRecordService;
import org.flexpay.eirc.service.RegistryService;
import org.flexpay.eirc.persistence.EircRegistryProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class RegistryServiceImpl implements RegistryService {

	private Logger log = LoggerFactory.getLogger(getClass());

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
	@Transactional (readOnly = false)
	public Registry create(Registry registry) throws FlexPayException {
		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		props.setRecipient(organizationDao.read(props.getRecipientStub().getId()));
		props.setSender(organizationDao.read(props.getSenderStub().getId()));
		registryDao.create(registry);

		for (RegistryContainer container : registry.getContainers()) {
			registryContainerDao.create(container);
		}

		log.debug("Created Registry: {}", registry);

		return registry;
	}

	/**
	 * Get all SpRegistry by spFile id in page mode
	 *
	 * @param pager Page object
	 * @return List of SpRegistry objects for pager
	 */
	@Transactional (readOnly = false)
	public List<Registry> findObjects(Page<Registry> pager, Long spFileId) {
		return registryDao.findObjects(pager, spFileId);
	}

	/**
	 * Read SpRegistry object by its unique id
	 *
	 * @param stub Registry stub
	 * @return SpRegistry object, or <code>null</code> if object not found
	 */
	@Nullable
	public Registry read(@NotNull Stub<Registry> stub) {
		Registry registry = registryDao.readFull(stub.getId());
		if (registry == null) {
			log.debug("Registry #{} not found", stub);
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
	public Registry readWithContainers(@NotNull Stub<Registry> stub) {
		List<Registry> registries = registryDao.listRegistryWithContainers(stub.getId());
		if (registries.isEmpty()) {
			return null;
		}

		return registries.get(0);
	}

	/**
	 * Update SpRegistry
	 *
	 * @param registry SpRegistry to update for
	 * @return Updated SpRegistry object
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if SpRegistry object is invalid
	 */
	@Transactional (readOnly = false)
	public Registry update(Registry registry) throws FlexPayException {
		registryDao.update(registry);

		return registry;
	}

	@Transactional (readOnly = false)
	public void delete(Registry registry) {
		registryDao.delete(registry);
	}

	/**
	 * Delete all records for registry
	 *
	 * @param stub registry stub
	 */
	@Transactional (readOnly = false)
	public void deleteRecords(Stub<Registry> stub) {
		registryDao.deleteRecords(stub.getId());
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
	public List<Registry> findObjects(OrganizationFilter senderFilter, OrganizationFilter recipientFilter,
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
	public Collection<Registry> findObjects(@NotNull Set<Long> objectIds) {
		return registryDaoExt.findRegistries(objectIds);
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
