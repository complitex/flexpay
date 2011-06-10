package org.flexpay.common.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.RegistryContainerDao;
import org.flexpay.common.dao.registry.RegistryDao;
import org.flexpay.common.dao.registry.RegistryDaoExt;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPModule;
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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.list;

@Transactional (readOnly = true)
public class RegistryServiceImpl implements RegistryService {

	private Logger log = LoggerFactory.getLogger(getClass());

	protected RegistryDao registryDao;
	private RegistryDaoExt registryDaoExt;
	private RegistryContainerDao registryContainerDao;

	private RegistryRecordService registryRecordService;

	/**
	 * Create Registry
	 *
	 * @param registry Registry object
	 * @return created Registry object
	 */
	@Transactional (readOnly = false)
    @Override
	public Registry create(Registry registry) throws FlexPayException {

		validate(registry);

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
    @Override
	public List<Registry> findObjects(Page<Registry> pager, Long spFileId) {
		return registryDao.findObjects(pager, spFileId);
	}

	private void validate(Registry registry) throws FlexPayException {
		FPModule module = registry.getModule();
		Long moduleId = module == null ? null : module.getId();
		if (moduleId == null || moduleId <= 0L) {
			throw new FlexPayException("No module", "common.error.registry.no_module");
		}
	}

	/**
	 * Read SpRegistry object by its unique id
	 *
	 * @param stub Registry stub
	 * @return SpRegistry object, or <code>null</code> if object not found
	 */
	@Nullable
    @Override
	public Registry read(@NotNull Stub<Registry> stub) {
		Registry registry = registryDao.readFull(stub.getId());
		if (registry == null) {
			log.debug("Registry #{} not found", stub);
			return null;
		}

		return registry;
	}

    /**
	 * Read Registry with containers included
	 *
	 * @param stub Registry stub
	 * @return Registry if found, or <code>null</code> otherwise
	 */
    @Override
	public Registry readWithContainers(@NotNull Stub<Registry> stub) {
		List<Registry> registries = registryDao.listRegistryWithContainers(stub.getId());
		return registries.isEmpty() ? null : registries.get(0);
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
    @Override
	public Registry update(Registry registry) throws FlexPayException {

		validate(registry);

		registry.setErrorsNumber(registryRecordService.getErrorsNumber(registry));

		registryDao.update(registry);

        List<RegistryContainer> removeContainers = list();
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
    @Override
	public void delete(Registry registry) {
		registryDao.delete(registry);
	}

	/**
	 * Delete all records for registry
	 *
	 * @param stub registry stub
	 */
	@Transactional (readOnly = false)
    @Override
	public void deleteRecords(Stub<Registry> stub) {
		registryDao.deleteRecords(stub.getId());
	}

	/**
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	@Override
	public Collection<Registry> findObjects(@NotNull Set<Long> objectIds) {
		return registryDaoExt.findRegistries(objectIds);
	}

    @Override
    public List<Registry> findRegistries(int typeCode, Date from, Date till) {
        return registryDao.findRegistries(typeCode, from, till);
    }

    @Override
    public List<Registry> findRegistriesInDateInterval(Date from, Date till) {
        return registryDao.findRegistriesInDateInterval(from, till);
    }

    @Override
    public List<Registry> findRegistries(Long recipientCode, Date from, Date till) {
        return registryDao.findRegistriesByDateIntervalAndRecipient(recipientCode, from, till);
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	public Long getRegistriesCount(int typeCode, Long recipientCode, Date from, Date till) {
		return (Long) registryDao.findRegistriesCount(typeCode, recipientCode, from, till).get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int checkRegistryErrorsNumber(@NotNull Registry registry) {
		int errorsNumber = registryRecordService.getErrorsNumber(registry);
		if (registry.errorsNumberNotInit() || errorsNumber != registry.getErrorsNumber()) {
			registry.setErrorsNumber(errorsNumber);
			registryDao.update(registry);
		}

		return errorsNumber;
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
