package org.flexpay.common.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.RegistryContainerDao;
import org.flexpay.common.dao.registry.RegistryDao;
import org.flexpay.common.dao.registry.RegistryDaoExt;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional (readOnly = true)
public class RegistryServiceImpl implements RegistryService {

	private Logger log = LoggerFactory.getLogger(getClass());

	protected RegistryDao registryDao;
	private RegistryDaoExt registryDaoExt;
	private RegistryContainerDao registryContainerDao;

	private RegistryRecordService registryRecordService;

	/**
	 * Create SpRegistry
	 *
	 * @param registry SpRegistry object
	 * @return created SpRegistry object
	 */
	@Transactional (readOnly = false)
	public Registry create(Registry registry) throws FlexPayException {

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

        List<RegistryContainer> removeContainers = new ArrayList<RegistryContainer>();
        for (RegistryContainer container : registry.getContainers()) {
            if (container.isNew() && !StringUtils.isEmpty(container.getData())) {
			    registryContainerDao.create(container);
            } else if (container.isNotNew() && StringUtils.isEmpty(container.getData())) {
                removeContainers.add(container);
                registryContainerDao.delete(container);
            } else if (container.isNotNew()) {
                registryContainerDao.update(container);
            }
		}

        registry.getContainers().removeAll(removeContainers);

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
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	public Collection<Registry> findObjects(@NotNull Set<Long> objectIds) {
		return registryDaoExt.findRegistries(objectIds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getRegistriesCount(int typeCode, Long recipientCode, Date from, Date till) {
		
		return (Long) (registryDao.findRegistriesCount(typeCode, recipientCode, from, till)).get(0);
	}

	@Required
	public void setSpRegistryDao(RegistryDao registryDao) {
		this.registryDao = registryDao;
	}

	@Required
	public void setRegistryDaoExt(RegistryDaoExt registryDaoExt) {
		this.registryDaoExt = registryDaoExt;
	}

	@Required
	public void setSpRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	@Required
	public void setRegistryContainerDao(RegistryContainerDao registryContainerDao) {
		this.registryContainerDao = registryContainerDao;
	}
}
